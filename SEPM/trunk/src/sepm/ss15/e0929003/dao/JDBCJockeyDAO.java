package sepm.ss15.e0929003.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.Jockey;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCJockeyDAO implements JockeyDAO {

    private static final Logger logger = LogManager.getLogger(JDBCJockeyDAO.class);
    private Connection con = null;

    public JDBCJockeyDAO() throws DAOException {
        con = JDBCSingletonConnection.getConnection();
        logger.debug("DAO created successfully.");
    }

    @Override
    public Jockey create(Jockey jockey) throws DAOException {
        logger.debug("Entering create method and trying to insert into the table Jockey:\n{}",jockey);
        checkIfJockeyIsNull(jockey);
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement createStmt = con.prepareStatement("INSERT INTO Jockey(first_name,last_name,country,skill) VALUES (?,?,?,"+jockey.getSkill()+");", Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1, jockey.getFirstName());
            createStmt.setString(2,jockey.getLastName());
            createStmt.setString(3,jockey.getCountry());
            createStmt.executeUpdate();
            ResultSet rs = createStmt.getGeneratedKeys();
            rs.next();
            jockey.setId(rs.getInt(1));
            jockey.setDeleted(false);
            con.commit();
            logger.debug("Successfully inserted row into the table Jockey:\n{}",jockey);
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                logger.debug(e.getMessage());
                throw new DAOException(e.getMessage());
            }
            throw new DAOException(e.getMessage());
        }
        return jockey;
    }

    @Override
    public List<Jockey> search(Jockey from, Jockey to) throws DAOException {
        logger.debug("Entering search method and trying to filter by skill from\n{}to\n{}",from,to);
        checkIfJockeyIsNull(from);
        checkIfJockeyIsNull(to);
        String sql ="SELECT* FROM Jockey WHERE is_deleted=false";
        if((from.getSkill()!=null) == (to.getSkill()!=null)){
            if(from.getSkill()!=null && to.getSkill()!=null){
                sql = sql + " AND skill BETWEEN "+ from.getSkill() + " AND "+to.getSkill() ;
            }
        }
        else{
            logger.debug("One or more variables are not initialized.");
            throw new DAOException("One or more variables are not initialized.");
        }
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        ArrayList<Jockey> list = new ArrayList<Jockey>();
        try {
            ResultSet rs = con.prepareStatement(sql+";").executeQuery();
            while(rs.next()){
                list.add(new Jockey(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getBoolean(6)));
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        logger.debug("Successfully returned filtered list of jockeys:\n{}",list);
        return list;
    }

    @Override
    public void update(Jockey jockey) throws DAOException {
        logger.debug("Entering update method with:\n{}",jockey);
        checkIfJockeyIsNull(jockey);
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement updateStmt = con.prepareStatement("UPDATE Jockey SET first_name=?,last_name=?,country=?,skill="+jockey.getSkill()+",is_deleted="+jockey.isDeleted()+" WHERE id="+jockey.getId()+";");
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM Jockey WHERE id="+jockey.getId()+";");
            if(!rs.next()){
                logger.debug("Jockey with id {} doesn't exist.",jockey.getId());
                throw new DAOException("Jockey with id " + jockey.getId() + " doesn't exist.");
            }
            updateStmt.setString(1,jockey.getFirstName());
            updateStmt.setString(2, jockey.getLastName());
            updateStmt.setString(3, jockey.getCountry());
            updateStmt.executeUpdate();
            con.commit();
            logger.debug("Successfully updated jockey in the table Jockey:\n{}",jockey);
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                logger.debug(e.getMessage());
                throw new DAOException(e.getMessage());
            }
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void delete(Jockey jockey) throws DAOException {
        logger.debug("Entering delete method with:\n{}",jockey);
        checkIfJockeyIsNull(jockey);
        jockey.setDeleted(true);
        update(jockey);
    }

    private void checkIfJockeyIsNull(Jockey jockey) throws DAOException{
        if(jockey == null){
            logger.debug("Jockey is null.");
            throw new DAOException("Jockey can't be null.");
        }
    }
}

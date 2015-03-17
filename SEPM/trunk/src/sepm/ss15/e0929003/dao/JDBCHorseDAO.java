package sepm.ss15.e0929003.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.Horse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCHorseDAO implements HorseDAO {

    private static final Logger logger = LogManager.getLogger(JDBCHorseDAO.class);
    private Connection con = null;

    public JDBCHorseDAO() throws DAOException {
        con = JDBCSingletonConnection.getConnection();
        logger.debug("DAO created successfully.");
    }

    @Override
    public Horse create(Horse horse) throws DAOException {
        logger.debug("Entering create method and trying to insert into the table Horse:\n{}",horse);
        checkIfHorseIsNull(horse);
        checkIfAttributesAreNull(0,horse.getName(),horse.getAge(),horse.getMinSpeed(),horse.getMaxSpeed(),horse.getPicture(),horse.isDeleted());
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement createStmt = con.prepareStatement("INSERT INTO Horse(name,age,min_speed,max_speed,picture) VALUES (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1,horse.getName());
            createStmt.setInt(2,horse.getAge());
            createStmt.setDouble(3,horse.getMinSpeed());
            createStmt.setDouble(4,horse.getMaxSpeed());
            createStmt.setString(5,horse.getPicture());
            createStmt.executeUpdate();
            ResultSet rs = createStmt.getGeneratedKeys();
            rs.next();
            horse.setId(rs.getInt(1));
            con.commit();
            logger.debug("Successfully inserted row into the table Horse:\n{}",horse);
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
        return horse;
    }

    @Override
    public List<Horse> search(Horse from, Horse to) throws DAOException {
        logger.debug("Entering search method and trying to filter by age,minSpeed and maxSpeed from\n{}to\n{}",from,to);
        checkIfHorseIsNull(from);
        checkIfHorseIsNull(to);
        String sql ="SELECT* FROM Horse WHERE is_deleted=false";
        if(((from.getAge()!=null) == (to.getAge()!=null)) && ((from.getMinSpeed()!=null) == (to.getMinSpeed()!=null)) && ((from.getMaxSpeed()!=null) == (to.getMaxSpeed()!=null))){
            if(from.getAge()!=null && to.getAge()!=null){
                sql = sql + " AND age BETWEEN "+ from.getAge() + " AND "+to.getAge() ;
            }
            if(from.getMinSpeed()!=null && to.getMinSpeed()!=null){
                sql = sql + " AND min_speed BETWEEN "+ from.getMinSpeed() + " AND "+to.getMinSpeed() ;
            }
            if(from.getMaxSpeed()!=null && to.getMaxSpeed()!=null){
                sql = sql + " AND max_speed BETWEEN "+ from.getMaxSpeed() + " AND "+to.getMaxSpeed() ;
            }
        }
        else{
            logger.debug("One or more variables are not initialized.");
            throw new DAOException("One or more variables are not initialized.");
        }
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        ArrayList<Horse> list = new ArrayList<Horse>();
        try {
            ResultSet rs = con.createStatement().executeQuery(sql+";");
            while(rs.next()){
                list.add(new Horse(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6),rs.getBoolean(7)));
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        logger.debug("Successfully returned filtered list of horses:\n{}",list);
        return list;
    }

    @Override
    public void update(Horse horse) throws DAOException {
        logger.debug("Entering update method with:\n{}",horse);
        checkIfHorseIsNull(horse);
        checkIfAttributesAreNull(horse.getId(),horse.getName(),horse.getAge(),horse.getMinSpeed(),horse.getMaxSpeed(),horse.getPicture(),horse.isDeleted());
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement updateStmt = con.prepareStatement("UPDATE Horse SET name=?,age=?,min_speed=?,max_speed=?,picture=?,is_deleted=? WHERE id=?;");
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM Horse WHERE id="+horse.getId()+";");
            if(!rs.next()){
                logger.debug("Horse with id {} doesn't exist.",horse.getId());
                throw new DAOException("Horse with id " + horse.getId() + " doesn't exist.");
            }
            updateStmt.setString(1,horse.getName());
            updateStmt.setInt(2,horse.getAge());
            updateStmt.setDouble(3,horse.getMinSpeed());
            updateStmt.setDouble(4,horse.getMaxSpeed());
            updateStmt.setString(5,horse.getPicture());
            updateStmt.setBoolean(6,horse.isDeleted());
            updateStmt.setInt(7,horse.getId());
            updateStmt.executeUpdate();
            con.commit();
            logger.debug("Successfully updated horse in the table Horse:\n{}",horse);
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
    public void delete(Horse horse) throws DAOException {
        logger.debug("Entering delete method with:\n{}",horse);
        horse.setDeleted(true);
        update(horse);
    }

    private void checkIfHorseIsNull(Horse horse) throws DAOException{
        if(horse == null){
            logger.debug("Horse is null.");
            throw new DAOException("Horse can't be null.");
        }
    }

    private void checkIfAttributesAreNull(Integer id, String name, Integer age, Double minSpeed, Double maxSpeed, String picture, Boolean isDeleted) throws DAOException {
        if(id==null || name==null || age==null || minSpeed==null || maxSpeed==null || picture==null || isDeleted==null){
            logger.debug("One or more variables are not initialized.");
            throw new DAOException("One or more variables are not initialized.");
        }
    }
}

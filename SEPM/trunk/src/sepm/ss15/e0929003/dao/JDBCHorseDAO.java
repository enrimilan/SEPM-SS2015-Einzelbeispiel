package sepm.ss15.e0929003.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.Horse;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        logger.debug("Entering create method and trying to insert into the table Horse:\n{}", horse);
        checkIfHorseIsNull(horse);
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement createStmt = con.prepareStatement("INSERT INTO Horse(name,age,min_speed,max_speed,picture) VALUES (?,"+horse.getAge()+","+horse.getMinSpeed()+","+horse.getMaxSpeed()+",?);", Statement.RETURN_GENERATED_KEYS);
            createStmt.setString(1,horse.getName());
            createStmt.setString(2,horse.getPicture());
            createStmt.executeUpdate();
            ResultSet rs = createStmt.getGeneratedKeys();
            rs.next();
            horse.setId(rs.getInt(1));
            horse.setDeleted(false);
            update(horse);
            logger.debug("Successfully inserted row into the table Horse:\n{}",horse);
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        return horse;
    }

    @Override
    public List<Horse> search(Horse from, Horse to) throws DAOException {
        logger.debug("Entering search method and trying to filter by age,minSpeed and maxSpeed from\n{}to\n{}",from,to);
        checkIfHorseIsNull(from);
        checkIfHorseIsNull(to);
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        ArrayList<Horse> list = new ArrayList<Horse>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM Horse WHERE is_deleted=false AND age BETWEEN " + from.getAge() + " AND " + to.getAge() + " AND min_speed BETWEEN " + from.getMinSpeed() + " AND " + to.getMinSpeed() + " AND max_speed BETWEEN " + from.getMaxSpeed() + " AND " + to.getMaxSpeed() + ";");
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
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            PreparedStatement updateStmt = con.prepareStatement("UPDATE Horse SET name=?,age="+horse.getAge()+",min_speed="+horse.getMinSpeed()+",max_speed="+horse.getMaxSpeed()+",picture=?,is_deleted="+horse.isDeleted()+" WHERE id="+horse.getId()+";");
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM Horse WHERE id="+horse.getId()+";");
            if(!rs.next()){
                logger.debug("Horse with id {} doesn't exist.",horse.getId());
                throw new DAOException("Horse with id " + horse.getId() + " doesn't exist.");
            }
            File source = new File(horse.getPicture());
            int index = source.getName().lastIndexOf('.');
            String extension ="";
            if (index >= 0) {
                extension = source.getName().substring(index+1);
            }
            String picture = "src/res/pictures/"+horse.getId()+"."+extension;
            updateStmt.setString(1,horse.getName());
            updateStmt.setString(2,picture);
            updateStmt.executeUpdate();
            File dest = new File(picture);
            CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
            Files.copy(source.toPath(), dest.toPath(), options);
            con.commit();
            horse.setPicture(picture);
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
        } catch (IOException e) {
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
        checkIfHorseIsNull(horse);
        horse.setDeleted(true);
        update(horse);
    }

    @Override
    public void loadTestData() throws DAOException{
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        JDBCSingletonConnection.reset(JDBCRaceResultDAO.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
        try {
        File folder = new File("src/res/pictures");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Files.delete(new File("src/res/pictures/"+listOfFiles[i].getName()).toPath());
            }
        }
        for(int i=1; i<11; i++){
            File source = new File("src/res/pictures/test_pictures/"+i+".jpg");
            File dest = new File("src/res/pictures/"+i+".jpg");
            CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
            Files.copy(source.toPath(), dest.toPath(), options);
        }
        } catch (IOException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void close() throws DAOException{
        JDBCSingletonConnection.closeConnection();
    }

    private void checkIfHorseIsNull(Horse horse) throws DAOException{
        if(horse == null){
            logger.debug("Horse is null.");
            throw new DAOException("Horse can't be null.");
        }
    }
}

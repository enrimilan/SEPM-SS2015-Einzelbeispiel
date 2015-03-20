package sepm.ss15.e0929003.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.RaceResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JDBCRaceResultDAO implements RaceResultDAO{

    private static final Logger logger = LogManager.getLogger(JDBCRaceResultDAO.class);
    private Connection con = null;

    public JDBCRaceResultDAO() throws DAOException {
        con = JDBCSingletonConnection.getConnection();
        logger.debug("DAO created successfully.");
    }

    @Override
    public void createRaceResults(List<RaceResult> raceResults) throws DAOException {
        logger.debug("Entering create method and trying to insert race results into the table RaceResult:\n{}".trim(),raceResults);
        if(raceResults==null){
            logger.debug("List<RaceResult> is null.");
            throw new DAOException("List of race results can't be null.");
        }
        for(RaceResult raceResult: raceResults) {
            checkIfRaceResultIsNull(raceResult);
        }
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        try {
            for(RaceResult raceResult: raceResults) {
                logger.debug("Trying to insert row into the table RaceResult: {}", raceResult.toString().trim());
                ResultSet rs = con.createStatement().executeQuery("SELECT count(*) FROM RaceResult WHERE race_id="+raceResult.getRaceId()+" AND (horse_id="+raceResult.getHorseId()+" OR jockey_id="+raceResult.getJockeyId()+");");
                rs.next();
                if(rs.getInt(1)>0){
                    logger.debug("Race result with id {} and horse id {} / jockey id {} already exists.", raceResult.getRaceId(), raceResult.getHorseId(), raceResult.getJockeyId());
                    throw new DAOException("Horse or jockey already participates in this race.");
                }
                Integer raceId = raceResult.getRaceId();
                Integer horseId = raceResult.getHorseId();
                Integer jockeyId = raceResult.getJockeyId();
                Double randomSpeed = raceResult.getRandomSpeed();
                Double luckFactor = raceResult.getLuckFactor();
                Double jockeySkillCalc = raceResult.getJockeySkillCalc();
                Double averageSpeed = raceResult.getAverageSpeed();
                Integer rank = raceResult.getRank();
                PreparedStatement createStmt = con.prepareStatement("INSERT INTO RaceResult(race_id,horse_id,jockey_id,horse_name,jockey_name,random_speed,luck_factor,jockey_skill_calc,average_speed,rank) VALUES ("+raceId+","+horseId+","+jockeyId+",?,?,"+randomSpeed+","+luckFactor+","+jockeySkillCalc+","+averageSpeed+","+rank+");");
                createStmt.setString(1, raceResult.getHorseName());
                createStmt.setString(2, raceResult.getJockeyName());
                createStmt.executeUpdate();
                logger.debug("Successfully added to transaction: {}", raceResult.toString().trim());
            }
            con.commit();
            logger.debug("Successfully inserted all rows into the table RaceResult:\n{}".trim(), raceResults);
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
    public List<RaceResult> search(RaceResult raceResult) throws DAOException {
        logger.debug("Entering search method with:\n{}",raceResult);
        checkIfRaceResultIsNull(raceResult);
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        ArrayList<RaceResult> list = new ArrayList<RaceResult>();
        ResultSet rs = null;
        try{
            String query = "SELECT* FROM RaceResult";
                query = query +" WHERE 1=1";
                if(raceResult.getRaceId()!=null){
                    query = query + " AND RACE_ID="+raceResult.getRaceId();
                }
                if(raceResult.getHorseId()!=null){
                    query = query + " AND HORSE_ID="+raceResult.getHorseId();
                }
                if(raceResult.getJockeyId()!=null){
                    query = query + " AND JOCKEY_ID="+raceResult.getJockeyId();
                }
                rs = con.createStatement().executeQuery(query+ " ORDER BY RACE_ID ASC, RANK ASC;");
            while(rs.next()){
                list.add(new RaceResult(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getInt(10)));
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        logger.debug("Successfully returned filtered list of race results:\n{}".trim(), list);
        return list;
    }

    @Override
    public HashMap<Integer, Integer> getStatistics(RaceResult raceResult) throws DAOException {
        logger.debug("Entering getStatistics method with:\n{}", raceResult);
        checkIfRaceResultIsNull(raceResult);
        if(raceResult.getHorseId()==null && raceResult.getJockeyId()==null){
            logger.debug("horseId and jockeyId are both null.");
            throw new DAOException("horseId and jockeyId can't be both empty.");
        }
        con = JDBCSingletonConnection.reconnectIfConnectionToDatabaseLost();
        HashMap<Integer,Integer> statistics = new HashMap<Integer, Integer>();
        String query = "SELECT RANK, count(*) FROM RaceResult WHERE 1=1";
        if(raceResult.getHorseId()!=null){
            query = query + " AND HORSE_ID="+raceResult.getHorseId();
        }
        if(raceResult.getJockeyId()!=null){
            query = query + " AND JOCKEY_ID="+raceResult.getJockeyId();
        }
        try {
            ResultSet rs = con.createStatement().executeQuery(query+ " GROUP BY RANK;");
            while(rs.next()){
                statistics.put(rs.getInt(1),rs.getInt(2));
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        logger.debug("Successfully returned statistics:\n{}".trim(), statistics);
        return statistics;
    }

    private void checkIfRaceResultIsNull(RaceResult raceResult) throws DAOException {
        if(raceResult == null){
            logger.debug("RaceResult is null.");
            throw new DAOException("RaceResult can't be null.");
        }
    }
}

package sepm.ss15.e0929003.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.dao.*;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.entities.RaceResult;

import java.util.*;

public class SimpleService implements Service {

    private static final Logger logger = LogManager.getLogger(SimpleService.class);
    private HorseDAO horseDAO;
    private JockeyDAO jockeyDAO;
    private RaceResultDAO raceResultDAO;
    private Integer raceId;
    private HashMap<Jockey,Horse> participants;
    private Jockey lastRemovedJockeyFromRace;
    private Horse lastRemovedHorseFromRace;

    public SimpleService() throws ServiceException {
        this.participants = new HashMap<Jockey,Horse>();
        try {
            this.horseDAO = new JDBCHorseDAO();
            this.jockeyDAO = new JDBCJockeyDAO();
            this.raceResultDAO = new JDBCRaceResultDAO();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
        logger.info("Service started.");
    }

    @Override
    public Horse createHorse(Horse horse) throws ServiceException {
        logger.debug("Entering createHorse method.");
        if(horse!=null) horse.setId(1337); //some dummy id to pass the validation.
        validateHorse(horse);
        horse.setId(null);
        try {
            return horseDAO.create(horse);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void editHorse(Horse horse) throws ServiceException {
        logger.debug("Entering editHorse method.");
        validateHorse(horse);
        try {
            horseDAO.update(horse);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteHorse(Horse horse) throws ServiceException {
        logger.debug("Entering deleteHorse method.");
        validateHorse(horse);
        try {
            horseDAO.delete(horse);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Horse> searchHorses(Horse from, Horse to) throws ServiceException {
        logger.debug("Entering searchHorses method.");
        //set some dummy values that are not important in order to pass the validation.
        if(from!=null) {
            from.setId(1337);
            from.setName("dummy");
            from.setPicture("dummy");
        }
        if(to!=null){
            to.setId(1337);
            to.setName("dummy");
            to.setPicture("dummy");
        }
        validateHorse(from);
        validateHorse(to);
        try {
            return horseDAO.search(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Jockey createJockey(Jockey jockey) throws ServiceException {
        logger.debug("Entering createJockey method.");
        if(jockey!=null)jockey.setId(1337); //some dummy id to pass the validation.
        validateJockey(jockey);
        jockey.setId(null);
        try {
            return jockeyDAO.create(jockey);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void editJockey(Jockey jockey) throws ServiceException {
        logger.debug("Entering editJockey method.");
        validateJockey(jockey);
        try {
            jockeyDAO.update(jockey);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteJockey(Jockey jockey) throws ServiceException {
        logger.debug("Entering deleteJockey method.");
        validateJockey(jockey);
        try {
            jockeyDAO.delete(jockey);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Jockey> searchJockeys(Jockey from, Jockey to) throws ServiceException {
        logger.debug("Entering searchJockeys method.");
        if(from==null || to==null){
            logger.debug("from: {} to {}",from,to);
            throw new ServiceException("Jockey can't be empty.");
        }
        if(!((from.getSkill()!=null) == (to.getSkill()!=null))){
            logger.warn("from skill: {} to skill {}", from.getSkill(), to.getSkill());
            throw new ServiceException("Please fill all the fields.");
        }
        try {
            return jockeyDAO.search(from,to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void createNewRace() throws ServiceException{
        participants.clear();
        raceId++;
        logger.debug("New id {} for new race.",raceId);
    }

    @Override
    public void addJockeyAndHorseToRace(Jockey jockey, Horse horse) throws ServiceException {
        logger.debug("Entering addJockeyAndHorseToRace method.");
        validateJockey(jockey);
        validateHorse(horse);
        if(participants.containsKey(jockey) || participants.containsValue(horse)){
            logger.warn("Jockey {} or horse {} already on the list.",jockey,horse);
            throw new ServiceException("Jockey or Horse are already participants.");
        }
        participants.put(jockey,horse);
        logger.debug("Successfully added {}-{} to the list.",jockey,horse);
    }

    @Override
    public void removeJockeyAndHorseFromRace(Jockey jockey, Horse horse) throws ServiceException {
        logger.debug("Entering removeJockeyAndHorseFromRace method.");
        validateJockey(jockey);
        validateHorse(horse);
        if(!participants.containsKey(jockey) || !participants.containsValue(horse)){
            logger.warn("Jockey {} or horse {} does not exist.",jockey,horse);
            throw new ServiceException("Jockey or horse does not exist.");
        }
        Horse h = participants.get(jockey);
        if(!h.equals(horse)){
            logger.warn("{}-{} do not exist.", jockey, horse);
            throw new ServiceException("Jockey-Horse combination does not exist.");
        }
        participants.remove(jockey);
        logger.debug("Successfully removed {}-{} from the list.",jockey,horse);
        lastRemovedJockeyFromRace = jockey;
        lastRemovedHorseFromRace = horse;
    }

    @Override
    public Horse getLastRemovedHorseFromRace() throws ServiceException {
        return lastRemovedHorseFromRace;
    }

    @Override
    public Jockey getLastRemovedJockeyFromRace() throws ServiceException {
        return lastRemovedJockeyFromRace;
    }

    @Override
    public List<RaceResult> startRaceSimulation() throws ServiceException {
        logger.debug("Entering startRaceSimulation.");
        if(participants.isEmpty()){
            logger.warn("No participants.");
            throw new ServiceException("There has to be at least one jockey and one horse who participate at the race together.");
        }
        ArrayList<RaceResult> raceResults = new ArrayList<RaceResult>();
        Random r = new Random();
        for(Map.Entry<Jockey, Horse> entry : participants.entrySet()){
            Jockey jockey = entry.getKey();
            Horse horse = entry.getValue();
            validateJockey(jockey);
            validateHorse(horse);
            RaceResult raceResult = new RaceResult();
            raceResult.setRaceId(raceId);
            raceResult.setJockeyId(jockey.getId());
            raceResult.setHorseId(horse.getId());
            raceResult.setJockeyName(jockey.getFirstName()+" "+jockey.getLastName());
            raceResult.setHorseName(horse.getName());
            Double jockeySkillCalc = ((Math.atan(jockey.getSkill()))/5) * (0.15/Math.PI);
            raceResult.setJockeySkillCalc(jockeySkillCalc);
            Double randomSpeed = (double)Math.round((horse.getMinSpeed() + (horse.getMaxSpeed() - horse.getMinSpeed()) * r.nextDouble())*100)/100;
            Double luckFactor = (double)Math.round((0.95 + (1.05 - 0.95) * r.nextDouble())*100)/100;
            raceResult.setAverageSpeed((double) Math.round(randomSpeed * jockeySkillCalc * luckFactor * 100) / 100);
            raceResults.add(raceResult);
            logger.debug("raceResult prepared: {}" ,raceResult);
        }

        Collections.sort(raceResults, new Comparator<RaceResult>() {
            public int compare(RaceResult one, RaceResult other) {
                if (one.getAverageSpeed() >= other.getAverageSpeed()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        for(int i=0; i<raceResults.size(); i++){
            raceResults.get(i).setRank(i+1);
        }
        logger.debug("RaceResults created and sorted by average speed: {}",raceResults);
        logger.debug("Starting race simulation.");
        try {
            raceResultDAO.createRaceResults(raceResults);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
        logger.debug("Race simulation completed successfully.");
        return raceResults;
    }

    @Override
    public List<RaceResult> searchRaceResults(RaceResult raceResult) throws ServiceException {
        logger.debug("Entering searchResults with {}",raceResult);
        if(raceResult==null){
            logger.debug("RaceResult is null");
            throw new ServiceException("RaceResult can't be empty.");
        }
        try {
            List<RaceResult> list = raceResultDAO.search(raceResult);
            if(raceId==null){
                if(!list.isEmpty()){
                    raceId = list.get(list.size()-1).getRaceId();
                }
                else{
                    raceId = 1;
                }
                logger.debug("Last inserted race id is: {}",raceId);
            }
            return list;
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Integer> evaluateStatistics(RaceResult raceResult) throws ServiceException {
        logger.debug("Entering evaluateStatistics with {}",raceResult);
        if(raceResult==null){
            logger.debug("RaceResult is null");
            throw new ServiceException("RaceResult can't be empty.");
        }
        if(raceResult.getJockeyId() == null && raceResult.getHorseId() == null) {
            logger.warn("Jockey id: {} and horse id: {}",raceResult.getJockeyId(),raceResult.getHorseId());
            throw new ServiceException("Both ids can't be empty.");
        }
        try {
            return raceResultDAO.getStatistics(raceResult);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void validateHorse(Horse horse) throws ServiceException {
        logger.debug("Validating horse: {}",horse);

        if(horse==null){
            logger.debug("Horse is null.");
            throw new ServiceException("Horse can't be empty.");
        }

        if(horse.getId()==null || horse.getId()<=0){
            logger.warn("ID: {}", horse.getId());
            throw new ServiceException("Invalid ID.");
        }

        if(horse.getName()==null || horse.getName().isEmpty()){
            logger.warn("Name: '{}'.", horse.getName());
            throw new ServiceException("Name can't be empty.");
        }
        else if(horse.getName().length()>30){
            logger.warn("Name is {} characters long.", horse.getName().length());
            throw new ServiceException("Name is too long.");
        }
        else if(!horse.getName().matches("^[\\p{L} -]+[']?[\\p{L} -]*$")){
            logger.warn("Name '{}' does not match pattern.",horse.getName());
            throw new ServiceException("Name is not correct.");
        }

        if(horse.getAge()==null){
            logger.warn("Age is empty.");
            throw new ServiceException("Age can't be empty.");
        }
        else if(horse.getAge()<=0 || horse.getAge()>40){
            logger.warn("Age is {}", horse.getAge());
            throw new ServiceException("Age must be between 1 and 40.");
        }

        if(horse.getMinSpeed()==null){
            logger.warn("Minimal speed is empty.");
            throw new ServiceException("Minimal speed can't be empty.");
        }
        else if(horse.getMinSpeed()<50.0 || horse.getMinSpeed()>100.0){
            logger.warn("Minimal speed is {}", horse.getMinSpeed());
            throw new ServiceException("Minimal speed must be between 50.0 and 100.0.");
        }

        if(horse.getMaxSpeed()==null){
            logger.warn("Maximal speed is empty.");
            throw new ServiceException("Maximal speed can't be empty.");
        }
        else if(horse.getMaxSpeed()<50.0 || horse.getMaxSpeed()>100.0){
            logger.warn("Maximal speed is {}", horse.getMaxSpeed());
            throw new ServiceException("Maximal speed must be between 50.0 and 100.0.");
        }

        if(horse.getMinSpeed()!=null && horse.getMaxSpeed()!=null && horse.getMinSpeed()>horse.getMaxSpeed()){
            logger.warn("Minimal speed {} is greater than maximal speed {}", horse.getMinSpeed(), horse.getMaxSpeed());
            throw new ServiceException("Minimal speed can't be greater than maximal speed.");
        }

        if(horse.getPicture()==null || horse.getPicture().isEmpty()){
            logger.warn("Picture path: '{}'.");
            throw new ServiceException("Picture path can't be empty");
        }
        logger.debug("Horse validation successful.");
    }

    public void validateJockey(Jockey jockey) throws ServiceException {
        logger.debug("Validating jockey: {}",jockey);

        if(jockey==null){
            logger.debug("Jockey is null.");
            throw new ServiceException("Jockey can't be empty.");
        }

        if(jockey.getId()==null || jockey.getId()<=0){
            logger.warn("ID: {}", jockey.getId());
            throw new ServiceException("Invalid ID.");
        }

        if(jockey.getFirstName()==null || jockey.getFirstName().isEmpty()){
            logger.warn("First Name: '{}'.", jockey.getFirstName());
            throw new ServiceException("First Name can't be empty.");
        }
        else if(jockey.getFirstName().length()>30){
            logger.warn("First Name is {} characters long.", jockey.getFirstName().length());
            throw new ServiceException("First Name is too long.");
        }
        else if(!jockey.getFirstName().matches("^[\\p{L} -]+[']?[\\p{L} -]*$")){
            logger.warn("First Name '{}' does not match pattern.",jockey.getFirstName());
            throw new ServiceException("First Name is not correct.");
        }

        if(jockey.getLastName()==null || jockey.getLastName().isEmpty()){
            logger.info("Last Name: {}",jockey.getLastName());
            throw new ServiceException("Last Name can't be empty.");
        }
        else if(jockey.getLastName().length()>30){
            logger.info("Last Name length: {}",jockey.getLastName().length());
            throw new ServiceException("Last Name is too long.");
        }
        else if(!jockey.getLastName().matches("^[\\p{L} -]+[']?[\\p{L} -]*$")){
            logger.warn("Last Name '{}' does not match pattern.", jockey.getLastName());
            throw new ServiceException("Last Name is not correct.");
        }

        if(jockey.getCountry()==null || jockey.getCountry().isEmpty()){
            logger.info("Country: {}",jockey.getCountry());
            throw new ServiceException("Country can't be empty.");
        }
        else if(jockey.getCountry().length()>30){
            logger.info("Country: {}",jockey.getCountry());
            throw new ServiceException("Country name is too long.");
        }
        else if(!jockey.getCountry().matches("^[\\p{L} -]+[']?[\\p{L} -]*$")){
            logger.warn("Country '{}' does not match pattern.",jockey.getCountry());
            throw new ServiceException("Country is not correct.");
        }

        if(jockey.getSkill()==null){
            logger.warn("Skill is empty.");
            throw new ServiceException("Skill can't be empty.");
        }
        logger.debug("Jockey validation successful.");
    }

}
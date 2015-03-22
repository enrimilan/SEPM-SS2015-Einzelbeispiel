package sepm.ss15.e0929003.service;

import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.entities.RaceResult;

import java.util.HashMap;
import java.util.List;

public interface Service {

    /**
     * Creates a new horse.
     * @param horse the horse to be created.
     * @return the created horse(containing the id).
     * @throws ServiceException if:
     *  - horse is null.
     *  - at least one of the attributes name,age,minSpeed,maxSpeed,picture of the object horse is null.
     *  - age<=0,age>40; minSpeed<50.0, maxSpeed>100.0, minSpeed>maxSpeed; the path for the picture doesn't exist; name is invalid.
     *  - HorseDAO can't persist the horse.
     */
    public Horse createHorse(Horse horse) throws ServiceException;

    /**
     * Modifies a horse.
     * @param horse the horse to be modified.
     * @throws ServiceException if:
     *  - horse is null.
     *  - at least one of the attributes id,name,age,minSpeed,maxSpeed,picture of the object horse is null.
     *  - id<=0; age<=0,age>40; minSpeed<50.0, maxSpeed>100.0, minSpeed>maxSpeed; the path for the picture doesn't exist; name is invalid.
     *  - HorseDAO can't update the horse.
     */
    public void editHorse(Horse horse) throws ServiceException;

    /**
     * Deletes a horse.
     * @param horse the horse to be deleted.
     * @throws ServiceException if:
     * - horse is null.
     * - at least one of the attributes id,name,age,minSpeed,maxSpeed,picture of the object horse is null.
     * - id<=0; age<=0,age>40; minSpeed<50.0, maxSpeed>100.0, minSpeed>maxSpeed; the path for the picture doesn't exist; name is invalid.
     * - HorseDAO can't delete the horse.
     */
    public void deleteHorse(Horse horse) throws ServiceException;

    /**
     * Returns a list of horses, for which age, minSpeed and maxSpeed are between those in the objects 'from' and 'to'.
     * @param from contains the minimum age, minSpeed and maxSpeed to search.
     * @param to contains the maximum age, minSpeed and maxSpeed to search.
     * @return the results of the search.
     * @throws ServiceException if:
     *  - from or to is null.
     *  - at least one of the attributes age,minSpeed,maxSpeed in the objects 'from' or 'to' is null.
     *  - age<=0,age>40; minSpeed<50.0, maxSpeed>100.0, minSpeed>maxSpeed.
     *  - HorseDAO can't do the search.
     */
    public List<Horse> searchHorses(Horse from, Horse to) throws ServiceException;

    /**
     * Creates a new jockey.
     * @param jockey the jockey to be created.
     * @return the created jockey(containing the id).
     * @throws ServiceException if:
     *  - jockey is null.
     *  - at least one of the attributes firstName,lastName,country,skill of the object jockey is null.
     *  - firstName,lastName or country is invalid.
     *  - JockeyDAO can't persist the jockey.
     */
    public Jockey createJockey(Jockey jockey) throws ServiceException;

    /**
     * Modifies a jockey.
     * @param jockey the jockey to be modified.
     * @throws ServiceException if:
     *  - jockey is null.
     *  - at least one of the attributes id,firstName,lastName,country,skill of the object jockey is null.
     *  - firstName,lastName or country is invalid.
     *  - id<=0.
     *  - JockeyDAO can't modify the jockey.
     */
    public void editJockey(Jockey jockey) throws ServiceException;

    /**
     * Deletes a jockey.
     * @param jockey the jockey to be deleted.
     * @throws ServiceException if:
     *  - jockey is null.
     *  - at least one of the attributes id,firstName,lastName,country,skill of the object jockey is null.
     *  - firstName,lastName or country is invalid.
     *  - id<=0.
     *  - JockeyDAO can't delete the jockey.
     */
    public void deleteJockey(Jockey jockey) throws ServiceException;

    /**
     * Returns a list of jockeys, for which skill is between the skills in the objects 'from' and 'to'.
     * If skill is in 'from' and in 'to' null, then all the jockeys will be returned.
     * @param from contains the minimum skill to search.
     * @param to contains the maximum skill to search.
     * @return the results of the search.
     * @throws ServiceException if:
     *  - from or to is null.
     *  - skill is null in one object and in the other not.
     *  - JockeyDAO can't do the search.
     */
    public List<Jockey> searchJockeys(Jockey from, Jockey to) throws ServiceException;

    /**
     * Creates a new race.
     * @throws ServiceException
     */
    public void createNewRace() throws ServiceException;

    /**
     * Adds a jockey-horse combination to the list of participants.
     * @param jockey
     * @param horse
     * @throws ServiceException if:
     *  - jockey or horse is null.
     *  - attributes are null or invalid.
     *  - jockey or horse is already in that list.
     */
    public void addJockeyAndHorseToRace(Jockey jockey, Horse horse) throws ServiceException;

    /**
     * Removes a jockey-horse combination from the list of participants.
     * @param jockey
     * @param horse
     * @throws ServiceException if:
     *  - jockey or horse is null or it doesn't exist.
     *  - jockey-horse combination doesn't exist.
     *  - attributes are null or invalid.
     */
    public void removeJockeyAndHorseFromRace(Jockey jockey, Horse horse) throws ServiceException;

    /**
     * @return last horse which was removed from the list of participants, as a result of calling removeJockeyAndHorseFromRace().
     * @throws ServiceException
     */
    public Horse getLastRemovedHorseFromRace() throws ServiceException;

    /**
     * @return last jockey which was removed from the list of participants, as a result of calling removeJockeyAndHorseFromRace().
     * @throws ServiceException
    */
    public Jockey getLastRemovedJockeyFromRace() throws ServiceException;

    /**
     * Simulates a race.
     * @return the result of the race, ordered by rank.
     * @throws ServiceException if:
     *  - objects are null.
     *  - there are no participants.
     *  - RaceResultDAO couldn't create the results.
     */
    public List<RaceResult> startRaceSimulation() throws ServiceException;

    /**
     * Returns a list of race results filtered by the attributes horseId, jockeyId and raceResultId.
     * If any of those is equal to null, it will be ignored for filtering.
     * @param raceResult containing the 3 ids for filtering.
     * @return the filtered list of race results if no errors occurred.
     * @throws ServiceException if:
     *  - RaceResultDAO can't return the list.
     */
    public List<RaceResult> searchRaceResults(RaceResult raceResult) throws ServiceException;

    /**
     * Returns how often a jockey, a horse or a combination of these were placed at a rank.
     * @param raceResult containing the ids of the horse and the jockey (horseId and jockeyId).
     * @return a HashMap containing the ranks and how often a horse, a jockey or a combination of them were placed at the respective rank.
     * @throws ServiceException if:
     *  - raceResult is null.
     *  - horseId and jockeyId are both null.
     *  - RaceResultDAO can't evaluate the statistics.
     */
    public HashMap<Integer,Integer> evaluateStatistics(RaceResult raceResult) throws ServiceException;


}

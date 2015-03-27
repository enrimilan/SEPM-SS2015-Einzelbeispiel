package sepm.ss15.e0929003.dao;

import sepm.ss15.e0929003.entities.RaceResult;

import java.util.HashMap;
import java.util.List;

public interface RaceResultDAO {

    /**
     * Creates the results for a race.
     * @param raceResults the race results to be created.
     * @throws DAOException if one or more race results couldn't be created. Reasons for that:
     *  - raceResults is null.
     *  - at least one of the race results is null.
     *  - a race result with the same raceId and horseId already exists.
     *  - a race result with the same raceId and jockeyId already exists.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public void createRaceResults(List<RaceResult> raceResults) throws DAOException;

    /**
     * Returns a list of race results filtered by the attributes horseId, jockeyId and raceResultId.
     * If any of those is equal to null, it will be ignored for filtering.
     * @param raceResult containing the 3 ids for filtering.
     * @return the filtered list of race results if no errors occurred.
     * @throws DAOException if the search couldn't be performed. Reasons for that:
     *  - raceResult is null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public List<RaceResult> search(RaceResult raceResult) throws DAOException;

    /**
     * Returns how often a jockey, a horse or a combination of these were placed at a rank.
     * @param raceResult containing the ids of the horse and the jockey (horseId and jockeyId).
     * @return a HashMap containing the ranks and how often a horse, a jockey or a combination of them were placed at the respective rank.
     * @throws DAOException if the operation couldn't be performed. Reasons for that:
     *  - raceResult is null.
     *  - horseId and jockeyId are both null.
     *  - other reasons depending on the chosen DAO implementation.
     */
    public HashMap<Integer,Integer> getStatistics(RaceResult raceResult) throws DAOException;

    /**
     * Loads test data for the application.
     */
    public void loadTestData() throws DAOException;
}

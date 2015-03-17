package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.RaceResultDAO;
import sepm.ss15.e0929003.entities.RaceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractRaceResultDAOTest {

    protected RaceResultDAO raceResultDAO;
    protected RaceResult raceResult1;
    protected RaceResult raceResult2;
    protected RaceResult raceResult3;
    protected RaceResult raceResult4;
    protected RaceResult raceResult5;

    protected void setRaceResultDAO(RaceResultDAO raceResultDAO) {
        this.raceResultDAO = raceResultDAO;
    }

    protected void setRaceResults(RaceResult raceResult1, RaceResult raceResult2, RaceResult raceResult3, RaceResult raceResult4, RaceResult raceResult5){
        this.raceResult1 = raceResult1;
        this.raceResult2 = raceResult2;
        this.raceResult3 = raceResult3;
        this.raceResult4 = raceResult4;
        this.raceResult5 = raceResult5;
    }

    @Test(expected = DAOException.class)
    public void createWithRaceResultNullShouldThrowDAOException() throws DAOException {
        ArrayList<RaceResult> list = new ArrayList<RaceResult>();
        list.add(raceResult1);
        list.add(raceResult2);
        list.add(raceResult3);
        list.add(raceResult4);
        list.add(null);
        raceResultDAO.createRaceResults(list);
    }

    @Test(expected = DAOException.class)
    public void createWithRaceResultContainingHorseTwiceShouldThrowDAOException() throws DAOException {
        ArrayList<RaceResult> list = new ArrayList<RaceResult>();
        list.add(raceResult1);
        list.add(raceResult2);
        list.add(raceResult3);
        list.add(raceResult4);
        raceResult5.setHorseId(raceResult1.getHorseId());
        list.add(raceResult5);
        raceResultDAO.createRaceResults(list);
    }

    @Test
    public void createWithValidRaceResultsShouldPersist() throws DAOException {
        ArrayList<RaceResult> list = new ArrayList<RaceResult>();
        list.add(raceResult1);
        list.add(raceResult2);
        list.add(raceResult3);
        list.add(raceResult4);
        list.add(raceResult5);
        raceResultDAO.createRaceResults(list);
        List<RaceResult> races = raceResultDAO.search(new RaceResult(5, null, null, null, null, null, null, null, null, null));
        assertTrue(races.contains(raceResult1));
        assertTrue(races.size()==5);
    }

    @Test(expected = DAOException.class)
    public void searchWithRaceResultNullShouldThrowDAOException() throws DAOException {
        raceResultDAO.search(null);
    }

    @Test
    public void searchWithRaceResultNotNullShouldReturnFilteredListOfRaceResultsWith2Elements() throws DAOException {
        List<RaceResult> races = raceResultDAO.search(new RaceResult(null, null, 1, null, null, null, null, null, null, null));
        assertTrue(races.size()==2);
    }

    @Test(expected = DAOException.class)
    public void getStatisticsWithBothIdsNullShouldThrowDAOException() throws DAOException {
        raceResultDAO.getStatistics(new RaceResult());
    }

    @Test
    public void getStatisticsWithJockeyIdShouldReturnStatisticsWith1Element() throws DAOException {
        HashMap<Integer,Integer> statistics = raceResultDAO.getStatistics(new RaceResult(null, null, 1, null, null, null, null, null, null, null));
        assertTrue(statistics.get(3)==2);
        assertTrue(statistics.size()==1);
    }
}

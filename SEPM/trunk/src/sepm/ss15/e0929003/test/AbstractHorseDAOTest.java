package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.HorseDAO;
import sepm.ss15.e0929003.entities.Horse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractHorseDAOTest {

    private HorseDAO horseDAO;
    private Horse validHorse;
    private Horse horseWithNegativeAge;
    private Horse horseWithNegativeId;

    protected void setHorseDAO(HorseDAO horseDAO) {
        this.horseDAO = horseDAO;
    }

    protected void setHorses(Horse validHorse, Horse horseWithNegativeAge, Horse horseWithNegativeId){
        this.validHorse = validHorse;
        this.horseWithNegativeAge = horseWithNegativeAge;
        this.horseWithNegativeId = horseWithNegativeId;
    }

    @Test(expected = DAOException.class)
    public void createHorseWithNullShouldThrowDAOException() throws DAOException {
        horseDAO.create(null);
    }

    @Test(expected = DAOException.class)
    public void createWithNegativeAgeShouldThrowDAOException() throws DAOException {
        horseDAO.create(horseWithNegativeAge);
    }

    @Test
    public void createHorseWithValidParametersShouldPersist() throws DAOException {
        List<Horse> horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertFalse(horses.contains(validHorse));
        Horse h = horseDAO.create(validHorse);
        horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertTrue(horses.contains(h));
        horseDAO.delete(h);
    }

    @Test(expected = DAOException.class)
    public void searchWithOneHorseNullNullShouldThrowDAOException() throws DAOException {
        horseDAO.search(validHorse,null);
    }

    @Test
    public void searchWithValidParametersShouldReturnAllHorses() throws DAOException {
        List<Horse> horses = horseDAO.search(new Horse(null, null, 1, 50.0, 50.0, null, null), new Horse(null, null, 40, 100.0, 100.0, null, null));
        assertEquals(horses.size(),8);
    }

    @Test(expected = DAOException.class)
    public void updateWithNonExistingIdShouldThrowDAOException() throws DAOException {
        horseDAO.update(horseWithNegativeAge);
    }

    @Test
    public void updateWithValidIdShouldUpdateHorse() throws DAOException {
        Horse h = horseDAO.create(validHorse);
        List<Horse> horses = horseDAO.search(new Horse(null, null, 1, 50.0, 50.0, null, null), new Horse(null, null, 40, 100.0, 100.0, null, null));
        assertTrue(horses.contains(h));
        h.setAge(h.getAge()+1);
        horseDAO.update(h);
    }

    @Test(expected = DAOException.class)
    public void deleteWithNonExistingIdShouldThrowDAOException() throws DAOException {
        horseDAO.delete(horseWithNegativeId);
    }

    @Test
    public void deleteWithValidIdShouldDeleteHorse() throws DAOException{
        Horse h = horseDAO.create(validHorse);
        List<Horse> horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertTrue(horses.contains(h));
        horseDAO.delete(h);
        horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertFalse(horses.contains(h));
    }
}
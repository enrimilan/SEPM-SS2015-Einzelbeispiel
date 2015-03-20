package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.HorseDAO;
import sepm.ss15.e0929003.entities.Horse;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractHorseDAOTest {

    protected HorseDAO horseDAO;
    protected Horse validHorse1;
    protected Horse validHorse2;
    protected Horse invalidHorse1;
    protected Horse invalidHorse2;

    protected void setHorseDAO(HorseDAO horseDAO) {
        this.horseDAO = horseDAO;
    }

    protected void setHorses(Horse validHorse1, Horse validHorse2, Horse invalidHorse1, Horse invalidHorse2){
        this.validHorse1 = validHorse1;
        this.validHorse2 = validHorse2;
        this.invalidHorse1 = invalidHorse1;
        this.invalidHorse2 = invalidHorse2;

    }

    @Test(expected = DAOException.class)
    public void createWithNullShouldThrowDAOException() throws DAOException {
        horseDAO.create(null);
    }

    @Test(expected = DAOException.class)
    public void createWithNegativeAgeShouldThrowDAOException() throws DAOException {
        horseDAO.create(invalidHorse1);
    }

    @Test
    public void createHorseWithValidParametersShouldPersist() throws DAOException {
        List<Horse> horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertFalse(horses.contains(validHorse1));
        Horse h = horseDAO.create(validHorse1);
        horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertTrue(horses.contains(h));
    }

    @Test(expected = DAOException.class)
    public void searchWithOneHorseNullNullShouldThrowDAOException() throws DAOException {
        horseDAO.search(validHorse1,null);
    }

    @Test
    public void searchWithValidParametersShouldReturn2Horses() throws DAOException {
        List<Horse> horses = horseDAO.search(validHorse1, validHorse2);
        assertTrue(horses.size() == 2);
        assertTrue(horses.contains(new Horse(2,"Avenida",23,56.0,82.9,"",false)));
        assertTrue(horses.contains(new Horse(4,"Trixie",19,59.6,90.7,"",false)));
    }

    @Test(expected = DAOException.class)
    public void updateWithNonExistingIdShouldThrowDAOException() throws DAOException {
        invalidHorse2.setId(-1);
        horseDAO.update(invalidHorse1);
    }

    @Test
    public void updateWithValidIdShouldUpdateHorse() throws DAOException {
        Horse h = horseDAO.create(validHorse1);
        List<Horse> horses = horseDAO.search(new Horse(null, null, 1, 50.0, 50.0, null, null), new Horse(null, null, 40, 100.0, 100.0, null, null));
        assertTrue(horses.contains(h));
        h.setAge(2);
        horseDAO.update(h);
        horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertTrue(horses.contains(h));
    }

    @Test(expected = DAOException.class)
    public void deleteWithNonExistingIdShouldThrowDAOException() throws DAOException {
        invalidHorse2.setId(-1);
        invalidHorse2.setDeleted(true);
        horseDAO.delete(invalidHorse2);
    }

    @Test
    public void deleteWithValidIdShouldDeleteHorse() throws DAOException{
        Horse h = horseDAO.create(validHorse1);
        List<Horse> horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertTrue(horses.contains(h));
        horseDAO.delete(h);
        horses = horseDAO.search(new Horse(null,null,1,50.0,50.0,null,null), new Horse(null,null,40,100.0,100.0,null,null));
        assertFalse(horses.contains(h));
    }
}
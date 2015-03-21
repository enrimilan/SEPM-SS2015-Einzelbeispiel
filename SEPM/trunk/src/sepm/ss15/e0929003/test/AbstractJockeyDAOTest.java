package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JockeyDAO;
import sepm.ss15.e0929003.entities.Jockey;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractJockeyDAOTest {

    private JockeyDAO jockeyDAO;
    private Jockey validJockey;
    private Jockey jockeyWithSkillNull;
    private Jockey jockeyWithNegativeId;


    protected void setJockeyDAO(JockeyDAO jockeyDAO) {
        this.jockeyDAO = jockeyDAO;
    }

    protected void setJockeys(Jockey validJockey, Jockey jockeyWithSkillNull, Jockey jockeyWithNegativeId){
        this.validJockey = validJockey;
        this.jockeyWithSkillNull = jockeyWithSkillNull;
        this.jockeyWithNegativeId = jockeyWithNegativeId;
    }

    @Test(expected = DAOException.class)
    public void createWithNullShouldThrowDAOException() throws DAOException {
        jockeyDAO.create(null);
    }

    @Test
    public void createJockeyWithValidParametersShouldPersist() throws DAOException {
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertFalse(jockeys.contains(validJockey));
        Jockey j = jockeyDAO.create(validJockey);
        jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertTrue(jockeys.contains(j));
    }

    @Test(expected = DAOException.class)
    public void searchWithSkillNullShouldThrowDAOException() throws DAOException {
        jockeyDAO.search(validJockey, jockeyWithSkillNull);
    }

    @Test
    public void searchWithValidParametersShouldReturnAllJockeys() throws DAOException {
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.size() == 8);
    }

    @Test(expected = DAOException.class)
    public void updateWithNonExistingIdShouldThrowDAOException() throws DAOException {
        jockeyDAO.update(jockeyWithNegativeId);
    }

    @Test
    public void updateWithValidIdShouldUpdateJockey() throws DAOException {
        Jockey j = jockeyDAO.create(validJockey);
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.contains(j));
        j.setCountry(j.getCountry()+"dummy");
        jockeyDAO.update(j);
    }

    @Test(expected = DAOException.class)
    public void deleteWithNonExistingIdShouldThrowDAOException() throws DAOException {
        jockeyDAO.delete(jockeyWithNegativeId);
    }

    @Test
    public void deleteWithValidIdShouldDeleteJockey() throws DAOException{
        Jockey j = jockeyDAO.create(validJockey);
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.contains(j));
        jockeyDAO.delete(j);
        jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertFalse(jockeys.contains(j));
    }
}

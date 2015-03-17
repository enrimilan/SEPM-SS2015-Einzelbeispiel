package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JockeyDAO;
import sepm.ss15.e0929003.entities.Jockey;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractJockeyDAOTest {

    protected JockeyDAO jockeyDAO;
    protected Jockey validJockey1;
    protected Jockey validJockey2;
    protected Jockey invalidJockey1;
    protected Jockey invalidJockey2;


    protected void setJockeyDAO(JockeyDAO jockeyDAO) {
        this.jockeyDAO = jockeyDAO;
    }

    protected void setJockeys(Jockey validJockey1, Jockey validJockey2, Jockey invalidJockey1, Jockey invalidJockey2){
        this.validJockey1 = validJockey1;
        this.validJockey2 = validJockey2;
        this.invalidJockey1 = invalidJockey1;
        this.invalidJockey2 = invalidJockey2;
    }

    @Test(expected = DAOException.class)
    public void createWithNullShouldThrowDAOException() throws DAOException {
        jockeyDAO.create(null);
    }

    @Test
    public void createJockeyWithValidParametersShouldPersist() throws DAOException {
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertFalse(jockeys.contains(validJockey1));
        Jockey j = jockeyDAO.create(validJockey1);
        jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertTrue(jockeys.contains(j));
    }

    @Test(expected = DAOException.class)
    public void searchWithSkillNullShouldThrowDAOException() throws DAOException {
        jockeyDAO.search(validJockey1,invalidJockey1);
    }

    @Test
    public void searchWithValidParametersShouldReturn4Jockeys() throws DAOException {
        List<Jockey> jockeys = jockeyDAO.search(validJockey1, validJockey2);
        assertTrue(jockeys.size() == 4);
        assertTrue(jockeys.contains(new Jockey(2,"Robby","Albarado","United States",1.0,false)));
        assertTrue(jockeys.contains(new Jockey(3,"Steve","Cauthen","United States",-2.0,false)));
        assertTrue(jockeys.contains(new Jockey(9,"Lance","Sullivan","New Zealand",2.0,false)));
        assertTrue(jockeys.contains(new Jockey(10,"Stephane","Pasquier","France",0.0,false)));
    }

    @Test(expected = DAOException.class)
    public void updateWithNonExistingIdShouldThrowDAOException() throws DAOException {
        jockeyDAO.update(invalidJockey2);
    }

    @Test
    public void updateWithValidIdShouldUpdateJockey() throws DAOException {
        Jockey j = jockeyDAO.create(validJockey1);
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.contains(j));
        j.setCountry("Germany");
        jockeyDAO.update(j);
        jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.contains(j));
    }

    @Test(expected = DAOException.class)
    public void deleteWithNonExistingIdShouldThrowDAOException() throws DAOException {
        invalidJockey2.setDeleted(true);
        jockeyDAO.delete(invalidJockey2);
    }

    @Test
    public void deleteWithValidIdShouldDeleteJockey() throws DAOException{
        Jockey j = jockeyDAO.create(validJockey1);
        List<Jockey> jockeys = jockeyDAO.search(new Jockey(), new Jockey());
        assertTrue(jockeys.contains(j));
        jockeyDAO.delete(j);
        jockeys = jockeyDAO.search(new Jockey(),new Jockey());
        assertFalse(jockeys.contains(j));
    }
}

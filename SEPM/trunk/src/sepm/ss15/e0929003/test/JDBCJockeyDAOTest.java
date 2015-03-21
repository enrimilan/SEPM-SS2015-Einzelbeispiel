package sepm.ss15.e0929003.test;

import org.junit.After;
import org.junit.Before;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JDBCJockeyDAO;
import sepm.ss15.e0929003.dao.JDBCSingletonConnection;
import sepm.ss15.e0929003.entities.Jockey;

import java.sql.SQLException;

public class JDBCJockeyDAOTest extends AbstractJockeyDAOTest {

    @Before
    public void setUp() throws DAOException, SQLException {
        setJockeyDAO(new JDBCJockeyDAO());
        Jockey validJockey = new Jockey(null,"Test Jockey ","1","Country",-3.0,false);
        Jockey jockeyWithSkillNull = new Jockey(null,"Test Jockey ","2","Country",null,false);
        Jockey jockeyWithNegativeId = new Jockey(-1,"Test Jockey ","3","Country",-1.0,false);
        setJockeys(validJockey, jockeyWithSkillNull, jockeyWithNegativeId);
    }

    @After
    public void tearDown() throws DAOException, SQLException {
        JDBCSingletonConnection.getConnection().createStatement().executeUpdate("DELETE FROM Jockey WHERE id>10;");
        JDBCSingletonConnection.getConnection().commit();
    }
}

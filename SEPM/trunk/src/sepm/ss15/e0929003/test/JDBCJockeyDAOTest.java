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
        Jockey validJockey1 = new Jockey(null,"Test Jockey ","1","Country",-3.0,false);
        Jockey validJockey2 = new Jockey(null,"Test Jockey ","2","Country",2.0,false);
        Jockey invalidJockey1 = new Jockey(null,"Test Jockey ","3","Country",null,false);
        Jockey invalidJockey2 = new Jockey(-1,"Test Jockey ","4","Country",-1.0,false);
        setJockeys(validJockey1, validJockey2, invalidJockey1, invalidJockey2);
    }

    @After
    public void tearDown() throws DAOException, SQLException {
        JDBCSingletonConnection.getConnection().createStatement().executeUpdate("DELETE FROM Jockey WHERE id>10;");
        JDBCSingletonConnection.getConnection().commit();
    }
}

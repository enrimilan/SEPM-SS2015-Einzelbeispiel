package sepm.ss15.e0929003.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JDBCJockeyDAO;
import sepm.ss15.e0929003.dao.JDBCSingletonConnection;
import sepm.ss15.e0929003.entities.Jockey;

import java.sql.SQLException;

public class JDBCJockeyDAOTest extends AbstractJockeyDAOTest {

    @BeforeClass
    public static void resetBefore() throws DAOException {
        JDBCSingletonConnection.getConnection();
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }

    @Before
    public void setUp() throws DAOException, SQLException {
        setJockeyDAO(new JDBCJockeyDAO());
        Jockey validJockey = new Jockey(null,"Test Jockey ","1","Country",-3.0,false);
        Jockey jockeyWithSkillNull = new Jockey(null,"Test Jockey ","2","Country",null,false);
        Jockey jockeyWithNegativeId = new Jockey(-1,"Test Jockey ","3","Country",-1.0,false);
        setJockeys(validJockey, jockeyWithSkillNull, jockeyWithNegativeId);
    }

    @AfterClass
    public static void resetAfter() throws DAOException {
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }
}
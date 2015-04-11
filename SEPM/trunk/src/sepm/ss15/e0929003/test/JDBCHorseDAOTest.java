package sepm.ss15.e0929003.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JDBCHorseDAO;
import sepm.ss15.e0929003.dao.JDBCSingletonConnection;
import sepm.ss15.e0929003.entities.Horse;

import java.sql.SQLException;

public class JDBCHorseDAOTest extends AbstractHorseDAOTest{

    @BeforeClass
    public static void resetBefore() throws DAOException {
        JDBCSingletonConnection.getConnection();
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }

    @Before
    public void setUp() throws DAOException, SQLException {
        setHorseDAO(new JDBCHorseDAO());
        Horse validHorse = new Horse(null,"Test Horse 1",19,52.0,75.0,"",false);
        Horse horseWithNegativeAge = new Horse(null,"Test Horse 2",-1,70.0,80.0,"",false);
        Horse horseWithNegativeId = new Horse(-1,"Test Horse 3",29,70.0,80.0,"",false);
        setHorses(validHorse, horseWithNegativeAge, horseWithNegativeId);
    }

    @AfterClass
    public static void resetAfter() throws DAOException {
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }

}

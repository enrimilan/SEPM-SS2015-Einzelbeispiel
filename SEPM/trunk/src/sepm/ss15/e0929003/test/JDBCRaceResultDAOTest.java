package sepm.ss15.e0929003.test;

import org.junit.AfterClass;
import org.junit.Before;
import sepm.ss15.e0929003.dao.DAOException;
import sepm.ss15.e0929003.dao.JDBCRaceResultDAO;
import sepm.ss15.e0929003.dao.JDBCSingletonConnection;
import sepm.ss15.e0929003.entities.RaceResult;

import java.sql.SQLException;

public class JDBCRaceResultDAOTest extends AbstractRaceResultDAOTest {

    @Before
    public void setUp() throws DAOException, SQLException {
        setRaceResultDAO(new JDBCRaceResultDAO());
        RaceResult raceResult1 = new RaceResult(null,1,1,"Jacky","Eddie Ahern",60.0,0.95,2.48,241.36,2);
        RaceResult raceResult2 = new RaceResult(null,2,2,"Avenida","Robby Albarado",70.0,1.05,1.54,131.19,4);
        RaceResult raceResult3 = new RaceResult(null,3,3,"Manchego","Steve Cauthen",77.0,1.00,-0.04,-3.08,5);
        RaceResult raceResult4 = new RaceResult(null,4,4,"Trixie","Patricia Cooksey",80.0,0.97,2.85,221.16,3);
        RaceResult raceResult5 = new RaceResult(null,5,5,"Dark Sparks","Alan Garcia",90.0,1.02,3.15,289.17,1);
        setRaceResults(raceResult1, raceResult2, raceResult3, raceResult4, raceResult5);
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }

    @AfterClass
    public static void resetAfter() throws DAOException {
        JDBCSingletonConnection.reset(JDBCJockeyDAOTest.class.getClassLoader().getResource("res/testdata.sql").getPath().substring(1));
    }

}

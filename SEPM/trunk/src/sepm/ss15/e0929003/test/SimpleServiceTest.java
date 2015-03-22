package sepm.ss15.e0929003.test;

import org.junit.Before;
import org.junit.Test;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.ServiceException;
import sepm.ss15.e0929003.service.SimpleService;

public class SimpleServiceTest extends AbstractServiceTest {

    private SimpleService service;

    @Before
    public void setUp() throws ServiceException {
        this.service = new SimpleService();
        setService(service);
        Horse horseWithInvalidName = new Horse(1,"Test.Horse",11,51.0,99.0,"test",false);
        Horse horseWithInvalidAge = new Horse(1,"Test Horse",41,51.0,99.0,"test",false);
        Horse validHorse = new Horse(1,"Test Horse",40,51.0,99.0,"test",false);
        Jockey jockeyWithInvalidFirstName = new Jockey(1,"Test''Jockey","bla","bla",1.0,false);
        Jockey validJockey = new Jockey(1,"Test Jockey","bla","bla",1.0,false);
        setEntities(horseWithInvalidName, horseWithInvalidAge, validHorse, jockeyWithInvalidFirstName, validJockey);
    }

    @Test(expected = ServiceException.class)
    public void validateHorseWithInvalidNameShouldThrowServiceException() throws ServiceException {
        service.validateHorse(horseWithInvalidName);
    }

    @Test(expected = ServiceException.class)
    public void validateHorseWithInvalidAgeShouldThrowServiceException() throws ServiceException {
        service.validateHorse(horseWithInvalidAge);
    }

    @Test
    public void validateHorseWithValidHorseShouldBeOk() throws ServiceException {
        service.validateHorse(validHorse);
    }

    @Test(expected = ServiceException.class)
    public void validateJockeyWithInvalidFirstNameShouldThrowServiceException() throws ServiceException {
        service.validateJockey(jockeyWithInvalidFirstName);
    }

    @Test
    public void validateJockeyWithValidJockeyShouldBeOk() throws ServiceException {
        service.validateJockey(validJockey);
    }
}

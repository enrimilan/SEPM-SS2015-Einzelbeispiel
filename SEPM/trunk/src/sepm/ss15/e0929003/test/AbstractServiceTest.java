package sepm.ss15.e0929003.test;

import org.junit.Test;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

public abstract class AbstractServiceTest {

    protected Service service;
    protected Horse horseWithInvalidName;
    protected Horse horseWithInvalidAge;
    protected Horse validHorse;
    protected Jockey jockeyWithInvalidFirstName;
    protected Jockey validJockey;

    protected void setService(Service service){
        this.service = service;
    }

    protected void setEntities(Horse horseWithInvalidName, Horse horseWithInvalidAge, Horse validHorse, Jockey jockeyWithInvalidFirstName, Jockey validJockey){
        this.horseWithInvalidName = horseWithInvalidName;
        this.horseWithInvalidAge = horseWithInvalidAge;
        this.validHorse = validHorse;
        this.jockeyWithInvalidFirstName = jockeyWithInvalidFirstName;
        this.validJockey = validJockey;
    }

    @Test(expected = ServiceException.class)
    public void addJockeyAndHorseToRaceTwiceShouldThrowServiceException() throws ServiceException {
        service.addJockeyAndHorseToRace(validJockey,validHorse);
        service.addJockeyAndHorseToRace(validJockey,validHorse);
    }

    @Test(expected = ServiceException.class)
    public void removeJockeyAndHorseFromRaceNoParticipantsShouldThrowServiceException() throws ServiceException {
        service.removeJockeyAndHorseFromRace(validJockey, validHorse);
    }

    @Test
    public void removeJockeyAndHorseFromRaceWithExistingParticipantsShouldBeOk() throws ServiceException {
        service.addJockeyAndHorseToRace(validJockey,validHorse);
        service.removeJockeyAndHorseFromRace(validJockey,validHorse);
        validHorse.equals(service.getLastRemovedHorseFromRace());
        validJockey.equals(service.getLastRemovedJockeyFromRace());
    }

}

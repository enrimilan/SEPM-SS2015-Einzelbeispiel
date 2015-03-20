package sepm.ss15.e0929003.service;

import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.entities.RaceResult;

import java.util.HashMap;
import java.util.List;

public class SimpleService implements Service {

    @Override
    public Horse createHorse(Horse horse) throws ServiceException {
        return null;
    }

    @Override
    public void editHorse(Horse horse) throws ServiceException {

    }

    @Override
    public void deleteHorse(Horse horse) throws ServiceException {

    }

    @Override
    public List<Horse> searchHorses(Horse from, Horse to) throws ServiceException {
        return null;
    }

    @Override
    public Jockey createJockey(Jockey jockey) throws ServiceException {
        return null;
    }

    @Override
    public void editJockey(Jockey jockey) throws ServiceException {

    }

    @Override
    public void deleteJockey(Jockey jockey) throws ServiceException {

    }

    @Override
    public List<Jockey> searchJockeys(Jockey from, Jockey to) throws ServiceException {
        return null;
    }

    @Override
    public void addJockeyAndHorseToRace(Jockey jockey, Horse horse) throws ServiceException {

    }

    @Override
    public void removeJockeyAndHorseFromRace(Jockey jockey) throws ServiceException {

    }

    @Override
    public Horse getLastRemovedHorseFromRace() throws ServiceException {
        return null;
    }

    @Override
    public Jockey getLastRemovedJockeyFromRace() throws ServiceException {
        return null;
    }

    @Override
    public List<RaceResult> startRaceSimulation() throws ServiceException {
        return null;
    }

    @Override
    public List<RaceResult> searchRaceResults(RaceResult raceResult) throws ServiceException {
        return null;
    }

    @Override
    public HashMap<Integer, Integer> evaluateStatistics(RaceResult raceResult) throws ServiceException {
        return null;
    }
}

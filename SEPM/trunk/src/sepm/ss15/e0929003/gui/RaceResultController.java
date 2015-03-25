package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.List;

public class RaceResultController {

    private MainViewController mainViewController;
    private TableView<RaceResult> raceResultsTable;
    private Service service;

    public RaceResultController(MainViewController mainViewController){
        this.mainViewController = mainViewController;
        this.raceResultsTable = mainViewController.getRaceResultsTable();
        this.service = mainViewController.getService();
    }

    public void searchRaceResults(TextField raceIdTextFieldInRacesTab,TextField horseIdTextFieldInRacesTab, TextField jockeyIdTextFieldInRacesTab){
        Integer raceId = null;
        Integer horseId = null;
        Integer jockeyId = null;
        if(!raceIdTextFieldInRacesTab.getText().isEmpty()){
            raceId = Integer.valueOf(raceIdTextFieldInRacesTab.getText());
        }
        if(!horseIdTextFieldInRacesTab.getText().isEmpty()){
            horseId = Integer.valueOf(horseIdTextFieldInRacesTab.getText());
        }
        if(!jockeyIdTextFieldInRacesTab.getText().isEmpty()){
            jockeyId = Integer.valueOf(jockeyIdTextFieldInRacesTab.getText());
        }
        try {
            List<RaceResult> list = service.searchRaceResults(new RaceResult(raceId,horseId,jockeyId,null,null,null,null,null,null,null));
            raceResultsTable.setItems(FXCollections.observableArrayList(list));
            raceResultsTable.getColumns().get(0).setVisible(false);
            raceResultsTable.getColumns().get(0).setVisible(true);
        }
        catch (ServiceException e) {
            mainViewController.showAlertDialog("Search race results", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
    }

    }

}

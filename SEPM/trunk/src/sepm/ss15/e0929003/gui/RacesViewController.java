package sepm.ss15.e0929003.gui;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.List;

public class RacesViewController extends MainViewController {

    private static final Logger logger = LogManager.getLogger(Main.class);

    @FXML
    private TableView<RaceResult> raceResultsTable;
    @FXML
    private TableColumn<RaceResult,String> raceResultIdColumn,raceResultHorseIdColumn,raceResultJockeyIdColumn,rankColumn,raceResultHorseNameColumn,jockeyNameColumn,randomSpeedColumn,luckFactorColumn,skillColumn,averageSpeedColumn;
    @FXML
    private CheckBox raceIdCheckBoxInRacesTab,horseIdCheckBoxInRacesTab,jockeyIdCheckBoxInRacesTab;
    @FXML
    private TextField raceIdTextFieldInRacesTab,horseIdTextFieldInRacesTab,jockeyIdTextFieldInRacesTab;
    @FXML
    private Button raceSearchButton;

    public void initialize() {
        raceResultIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("raceId"));
        raceResultHorseIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseId"));
        raceResultJockeyIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyId"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("rank"));
        raceResultHorseNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseName"));
        jockeyNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyName"));
        randomSpeedColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("randomSpeed"));
        luckFactorColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("luckFactor"));
        skillColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeySkillCalc"));
        averageSpeedColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("averageSpeed"));
        logger.debug("Initialized.");
    }

    public void setServiceAndFillTableWithData(Service service) throws ServiceException{
        this.service = service;
        raceIdCheckBoxInRacesTab.setSelected(false);
        horseIdCheckBoxInRacesTab.setSelected(false);
        jockeyIdCheckBoxInRacesTab.setSelected(false);
        onRacesCheckBoxesSelected();
        List<RaceResult> list = service.searchRaceResults(new RaceResult());
        raceResultsTable.setItems(FXCollections.observableArrayList(list));
    }

    public void setRaceId(Integer raceId){
        raceIdCheckBoxInRacesTab.setSelected(true);
        raceIdTextFieldInRacesTab.setDisable(false);
        raceIdTextFieldInRacesTab.setText(raceId + "");
        jockeyIdCheckBoxInRacesTab.setSelected(false);
        horseIdCheckBoxInRacesTab.setSelected(false);
        onRacesCheckBoxesSelected();
    }

    @FXML
    public void onRacesCheckBoxesSelected(){
        setDisabled(raceIdTextFieldInRacesTab, raceIdCheckBoxInRacesTab, raceIdTextFieldInRacesTab, raceIdTextFieldInRacesTab, raceIdCheckBoxInRacesTab, raceIdCheckBoxInRacesTab);
        setDisabled(horseIdTextFieldInRacesTab, horseIdCheckBoxInRacesTab, horseIdTextFieldInRacesTab, horseIdTextFieldInRacesTab, horseIdCheckBoxInRacesTab, horseIdCheckBoxInRacesTab);
        setDisabled(jockeyIdTextFieldInRacesTab, jockeyIdCheckBoxInRacesTab, jockeyIdTextFieldInRacesTab, jockeyIdTextFieldInRacesTab, jockeyIdCheckBoxInRacesTab, jockeyIdCheckBoxInRacesTab);
        onTypingInRacesTextFields();
    }

    @FXML
    public void onTypingInRacesTextFields(){
        boolean ok1 = validateInput(raceIdTextFieldInRacesTab,raceIdCheckBoxInRacesTab, raceIdTextFieldInRacesTab, REGEXTFORINTEGERS);
        boolean ok2 = validateInput(horseIdTextFieldInRacesTab,horseIdCheckBoxInRacesTab, horseIdTextFieldInRacesTab, REGEXTFORINTEGERS);
        boolean ok3 = validateInput(jockeyIdTextFieldInRacesTab,jockeyIdCheckBoxInRacesTab, jockeyIdTextFieldInRacesTab, REGEXTFORINTEGERS);
        raceSearchButton.setDisable(!(ok1 && ok2 && ok3));
    }

    @FXML
    public void onRaceSearchButtonClicked(){
        logger.info("User clicked 'Search'");
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
            if(list.isEmpty()){
                showAlertDialog("Search race results", "", "No race results found", Alert.AlertType.INFORMATION);
            }
        }
        catch (ServiceException e) {
            showAlertDialog("Search race results", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
        }

    }

}

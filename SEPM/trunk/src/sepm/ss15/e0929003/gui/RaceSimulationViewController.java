package sepm.ss15.e0929003.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.List;
import java.util.Optional;

public class RaceSimulationViewController {

    private Service service;
    private Stage stage;
    private boolean horseIsSelected;
    private boolean jockeyIsSelected;
    private RacesViewController racesController;

    @FXML
    private TableView<Horse> horseTable;
    @FXML
    private TableColumn<Horse,String> horseIdColumn,horseNameColumn;
    @FXML
    private TableView<Jockey> jockeyTable;
    @FXML
    private TableColumn<Jockey,String> jockeyIdColumn,jockeyFirstNameColumn,jockeyLastNameColumn;
    @FXML
    private TableView<RaceResult> raceResultsTable;
    @FXML
    private TableColumn<RaceResult,String> raceResultHorseIdColumn,raceResultJockeyIdColumn,raceResultHorseNameColumn,jockeyNameColumn;
    @FXML
    private Button addToRaceButton,startSimulationButton;

    public void initialize(){
        horseIdColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("id"));
        horseNameColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("name"));
        jockeyIdColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("id"));
        jockeyFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("firstName"));
        jockeyLastNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("lastName"));
        raceResultHorseIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseId"));
        raceResultJockeyIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyId"));
        raceResultHorseNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseName"));
        jockeyNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyName"));
        horseTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Horse>() {
            public void changed(ObservableValue<? extends Horse> observable, Horse oldValue, Horse newValue) {
                if(newValue!=null){
                    horseIsSelected = true;
                }
                else{
                    horseIsSelected = false;
                }
                addToRaceButton.setDisable(!(horseIsSelected&&jockeyIsSelected));
            }
        });
        jockeyTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Jockey>() {
            public void changed(ObservableValue<? extends Jockey> observable, Jockey oldValue, Jockey newValue) {
                if(newValue!=null){
                    jockeyIsSelected = true;
                }
                else{
                   jockeyIsSelected = false;
                }
                addToRaceButton.setDisable(!(horseIsSelected&&jockeyIsSelected));
            }
        });
        raceResultsTable.setRowFactory(new Callback<TableView<RaceResult>, TableRow<RaceResult>>() {
            @Override
            public TableRow<RaceResult> call(TableView<RaceResult> tableView) {
                final TableRow<RaceResult> row = new TableRow<RaceResult>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Remove from race");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        onRemoveFromListClicked();
                    }
                });
                contextMenu.getItems().add(deleteMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setRacesViewController(RacesViewController racesController){
        this.racesController = racesController;
    }

    public void setServiceAndAddAvailableHorsesAndJockeys(Service service) throws ServiceException{
        this.service = service;
        List<Horse> horsesList = service.searchHorses(new Horse(null,null,1,50.0,50.0,null,null),new Horse(null,null,40,100.0,100.0,null,null));
        horseTable.setItems(FXCollections.observableArrayList(horsesList));
        List<Jockey> jockeysList = service.searchJockeys(new Jockey(),new Jockey());
        jockeyTable.setItems(FXCollections.observableArrayList(jockeysList));
        horseTable.getSelectionModel().selectFirst();
        jockeyTable.getSelectionModel().selectFirst();
        service.createNewRace();
    }

    @FXML
    public void onAddToRaceButtonClicked(){
        try {
            Jockey selectedJockey = jockeyTable.getSelectionModel().getSelectedItem();
            Horse selectedHorse = horseTable.getSelectionModel().getSelectedItem();
            service.addJockeyAndHorseToRace(selectedJockey, selectedHorse);
            jockeyTable.getItems().remove(selectedJockey);
            horseTable.getItems().remove(selectedHorse);
            raceResultsTable.getItems().add(new RaceResult(null, selectedHorse.getId(), selectedJockey.getId(), selectedHorse.getName(), selectedJockey.getFirstName() + " " + selectedJockey.getLastName(), null, null, null, null, null));
            horseTable.getSelectionModel().selectBelowCell();
            jockeyTable.getSelectionModel().selectBelowCell();
            if(!raceResultsTable.getItems().isEmpty()){
                startSimulationButton.setDisable(false);
            }
        } catch (ServiceException e) {
            racesController.showAlertDialog("Add horse and jockey to race", "Couldn't add horse and jockey to this race.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onStartSimulationButtonClicked(){
        Optional<ButtonType> result = racesController.showAlertDialog("Race Simulation", "", "Start race simulation?", Alert.AlertType.CONFIRMATION);
        if (result.get() == ButtonType.OK) {
            try {
                List<RaceResult> results = service.startRaceSimulation();
                racesController.setRaceId(results.get(0).getRaceId());
                racesController.onRaceSearchButtonClicked();
                stage.close();
                racesController.showAlertDialog("Race simulation", "", "Race simulation successful!", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                racesController.showAlertDialog("Race simulation", "Race simulation failed.", e.getMessage(), Alert.AlertType.WARNING);
            }
        }

    }

    @FXML
    public void onAbortRaceButtonClicked(){
        Optional<ButtonType> result = racesController.showAlertDialog("Abort race", "", "Abort race? Data will be lost.", Alert.AlertType.CONFIRMATION);
        if (result.get() == ButtonType.OK) {
            stage.close();
        }
    }

    private void onRemoveFromListClicked(){
        RaceResult selectedRaceResult = raceResultsTable.getSelectionModel().getSelectedItem();
        Integer jockeyId = selectedRaceResult.getJockeyId();
        Integer horseId = selectedRaceResult.getHorseId();
        try {
            service.removeJockeyAndHorseFromRace(new Jockey(jockeyId,"dummy","dummy","dummy",0.0,false),new Horse(horseId,"dummy",20,75.0,76.0,"dummy",false));
            horseTable.getItems().add(service.getLastRemovedHorseFromRace());
            jockeyTable.getItems().add(service.getLastRemovedJockeyFromRace());
            raceResultsTable.getItems().remove(selectedRaceResult);
            horseTable.getSelectionModel().selectLast();
            jockeyTable.getSelectionModel().selectLast();
            if(raceResultsTable.getItems().isEmpty()){
                startSimulationButton.setDisable(true);
            }
        } catch (ServiceException e) {
            racesController.showAlertDialog("Remove horse and jockey from race", "Couldn't remove jockey and horse from this race", e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}

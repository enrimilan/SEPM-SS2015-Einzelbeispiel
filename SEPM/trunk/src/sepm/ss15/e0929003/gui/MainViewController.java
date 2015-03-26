package sepm.ss15.e0929003.gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.IOException;
import java.util.Optional;

public class MainViewController<T> {

    protected final String REGEXFORDOUBLES ="^[-]?[0-9]+(\\.[0-9][0-9]?)?$";
    protected final String REGEXTFORINTEGERS="^[-]?[0-9][0-9]?$";
    protected Service service;
    protected Stage primaryStage;

    @FXML
    private HorsesViewController horsesController;
    @FXML
    private JockeysViewController jockeysController;
    @FXML
    private StatisticsViewController statisticsController;
    @FXML
    private RacesViewController racesController;

    public void initialize() {
    }

    public void setServicesAndFillTablesWithData(Service service) throws ServiceException {
        this.service = service;
        horsesController.setServiceAndFillTableWithData(service);
        jockeysController.setServiceAndFillTableWithData(service);
        racesController.setServiceAndFillTableWithData(service);
        statisticsController.setService(service);
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
        horsesController.setPrimaryStage(primaryStage);
        jockeysController.setPrimaryStage(primaryStage);
    }

    @FXML
    public void onNewHorseClicked() throws IOException{
        horsesController.openNewWindow(Mode.CREATE);
    }

    @FXML
    public void onNewJockeyClicked() throws IOException{
        jockeysController.openNewWindow(Mode.CREATE);
    }

    @FXML
    private void onNewRaceClicked(){
        //TODO
    }

    @FXML
    public void onExitClicked(){
        Optional<ButtonType> result = showAlertDialog("Exit", "", "Exit application?", Alert.AlertType.CONFIRMATION);
        if (result.get() == ButtonType.OK) {
            primaryStage.close();
        }
    }

    @FXML
    public void onAboutClicked(){
        showAlertDialog("About","","Wendy's Rennpferde\nVersion 1.0",Alert.AlertType.INFORMATION);
    }

    protected void setDisabled(Parent parent, CheckBox checkBox, TextField textField1, TextField textField2, Labeled labeled1, Labeled labeled2){
        if(checkBox.isSelected()){
            parent.setDisable(false);
        }
        else{
            textField1.setText("");
            textField2.setText("");
            parent.setDisable(true);
            labeled1.setTextFill(Color.web("#000000"));
            labeled2.setTextFill(Color.web("#000000"));
        }
    }

    protected void setRowFactory(final Mode mode,TableView<T> tableView){
        tableView.setRowFactory(new Callback<TableView<T>, TableRow<T>>() {
            @Override
            public TableRow<T> call(TableView<T> tableView) {
                final TableRow<T> row = new TableRow<T>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Delete");
                final MenuItem editMenuItem = new MenuItem("Edit");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        onDeleteClicked();
                    }
                });
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            openNewWindow(mode);
                        } catch (IOException e) {}
                    }
                });
                contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    protected void onDeleteClicked(){

    }

    public void openNewWindow(Mode mode) throws IOException {
    }


    protected boolean validateInput(TextField textField, Labeled labeled, Parent parent, String regex){
        if(parent.isDisabled()){
            return true;
        }
        if(!textField.getText().matches(regex)){
            labeled.setTextFill(Color.web("#FF0000"));
            return false;
        }
        else{
            labeled.setTextFill(Color.web("#000000"));
            return true;
        }
    }

    protected Optional showAlertDialog(String title, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }
}

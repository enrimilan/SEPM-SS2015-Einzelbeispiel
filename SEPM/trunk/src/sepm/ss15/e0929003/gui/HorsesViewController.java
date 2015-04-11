package sepm.ss15.e0929003.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public class HorsesViewController extends MainViewController {

    @FXML
    private TableView<Horse> horseTable;
    @FXML
    private TableColumn<Horse,String> horseIdColumn,horseNameColumn,horseAgeColumn,horseMinSpeedColumn,horseMaxSpeedColumn;
    @FXML
    private CheckBox ageCheckBox,minSpeedCheckBox,maxSpeedCheckBox;
    @FXML
    private HBox ageHBox,minSpeedHBox,maxSpeedHBox;
    @FXML
    private TextField fromAgeTextField,toAgeTextField,fromMinSpeedTextField,toMinSpeedTextField,fromMaxSpeedTextField,toMaxSpeedTextField;
    @FXML
    private Label fromAgeLabel,toAgeLabel,fromMinSpeedLabel,toMinSpeedLabel,fromMaxSpeedLabel,toMaxSpeedLabel;
    @FXML
    private Button horseSearchButton;
    @FXML
    private ImageView horseImageView;

    public void initialize() {
        horseIdColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("id"));
        horseNameColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("name"));
        horseAgeColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("age"));
        horseMinSpeedColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("minSpeed"));
        horseMaxSpeedColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("maxSpeed"));
        horseTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Horse>() {
            public void changed(ObservableValue<? extends Horse> observable, Horse oldValue, Horse newValue) {
                if(newValue==null || newValue.getPicture().isEmpty()){
                    horseImageView.setVisible(false);
                }
                else{
                    try {
                        File file = new File(newValue.getPicture());
                        Image value = new Image(file.toURI().toURL().toExternalForm());
                        horseImageView.setImage(value);
                        horseImageView.setVisible(true);
                    } catch (MalformedURLException e) {
                        horseImageView.setVisible(false);
                    }
                }
            }
        });
        setRowFactory(Mode.EDIT,horseTable);
    }

    public void setServiceAndFillTableWithData(Service service) throws ServiceException{
        this.service = service;
        ageCheckBox.setSelected(false);
        minSpeedCheckBox.setSelected(false);
        maxSpeedCheckBox.setSelected(false);
        onHorseCheckBoxesSelected();
        List<Horse> list = service.searchHorses(new Horse(null,null,1,50.0,50.0,null,null),new Horse(null,null,40,100.0,100.0,null,null));
        horseTable.setItems(FXCollections.observableArrayList(list));
        horseTable.getSelectionModel().selectFirst();
    }

    public Service getService(){
        return service;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public TableView<Horse> getHorseTable() {
        return horseTable;
    }

    public ImageView getHorseImageView(){
        return horseImageView;
    }

    @FXML
    public void onHorseCheckBoxesSelected(){
        setDisabled(ageHBox, ageCheckBox, fromAgeTextField, toAgeTextField, fromAgeLabel, toAgeLabel);
        setDisabled(minSpeedHBox, minSpeedCheckBox, fromMinSpeedTextField, toMinSpeedTextField, fromMinSpeedLabel, toMinSpeedLabel);
        setDisabled(maxSpeedHBox, maxSpeedCheckBox, fromMaxSpeedTextField, toMaxSpeedTextField, fromMaxSpeedLabel, toMaxSpeedLabel);
        onTypingInHorseTextFields();
    }
    @FXML
    public void onTypingInHorseTextFields(){
        boolean ok1 = validateInput(fromAgeTextField, fromAgeLabel,ageHBox, REGEXTFORINTEGERS);
        boolean ok2 = validateInput(toAgeTextField, toAgeLabel,ageHBox, REGEXTFORINTEGERS);
        boolean ok3 = validateInput(fromMinSpeedTextField, fromMinSpeedLabel,minSpeedHBox, REGEXFORDOUBLES);
        boolean ok4 = validateInput(toMinSpeedTextField, toMinSpeedLabel, minSpeedHBox, REGEXFORDOUBLES);
        boolean ok5 = validateInput(fromMaxSpeedTextField, fromMaxSpeedLabel, maxSpeedHBox, REGEXFORDOUBLES);
        boolean ok6 = validateInput(toMaxSpeedTextField, toMaxSpeedLabel, maxSpeedHBox, REGEXFORDOUBLES);
        horseSearchButton.setDisable(!(ok1&&ok2&&ok3&&ok4&&ok5&&ok6));
    }
    @FXML
    public void onHorseSearchButtonClicked(){
        List<Horse> list = null;
        Integer fromAge=null,toAge=null;
        Double fromMinSpeed=null,toMinSpeed=null,fromMaxSpeed=null,toMaxSpeed=null;
        if(!fromAgeTextField.getText().isEmpty() && !toAgeTextField.getText().isEmpty()) {
            fromAge = Integer.valueOf(fromAgeTextField.getText());
            toAge = Integer.valueOf(toAgeTextField.getText());
        }
        else {
            fromAge = 1;
            toAge = 40;
        }
        if(!fromMinSpeedTextField.getText().isEmpty() && !toMinSpeedTextField.getText().isEmpty()) {
            fromMinSpeed = Double.valueOf(fromMinSpeedTextField.getText());
            toMinSpeed = Double.valueOf(toMinSpeedTextField.getText());
        }
        else {
            fromMinSpeed = 50.0;
            toMinSpeed = 100.0;
        }
        if(!fromMaxSpeedTextField.getText().isEmpty() && !toMaxSpeedTextField.getText().isEmpty()) {
            fromMaxSpeed = Double.valueOf(fromMaxSpeedTextField.getText());
            toMaxSpeed = Double.valueOf(toMaxSpeedTextField.getText());
        }
        else {
            fromMaxSpeed = 50.0;
            toMaxSpeed = 100.0;
        }
        try {
            list = service.searchHorses(new Horse(null,null,fromAge,fromMinSpeed,fromMaxSpeed,null,null),new Horse(null,null,toAge,toMinSpeed,toMaxSpeed,null,null));
            horseTable.setItems(FXCollections.observableArrayList(list));
            horseTable.getSelectionModel().selectFirst();
            horseTable.getColumns().get(0).setVisible(false);
            horseTable.getColumns().get(0).setVisible(true);
            if(list.isEmpty()){
                showAlertDialog("Search horses", "", "No horses found", Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            showAlertDialog("Search horses", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @Override
    public void openNewWindow(Mode mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HorseEditCreateView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        HorseEditCreateViewController controller = loader.getController();
        controller.setMode(mode);
        controller.setStage(stage);
        controller.setHorsesViewController(this);
        stage.showAndWait();
    }

    @Override
    public void onDeleteClicked(){
        Horse h = horseTable.getSelectionModel().getSelectedItem();
        try {
            Optional<ButtonType> result = showAlertDialog("Delete horse", "", "Are you sure you want to delete this horse?", Alert.AlertType.CONFIRMATION);
            if (result.get() == ButtonType.OK) {
                service.deleteHorse(h);
                horseTable.getItems().remove(h);
                showAlertDialog("Delete horse", "", "Horse deleted successfully.", Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            showAlertDialog("Delete horse", "Couldn't delete horse.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

}

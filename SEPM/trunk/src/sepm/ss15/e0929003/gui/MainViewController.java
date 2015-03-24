package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainViewController {

    private Stage primaryStage;
    private Service service;
    private HorseController horseController;
    private JockeyController jockeyController;

    @FXML
    private TableView<Jockey> jockeysTable;
    @FXML
    private TableColumn<Jockey,String> jockeyIdColumn,jockeyFirstNameColumn,jockeyLastNameColumn,jockeyCountryColumn,jockeySkillColumn;
    @FXML
    private TableView<Horse> horsesTable;
    @FXML
    private TableColumn<Horse,String> horseIdColumn,horseNameColumn,horseAgeColumn,horseMinSpeedColumn,horseMaxSpeedColumn;
    @FXML
    private CheckBox skillCheckBox,ageCheckBox,minSpeedCheckBox,maxSpeedCheckBox;
    @FXML
    private HBox skillHBox,ageHBox,minSpeedHBox,maxSpeedHBox;
    @FXML
    private TextField fromSkillTextField,toSkillTextField,fromAgeTextField,toAgeTextField,fromMinSpeedTextField,toMinSpeedTextField,fromMaxSpeedTextField,toMaxSpeedTextField;
    @FXML
    private Label fromSkillLabel,toSkillLabel,fromAgeLabel,toAgeLabel,fromMinSpeedLabel,toMinSpeedLabel,fromMaxSpeedLabel,toMaxSpeedLabel;
    @FXML
    private Button jockeySearchButton,horseSearchButton;
    @FXML
    private ImageView horseImageView;

    @FXML
    private void initialize() {
        initializeHorseTable();
        initializeJockeyTable();
    }

    private void initializeHorseTable(){
        horseIdColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("id"));
        horseNameColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("name"));
        horseAgeColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("age"));
        horseMinSpeedColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("minSpeed"));
        horseMaxSpeedColumn.setCellValueFactory(new PropertyValueFactory<Horse, String>("maxSpeed"));
    }

    private void initializeJockeyTable(){
        jockeyIdColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("id"));
        jockeyFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("firstName"));
        jockeyLastNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("lastName"));
        jockeyCountryColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("country"));
        jockeySkillColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("skill"));
    }

    public TableView<Horse> getHorsesTable() {
        return horsesTable;
    }

    public TableView<Jockey> getJockeysTable() {
        return jockeysTable;
    }

    public Service getService() {
        return service;
    }

    public void setServiceAndControllers(Service service){
        this.service = service;
        this.horseController = new HorseController(this);
        this.jockeyController = new JockeyController(this);
    }

    public void fillTablesWithData(){
        try {
            List<Jockey> jockeyList =  service.searchJockeys(new Jockey(),new Jockey());
            jockeysTable.setItems(FXCollections.observableArrayList(jockeyList));
            List<Horse> horseList = service.searchHorses(new Horse(null, null, 1, 50.0, 50.0, null, null), new Horse(null, null, 40, 100.0, 100.0, null, null));
            horsesTable.setItems(FXCollections.observableArrayList(horseList));
        } catch (ServiceException e) {
            showAlertDialog("Warning","",e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public ImageView getHorseImageView(){
        return horseImageView;
    }

    @FXML
    public void onSkillChecked(){
        setHBoxDisabled(skillHBox, skillCheckBox, fromSkillTextField, toSkillTextField, fromSkillLabel, toSkillLabel);
        onTypingInJockeyTextFields();
    }

    @FXML
    public void onHorseCheckBoxSelected(){
        setHBoxDisabled(ageHBox, ageCheckBox, fromAgeTextField, toAgeTextField, fromAgeLabel, toAgeLabel);
        setHBoxDisabled(minSpeedHBox, minSpeedCheckBox, fromMinSpeedTextField, toMinSpeedTextField, fromMinSpeedLabel, toMinSpeedLabel);
        setHBoxDisabled(maxSpeedHBox, maxSpeedCheckBox, fromMaxSpeedTextField, toMaxSpeedTextField, fromMaxSpeedLabel, toMaxSpeedLabel);
        onTypingInHorseTextFields();
    }

    @FXML
    public void onTypingInHorseTextFields(){
        boolean ok1 = validateInput(fromAgeTextField, fromAgeLabel, ageHBox);
        boolean ok2 = validateInput(toAgeTextField, toAgeLabel, ageHBox);
        boolean ok3 = validateInput(fromMinSpeedTextField, fromMinSpeedLabel, minSpeedHBox);
        boolean ok4 = validateInput(toMinSpeedTextField, toMinSpeedLabel, minSpeedHBox);
        boolean ok5 = validateInput(fromMaxSpeedTextField, fromMaxSpeedLabel, maxSpeedHBox);
        boolean ok6 = validateInput(toMaxSpeedTextField, toMaxSpeedLabel, maxSpeedHBox);
        horseSearchButton.setDisable(!(ok1&&ok2&&ok3&&ok4&&ok5&&ok6));
    }

    @FXML
    public void onTypingInJockeyTextFields(){
        boolean ok1 = validateInput(fromSkillTextField, fromSkillLabel,skillHBox);
        boolean ok2 = validateInput(toSkillTextField, toSkillLabel,skillHBox);
        jockeySearchButton.setDisable(!(ok1&&ok2));
    }

    @FXML
    public void onHorseSearchButtonClicked(){
        horseController.searchClicked(fromAgeTextField,toAgeTextField,fromMinSpeedTextField,toMinSpeedTextField,fromMaxSpeedTextField,toMaxSpeedTextField);
    }

    @FXML
    public void onJockeySearchButtonClicked(){
        jockeyController.searchClicked(skillCheckBox, fromSkillTextField, toSkillTextField);
    }

    @FXML
    public void newHorseClicked() throws IOException{
        horseController.editOrCreateClicked("HorseEditCreateView.fxml", Mode.CREATE);
    }

    @FXML
    public void newJockeyClicked() throws IOException{
        jockeyController.editOrCreateClicked("JockeyEditCreateView.fxml",Mode.CREATE);
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

    private void setHBoxDisabled(HBox hBox, CheckBox checkBox, TextField fromTextField, TextField toTextField, Label fromLabel, Label toLabel){
        if(checkBox.isSelected()){
            hBox.setDisable(false);
        }
        else{
            fromTextField.setText("");
            toTextField.setText("");
            hBox.setDisable(true);
            fromLabel.setTextFill(Color.web("#000000"));
            toLabel.setTextFill(Color.web("#000000"));
        }
    }

    private boolean validateInput(TextField textField, Label label, HBox hBox){
        if(hBox.isDisabled()){
            return true;
        }
        if(!textField.getText().matches("^[-]?[0-9]+(\\.[0-9][0-9]?)?$")){
            label.setTextFill(Color.web("#FF0000"));
            return false;
        }
        else{
            label.setTextFill(Color.web("#000000"));
            return true;
        }
    }

    public Optional showAlertDialog(String title, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

}

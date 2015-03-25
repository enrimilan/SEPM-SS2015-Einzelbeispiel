package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MainViewController {

    private final String REGEXFORDOUBLES ="^[-]?[0-9]+(\\.[0-9][0-9]?)?$";
    private final String REGEXTFORINTEGERS="^[-]?[0-9][0-9]?$";
    private Stage primaryStage;
    private Service service;
    private HorseController horseController;
    private JockeyController jockeyController;
    private RaceResultController raceResultController;
    private StatisticsController statisticsController;


    /**************
     * HORSES-TAB *
     **************/
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


    /***************
     * JOCKEYS-TAB *
     ***************/
    @FXML
    private TableView<Jockey> jockeysTable;
    @FXML
    private TableColumn<Jockey,String> jockeyIdColumn,jockeyFirstNameColumn,jockeyLastNameColumn,jockeyCountryColumn,jockeySkillColumn;
    @FXML
    private CheckBox skillCheckBox;
    @FXML
    private HBox skillHBox;
    @FXML
    private TextField fromSkillTextField,toSkillTextField;
    @FXML
    private Label fromSkillLabel,toSkillLabel;
    @FXML
    private Button jockeySearchButton;


    /*************
     * RACES-TAB *
     *************/
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


    /******************
     * STATISTICS-TAB *
     ******************/
    @FXML
    private CheckBox horseIdCheckBoxInStatisticsTab,jockeyIdCheckBoxInStatisticsTab;
    @FXML
    private TextField horseIdTextFieldInStatisticsTab,jockeyIdTextFieldInStatisticsTab;
    @FXML
    private Button showStatisticsButton;
    @FXML
    private BarChart chart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    public MainViewController(){

    }

    @FXML
    private void initialize() {
        initializeHorseTable();
        initializeJockeyTable();
        initializeRaceResultsTable();
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

    private void initializeRaceResultsTable(){
        raceResultIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("raceId"));
        raceResultHorseIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseId"));
        raceResultJockeyIdColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyId"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("rank"));
        raceResultHorseNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("horseName"));
        jockeyNameColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("jockeyName"));
        randomSpeedColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("randomSpeed"));
        luckFactorColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("luckFactor"));
        skillColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("skill"));
        averageSpeedColumn.setCellValueFactory(new PropertyValueFactory<RaceResult, String>("averageSpeed"));
    }

    public TableView<Horse> getHorseTable() {
        return horseTable;
    }

    public TableView<Jockey> getJockeysTable() {
        return jockeysTable;
    }

    public TableView<RaceResult> getRaceResultsTable() {
        return raceResultsTable;
    }

    public Service getService() {
        return service;
    }

    public void setServiceAndInitializeControllers(Service service){
        this.service = service;
        this.horseController = new HorseController(this);
        this.jockeyController = new JockeyController(this);
        this.raceResultController = new RaceResultController(this);
        this.statisticsController = new StatisticsController(this,chart,xAxis,yAxis);
    }

    public void fillTablesWithData(){
        try {
            List<Jockey> jockeyList =  service.searchJockeys(new Jockey(),new Jockey());
            jockeysTable.setItems(FXCollections.observableArrayList(jockeyList));
            List<Horse> horseList = service.searchHorses(new Horse(null, null, 1, 50.0, 50.0, null, null), new Horse(null, null, 40, 100.0, 100.0, null, null));
            horseTable.setItems(FXCollections.observableArrayList(horseList));
            List<RaceResult> raceResultList = service.searchRaceResults(new RaceResult());
            raceResultsTable.setItems(FXCollections.observableArrayList(raceResultList));
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


    /**************************
     * HORSES-TAB - LISTENERS *
     **************************/
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
        horseController.searchClicked(fromAgeTextField,toAgeTextField,fromMinSpeedTextField,toMinSpeedTextField,fromMaxSpeedTextField,toMaxSpeedTextField);
    }


    /***************************
     * JOCKEYS-TAB - LISTENERS *
     ***************************/

    @FXML
    public void onSkillCheckBoxSelected(){
        setDisabled(skillHBox, skillCheckBox, fromSkillTextField, toSkillTextField, fromSkillLabel, toSkillLabel);
        onTypingInJockeyTextFields();
    }

    @FXML
    public void onTypingInJockeyTextFields(){
        boolean ok1 = validateInput(fromSkillTextField, fromSkillLabel, skillHBox, REGEXFORDOUBLES);
        boolean ok2 = validateInput(toSkillTextField, toSkillLabel, skillHBox, REGEXFORDOUBLES);
        jockeySearchButton.setDisable(!(ok1&&ok2));
    }

    @FXML
    public void onJockeySearchButtonClicked(){
        jockeyController.searchClicked(skillCheckBox, fromSkillTextField, toSkillTextField);
    }


    /*************************
     * RACES-TAB - LISTENERS *
     *************************/
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
        raceResultController.searchRaceResults(raceIdTextFieldInRacesTab,horseIdTextFieldInRacesTab,jockeyIdTextFieldInRacesTab);
    }


    /******************************
     * STATISTICS-TAB - LISTENERS *
     ******************************/
    @FXML
    public void onStatisticsCheckBoxesSelected(){
        setDisabled(horseIdTextFieldInStatisticsTab, horseIdCheckBoxInStatisticsTab, horseIdTextFieldInStatisticsTab, horseIdTextFieldInStatisticsTab, horseIdCheckBoxInStatisticsTab, horseIdCheckBoxInStatisticsTab);
        setDisabled(jockeyIdTextFieldInStatisticsTab, jockeyIdCheckBoxInStatisticsTab, jockeyIdTextFieldInStatisticsTab, jockeyIdTextFieldInStatisticsTab, jockeyIdCheckBoxInStatisticsTab, jockeyIdCheckBoxInStatisticsTab);
        onTypingInStatisticsTextFields();
    }

    @FXML
    public void onTypingInStatisticsTextFields(){
        boolean ok1 = validateInput(horseIdTextFieldInStatisticsTab,horseIdCheckBoxInStatisticsTab, horseIdTextFieldInStatisticsTab, REGEXTFORINTEGERS);
        boolean ok2 = validateInput(jockeyIdTextFieldInStatisticsTab,jockeyIdCheckBoxInStatisticsTab, jockeyIdTextFieldInStatisticsTab, REGEXTFORINTEGERS);
        showStatisticsButton.setDisable(!(ok1&&ok2&&(horseIdCheckBoxInStatisticsTab.isSelected() || jockeyIdCheckBoxInStatisticsTab.isSelected())));
    }

    @FXML
    public void onShowStatisticsClicked(){
        statisticsController.onShowStatisticsClicked(horseIdTextFieldInStatisticsTab,jockeyIdTextFieldInStatisticsTab);
    }


    /************
     * MENU-BAR *
     ************/
    @FXML
    public void onNewHorseClicked() throws IOException{
        horseController.openNewWindow("HorseEditCreateView.fxml", Mode.CREATE);
    }

    @FXML
    public void onNewJockeyClicked() throws IOException{
        jockeyController.openNewWindow("JockeyEditCreateView.fxml", Mode.CREATE);
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

    private void setDisabled(Parent parent, CheckBox checkBox, TextField textField1, TextField textField2, Labeled labeled1, Labeled labeled2){
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

    private boolean validateInput(TextField textField, Labeled labeled, Parent parent, String regex){
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

    public Optional showAlertDialog(String title, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

}

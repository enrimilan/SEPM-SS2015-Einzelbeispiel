package sepm.ss15.e0929003.gui;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JockeysViewController extends MainViewController {

    @FXML
    private TableView<Jockey> jockeyTable;
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

    public void initialize() {
        jockeyIdColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("id"));
        jockeyFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("firstName"));
        jockeyLastNameColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("lastName"));
        jockeyCountryColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("country"));
        jockeySkillColumn.setCellValueFactory(new PropertyValueFactory<Jockey, String>("skill"));
        setRowFactory(Mode.EDIT, jockeyTable);
    }

    public void setServiceAndFillTableWithData(Service service) throws ServiceException{
        this.service = service;
        skillCheckBox.setSelected(false);
        onSkillCheckBoxSelected();
        List<Jockey> list = service.searchJockeys(new Jockey(),new Jockey());
        jockeyTable.setItems(FXCollections.observableArrayList(list));
    }

    public Service getService() {
        return service;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public TableView<Jockey> getJockeyTable() {
        return jockeyTable;
    }

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
        List<Jockey> list = null;
        try {
            if(skillCheckBox.isSelected()){
                list = service.searchJockeys(new Jockey(null,null,null,null,Double.valueOf(fromSkillTextField.getText()),null),new Jockey(null,null,null,null,Double.valueOf(toSkillTextField.getText()),null));
            }
            else{
                list = service.searchJockeys(new Jockey(),new Jockey());
            }
            jockeyTable.setItems(FXCollections.observableArrayList(list));
            jockeyTable.getSelectionModel().selectFirst();
            jockeyTable.getColumns().get(0).setVisible(false);
            jockeyTable.getColumns().get(0).setVisible(true);
            if(list.isEmpty()){
                showAlertDialog("Search jockeys", "", "No jockeys found", Alert.AlertType.INFORMATION);
            }

        } catch (ServiceException e) {
            showAlertDialog("Search jockeys", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @Override
    public void openNewWindow(Mode mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JockeyEditCreateView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        JockeyEditCreateViewController controller = loader.getController();
        controller.setMode(mode);
        controller.setStage(stage);
        controller.setJockeysViewController(this);
        stage.showAndWait();
    }

    @Override
    public void onDeleteClicked(){
        Jockey j = jockeyTable.getSelectionModel().getSelectedItem();
        try {
            Optional<ButtonType> result = showAlertDialog("Delete jockey", "", "Are you sure you want to delete this jockey?", Alert.AlertType.CONFIRMATION);
            if (result.get() == ButtonType.OK) {
                service.deleteJockey(j);
                jockeyTable.getItems().remove(j);
                showAlertDialog("Delete jockey", "", "Jockey deleted successfully.", Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            showAlertDialog("Delete jockey", "Couldn't delete jockey.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

}

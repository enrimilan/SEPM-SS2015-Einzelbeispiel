package sepm.ss15.e0929003.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;


public class JockeyEditCreateViewController {

    private MainViewController mainViewController;
    private Service service;
    private TableView<Jockey> jockeysTable;
    private Jockey jockey;
    private Mode mode;
    private Stage stage;
    @FXML
    private TextField firstNameTextField, lastNameTextField, countryTextField, skillTextField;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setMainViewControllerAndSetup(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        this.service = mainViewController.getService();
        this.jockeysTable = mainViewController.getJockeysTable();
        if(mode==Mode.CREATE){
            this.jockey = new Jockey();
            stage.setTitle("New jockey");
            skillTextField.setText(0.0 + "");
            skillTextField.setDisable(true);
        }
        else if(mode== Mode.EDIT){
            this.jockey = jockeysTable.getSelectionModel().getSelectedItem();
            stage.setTitle("Edit jockey");
            firstNameTextField.setText(jockey.getFirstName());
            lastNameTextField.setText(jockey.getLastName());
            countryTextField.setText(jockey.getCountry());
            skillTextField.setText(jockey.getSkill() + "");
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void onOkButtonClicked() {
        jockey.setFirstName(firstNameTextField.getText());
        jockey.setLastName(lastNameTextField.getText());
        jockey.setCountry(countryTextField.getText());
        jockey.setSkill(Double.valueOf(skillTextField.getText()));
        if (mode == Mode.CREATE) {
            try {
                Jockey j = service.createJockey(jockey);
                jockeysTable.getItems().add(j);
                stage.close();
                mainViewController.showAlertDialog("Create jockey", "", "Jockey created successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                mainViewController.showAlertDialog("Create jockey", "Couldn't create new jockey.", e.getMessage(), Alert.AlertType.INFORMATION);
            }
        } else if (mode == Mode.EDIT) {
            try {
                service.editJockey(jockey);
                jockeysTable.getColumns().get(0).setVisible(false);
                jockeysTable.getColumns().get(0).setVisible(true);
                stage.close();
                mainViewController.showAlertDialog("Edit jockey", "", "Jockey updated successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                mainViewController.showAlertDialog("Edit jockey", "Couldn't update jockey.", e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }

    }

    @FXML
    public void onCancelButtonClicked(){
        stage.close();
    }
}

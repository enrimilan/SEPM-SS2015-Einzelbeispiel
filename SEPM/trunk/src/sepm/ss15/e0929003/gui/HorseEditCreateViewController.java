package sepm.ss15.e0929003.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.File;

public class HorseEditCreateViewController {

    private MainViewController mainViewController;
    private Service service;
    private TableView<Horse> horsesTable;
    private Horse horse;
    private Mode mode;
    private Stage stage;
    @FXML
    private TextField nameTextField, ageTextField, minSpeedTextField,maxSpeedTextField,pictureTextField;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setMainViewControllerAndSetup(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        this.service = mainViewController.getService();
        this.horsesTable = mainViewController.getHorseTable();
        if(mode==Mode.CREATE){
            this.horse = new Horse();
            stage.setTitle("New horse");
        }
        else if(mode== Mode.EDIT){
            this.horse = horsesTable.getSelectionModel().getSelectedItem();
            stage.setTitle("Edit horse");
            nameTextField.setText(horse.getName());
            ageTextField.setText(horse.getAge()+"");
            minSpeedTextField.setText(horse.getMinSpeed()+"");
            maxSpeedTextField.setText(horse.getMaxSpeed()+"");
            pictureTextField.setText(horse.getPicture());
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void onBrowseButtonClicked(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose picture");
        FileChooser.ExtensionFilter allowedFileTypes = new FileChooser.ExtensionFilter("Pictures (*.jpg;*.jpeg;*.png)", "*.jpg","*.jpeg","*.png");
        chooser.getExtensionFilters().add(allowedFileTypes);
        File file = chooser.showOpenDialog(stage);
        if(file!=null){
            pictureTextField.setText(file.getAbsolutePath());
        }

    }

    @FXML
    public void onOkButtonClicked() {
        horse.setName(nameTextField.getText());
        horse.setAge(Integer.valueOf(ageTextField.getText()));
        horse.setMinSpeed(Double.valueOf(minSpeedTextField.getText()));
        horse.setMaxSpeed(Double.valueOf(maxSpeedTextField.getText()));
        horse.setPicture(pictureTextField.getText());
        if (mode == Mode.CREATE) {
            try {
                Horse h = service.createHorse(horse);
                horsesTable.getItems().add(h);
                stage.close();
                mainViewController.showAlertDialog("Create horse", "", "Horse created successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                mainViewController.showAlertDialog("Create horse", "Couldn't create new horse.", e.getMessage(), Alert.AlertType.INFORMATION);
            }
        } else if (mode == Mode.EDIT) {
            try {
                service.editHorse(horse);
                horsesTable.getColumns().get(0).setVisible(false);
                horsesTable.getColumns().get(0).setVisible(true);
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

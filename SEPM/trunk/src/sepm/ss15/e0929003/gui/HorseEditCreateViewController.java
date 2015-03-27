package sepm.ss15.e0929003.gui;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.File;

public class HorseEditCreateViewController {

    private HorsesViewController horsesViewController;
    private Service service;
    private TableView<Horse> horseTable;
    private Horse horse;
    private Mode mode;
    private Stage stage;
    @FXML
    private TextField nameTextField, ageTextField, minSpeedTextField,maxSpeedTextField,pictureTextField;
    @FXML
    private Label ageLabel,minSpeedLabel,maxSpeedLabel;
    @FXML
    private Button okButton;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setHorsesViewController(HorsesViewController horsesViewController) {
        this.horsesViewController = horsesViewController;
        this.service = horsesViewController.getService();
        this.horseTable = horsesViewController.getHorseTable();
        if(mode==Mode.CREATE){
            this.horse = new Horse();
            stage.setTitle("New horse");
        }
        else if(mode== Mode.EDIT){
            this.horse = horseTable.getSelectionModel().getSelectedItem();
            stage.setTitle("Edit horse");
            nameTextField.setText(horse.getName());
            ageTextField.setText(horse.getAge()+"");
            minSpeedTextField.setText(horse.getMinSpeed()+"");
            maxSpeedTextField.setText(horse.getMaxSpeed()+"");
            pictureTextField.setText(horse.getPicture());
        }
        okButton.setDisable(!inputIsValid());
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void onTypingInTextFields(){
        okButton.setDisable(!inputIsValid());
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
                horseTable.getItems().add(h);
                stage.close();
                horsesViewController.showAlertDialog("Create horse", "", "Horse created successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                horsesViewController.showAlertDialog("Create horse", "Couldn't create new horse.", e.getMessage(), Alert.AlertType.WARNING);
            }
        } else if (mode == Mode.EDIT) {
            try {
                service.editHorse(horse);
                horseTable.getColumns().get(0).setVisible(false);
                horseTable.getColumns().get(0).setVisible(true);
                stage.close();
                horsesViewController.showAlertDialog("Edit horse", "", "Horse updated successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                horsesViewController.showAlertDialog("Edit horse", "Couldn't update horse.", e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    public void onCancelButtonClicked(){
        stage.close();
    }

    private boolean inputIsValid(){
        boolean ok1 = horsesViewController.validateInput(ageTextField,ageLabel,null,"^[-]?[0-9][0-9]?$");
        boolean ok2 = horsesViewController.validateInput(minSpeedTextField,minSpeedLabel,null,"^[-]?[0-9]+(\\.[0-9][0-9]?)?$");
        boolean ok3 = horsesViewController.validateInput(maxSpeedTextField,maxSpeedLabel,null,"^[-]?[0-9]+(\\.[0-9][0-9]?)?$");
        return ok1 && ok2 && ok3;
    }
}

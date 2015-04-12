package sepm.ss15.e0929003.gui;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.File;
import java.net.MalformedURLException;

public class HorseEditCreateViewController {

    private static final Logger logger = LogManager.getLogger(Main.class);

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
        logger.info("User typed in textfield.");
        okButton.setDisable(!inputIsValid());
    }

    @FXML
    public void onBrowseButtonClicked(){
        logger.info("User clicked 'Browse...'");
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose picture");
        FileChooser.ExtensionFilter allowedFileTypes = new FileChooser.ExtensionFilter("Pictures (*.jpg;*.jpeg;*.png)", "*.jpg","*.jpeg","*.png");
        chooser.getExtensionFilters().add(allowedFileTypes);
        File file = chooser.showOpenDialog(stage);
        if(file!=null){
            logger.info("User chose file {}",file.getAbsolutePath());
            pictureTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void onOkButtonClicked() {
        logger.info("User clicked 'OK'");
        horse.setName(nameTextField.getText());
        horse.setAge(Integer.valueOf(ageTextField.getText()));
        horse.setMinSpeed(Double.valueOf(minSpeedTextField.getText()));
        horse.setMaxSpeed(Double.valueOf(maxSpeedTextField.getText()));
        horse.setPicture(pictureTextField.getText());
        if (mode == Mode.CREATE) {
            try {
                Horse h = service.createHorse(horse);
                if(horsesViewController.getFromAge()<=h.getAge() && horsesViewController.getToAge() >=h.getAge()
                        && horsesViewController.getFromMinSpeed()<=h.getMinSpeed() && horsesViewController.getToMinSpeed() >=h.getMinSpeed()
                        && horsesViewController.getFromMaxSpeed()<=h.getMaxSpeed() && horsesViewController.getToMaxSpeed()>=h.getMaxSpeed()){
                    horseTable.getItems().add(h);
                    horseTable.getSelectionModel().selectLast();
                }
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
                File file = new File(horse.getPicture());
                Image value = new Image(file.toURI().toURL().toExternalForm());
                horsesViewController.getHorseImageView().setImage(value);
                stage.close();
                horsesViewController.showAlertDialog("Edit horse", "", "Horse updated successfully.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                horsesViewController.showAlertDialog("Edit horse", "Couldn't update horse.", e.getMessage(), Alert.AlertType.WARNING);
            } catch (MalformedURLException e) {
                horsesViewController.showAlertDialog("Edit horse", "Couldn't update horse.", e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    public void onCancelButtonClicked(){
        logger.info("User clicked 'Cancel'");
        stage.close();
    }

    private boolean inputIsValid(){
        boolean ok1 = horsesViewController.validateInput(ageTextField,ageLabel,null,"^[-]?[0-9][0-9]?$");
        boolean ok2 = horsesViewController.validateInput(minSpeedTextField,minSpeedLabel,null,"^[-]?[0-9]+(\\.[0-9][0-9]?)?$");
        boolean ok3 = horsesViewController.validateInput(maxSpeedTextField,maxSpeedLabel,null,"^[-]?[0-9]+(\\.[0-9][0-9]?)?$");
        return ok1 && ok2 && ok3;
    }
}

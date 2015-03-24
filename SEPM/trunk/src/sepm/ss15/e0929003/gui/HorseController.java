package sepm.ss15.e0929003.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sepm.ss15.e0929003.entities.Horse;
import sepm.ss15.e0929003.service.ServiceException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public class HorseController extends AbstractController {

    private ImageView horseImageView;
    private TableView<Horse> horsesTable;

    public HorseController(MainViewController mainViewController){
        super(mainViewController);
        this.horsesTable = mainViewController.getHorsesTable();
        this.horseImageView = mainViewController.getHorseImageView();
        horsesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Horse>() {
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
        setRowFactory("HorseEditCreateView.fxml", Mode.EDIT,horsesTable);
    }

    @Override
    public void deleteClicked(){
        Horse h = horsesTable.getSelectionModel().getSelectedItem();
        try {
            Optional<ButtonType> result = mainViewController.showAlertDialog("Delete horse", "", "Are you sure you want to delete this horse?", Alert.AlertType.CONFIRMATION);
            if (result.get() == ButtonType.OK) {
                service.deleteHorse(h);
                horsesTable.getItems().remove(h);
                mainViewController.showAlertDialog("Delete horse", "", "Horse deleted successfully.", Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            mainViewController.showAlertDialog("Delete horse", "Couldn't delete horse.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    public void searchClicked(TextField fromAgeTextField, TextField toAgeTextField, TextField fromMinSpeedTextField, TextField toMinSpeedTextField, TextField fromMaxSpeedTextField, TextField toMaxSpeedTextField) {
        List<Horse> list = null;
        Integer fromAge=null,toAge=null;
        Double fromMinSpeed=null,toMinSpeed=null,fromMaxSpeed=null,toMaxSpeed=null;
        if(!fromAgeTextField.getText().isEmpty() && !toAgeTextField.getText().isEmpty()) {
            fromAge = (int)Math.round(Double.valueOf(fromAgeTextField.getText()));
            toAge = (int)Math.round(Double.valueOf(toAgeTextField.getText()));
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
            horsesTable.setItems(FXCollections.observableArrayList(list));
            horsesTable.getColumns().get(0).setVisible(false);
            horsesTable.getColumns().get(0).setVisible(true);
        } catch (ServiceException e) {
            mainViewController.showAlertDialog("Search horses", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}

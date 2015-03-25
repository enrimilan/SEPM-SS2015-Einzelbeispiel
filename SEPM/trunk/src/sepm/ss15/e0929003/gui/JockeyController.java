package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import sepm.ss15.e0929003.entities.Jockey;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.List;
import java.util.Optional;

public class JockeyController extends AbstractController {

    private TableView<Jockey> jockeysTable;

    public JockeyController(MainViewController mainViewController){
        super(mainViewController);
        this.jockeysTable = mainViewController.getJockeysTable();
        setRowFactory("JockeyEditCreateView.fxml", Mode.EDIT,jockeysTable);
    }

    @Override
    public void deleteClicked(){
        Jockey j = jockeysTable.getSelectionModel().getSelectedItem();
        try {
            Optional<ButtonType> result = mainViewController.showAlertDialog("Delete jockey", "", "Are you sure you want to delete this jockey?", Alert.AlertType.CONFIRMATION);
            if (result.get() == ButtonType.OK) {
                service.deleteJockey(j);
                jockeysTable.getItems().remove(j);
                mainViewController.showAlertDialog("Delete jockey","","Jockey deleted successfully.",Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            mainViewController.showAlertDialog("Delete jockey","Couldn't delete jockey.",e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    public void searchClicked(CheckBox skillCheckBox,TextField fromSkillTextField, TextField toSkillTextField) {
        List<Jockey> list = null;
        try {
            if(skillCheckBox.isSelected()){
                list = service.searchJockeys(new Jockey(null,null,null,null,Double.valueOf(fromSkillTextField.getText()),null),new Jockey(null,null,null,null,Double.valueOf(toSkillTextField.getText()),null));
            }
            else{
                list = service.searchJockeys(new Jockey(),new Jockey());
            }
            jockeysTable.setItems(FXCollections.observableArrayList(list));
            jockeysTable.getColumns().get(0).setVisible(false);
            jockeysTable.getColumns().get(0).setVisible(true);
        } catch (ServiceException e) {
            mainViewController.showAlertDialog("Search jockeys", "Search failed", e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}

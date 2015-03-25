package sepm.ss15.e0929003.gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sepm.ss15.e0929003.service.Service;

import java.io.IOException;

public abstract class AbstractController<T> {

    protected MainViewController mainViewController;
    private Stage primaryStage;
    protected Service service;

    public AbstractController(MainViewController mainViewController){
        this.mainViewController = mainViewController;
        this.primaryStage = mainViewController.getPrimaryStage();
        this.service = mainViewController.getService();
    }

    public void setRowFactory(final String resource,final Mode mode,TableView<T> tableView){
        tableView.setRowFactory(new Callback<TableView<T>, TableRow<T>>() {
            @Override
            public TableRow<T> call(TableView<T> tableView) {
                final TableRow<T> row = new TableRow<T>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Delete");
                final MenuItem editMenuItem = new MenuItem("Edit");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deleteClicked();
                    }
                });
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            openNewWindow(resource, mode);
                        } catch (IOException e) {}
                    }
                });
                contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    public void openNewWindow(String resource, Mode mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        if(resource.equals("HorseEditCreateView.fxml")){
            HorseEditCreateViewController controller = loader.getController();
            controller.setMode(mode);
            controller.setMainViewControllerAndSetup(mainViewController);
            controller.setStage(stage);

        }
        if(resource.equals("JockeyEditCreateView.fxml")){
            JockeyEditCreateViewController controller = loader.getController();
            controller.setMode(mode);
            controller.setMainViewControllerAndSetup(mainViewController);
            controller.setStage(stage);
        }
        stage.showAndWait();

    }

    public abstract void deleteClicked();
}

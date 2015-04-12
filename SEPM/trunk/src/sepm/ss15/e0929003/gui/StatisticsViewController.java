package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsViewController extends MainViewController{

    private static final Logger logger = LogManager.getLogger(Main.class);

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

    public void initialize() {
        logger.debug("Initialized.");
    }

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    public void onStatisticsCheckBoxesSelected(){
        logger.info("User selected/unselected checkbox");
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
       logger.info("User clicked 'Show statistics'");
        try {
            Integer horseId = null;
            Integer jockeyId = null;
            if(!horseIdTextFieldInStatisticsTab.getText().isEmpty()){
                horseId = Integer.valueOf(horseIdTextFieldInStatisticsTab.getText());
            }
            if(!jockeyIdTextFieldInStatisticsTab.getText().isEmpty()){
                jockeyId = Integer.valueOf(jockeyIdTextFieldInStatisticsTab.getText());
            }

           HashMap<Integer,Integer> statistics = service.evaluateStatistics(new RaceResult(null,horseId,jockeyId,null,null,null,null,null,null,null));
           if(statistics.isEmpty()){
                throw new ServiceException("No races for the given IDs found.");
           }
            ArrayList<BarChart.Data> list = new ArrayList<BarChart.Data>();
            int maxValue = 0;
            ArrayList<String> categories = new ArrayList<String>();
            for(Map.Entry<Integer,Integer> entry : statistics.entrySet()) {
                if(entry.getValue()>maxValue){
                    maxValue = entry.getValue();
                }
                list.add(new BarChart.Data(entry.getKey()+"",entry.getValue()));
                categories.add(entry.getKey()+"");
            }
            xAxis.getCategories().clear();
            xAxis.setCategories(FXCollections.observableArrayList(categories));
            yAxis.setUpperBound(maxValue+2);
            ObservableList barChartData = FXCollections.observableArrayList(new BarChart.Series( FXCollections.observableArrayList(list)));
            chart.setData(barChartData);
        } catch (ServiceException e) {
            showAlertDialog("Statistics", "Couldn't show statistics.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

}

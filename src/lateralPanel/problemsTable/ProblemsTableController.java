/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.problemsTable;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lateralPanel.problemView.ProblemViewController;
import model.Navegacion;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class ProblemsTableController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private ListView<Problem> problemsList;
    private ObservableList<Problem> observableList;
    @FXML
    private Button selectButton;
    private ArrayList<Problem> problemsText;
    private Problem problem;
    private int index, randomElement;
    private Random rand;
    private List<Problem> list;
    private ProblemViewController controller;

    @FXML
    private void randomProblem(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/lateralPanel/problemView/ProblemView.fxml"));
        VBox loaded = myLoader.load();
        ProblemViewController controller = myLoader.getController();
        rand = new Random();
        randomElement = rand.nextInt(observableList.size());
        controller.initData(observableList,randomElement);
        setMenu(loaded);
    }

    @FXML
    private void selectProblem(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/lateralPanel/problemView/ProblemView.fxml"));
        VBox loaded = myLoader.load();
        controller = myLoader.getController();
        index = problemsList.getSelectionModel().selectedIndexProperty().getValue();
        controller.initData(observableList, index);
        setMenu(loaded);
    }
    /**
     * Initializes the controller class.
     */
    
    class ProblemListCell extends ListCell<Problem> {
        @Override
        protected void updateItem(Problem item, boolean empty){
            super.updateItem(item, empty);
            if(item==null || empty)
                setText(null);
            else{
                setMinWidth(250);
                setMaxWidth(250);
                setPrefWidth(250);
                setWrapText(true);
                setText(item.getText());
            }
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { 
            list = Navegacion.getSingletonNavegacion().getProblems();
            observableList = FXCollections.observableArrayList(list);
            problemsList.setItems(observableList);
            problemsList.setCellFactory((c)->{return new ProblemListCell();});
            selectButton.disableProperty().bind(Bindings.equal(-1,problemsList.getSelectionModel().selectedIndexProperty()));
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(ProblemsTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    

    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/menuLoggedIn/MenuLoggedIn.fxml")));
    }
    
}

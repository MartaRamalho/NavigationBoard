/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.seeResults;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lateralPanel.logIn.CurrentUser;
import model.Session;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class SeeResultSessionsController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private TableView<Session> tableView;
    @FXML
    private TableColumn<Session, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Session, Integer> hitsColumn;
    @FXML
    private TableColumn<Session, Integer> faultsColumn;
    private ObservableList<Session> observableList;
    @FXML
    private DatePicker selectDate;
    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Session> listSessions = CurrentUser.getInstance(null).getUser().getSessions();
        observableList = FXCollections.observableArrayList(listSessions);
        //apply format to date in the table
        dateColumn.setCellFactory(tc -> new TableCell<Session, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                super.updateItem(item, empty);
                if(item==null || empty)
                    setText(null);
                else
                    setText(formatter.format(item));
            }
        });
        // propiedad para que se desactiven los días en adelante desde hoy
        selectDate.setDayCellFactory(param -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
        }
        });
        selectDate.editableProperty().setValue(false);
        //insert information in each column
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        hitsColumn.setCellValueFactory(new PropertyValueFactory<>("hits"));
        faultsColumn.setCellValueFactory(new PropertyValueFactory<>("faults"));
        //filter information shown according to wether the session happens after the selected date or not
        FilteredList<Session> filteredList = new FilteredList<>(observableList);
        selectDate.valueProperty().addListener((observable, oldValue, newValue)->{
            filteredList.setPredicate(session ->{
                if(newValue==null)
                    return true;
                int compare = newValue.compareTo(session.getLocalDate());
                if(compare==0 || compare<0)
                    return true;
                else 
                    return false;
            });
        });
        tableView.setItems(filteredList);
    }    
    
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/menuLoggedIn/MenuLoggedIn.fxml")));
    }
    
    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
}

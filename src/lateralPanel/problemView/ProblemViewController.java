/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.problemView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lateralPanel.logIn.CurrentUser;
import model.Answer;
import model.Problem;
import utils.DialogsGenerator;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class ProblemViewController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private TextArea problemTextArea;
    @FXML
    private ToggleGroup answers;
    private Problem problem;
    private int index;
    @FXML
    private Text problemNumber;
    private ObservableList<Problem> observableList;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;
    @FXML
    private RadioButton radio4;
    private  List<Answer> listUnmod;
    @FXML
    private Button checkButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkButton.setDisable(true);
        answers.selectedToggleProperty().addListener((o, oldVal, newVal)->{
            if(newVal!=null)
                checkButton.setDisable(false);
            else
                checkButton.setDisable(true);
        });
    }
    
    public void initData(ObservableList<Problem> o, int i) {
        observableList=o;
        problemTextArea.textProperty().setValue(observableList.get(i).getText());
        problem=observableList.get(i);
        problemTextArea.setEditable(false);
        problemTextArea.wrapTextProperty().setValue(true);
        problemNumber.setText("Problem #"+(i+1));
        index=i;
        listUnmod = problem.getAnswers();
        List<Answer> listAnswers = new ArrayList<>(listUnmod);
        Collections.shuffle(listAnswers);
        radio1.setText(listAnswers.get(0).getText());
        radio2.setText(listAnswers.get(1).getText());
        radio3.setText(listAnswers.get(2).getText());
        radio4.setText(listAnswers.get(3).getText());
    }
    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/problemsTable/ProblemsTable.fxml")));
    }

    @FXML
    private void checkAnswer(ActionEvent event) {
        RadioButton selected = (RadioButton) answers.getSelectedToggle();
        Answer answerSelected = new Answer();
        listUnmod.forEach(a ->{
            if(a.getText().equals(selected.getText())) 
                answerSelected.setValidity(a.getValidity());
        });
        if(answerSelected.getValidity()){
            selected.setTextFill(Color.GREEN);
            DialogsGenerator.dialogsGenerator("Correct Answer", "Correct Answer Chosen", 
                    "Your answer is correct!", 2);
            
            CurrentUser.getInstance(null).addHit();
        } else{
            selected.setTextFill(Color.RED);
            DialogsGenerator.dialogsGenerator("Wrong Answer", "Wrong Answer Chosen", 
                    "Your answer is not correct. Try again!", 4);
            
            CurrentUser.getInstance(null).addFault();
        }
    }


    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lateralPanel.signUp;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Navegacion;
import model.User;
import utils.DialogsGenerator;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class SignUpController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private VBox menu;
    @FXML
    private Circle clip;
    @FXML
    private ImageView avatar;
    public ArrayList<Image> avatarURLs;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private Text somethingWrong;
    @FXML
    private Button signUp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avatarURLs = new ArrayList<>();
         for(int i=0; i<5; i++){
             String path;
             if(i==0) 
                 path = "/resources/Avatars/default.png";
             else 
                 path = "/resources/Avatars/avatar"+i+".png";
             Image img = new Image(path);
             avatarURLs.add(img);
         }
        avatar.setImage(avatarURLs.get(0));
        dateOfBirth.editableProperty().setValue(false);
        // propiedad para que se desactiven los días en adelante desde hoy
        dateOfBirth.setDayCellFactory(param -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
        }
        });
        
         // bind para que el botón de aceptar esté desactivado si los campos no están rellenos
        signUp.disableProperty().bind(
                Bindings.or(
                        Bindings.or(
                                username.textProperty().isEmpty(), email.textProperty().isEmpty()), 
                        Bindings.or(
                                password.textProperty().isEmpty(), dateOfBirth.valueProperty().isNull())));
        //listeners para asegurar informacion correcta
        username.focusedProperty().addListener((observable, oldVal, newVal)->{
            if(!newVal){ //focus lost.
                checkField(username);
            }
        });
        email.focusedProperty().addListener((observable, oldVal, newVal)->{
            if(!newVal){ //focus lost.
                checkField(email);
            }
        });
        password.focusedProperty().addListener((observable, oldVal, newVal)->{
            if(!newVal){ //focus lost.
                checkField(password);
            }
        });
        
    }  
    //comprobar campos
    private void checkField(TextField field){
        if(field==username){
            if(!User.checkNickName(username.getText())){
                manageError(username);
            } else {
                manageCorrect(username);
            }
        } else if (field==email){
            if(!User.checkEmail(email.getText())){
                manageError(email);
            } else{
                manageCorrect(email);
            }
        } else if (field==password){
            if(!User.checkPassword(password.getText())){
                manageError(password);
            } else{
                manageCorrect(password);
            }
        }
    }
    
    
    private void manageError(TextField textField, BooleanProperty boolProp ){
        showErrorMessage(textField);
        textField.requestFocus();
    }
    private void manageCorrect(TextField textField){
        hideErrorMessage(textField);
    }
    
    private void hideErrorMessage(TextField textField)
    {
        somethingWrong.setText("");
        textField.styleProperty().setValue("");
    }
    
    public void setMenu(VBox v){
        menu=v;
        box.getChildren().clear();
        box.getChildren().add(menu);
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        setMenu(FXMLLoader.load(getClass().getResource("/lateralPanel/mainMenu/MainMenu.fxml")));
    }


    @FXML
    public void previousImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        if(i-1<0)
            i=avatarURLs.size()-1;
        else
            i--;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    public void nextImage(ActionEvent event) {
        Image current = avatar.getImage();
        int i = avatarURLs.indexOf(current);
        if(i+1==avatarURLs.size())
            i=0;
        else
            i++;
        avatar.setImage(avatarURLs.get(i));
    }

    @FXML
    private void handleSignUpPressed(ActionEvent event) throws NavegacionDAOException {
        if(Navegacion.getSingletonNavegacion().exitsNickName(username.getText())){
            DialogsGenerator.dialogsGenerator("Invalid Username", "Username already in use", "This username "
                    + "already exists. Please select a new one.", 4);
        }
        else if(Period.between(dateOfBirth.getValue(), java.time.LocalDate.now()).getYears()<12){
            DialogsGenerator.dialogsGenerator("Invalid birthdate", "The user is not old enough", 
                    "The user must be at least 12 years old.", 4);
        }
        else{
            Navegacion.getSingletonNavegacion().registerUser(username.getText(), email.getText(), 
                    password.getText(), avatar.getImage(), (LocalDate) dateOfBirth.getValue());
            email.clear();
            password.clear();
            username.clear();
            dateOfBirth.setValue(null);
            avatar.setImage(avatarURLs.get(0));
            DialogsGenerator.dialogsGenerator("User created", "User Created Successfully", "User Created. Log In to access "
                    + "your account", 2);
        }
    }
    
    private void manageError(TextField textField){
        showErrorMessage(textField);
        textField.requestFocus();
    }
    private void showErrorMessage(TextField textField){
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");
        if(textField.equals(username)){
            somethingWrong.setText("Invalid Username");
        }
        if(textField.equals(email)){
            somethingWrong.setText("Invalid Email");
        }
        if(textField.equals(password)){
            somethingWrong.setText("Invalid Password");
        }
    }
    
}
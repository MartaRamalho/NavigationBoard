/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Marta
 */
public class DialogsGenerator {
    public static boolean dialogsGenerator(String title,String header,String body,int type){
        //Types: 0-NONE,1-CONFIRMATION,2-INFORMATION 3.WARNING, 4-ERROR
        ButtonType yes = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
        Alert alert;
        switch(type){
           case 0: alert = new Alert(Alert.AlertType.NONE, "", yes); break;
           case 1: alert = new Alert(Alert.AlertType.CONFIRMATION, "", yes,no); break;
           case 2: alert = new Alert(Alert.AlertType.INFORMATION, "", ok); break;
           case 3: alert = new Alert(Alert.AlertType.WARNING, "", yes,no); break;
           case 4: alert = new Alert(Alert.AlertType.ERROR); break;
           default: alert = new Alert(Alert.AlertType.CONFIRMATION,"",ok); break;
           
        }
            if(!title.equals("")){
              alert.setTitle(title);  
            }else{alert.setTitle(null);}

            if(!header.equals("")){
                alert.setHeaderText(header);
            }else{alert.setHeaderText(null);}

            if(!body.equals("")){
                alert.setContentText(body);
            }else{alert.setContentText(null);}

            alert.showAndWait();
            if(alert.getResult()==yes){return true;}else{return false;}
    }
}

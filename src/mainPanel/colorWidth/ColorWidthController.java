/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mainPanel.colorWidth;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class ColorWidthController implements Initializable {

    @FXML
    private Slider scrollWidth;
    @FXML
    private ColorPicker picker;
    @FXML
    private Line line;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // colores de ejemplo en la cruz satánica
        line.strokeProperty().bind(picker.valueProperty());;
         
        // tamaños de ejemplo en la cruz satánica
        line.strokeWidthProperty().bind(scrollWidth.valueProperty());

    }
    
    public void setColor(Color color) {
        picker.valueProperty().setValue(color);
    }
    
    public void setWidth(Double width) {
        scrollWidth.valueProperty().setValue(width);
    }
    
    public ObjectProperty<Color> getColorProp() {
        return picker.valueProperty();
    }
    
    public DoubleProperty getWidthProp() {
        return scrollWidth.valueProperty();
    }
    
}

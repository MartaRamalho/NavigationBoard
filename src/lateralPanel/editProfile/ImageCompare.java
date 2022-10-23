/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lateralPanel.editProfile;

import javafx.scene.image.Image;

/**
 *
 * @author Marta
 */
public class ImageCompare {
    public static boolean isImageEqual(Image firstImage, Image secondImage){
    // Prevent `NullPointerException`
    if(firstImage != null && secondImage == null) return false;
    if(firstImage == null) return secondImage == null;

    // Compare images size
    if(firstImage.getWidth() != secondImage.getWidth()) return false;
    if(firstImage.getHeight() != secondImage.getHeight()) return false;

    // Compare images color
    for(int x = 0; x < firstImage.getWidth(); x++){
        for(int y = 0; y < firstImage.getHeight(); y++){
            int firstArgb = firstImage.getPixelReader().getArgb(x, y);
            int secondArgb = secondImage.getPixelReader().getArgb(x, y);

            if(firstArgb != secondArgb) return false;
        }
    }

    return true;
}
}

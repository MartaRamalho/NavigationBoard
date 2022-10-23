/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lateralPanel.logIn;

import DBAccess.NavegacionDAOException;
import java.time.LocalDateTime;
import model.Session;
import model.User;

/**
 *
 * @author Marta
 */
public class CurrentUser {
    private static CurrentUser instance;
    private User user;
    private int hits, faults;
    
    private CurrentUser(User u){
        user=u;
        hits=0;
        faults=0;
    }
    
    public static CurrentUser getInstance(User u) {
        if(instance == null) {
            instance = new CurrentUser(u);
        }
        return instance;
    }

    
    public User getUser(){
        return user;
    }
    
    public void addHit(){
        hits++;
    }
    
    public void addFault(){
        faults++;
    }
    
    public void endSession() throws NavegacionDAOException{
        LocalDateTime now = LocalDateTime.now();
        Session session = new Session(now, hits, faults);
        user.addSession(session);
        user=null;
        instance=null;
    }
    
    public CurrentUser clone(){
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException ex) {
            System.out.println("An object from this class cannot be cloned");
        }
            return null; 
    }
}

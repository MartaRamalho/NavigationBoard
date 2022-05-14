/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

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
        Session session = new Session(LocalDateTime.now(), hits, faults);
        user.addSession(session);
        user=null;
        this.instance=null;
        
    }
}

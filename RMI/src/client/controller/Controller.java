/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RmiInterface;

/**
 *
 * @author tranb
 */
public class Controller {
    private String host = "localhost";
    private int port = 8888;
    private Registry registry;
    private RmiInterface rmi;
    
    public Controller() {
        try {
            registry = LocateRegistry.getRegistry(host, port);
            rmi = (RmiInterface) registry.lookup("rmiserver");
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public RmiInterface getRmi(){
        return this.rmi;
    }
}

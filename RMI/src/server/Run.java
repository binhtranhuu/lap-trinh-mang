/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

/**
 *
 * @author tranb
 */
public class Run {
    public static void main(String[] args) {
        try {
            Controller con = new Controller();
        } catch (RemoteException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

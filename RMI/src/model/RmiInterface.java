/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.rmi.server.*;
import java.rmi.*;
import java.util.ArrayList;

/**
 *
 * @author tranb
 */
public interface RmiInterface extends Remote {
    public boolean add(Student s) throws RemoteException;
    public boolean update(Student s) throws RemoteException;
    public boolean delete(int ma) throws RemoteException;
    public ArrayList<Student> search(String k) throws RemoteException;
    public ArrayList<Student> getAllStudent() throws RemoteException;
}

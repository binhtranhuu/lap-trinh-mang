/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Request;
import model.Student;

/**
 *
 * @author tranb
 */
public class Controller {
    private String host = "localhost";
    private int port = 8888;
    private DatagramPacket packetClient;
    private DatagramSocket server;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Connection con;
    
    public Controller() {
        open();
        connectDb();
        while(true){
            try {
                byte[] data = new byte [1024];
                packetClient = new DatagramPacket(data, data.length);
                server.receive(packetClient);
                
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Request res = (Request)ois.readObject();
                switch(res.getName()){
                    case "update":
                        update((Student)res.getData());
                        break;
                    case "delete":
                        delete((int)res.getData());
                        break;
                    case "add":
                        add((Student) res.getData());
                        break;
                    case "search":
                        search((String)res.getData());
                        break;
                    case "all":
                        getListStudent();
                        break;
                   
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void search(String k){
        try {
            ArrayList<Student> res = new ArrayList<>();
            String sql ="select * from tbl_student where ten like '%"+k+"%'";
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                res.add(new Student(rs.getInt("ma"),rs.getString("ten"),rs.getString("khoa"),rs.getString("ngaysinh"),rs.getString("quequan")));
            }
            send(res);
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void getListStudent(){
        try {
            ArrayList<Student> res = new ArrayList<>();
            String sql ="select * from tbl_student";
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                res.add(new Student(rs.getInt("ma"),rs.getString("ten"),rs.getString("khoa"),rs.getString("ngaysinh"),rs.getString("quequan")));
            }
            send(res);
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void add(Student s){
        try {
            String sql ="insert into tbl_student(ten,khoa,ngaysinh,quequan) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, s.getTen());
            ps.setString(2, s.getKhoa());
            ps.setString(3, s.getNgaysinh());
            ps.setString(4, s.getQuequan());
            ps.execute();
            send(true);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            send(false);
        }
        
    }    
    public void delete(int ma){
        String sql ="delete from tbl_student where ma="+ma;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            send(true);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            send(false);
        }
        
    }
    public void update(Student s){
        try {
            String sql ="update tbl_student set ten=?,khoa=?,ngaysinh=?,quequan=? where ma="+s.getMa();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, s.getTen());
            ps.setString(2, s.getKhoa());
            ps.setString(3, s.getNgaysinh());
            ps.setString(4, s.getQuequan());
            ps.execute();
            send(true);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            send(false);
        }
        
        
        
    }
    
    public void send(Object o){
        ObjectOutputStream oos = null;
        try {
            byte[] data = new byte[1024];
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bais);
            oos.writeObject(o);
            oos.flush();
            data = bais.toByteArray();
            
            DatagramPacket dataP = new DatagramPacket(data, data.length,packetClient.getAddress(),packetClient.getPort());
            server.send(dataP);
            
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void connectDb(){
        try {
            String sql = "jdbc:mysql://localhost/LTM";
            String classUrl ="com.mysql.jdbc.Driver";
            Class.forName(classUrl);
            con = DriverManager.getConnection(sql,"root",""); // Connect Database with Xampp    
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    public void open(){
        try {
            server  = new DatagramSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

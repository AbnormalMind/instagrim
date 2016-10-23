/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;

    public User()
    {
        
    }
    
    public String GetUserFirstname(String username)
    {
        //String[] userInfo = new String[4];
        String StoredFirstname = "";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select first_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoredFirstname = row.getString("first_name");
            }
        }
    return StoredFirstname;  
    }

    public String GetUserLastname(String username)
    {
        String StoredLastname = "";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select last_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoredLastname = row.getString("last_name");
            }
        }
    return StoredLastname;  
    }

    public String GetUsername(String username)
    {
        return username;
    }

    public String GetDOB(String username)
    {
        String StoredDOB = "";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select dob from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoredDOB = row.getString("dob");
            }
        }
    return StoredDOB; 
    }

    public String GetMobile(String username)
    {
        String StoredTel = "";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select tel from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoredTel = row.getString("tel");
            }
        }
    return StoredTel; 
    }

    public String GetStatus(String username)
    {
        String StoreStatus ="";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select status from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoreStatus = row.getString("status");
            }
        }
    return StoreStatus;         
    }

    public String GetEmail(String username){
        String StoreEmail = "";
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select email from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
        } else {
            for (Row row : rs) {
               
                StoreEmail = row.getString("email");
            }
        }
    return StoreEmail;         
    }

    public boolean EditUserProfile(String firstname, String lastname, String username, String password, String dob, String telephone, String status, String email)
    {
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");    
        PreparedStatement ps = session.prepare("UPDATE userprofiles SET first_name = ?, last_name = ?, password = ?, dob=?, tel=?, status=?, email=? WHERE login = ?");
        
        System.out.println("ANYNAME: " + lastname);

        if (firstname.compareTo("") == 0) {
        System.out.println("THE firstname FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select first_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("first_name");
                    firstname = StoredPass;
                }
            }
        }

        if (lastname.compareTo("") == 0) {
        System.out.println("THE lastname FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select last_name from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("last_name");
                    lastname = StoredPass;
                }
            }
        }

        if (dob.compareTo("") == 0) {
        System.out.println("THE dob FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select dob from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("dob");
                    dob = StoredPass;
                }
            }        
        }

        if (telephone.compareTo("") == 0 || telephone.length() != 10) {
        System.out.println("THE tel FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select tel from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("tel");
                    telephone = StoredPass;
                }
            }        
        }

        if (status.compareTo("") == 0) {
        System.out.println("THE status FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select status from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("status");
                    status = StoredPass;
                }
            }        
        }

        if (email.compareTo("") == 0) {
        System.out.println("THE email FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select email from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("email");
                    email = StoredPass;
                }
            }        
        }

        if(EncodedPassword.compareTo("da39a3ee5e6b4b0d3255bfef95601890afd80709") == 0 || password.length() < 7) 
        //The password is being taken from the database if the field in the form is empty so that it wont get overwritten.
        {
        System.out.println("THE PASSWORD FIELD IS EMPTY!!!");
        PreparedStatement pass = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(pass);
        rs = session.execute(bs.bind(username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
            } else {
                for (Row row : rs) {
                    String StoredPass = row.getString("password");
                    EncodedPassword = StoredPass;
                }
            }
        }

        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(firstname,lastname,EncodedPassword,dob,telephone,status,email,username));
        return true;
    }   

    public boolean RegisterUser(String firstname, String lastname, String username, String Password){
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (first_name,last_name,login,password) Values(?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        firstname,lastname,username,EncodedPassword));
        //We are assuming this always works.  Also a transaction would be good here !
        
        return true;
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }

        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
    return false; 
    }
    

    public boolean IsValidToRegister(String username){
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select login from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
                String StoredUsername = row.getString("login");
                if (StoredUsername.compareTo(username) == 0)
                    return true;
            }
        }
    return false; 
    }


    public boolean IsValidPassword(String Password)
    {
        if(Password.length()<=6)
        {
            System.out.println("Password not long enough: " + Password.length());

            return false;
        }
        else
        {
            System.out.println("Password long enough: " + Password.length());
            return true;
        }             
    }
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}

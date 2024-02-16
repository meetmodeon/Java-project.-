package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void register(){
        sc.nextLine();
        System.out.println("Full Name: ");
        String full_name=sc.nextLine();
        System.out.println("Email: ");
        String email=sc.nextLine();
        System.out.println("Password: ");
        String password=sc.nextLine();

        if(user_exsit(email)){
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query="insert into User(full_name,email,password) values (?,?,?)";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Registration Successfull!");
            }else{
                System.out.println("Registration Failed!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String login(){
        sc.nextLine();
        System.out.println("Email: ");
        String email=sc.nextLine();
        System.out.println("Password: ");
        String password=sc.nextLine();
        String login_query="select *from User where email=? and password=?";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();

            if(resultSet.next()){
                return email;
            }else {
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exsit(String email){
        String query="select *from User where email=?";
        try{
            PreparedStatement preparedStatement=con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}


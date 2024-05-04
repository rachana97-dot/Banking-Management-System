package BANKINGMANAGEMENTSSYSTEM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	
	private Connection con;
	private Scanner scanner;

	public User(Connection con, Scanner scanner) {
		this.con = con;
		this.scanner    = scanner;
	}
	
	public void register() {
        scanner.nextLine();
		System.out.println("Full Name :");
		String full_name = scanner.nextLine();
		System.out.println("Email :");
		String email = scanner.nextLine();
		System.out.println("Password :");
		String password = scanner.nextLine();
		
		if(user_exit(email)) {
			System.out.println("User Already Exists for this Email Address!!");
			return;
			
		}
		String register_query = "insert into User(full_name, email, password)values(?,?,?)";
		try {
			PreparedStatement prepredstatement = con.prepareStatement(register_query);
			prepredstatement.setString(1, full_name);
			prepredstatement.setString(2, email);
			prepredstatement.setString(3, password);
			
			int affectedrows = prepredstatement.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Registration Successfull!");
			}else {
				System.out.println("Registration Failed!");
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String login() {
		scanner.nextLine();
		System.out.println("Email : ");
		String email = scanner.nextLine();
		
		System.out.println("Password");
		String password = scanner.nextLine();
		
		String login_query = "select*from User where email =? and password =?";
		
		try {
			PreparedStatement preparedstatements = con.prepareStatement(login_query);
			preparedstatements.setString(1, email);
			preparedstatements.setString(2,password);
			ResultSet resultset = preparedstatements.executeQuery();
			 
			if(resultset.next()) {
				return email;
			}else {
				return null;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}	

	private boolean user_exit(String email) {
		String query ="Select*from user where email =?";
		
		try {
			PreparedStatement preparedstatement = con.prepareStatement(query);
			preparedstatement.setString(1,email);
			
			ResultSet resultset = preparedstatement.executeQuery();
			
			if(resultset.next()){
				return true;
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
  
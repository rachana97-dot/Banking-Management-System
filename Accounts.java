package BANKINGMANAGEMENTSSYSTEM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	 
	private Connection con;
	
	private Scanner scanner;
	
	public Accounts(Connection con, Scanner scanner) {
		this.con =  con;
		this.scanner = scanner;	
	}
	public long open_account(String email) {
		if(!account_exist(email)) {
			String open_account_query = "insert into Account(account_number,full_name,email,balance,security_pin)values(?,?,?,?,?)";
			scanner.nextLine();
			System.out.println("Enter Full Name :");
			String full_name = scanner.nextLine();
			System.out.println("Enter Initial Amount :");
			double balance =scanner.nextDouble();
            scanner.nextLine();
			System.out.println("Enter Security Pin : ");
			String security_pin = scanner.nextLine();
			
			try {
				long account_number = generateAccountNumber();
				PreparedStatement preparedstatement = con.prepareStatement(open_account_query);
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, full_name);
				preparedstatement.setString(3,email);
				preparedstatement.setDouble(4,balance);
				preparedstatement.setString(5, security_pin);
				
				int rowsAffected = preparedstatement.executeUpdate();
				if(rowsAffected>0) {
					return account_number;
				}else {
					throw new RuntimeException("Account Creation Failed !!");
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account Already Exist");
	}
	
	public long getAccount_number(String email){
		String query = "select account_number from Account where email=?";
		
		try {
			PreparedStatement preparedstatement = con.prepareStatement(query);
			preparedstatement.setString(1,email );
			
			ResultSet resultset = preparedstatement.executeQuery();
			if(resultset.next()) {
				return resultset.getLong("account_number");
			}
		}catch(SQLException e ) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account Number Doesn't Exist!"); 
		
	}
	
	private long generateAccountNumber() {
		try {
			Statement statement = con.createStatement();
			String query = "select account_number from Account Order by account_number desc limit 1" ;
			ResultSet resultset = statement.executeQuery(query);
			if(resultset.next()) {
				long last_account_number = resultset.getLong("account_number");
				return last_account_number + 1;
			}else {
				return 10000100;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return  10000100;
	}
	
	public boolean account_exist(String email) {
		String query = "select account_number from Account where email=?";
		
		try {
			PreparedStatement preparedstatement = con.prepareStatement(query);
			preparedstatement.setString(1, email);
			ResultSet resultset = preparedstatement.executeQuery();
			if(resultset.next()) {
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

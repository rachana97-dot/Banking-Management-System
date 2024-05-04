package BANKINGMANAGEMENTSSYSTEM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Account_Manager {
	private Connection con;
	private Scanner scanner;

	public Account_Manager(Connection con, Scanner scanner) {
		this.con = con;
		this.scanner = scanner;
	}

	public void credit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount : ");
		double amount = scanner.nextDouble();
        scanner.nextLine();
		System.out.println("Enter Security Pin : ");
		String security_pin = scanner.nextLine();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedstatement = con
						.prepareStatement("select*from Account where account_number = ? and security_pin = ?");
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, security_pin);
				ResultSet resultset = preparedstatement.executeQuery();

				if (resultset.next()) {
					String credit_query = "update Account set balance = balance + ? where account_number = ? ";
					PreparedStatement preparedstatement1 = con.prepareStatement(credit_query);
					preparedstatement1.setDouble(1, amount);
					preparedstatement1.setLong(2, account_number);
					int rowsAffected = preparedstatement1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Rs." + amount + "credited Successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction Failed!");
						con.rollback();
						con.setAutoCommit(true);
					}

				} else {
					System.out.println("Invalid Security Pin!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
		System.out.println("Enter Amount : ");
		double amount = scanner.nextDouble();
        scanner.nextLine();
		System.out.println("Enter Security_pin :");
		String security_pin = scanner.nextLine();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedstatement = con.prepareStatement("select*from Account Where account_number =? and security_pin =?");
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, security_pin);
				ResultSet resultset = preparedstatement.executeQuery();

				if (resultset.next()) {
					double current_balance = resultset.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "update Account SET balance = balance - ? where account_number = ?";
						PreparedStatement preparedstatements = con.prepareStatement(debit_query);
						preparedstatements.setDouble(1, amount);
						preparedstatements.setLong(2, account_number);

						int rowsAffected = preparedstatements.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println("Rs." + amount + "debited Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
					} else {
						System.out.println("insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Pin!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}

public void transfer_money(long sender_account_number) throws SQLException{
    scanner.nextLine();
	System.out.println("Enter receiver Account Number :");
	long receiver_account_number = scanner.nextLong();
	System.out.println("Enter Amount :");
	double amount = scanner.nextDouble();
    scanner.nextLine();
	System.out.println("Enter Security Pin :");
	String security_pin = scanner.nextLine();
	
	try {
		con.setAutoCommit(false);
		if(sender_account_number!=0 && receiver_account_number!=0) {
			PreparedStatement preparedstatements = con.prepareStatement("select*from Account where account_number =? and security_pin =?");
			preparedstatements.setLong(1, sender_account_number);
			preparedstatements.setString(2, security_pin);
			ResultSet  resultset = preparedstatements.executeQuery();
			
			if(resultset.next()) {
				double current_balance = resultset.getDouble("balance");
				if(amount<=current_balance) {
					//write debit and credit query
					String debit_query ="update Account Set balance = balance -? where account_number =?";
					String credit_query ="update Account Set balance = balance +? where account_number =?";
					
					//Debit and credit prepared statements
					PreparedStatement creditpreparedstatement = con.prepareStatement(credit_query);
					PreparedStatement debitpreparedstatement = con.prepareStatement(debit_query);
					
					//set values for debit and credit prepared statements
					creditpreparedstatement.setDouble(1, amount);
					creditpreparedstatement.setLong(2, receiver_account_number);
					
					debitpreparedstatement.setDouble(1,amount);
					debitpreparedstatement.setLong(2, sender_account_number);
					
					int rowsAffected1 = debitpreparedstatement.executeUpdate();
					int rowsAffected2 = creditpreparedstatement.executeUpdate();
					if(rowsAffected1>0 && rowsAffected2>0) {
						System.out.println("Transaction Successful!");
						System.out.println("Rs."+amount+"Transaction successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					}else {
						System.out.println("transaction Failed");
						con.rollback();
						con.setAutoCommit(true);
					}
					
				}else {
					System.out.println("Insifficient Balance ");
				}
			}
			else {
					System.out.println("Invalid security pin");
				}
			
			}else {
				System.out.println("Invalid account number");
			}
	}catch(SQLException e){
		e.printStackTrace();
	}
	
	con.setAutoCommit(true);
}

public void getBalance(long account_number) {
    scanner.nextLine();
	System.out.println("Enter security Pin :");
	String security_pin = scanner.nextLine();
	try {
		PreparedStatement preparedstatement = con.prepareStatement("select balance from Account where account_number =? and security_pin =?");
		preparedstatement.setLong(1, account_number);
		preparedstatement.setString(2, security_pin);
		
		ResultSet resultset = preparedstatement.executeQuery();
		if(resultset.next()) {
			double balance = resultset.getDouble("balance");
			System.out.println("Balance : " + balance);
			}else {
				System.out.println("Invalid Pin!");
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
}

}


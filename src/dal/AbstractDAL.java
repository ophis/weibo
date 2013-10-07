package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

abstract class AbstractDAL {
	Connection conn = null;
	
	public AbstractDAL() {
		// TODO Auto-generated constructor stub
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/SecurityAndPrivacy?"+
												"user=root&password=wrnmbydh&useUnicode=true&characterEncoding=utf8");
			System.out.println("Link to database(InfoService) successful!");
			this.createTables();
		} catch (SQLException e) {
			// try to create a database
			if((e instanceof MySQLSyntaxErrorException)){
				MySQLSyntaxErrorException myException = (MySQLSyntaxErrorException)e;
				String messageString = myException.getMessage();
				if(messageString.startsWith("Unknown database")){
					System.out.println("Unknowen database, try to create one...");
					//create database
					try {
						conn = DriverManager.getConnection("jdbc:mysql://localhost/mysql?"+
								"user=root&password=wrnmbydh");
						if(conn!=null){
							Statement statement = conn.createStatement();
							statement.executeUpdate("CREATE DATABASE SecurityAndPrivacy charset utf8");
							System.out.println("Create database(SecurityAndPrivacy) successful!");
							conn = DriverManager.getConnection("jdbc:mysql://localhost/SecurityAndPrivacy?"+
										"user=root&password=wrnmbydh&useUnicode=true&characterEncoding=utf8");
							System.out.println("Link to new database(SecurityAndPrivacy) successful!");
							this.createTables();
						}
					} catch (SQLException e1) {
						System.out.println("Create database failed!");
					}
				}
			}
			else{
				System.out.println(e.getMessage());
			}
		}
	}

	//create tables if not exists
		private void createTables(){
			String createTableOntology = "CREATE TABLE IF NOT EXISTS User " +
					"(id INT NOT NULL AUTO_INCREMENT," +
					"uid VARCHAR(50) NOT NULL, " +
					"screen_name VARCHAR(50) NOT NULL,"+
					"PRIMARY KEY(id))";
			Statement statement;
			try {
				statement = conn.createStatement();
				System.out.println("Try to create tables if not exists...");
				statement.executeUpdate(createTableOntology);
				System.out.println("Create tables successfully!");
				statement.close();
			} catch (SQLException e) {
				System.out.println("Create tables failed!");
				e.printStackTrace();
			}
		}
		
		public void closeConnection(){
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
}


package security.dal;

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
												"user=root&password=wrnmbydh&useUnicode=true");
			System.out.println("Link to database(SecurityAndPrivacy) successful!");
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
							statement.executeUpdate("CREATE DATABASE SecurityAndPrivacy charset utf8mb4");
							System.out.println("Create database(SecurityAndPrivacy) successful!");
							conn = DriverManager.getConnection("jdbc:mysql://localhost/SecurityAndPrivacy?"+
										"user=root&password=wrnmbydh&useUnicode=true");
							System.out.println("Link to new database(SecurityAndPrivacy) successful!");
							this.createTables();
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
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
			String createTableUser = "CREATE TABLE IF NOT EXISTS User " +
					"(id INT NOT NULL AUTO_INCREMENT," +
					"uid VARCHAR(20) NOT NULL, " +
					"screen_name VARCHAR(40) NOT NULL,"+
					"name VARCHAR(10),"+
					//"class VARCHAR(4),"+
					//"description VARCHAR(140) CHARACTER SET utf8mb4,"+
					"url VARCHAR(200),"+
					"location VARCHAR(20),"+
					"gender VARCHAR(1),"+
					"education VARCHAR(200),"+
					"email VARCHAR(50),"+
					"qq VARCHAR(12),"+
					"msn VARCHAR(50),"+
					"sex_pre VARCHAR(6),"+
					"relationship_status VARCHAR(6),"+
					"created_at DATE,"+
					"allow_all_act_msg BOOLEAN,"+
					"verified BOOLEAN,"+
					"allow_all_comment BOOLEAN,"+
					"PRIMARY KEY(id))";
			String createTableTimeline = "CREATE TABLE IF NOT EXISTS Timeline " +
					"(id INT NOT NULL AUTO_INCREMENT," +
					"mid VARCHAR(30) NOT NULL, " +
					"uid VARCHAR(20) NOT NULL, " +
					"uscreen_name VARCHAR(40) NOT NULL," +
					"text LONGTEXT,"+
					"created_at DATE,"+
					"inReplyUid VARCHAR(10), " +
					"PRIMARY KEY(id))";
			Statement statement;
			try {
				statement = conn.createStatement();
				System.out.println("Try to create tables if not exists...");
				statement.executeUpdate(createTableUser);
				statement.executeUpdate(createTableTimeline);
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


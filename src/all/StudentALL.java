package all;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Student;

public class StudentALL {
	private String URL = "jdbc:mysql://localhost:3306/component?useSSL=false";
	private String Username = "root";
	private String Password = "123456";

	private static final String INSERT_USERS_SQL = "INSERT INTO student" + "  (name, gpa) VALUES "
			+ " (?, ?);";
	private static final String SELECT_USER_BY_ID = "select id,name,gpa from student where id =?";
	private static final String SELECT_ALL_USERS = "select * from student";
	private static final String DELETE_USERS_SQL = "delete from student where id = ?;";
	private static final String UPDATE_USERS_SQL = "update student set name = ?,gpa= ? where id = ?;";

	public StudentALL() {
	}	

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL, Username, Password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public void insertUser(Student stud) throws SQLException {
		System.out.println(INSERT_USERS_SQL);

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, stud.getName());
			preparedStatement.setString(2, stud.getGpa());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	public List<Student> selectAllUsers() {
		List<Student> users = new ArrayList<>();
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			ResultSet resul = preparedStatement.executeQuery();
			while (resul.next()) {
				int id = resul.getInt("id");
				String name = resul.getString("name");
				String gpa = resul.getString("gpa");
				users.add(new Student(id, name, gpa));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateUser(Student stud) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, stud.getName());
			statement.setString(2, stud.getGpa());
			statement.setInt(3, stud.getId());
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
	
	public Student selectUser(int id) {
		Student user = null;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet resul = preparedStatement.executeQuery();

			while (resul.next()) {
				String name = resul.getString("name");
				String gpa = resul.getString("gpa");
				user = new Student(id, name, gpa);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}
}

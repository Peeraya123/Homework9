package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import all.StudentALL;
import model.Student;

@WebServlet("/")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentALL studentALL;
	private List<Integer> numberIdUse = new ArrayList<>();
	public StudentServlet() {
		this.studentALL = new StudentALL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/insert":
				insertUser(request, response);
				break;
			case "/new":
				showNewForm(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String name = request.getParameter("name");
		String gpa = request.getParameter("gpa");
		Student newUser = new Student(name, gpa);
		studentALL.insertUser(newUser);
		response.sendRedirect("list");
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		// Object idForDeleteId = (Integer) id;
		Integer idDelete = Integer.valueOf(id);
		String name = request.getParameter("name");
		String gpa = request.getParameter("gpa");

		Student book = new Student(id, name, gpa);
		System.out.println(numberIdUse);
		numberIdUse.remove(idDelete);
		studentALL.updateUser(book);
		response.sendRedirect("list");

	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		studentALL.deleteUser(id);
		response.sendRedirect("list");

	}

	protected void Post(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Student> listUser = studentALL.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEdit(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		synchronized (getServletContext()) {
			int id = Integer.parseInt(request.getParameter("id"));
			if (numberIdUse.isEmpty() || !numberIdUse.contains(id)) {
				Student existingUser = studentALL.selectUser(id);
				RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
				request.setAttribute("student", existingUser);
				numberIdUse.add(id);
				System.out.print(numberIdUse);
				dispatcher.forward(request, response);
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("Error Edit Already .jsp");
				dispatcher.forward(request, response);
			}
		}
	}
}

package com.kmit.app;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.PrintWriter;
import java.util.*;
@WebServlet("/TodoServlet")
public class TodoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<Todo> todos = new ArrayList<Todo>();
    public TodoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	     response.setContentType("text/html");
	     PrintWriter out=response.getWriter();
	     String name = (String) request.getSession().getAttribute("name");
			int user_id=(int) request.getSession().getAttribute("user_id");
			todos.clear();
			ServletContext sc = getServletContext();
			String drivername=sc.getInitParameter("drivername");
		    String driverurl=sc.getInitParameter("driverurl");
		    String username=sc.getInitParameter("databaseusername");
		    String pass=sc.getInitParameter("databasepassword");
		    
		    Connection conn=null;
			PreparedStatement pstmt =null;
			ResultSet rs = null;
			
			try
			{
				Class.forName(drivername);
				conn = DriverManager.getConnection(driverurl, username, pass);
			
				if (conn != null && !conn.isClosed()) {

					String sql = "select * from todos where user_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, user_id);
					rs = pstmt.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							Todo t=new Todo();
							t.setTodo_id(rs.getInt("todo_id"));
							t.setCategory(rs.getString("category"));
							t.setDescription(rs.getString("description"));
				            todos.add(t);
				        }  
					}	
				}
				out.println("<html>"
						+ "<head>"
						+ "<title>Todo - List</title>"
						+ "<link href='css/bootstrap.min.css' rel='stylesheet'>"
						+ "<link href='css/login.css' rel='stylesheet'>"
						+ "<script src='js/jquery.min.js'></script>"
						+ "<script src='js/bootstrap.min.js'></script>"
						+ "</head>"
						+ "<body>"
						+ "<header>"
						+ "<nav class='navbar navbar-default'>"
						+ "<a href='/' class='navbar-brand'>Brand</a>"
						+ "<ul class='nav navbar-nav'>"
						+ "<li class='active'><a href='#'>Home</a></li>"
						+ "<li><a href='TodoServlet'>Todos</a></li>"
						+ "</ul>"
						+ "<ul class='nav navbar-nav navbar-right'>"
						+ "<li><a href='AccountUpdateServlet'><font size='4'>Hi, "+name+"</font></a></li>"
						+ "<li><a href='LogoutServlet'>Logout</a></li>"
						+ "</ul>"
						+ "</nav>"
						+ "</header>"
						+ "<div class='container'>"
						+ "<h1 align='center'>TODO List</h1>"
						+ "<form class='form-search' action='TodoSearchServlet' >"
						+ "<div class='input-append'>"
						+ "<input type='text' class='search-query' name='keyword'>"
						+ "<button type='submit' class='btn btn-large'>Search</button>"
						+ "</div>"
						+ "</form>"
						+ "<table class='table table-striped'>"
						+ "<caption>Your Todos are</caption>"
						+ "<thead>"
						+ "<th>Description</th>"
						+ "<th>Category</th>"
						+ "<th>Actions</th>"
						+ "<th>Remind</th>"
						+ "</thead>"
						+ "<tbody>");
				
						for(Todo todo : todos) {	
							out.println("<tr>"
									+ "<td>"+todo.getDescription()+"</td>"
									+ "<td>"+todo.getCategory()+"</td>"
									+ "<td>&nbsp;&nbsp;<a class='btn btn-danger' href='TodoDeleteServlet?todo_id="+todo.getTodo_id()+"'>Delete</a></td>"
									+ "<td>&nbsp;&nbsp;<a class='btn btn-primary' href='SMSServlet?todo_id="+todo.getTodo_id()+"'>Send</a></td>"
									+ "</tr>");
						}
				out.println("</tbody>"
						+ "</table>"
						+ "<a class='btn btn-success' href='TodoAddServlet'>Add New Todo</a>"
						+ "</div>"
						+ "</body>"
						+ "</html>");
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				request.setAttribute("error","Todo List failed : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
			finally
			{
				try {
					pstmt.close();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("error","Todo List failed : "+e.getMessage() );
					request.getRequestDispatcher("ErrorServlet").forward(request, response);
				}
			}

	     
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}

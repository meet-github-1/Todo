package com.kmit.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/TodoAddServlet")
public class TodoAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public TodoAddServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("view/addTodo.html").forward(request,response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		int user_id=(int) request.getSession().getAttribute("user_id");
		String description=request.getParameter("todo");
		String category=request.getParameter("category");
		ServletContext sc = getServletContext();
		String drivername=sc.getInitParameter("drivername");
	    String driverurl=sc.getInitParameter("driverurl");
	    String username=sc.getInitParameter("databaseusername");
	    String pass=sc.getInitParameter("databasepassword");
	    Connection conn=null;
	    PreparedStatement pstmt=null;
	    try
		{
			Class.forName(drivername);
			conn = DriverManager.getConnection(driverurl, username, pass);
			String sql="insert into todos(category,description,user_id) values(?,?,?);";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,category);
			pstmt.setString(2,description);
			pstmt.setInt(3,user_id);
			int insert=pstmt.executeUpdate();
			if(insert==1){
				response.sendRedirect("TodoServlet");
			}	
			else{
				request.setAttribute("error","Addition failed");
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.setAttribute("error","Addition Failed : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Addition failed : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
	}

}

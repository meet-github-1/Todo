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
@WebServlet("/TodoDeleteServlet")
public class TodoDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public TodoDeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		int todo_id=Integer.parseInt(request.getParameter("todo_id"));
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
			String sql="delete from todos where todo_id=?;";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,todo_id);
			int delete=pstmt.executeUpdate();
			if(delete==1){
				response.sendRedirect("TodoServlet");
			}	
			else{
				request.setAttribute("error","Not able to delete");
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.setAttribute("error","Not able to delete : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Not able to delete : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}

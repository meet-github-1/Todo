package com.kmit.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		int user_id=(int)request.getSession().getAttribute("user_id");
		String uname=request.getParameter("name");
		String fullname=request.getParameter("fullname");
		String email=request.getParameter("email");
		String mobile=request.getParameter("mobile");
		ServletContext sc = getServletContext();
		String drivername=sc.getInitParameter("drivername");
	    String driverurl=sc.getInitParameter("driverurl");
	    String username=sc.getInitParameter("databaseusername");
	    String pass=sc.getInitParameter("databasepassword");
	    Connection conn=null;
		PreparedStatement pstmt =null;
		try
		{
			Class.forName(drivername);
			conn = DriverManager.getConnection(driverurl, username, pass);
		   String sql="update login set username= ?,fullname= ?,email= ?,mobile= ? where user_id= ? ";
		    pstmt=conn.prepareStatement(sql);
		    pstmt.setString(1, uname);
		    pstmt.setString(2, fullname);
			pstmt.setString(3, email);
			pstmt.setString(4, mobile);
			pstmt.setInt(5, user_id);
		   int update=pstmt.executeUpdate();
		   if(update==1) {
			    response.sendRedirect("AccountUpdateServlet");
		   }
		   else {
			   request.setAttribute("error", "Account update failed");
			   request.getRequestDispatcher("ErrorServlet").forward(request, response);
		   }	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.setAttribute("error","Account update failed : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Account update failed : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
	}

}

package com.kmit.app;

import java.io.IOException;
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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MailServlet
 */
@WebServlet("/mail")
public class MailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.getRequestDispatcher("view/ForgotPassword.html").forward(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String email=request.getParameter("email");
		ServletContext sc=getServletContext();
	    String drivername=sc.getInitParameter("drivername");
	    String driverurl=sc.getInitParameter("driverurl");
	    String username=sc.getInitParameter("databaseusername");
	    String pass=sc.getInitParameter("databasepassword");
	    Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			Class.forName(drivername);
	    	conn=DriverManager.getConnection(driverurl,username,pass);
	    	String sql="select * from login where email= ?";
	    	pstmt=conn.prepareStatement(sql);
	    	pstmt.setString(1, email);
	    	rs=pstmt.executeQuery();
	    	if(rs!=null && rs.next()) {
	    		String message="Username :"+rs.getString("username")+"\npassword: "+rs.getString("password");
	    		SendMail.send(email,message);
	    		
	    	}
	    	else {
	    		request.setAttribute("error","Not able to find email");
		    	request.getRequestDispatcher("ErrorServlet").forward(request,response);
				
	    	}
			
		}
		catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error","Not able to find email"+" "+e.getMessage());
	    	request.getRequestDispatcher("ErrorServlet").forward(request,response);
			
		}
		finally {
			try {
	    		pstmt.close();
	    		rs.close();
	    		conn.close();
	    	}
	    	catch(Exception e) {
	    		e.printStackTrace();
	    		request.getRequestDispatcher("ErrorServlet").forward(request,response);
	    	}
		}
		response.sendRedirect("LoginServlet");

	}

}

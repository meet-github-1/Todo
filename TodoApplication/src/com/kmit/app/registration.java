package com.kmit.app;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
@WebServlet("/registration")
public class registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	public registration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("view/Registration.html").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String name=request.getParameter("name");
		String fullname=request.getParameter("fullname");
		String mobile=request.getParameter("mobile");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String confirm_password=request.getParameter("confirm_password");
	    if(!password.equals(confirm_password)) {
	    	request.getRequestDispatcher("view/Registration.html").forward(request,response);
	    }
	    else {
		ServletContext sc=getServletContext();
	    String drivername=sc.getInitParameter("drivername");
	    String driverurl=sc.getInitParameter("driverurl");
	    String username=sc.getInitParameter("databaseusername");
	    String pass=sc.getInitParameter("databasepassword");
	    try {
	    	Class.forName(drivername);
	    	conn=DriverManager.getConnection(driverurl,username,pass);
	        System.out.println("Connection established");
	       String sql="select * from login where username= ?";
	       pstmt=conn.prepareStatement(sql);
	       pstmt.setNString(1, name);
	       rs=pstmt.executeQuery();
	       if(rs!=null && rs.next()) {
	    	   request.setAttribute("error","username exist");
	    	   request.getRequestDispatcher("ErrorServlet").forward(request,response);
	       }
	       else {
	    	   String insertsql="insert into login (username,fullname,email,password,mobile) values(?,?,?,?,?)";
	    	   pstmt=conn.prepareStatement(insertsql);
	    	   pstmt.setString(1,name);
	    	   pstmt.setString(2,fullname);
	    	   pstmt.setString(3,email);
	    	   pstmt.setString(4,password);
	    	   pstmt.setString(5,mobile);
	    	   int i=pstmt.executeUpdate();
	    	   if(i==1) {
	    		   response.sendRedirect("LoginServlet");
	    	   }
	    	   else {
	    		   request.setAttribute("error","Not able to register");
	    		   request.getRequestDispatcher("ErrorServlet").forward(request,response);
	    	   }
	       }
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    	System.out.println("Connection Failed");
	    	request.setAttribute("error","Not able to register"+" "+e.getMessage());
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
	    }
	}

}

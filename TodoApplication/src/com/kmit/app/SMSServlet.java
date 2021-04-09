package com.kmit.app;

import java.io.IOException;
import java.sql.*;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/SMSServlet")
public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SMSServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
         int todoid=Integer.parseInt(request.getParameter("todo_id"));
		
         ServletContext sc = getServletContext();
         String drivername=sc.getInitParameter("drivername");
         String driverurl=sc.getInitParameter("driverurl");
         String username=sc.getInitParameter("databaseusername");
         String pass=sc.getInitParameter("databasepassword");

         Connection conn=null;
         PreparedStatement pstmt =null;
         ResultSet rs = null;
		
		try {
			Class.forName(drivername);
			conn = DriverManager.getConnection(driverurl, username, pass);
			
			String sql = "select mobile, description from todos t, login l where t.user_id=l.user_id and t.todo_id= ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoid);
			rs = pstmt.executeQuery();
			
			//if user is avilable
			if (rs != null && rs.next()) {
				//user is available
				String number=rs.getString("mobile");
				String message="You have a ToDo : "+rs.getString("description");
				SendSMS.sendSMS(number, message);
				response.sendRedirect("TodoServlet");
			}
			else {
				request.setAttribute("error","Not able to fetch : TODO_ID not found" );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("error","Not able to send : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Not able to send : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}

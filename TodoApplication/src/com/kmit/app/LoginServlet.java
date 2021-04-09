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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LoginServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.getRequestDispatcher("view/login.html").forward(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		ServletContext sc=getServletContext();
	    String drivername=sc.getInitParameter("drivername");
	    String driverurl=sc.getInitParameter("driverurl");
	    String username=sc.getInitParameter("databaseusername");
	    String pass=sc.getInitParameter("databasepassword");
	    //String gRecaptchaResponse=request.getParameter("g-recaptcha-response");
	    //System.out.println(gRecaptchaResponse);
	    //boolean verify =VerifyRecaptcha.verify(gRecaptchaResponse);
	    Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			//if(verify) {
				Class.forName(drivername);
				conn = DriverManager.getConnection(driverurl, username, pass);
				String sql = "select * from login where username= ? and password= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, password);
				rs = pstmt.executeQuery();
				if (rs != null && rs.next()) {
					//user is available
					
					HttpSession session = request.getSession();
					session.setAttribute("user_id", rs.getInt("user_id"));
					session.setAttribute("uname", rs.getString("username"));
					session.setAttribute("name", rs.getString("fullname"));
					
					response.sendRedirect("TodoServlet");
				}
				else {
					request.setAttribute("error","Not able to Login : User not found" );
					request.getRequestDispatcher("ErrorServlet").forward(request, response);
				}
			//}
				/*else {
				System.out.println("error : Captcha Not Verified ");
				request.setAttribute("error","error : Captcha Not Verified" );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}*/
			
		}
		catch(Exception e){
			e.printStackTrace();
			request.setAttribute("error","Not able to login"+" "+e.getMessage());
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

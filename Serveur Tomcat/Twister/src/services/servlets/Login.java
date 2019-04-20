package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.*;

@SuppressWarnings("serial")
public class Login extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter ();
		String ip = request.getRemoteAddr();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		out.println(Authentification.login(username, password, ip));		
	}
}


package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.*;

@SuppressWarnings("serial")
public class DeleteFriend extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter ();

		String key = request.getParameter("key");
		String username = request.getParameter("username");
		
		out.println(Friend.deleteFriend(key, username));			
	}
}


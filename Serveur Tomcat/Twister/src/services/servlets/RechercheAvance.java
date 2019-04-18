package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.Recherche;

@SuppressWarnings("serial")
public class RechercheAvance extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter ();

		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String popularite = request.getParameter("popularite");
		
		out.println(Recherche.rechercheAvance(from,to,Integer.parseInt(popularite)));		
	}
}

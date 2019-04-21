package services.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.tools.UserTools;

@SuppressWarnings("serial")
public class UserImage extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("image/png");
		
		String username = request.getParameter("username");
		
		String pathToWeb = null;
		try {
			pathToWeb = UserTools.getImage(username);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		File f = new File(pathToWeb);
		BufferedImage bi = ImageIO.read(f);
		
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "png", out);
		
		out.close();
	}
}
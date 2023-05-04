package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.beans.Item;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/SellPage")
public class GoToSellPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public GoToSellPage() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		User user = (User) session.getAttribute("user");

		// Redirect to the SellPage and add missions to the parameters
		String path = "/WEB-INF/Sell.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
		List<Item> items = null;
		ItemDAO itemDAO = new ItemDAO(connection);
		try {
			items = itemDAO.findAllItemUnSold();
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<table border='1'>");
		printWriter.println("<th>ID</th><th>Name</th><th>Description</th><th>Image</th><th>Price</th>");
		for (Item item : items) {
			printWriter.println("<tr>");
			printWriter.println("<trd>"+item.getItemID()+"</td>");
			printWriter.println("<trd>"+item.getItemName()+"</td>");
			printWriter.println("<trd>"+item.getDescription()+"</td>");
			printWriter.println("<td><img src='" + item.getImagePath()+"'style='height: 100px;width: 100px'></td>");
			printWriter.println("<trd>"+item.getItemPrice()+"</td>");
			printWriter.println("</tr>");
		}
		printWriter.println("</table>");
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
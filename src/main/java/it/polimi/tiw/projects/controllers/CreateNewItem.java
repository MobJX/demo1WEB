package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
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

import it.polimi.tiw.projects.beans.Item;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class CreateNewItem
 */
@WebServlet("/CreateNewItem")
public class CreateNewItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNewItem() {
        super();
        // TODO Auto-generated constructor stub
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


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// If the user is not logged in (not present in session) redirect to the login
				String loginpath = getServletContext().getContextPath() + "/index.html";
				HttpSession session = request.getSession();
				if (session.isNew() || session.getAttribute("user") == null) {
					response.sendRedirect(loginpath);
					return;
				}
				
		        User user = (User) session.getAttribute("user");
		        
		        response.setCharacterEncoding("utf-8");
		        response.setContentType("text/html");
		
		System.out.println("----------Create a new Item SERVLET---------");

		String itemName = request.getParameter("itemName");
		String itemPrice = request.getParameter("itemPrice");
		String itemDescription = request.getParameter("itemDescription");
		String itemImage = request.getParameter("itemImage");
		
		PrintWriter printWriter = response.getWriter();
		
		printWriter.println("<h1>Added successfully!</h1>");
		printWriter.println("<p>Item: " + itemName + "</p>");
		printWriter.println("<p>Price: " + itemPrice + "€</p>" );	
		printWriter.println("<p>Description: " + itemDescription + "</p>");	
		printWriter.println("Image: <img src=\"" + itemImage + "\" style=\"width:400px;height:300px;\"><br><br>");
		printWriter.println("<a href='SellPage'>Return</a>");
		
		Item item = new Item(itemName, itemDescription, Float.parseFloat(itemPrice), itemImage);
		ItemDAO itemDAO = new ItemDAO(connection);
		try {
			itemDAO.createItem(item);
		} catch (SQLException e){
			e.printStackTrace();
		}

		
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

package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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


@WebServlet("/CreateNewItem")
public class CreateNewItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

       
    public CreateNewItem() {
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


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// If the user is not logged in (not present in session) redirect to the login
				String loginpath = getServletContext().getContextPath() + "/index.html";
				HttpSession session = request.getSession();
				if (session.isNew() || session.getAttribute("user") == null) {
					response.sendRedirect(loginpath);
					return;
				}
				User user = (User) session.getAttribute("user");
				
		        
				System.out.println("----------Create a new Item SERVLET---------");

				String itemName = null;
				String itemPrice = null;
				String itemDescription = null;
				String itemImage = null;
				
				try {
					itemName = request.getParameter("itemName");
					itemPrice = request.getParameter("itemPrice");
					itemDescription = request.getParameter("itemDescription");
					 itemImage = request.getParameter("itemImage");
					} catch ( NullPointerException e) {
					// only for debugging e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
					return;
				}
				
				Item item = new Item(itemName, itemDescription, Float.parseFloat(itemPrice), itemImage);
			
				System.out.println("----------Create a new Item SERVLET---------");
				ItemDAO itemDAO = new ItemDAO(connection);
				try {
					int newItemID = itemDAO.createItem(item);
					item.setItemID(newItemID);
					System.out.println("ItemID: " + newItemID);
					
				} catch (SQLException e){
					e.printStackTrace();
				}
				
		        // Redirect to the ShowItem
				String path = "/WEB-INF/NewItem.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("item", item);
				templateEngine.process(path, ctx, response.getWriter());
				
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

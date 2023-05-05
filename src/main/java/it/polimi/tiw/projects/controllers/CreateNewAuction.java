package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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
import it.polimi.tiw.projects.dao.AuctionDAO;
import it.polimi.tiw.projects.beans.Auction;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/CreateNewAuction")
public class CreateNewAuction extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;


    public CreateNewAuction() {
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
		
		System.out.println("----------Create a new Auc Item SERVLET---------");
		
		String[] selectedItems = null;
		String expiryDateTimeStr = null;
		String minUP = null;
		Date expiryDateTime = null;
		
		try {
			selectedItems = request.getParameterValues("selectedItems");
			expiryDateTimeStr = request.getParameter("expiryDateTime");
			minUP = request.getParameter("minUP");
		} catch ( NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(expiryDateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		expiryDateTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		float totalPrice = 0;
		ItemDAO itemDAO = new ItemDAO(connection);
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		List<Item> aucitems = new ArrayList<>();
		Auction auction = null;
		try {
			
			// for all itemID selected count the price
			for (String itemId : selectedItems) {
			    Item item = itemDAO.findItem(Integer.parseInt(itemId));
			    totalPrice += item.getItemPrice();
			}
			
			// create a new auction
			auction = new Auction(user.getId(),totalPrice,Integer.parseInt(minUP),expiryDateTime);
			
			//update auction SQL
			auctionDAO.createAuction(auction);
			
			// return a list of item in this auction to NewAuction
			aucitems = itemDAO.findAll_Item_OfAuction(auction.getAucID());
			auction.setAucItems(aucitems);
			
			// update auc_item SQL
			for (String itemId : selectedItems) {
			    Item item = itemDAO.findItem(Integer.parseInt(itemId));
			    itemDAO.insertItemInAuction(auction, item);
			}

			
		} catch (SQLException e){
			e.printStackTrace();
		}

		// Redirect to the NewAuction
		String path = "/WEB-INF/NewAuction.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("auction", auction);
		ctx.setVariable("aucItems", aucitems);
		
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

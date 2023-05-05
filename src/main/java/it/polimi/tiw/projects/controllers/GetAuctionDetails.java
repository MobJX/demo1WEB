package it.polimi.tiw.projects.controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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

import it.polimi.tiw.projects.beans.Auction;
import it.polimi.tiw.projects.beans.Item;
import it.polimi.tiw.projects.beans.Offer;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.beans.WonAuction;
import it.polimi.tiw.projects.dao.AuctionDAO;
import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.dao.OfferDAO;
import it.polimi.tiw.projects.dao.WonAuctionDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;




@WebServlet("/GetAuctionDetails")
public class GetAuctionDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public GetAuctionDetails() {
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

		// get and check params
		Integer aucID = null;
		

	    
		try {
			aucID = Integer.parseInt(request.getParameter("aucID"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		Date now = new Date();
		boolean  canClose = false;
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		Auction auction = null;
		
		List<Item> items = null;
		ItemDAO itemDAO = new ItemDAO(connection);

		OfferDAO offerDAO = new OfferDAO(connection);
		List<Offer> offers = null;
		
		UserDAO userDAO = new UserDAO(connection);
		try {
			//verify that auction exists or not
			auction = auctionDAO.findAuction(aucID);
			
			
			if (auction == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
			
			System.out.println("Founded auction " + auction.getAucID());
			//find all of item in this auction 
			items = itemDAO.findAll_Item_OfAuction(auction.getAucID());
			auction.setAucItems(items);
			
			offers = offerDAO.findAllOfferOfAuction(auction);
			
			for (Offer offer:offers) {
				User userOffer = userDAO.findUserOfAuctionByOffer(offer);
				offer.setUserName(userOffer.getUsername());
			}
			
			if (now.after(auction.getExpiryDateTime()) && !auction.isClosed()){
				canClose = true;
			}
		

		} catch (SQLException e) {
			e.printStackTrace();
			//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
			return;
		}
		
		
		// Redirect to the AuctionDetail page and add auction to the parameters
		String path = "/WEB-INF/AuctionDetails.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("canClose", canClose);
		ctx.setVariable("auction",auction);
		ctx.setVariable("offers", offers);

		
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
package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.beans.WonAuction;
import it.polimi.tiw.projects.beans.Auction;
import it.polimi.tiw.projects.beans.Item;
import it.polimi.tiw.projects.beans.Offer;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.dao.OfferDAO;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.dao.AuctionDAO;
import it.polimi.tiw.projects.dao.WonAuctionDAO;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;

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

		
		List<Item> items = null;
		//List<Auction> auctionClosed = null;
		//List<Auction> auctionOpen = null;
		ItemDAO itemDAO = new ItemDAO(connection);
		//AuctionDAO auctionDAO = new AuctionDAO(connection);
		try {
			items = itemDAO.findAll_Item_Not_In_Auctioned();
		//	auctionClosed = auctionDAO.findAllAuctionOFF(user);
		//	auctionOpen = auctionDAO.findAllAuctionON(user.getId());
		//	
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		}
		
		List<Auction> aucON = new ArrayList<>();
		List<Item> itemsON = new ArrayList<>();
		List<Offer> offers = new ArrayList<>();
		float maxOffer = 0;
		Duration remaingTimes = null;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiry;
		
		
		List<Auction> aucOFF = new ArrayList<>();
		User bidder = null;

		UserDAO userDAO = new UserDAO(connection);
		OfferDAO offerDAO = new OfferDAO(connection);
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		WonAuctionDAO wonAuctionDAO = new WonAuctionDAO(connection);
		
		try {
			// Open Auctions
			aucON = auctionDAO.findAllAuctionON(user.getId());
			for	(Auction auc : aucON) {
				maxOffer = 0;
				itemsON = itemDAO.findAll_Item_OfAuction(auc.getAucID());
				offers = offerDAO.findAllOfferOfAuction(auc);
				for (Offer offer : offers) {
					if (offer.getOfferPrice()>maxOffer) {
						maxOffer = offer.getOfferPrice();
					}
				}
				expiry = auc.getExpiryDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				remaingTimes = Duration.between(now, expiry);
				auc.setRemaingTimes(remaingTimes);
				auc.setMaxOffer(maxOffer);
				auc.setAucItems(itemsON);
			}
			
			// Closed Auctions
			aucOFF = auctionDAO.findAllAuctionOFF(user);
		
			
			for	(Auction auc : aucOFF) {
				maxOffer = 0;
				offers = offerDAO.findAllOfferOfAuction(auc);
				for (Offer offer : offers) {
					if (offer.getOfferPrice()>maxOffer) {
						maxOffer = offer.getOfferPrice();
					}
				}
				bidder = userDAO.findUserOfAuction(auc);
				auc.setBidder(bidder);
				auc.setMaxOffer(maxOffer);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		}

		// Redirect to the SellPage and add missions to the parameters
				String path = "/WEB-INF/Sell.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("aucItems", items);
				ctx.setVariable("aucON", aucON);
				ctx.setVariable("aucOFF", aucOFF);
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
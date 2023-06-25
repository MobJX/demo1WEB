package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.beans.WonAuction;
import it.polimi.tiw.projects.dao.AuctionDAO;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.dao.WonAuctionDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/FindItemWithKeyword")
public class FindItemWithKeyword extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	
	public FindItemWithKeyword() {
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
		String keyword = null;
		try {
			keyword = request.getParameter("keyword");
			} catch ( NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		ItemDAO itemDAO = new ItemDAO(connection);
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		//List<Item> items =  null;
		List<Integer> itemsID = null;
		List<Auction> auctions = null;
		
			try {
				//items = itemDAO.findItemWithKeyWord(keyword);
				itemsID = itemDAO.findItem_ID_WithKeyWord(keyword);
				if (itemsID == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//now items  contains all the item that have keyword in name or in description and also not sold
			//so now using AuctionDAO's method public List<Auction> findAllAuctionWithItemID
			try {
				auctions = auctionDAO.findAllAuctionWithItemID(itemsID);
				if(auctions == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//find all info of auction tha won by the user
			WonAuctionDAO wonAuctionDAO = new WonAuctionDAO(connection);
			List<WonAuction> wonAuctions = new ArrayList<>();
			
			try {
				wonAuctions = wonAuctionDAO.findAllAuctionsWonByUser(user.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Item> items = null;
			
			try {
				for (Auction auc : wonAuctions) {
					items = itemDAO.findAll_Item_OfAuction(auc.getAucID());
					auc.setAucItems(items);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}

			
			
			
			// Redirect to the buy page and add auction to the parameters
			String path = "/WEB-INF/Buy.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			//ctx.setVariable("KeywordItems", items);
			ctx.setVariable("KeywordAuctions", auctions);
			ctx.setVariable("wonAuctions", wonAuctions);
			//System.out.println("this is ItemID "+items.get(0).getItemID());
			//System.out.println("this is AuctionTime"+auctions.get(0).getExpiryDateTime());
			//System.out.println("this is AuctionID "+auctions.get(0).getAucID()); FOR DEBUGGING
			
			templateEngine.process(path, ctx, response.getWriter());
		
				
				
				
	}
}

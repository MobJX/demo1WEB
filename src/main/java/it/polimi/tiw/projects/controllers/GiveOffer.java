package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import it.polimi.tiw.projects.beans.Offer;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.beans.WonAuction;
import it.polimi.tiw.projects.dao.AuctionDAO;
import it.polimi.tiw.projects.dao.ItemDAO;
import it.polimi.tiw.projects.dao.OfferDAO;
import it.polimi.tiw.projects.dao.WonAuctionDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

//TODO:	Dopo l'invio dell'offerta la pagina OFFERTA mostra l'elenco delle offerte aggiornate.
//TODO: receive aucID and check input from user is compatible 
//TODO: need check maxOffer and minUp of auction 
//TODO: the input amount should be greater than  sum of maxOffer and minUp of auction

@WebServlet("/GiveOffer")
public class GiveOffer extends HttpServlet {
    
    
    private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	
	public GiveOffer() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取请求参数
        int auctionId;
		float userBid;
		try {
			auctionId = Integer.parseInt(request.getParameter("auctionID"));
			userBid = Float.parseFloat(request.getParameter("offer"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        
 //■■■■■■■■■■■■■■■■■■■■■ regenerate info needed ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
      //find all info of auction tha won by the user
		WonAuctionDAO wonAuctionDAO = new WonAuctionDAO(connection);
		List<WonAuction> wonAuctions = new ArrayList<>();
		
		try {
			wonAuctions = wonAuctionDAO.findAllAuctionsWonByUser(user.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        AuctionDAO auctionDAO = new AuctionDAO(connection);
        // regenerate info of items!
        // show the items info 
        ItemDAO itemDAO = new ItemDAO(connection);
		Item item = null;
		List<Integer> allItemID = new ArrayList<Integer>();
		List<Item> itemsOfAuction = new ArrayList<Item>();
		
		try {
			allItemID = itemDAO.findAll_ItemID_OfAuction(auctionId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for	(int i = 0; i < allItemID.size();i++) {
			//item = new Item();
			try {
				item = itemDAO.findItem(allItemID.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			itemsOfAuction.add(item);
			}
		
    
//■■■■■■■■■■■■■■■ New Offer ■■■■■■■■■■■■■■■■■■■■■
        // 获取Auction和当前最高出价
		//getting the max offer of the auction
        Auction auction = null;
        float currentMaxBid = 0;
        try {
            auction = auctionDAO.findAuction(auctionId);
            currentMaxBid = auctionDAO.getCurrentMaxBid(auctionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //获取minUP
        //getting minUP
        //检查是否符合！
        //checking the validation of offer 
        float minUp = auction.getMinUP();
        
        if (userBid <= currentMaxBid + minUp) {
            // 不符合出价要求，返回给用户重新输入出价的机会
        	//if the validation failed then let the user give another offer 
        	OfferDAO offerDAO = new OfferDAO(connection);
        	List<Offer> Oldoffers = null;
        	try {
				Oldoffers = offerDAO.findAllOfferOfAuction(auction);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            String path = "/WEB-INF/OfferDetails.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("auction", auction);
            ctx.setVariable("errorMessage", "Your bid must be greater than current highest offer plus the minimum increase.");
            ctx.setVariable("wonAuctions", wonAuctions);
            ctx.setVariable("items", itemsOfAuction);
            ctx.setVariable("offers", Oldoffers);
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }
        /*
        float minUp = auction.getMinUP();
        if (userBid <= currentMaxBid + minUp) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Your bid must be greater than current highest offer plus the minimum increase.");
            return;
        }*/
        
        
     // 创建OfferDAO
        OfferDAO offerDAO = new OfferDAO(connection);
        List<Offer> offers = null;
        
        Offer newOffer = new Offer();
        newOffer.setAucID(auctionId);
        newOffer.setUserID(user.getId());
        newOffer.setOfferPrice(userBid);
        try {
            offerDAO.createOffer(newOffer);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to add new offer to database.");
            return;
        }
        
        
        
     // 更新拍卖和报价信息
        try {
            auction = auctionDAO.findAuction(auctionId);
            currentMaxBid = auctionDAO.getCurrentMaxBid(auctionId);
            offers = offerDAO.findAllOfferOfAuction(auction);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    	
		// Redirect to the OfferDetail page and  regenerate page重新渲染页面
		String path = "/WEB-INF/OfferDetails.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	   // ctx.setVariable("currentMaxBid", currentMaxBid);
		// 添加更新后的拍卖和报价信息到上下文中
		//adding updated info 
       ctx.setVariable("auction", auction);
       ctx.setVariable("offers", offers);
       ctx.setVariable("items", itemsOfAuction);
       ctx.setVariable("wonAuctions", wonAuctions);
       ctx.setVariable("minUP", auction.getMinUP());
       templateEngine.process(path, ctx, response.getWriter());
		
    }
    
    
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		doPost(request, response);
//	}
    
    
    
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

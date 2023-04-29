package it.polimi.tiw.projects.dao;

import java.sql.Connection;

public class WinnerDAO {
	
	private Connection con;
	//TODO: 通过userID 找到所有该用户参与的offer 然后通过 offer Group BY aucID 得到所有的拍卖的ID 可以通过set 然后 set to list 去duplicate
	//TODO： 通过得到的所有用户参与过的拍卖的ID 然后在offer表单通过 where aucID = 之前的结果 然后 group by aucID 然后 ORDER BY OfferPrice
	//TODO： 我是不是可以通过这样 JOIN OFFER AUCTION USER 然后group by aucID 再 Order by price 最后我去select
	
	private static String SELECT_AUC_OFFER_BY_AUCID ="SELECT offerID, userID, offerPrice FROM Offer WHERE aucID = ?";
	private static String ORDER_OFFERPRICE_DESC = "SELECT offerID, userID, offerPrice FROM Offer WHERE aucID = ? ORDER BY offerPrice DESC";
	
	String query = "Select aucID, max(offerprice)"
			+ "		From  Offer "
			+ "		Group by aucID "
			+ "		Having offerPrice = max(offerprice) And userID = ? ";
	
	public WinnerDAO(Connection con) {
		super();                                                      
		this.con = con;
	}

	
	

}

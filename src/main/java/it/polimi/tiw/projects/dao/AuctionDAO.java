package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polimi.tiw.projects.beans.Auction;
import it.polimi.tiw.projects.beans.Item;
import it.polimi.tiw.projects.beans.User;





public class AuctionDAO {
	
	
	private Connection con;

	private static final String SELECT_AUCTION_BY_ID = "SELECT * FROM Auction WHERE aucID=? ";
	private static final String INSERT_AUCTION_SQL = "INSERT INTO Auction (ownerID,aucPrice,minUP,expiryDateTime,isClosed)"
														+ " VALUES(?,?,?,?,?)"; //去掉了aucID
	private static final String CLOSE_AUCTION_BY_ID = "UPDATE Auction SET isClosed = ? "
														+"WHERE aucID = ?";
	private static final String SELECT_AUCTION_BY_STATUS_ID = "SELECT * FROM Auction WHERE ownerID =? and isClosed = ?";
	private static final String SELECT_AUCTION_BY_ITEM_ID ="SELECT * FROM Auction WHERE isClosed=? and "
			                                    + "aucID = (SELECT aucID FROM auc_item WHERE itemID = ? GROUP BY aucID)"
			                                    +"Order by expiryDateTime";
		//SELECT * FROM Auction WHERE isClosed=false and aucID = (SELECT aucID FROM auc_item  WHERE itemID = 1 GROUP BY aucID);
	
	
	public AuctionDAO(Connection connection) {
		this.con = connection;
		 
	}
	
	
	/**find the Auction with specific aucID
	 * 
	 * @return Auction 
	 * @throws SQLException
	 */
	
	public Auction findAuction( int id) throws SQLException{
		System.out.println("Found auction " + id);
		Auction auc = null;
		//String query ="SELECT * from Auction where aucID=? ";
		try(PreparedStatement pstatement = con.prepareStatement(SELECT_AUCTION_BY_ID);){
			pstatement.setInt(1,id);
			try(ResultSet result = pstatement.executeQuery();){
				if(result.next()) {
					auc = new Auction();
					auc.setAucID(result.getInt("aucID"));
					auc.setOwnerID(result.getInt("ownerID"));
					auc.setAucPrice(result.getFloat("aucPrice"));
					auc.setClosed(result.getBoolean("isClosed"));
					auc.setMinUP(result.getFloat("minUP"));
					auc.setExpiryDateTime(result.getTimestamp("expiryDateTime"));
					
				}
			}
		}
		return auc;
	}
	
	/**
	 * Create Auction with given parameters and the aucID will be id of this class
	 * adding it into the database table Auction
	 * @parameter ownerID
	 * @parameter aucPrice
	 * @parameter minUP
	 * @parameter deadLineDay
	 * @parameter deadLineTime
	 * @parameter isClosed
	 * @throws SQLException
	 */
	/*	
	public void createAuction(int ownerID , float aucPrice, float minUP, Date deadLineDay ,
								Time deadLineTime ,boolean isClosed)throws SQLException{
		
		//String query = "INSERT into Auction (aucID,ownerID,aucPrice,minUP,deadLineDay,deadLineTime,isClosed)"	+ "VALURS(?,?,?,?,?,?,?)";
		
		try(PreparedStatement pstatement = con.prepareStatement(INSERT_AUCTION_SQL);){
			pstatement.setInt(1,id);
			pstatement.setInt(2,ownerID);
			pstatement.setFloat(3, aucPrice);
			pstatement.setFloat(4, minUP);
			pstatement.setDate(5, deadLineDay);
			pstatement.setTime(6, deadLineTime);
			pstatement.setBoolean(7, isClosed);
			pstatement.executeUpdate();
			
		}
		
	}
	*/

	public void createAuction(Auction auction)throws SQLException{
		
		//String query = "INSERT into Auction (aucID?,ownerID,aucPrice,minUP,deadLineDay,deadLineTime,isClosed)"	+ "VALURS(?,?,?,?,?,?,?)";
		
		try(PreparedStatement pstatement = con.prepareStatement(INSERT_AUCTION_SQL);){
			pstatement.setInt(1,auction.getOwnerID());
			pstatement.setFloat(2,auction.getAucPrice());
			pstatement.setFloat(3,auction.getMinUP());
			pstatement.setTimestamp(4, new Timestamp(auction.getExpiryDateTime().getTime()));
			pstatement.setBoolean(5, auction.isClosed());
			pstatement.executeUpdate();
			
		}
		
	}
	
	
	
			
	
	
	/**UPDATE AUCTION STATE
	 * Close the Auction Updating the table with isClosed = TRUE
	 * the method returns true if update succeed else returns false 
	 * @parameter auction
	 * @return boolean 
	 * @throws SQLException
	 */
	
	public boolean closeAuction(Auction auction) throws SQLException {
		boolean isClosed = true;
		boolean rowUpdated;
		try(PreparedStatement pstatement = con.prepareStatement(CLOSE_AUCTION_BY_ID);){
			pstatement.setBoolean(1,isClosed);
			pstatement.setInt(2, auction.getAucID());
			//if succeed will be true, if failed will be false  
			rowUpdated = pstatement.executeUpdate()>0;
			
		}
		
		return rowUpdated;
	}
	
	
	
	/*find all the auction created by the user which are still available
	 * return a list of auction with ON status ASTA ANCORA APERTA
	 * 
	 * @param user
	 * @return List<Auction> 
	 * @throws SQLException
	 */
	public List<Auction> findAllAuctionON(int userID) throws SQLException{
		List<Auction> auctions = new ArrayList<Auction>();
		try(PreparedStatement pstatement = con.prepareStatement(SELECT_AUCTION_BY_STATUS_ID);){
			pstatement.setInt(1, userID);
			pstatement.setBoolean(2, false);
			try(ResultSet result = pstatement.executeQuery();){
				while(result.next()) {
					Auction auc = new Auction();
					auc.setAucID(result.getInt("aucID"));
					auc.setOwnerID(result.getInt("ownerID"));
					auc.setAucPrice(result.getFloat("aucPrice"));
					auc.setClosed(result.getBoolean("isClosed"));
					auc.setMinUP(result.getFloat("minUP"));
					auc.setExpiryDateTime(result.getTimestamp("expiryDateTime"));
					//adding auction with ON status to the list
					auctions.add(auc);
					
					
				}
			}
		}
		return auctions;
		
	}
	
	/**
	 * find all the auction created be the user already finished session
	 * return a list of auction with OFF status ASTA CHIUSA
	 * @param user 
	 * @return List<Auction> 
	 * @throws SQLException
	 */
	
	public List<Auction> findAllAuctionOFF(User user) throws SQLException{
		List<Auction> auctions = new ArrayList<Auction>();
		try(PreparedStatement pstatement = con.prepareStatement(SELECT_AUCTION_BY_STATUS_ID);){
			pstatement.setInt(1, user.getId());
			pstatement.setBoolean(2, true);
			try(ResultSet result = pstatement.executeQuery();){
				while(result.next()) {
					Auction auc = new Auction();
					auc.setAucID(result.getInt("aucID"));
					auc.setOwnerID(result.getInt("ownerID"));
					auc.setAucPrice(result.getFloat("aucPrice"));
					auc.setClosed(result.getBoolean("isClosed"));
					auc.setMinUP(result.getFloat("minUP"));
					auc.setExpiryDateTime(result.getTimestamp("expiryDateTime"));
					
					//adding auction with ON status to the list
					auctions.add(auc);
					
					
				}
			}
		}
		return auctions;
	}
	
	/**
	 * this method use a list of itemID , all those item with specific keyword
	 * then using Set to eliminate duplicate aucID because an auction could contains more items with keyword
	 * return a list of auction tha contains those item with keyword
	 * @param itemID
	 * @return
	 * @throws SQLException
	 */
	public List<Auction> findAllAuctionWithItemID (List<Integer> itemID ) throws SQLException{
		
		List<Auction> auctions = new ArrayList<Auction>();
		Set<Integer> auctionsIDSet = new HashSet<Integer>();
		
		try(PreparedStatement pstatement = con.prepareStatement(SELECT_AUCTION_BY_ITEM_ID);){
			for(int i=0; i < itemID.size(); i++) {
				pstatement.setBoolean(1, false);
				pstatement.setInt(2, itemID.get(i));
				try(ResultSet result = pstatement.executeQuery();){
					while(result.next()) {
						int id = result.getInt("aucID");
						auctionsIDSet.add(id);
						
					}
				}
				
			}
			
		}
		
		//transfer from Set to List to use get() method
		List <Integer> auctionsIDList = new ArrayList<Integer>(auctionsIDSet);
		
		//adding the auction using ID found previously
		try(PreparedStatement pstatement = con.prepareStatement(SELECT_AUCTION_BY_ID);){
			for(int i=0; i < auctionsIDList.size(); i++) {
				pstatement.setInt(1, auctionsIDList.get(i));
				try(ResultSet result = pstatement.executeQuery();){
					while(result.next()) {
						Auction auc = new Auction();
						auc.setAucID(result.getInt("aucID"));
						auc.setOwnerID(result.getInt("ownerID"));
						auc.setAucPrice(result.getFloat("aucPrice"));
						auc.setClosed(result.getBoolean("isClosed"));
						auc.setMinUP(result.getFloat("minUP"));
						//auc.setExpiryDateTime(new Date (result.getDate("expiryDateTime").getTime()));
						auc.setExpiryDateTime(result.getTimestamp("expiryDateTime"));
						// new Timestamp(auction.getExpiryDateTime().getTime())
						//adding auction with ON status to the list
						auctions.add(auc);
					}
				}
			}
		}
		
		
		
		return auctions;
		
		
	}
	
	
	/**
	 * it will return the max bid of specific auction
	 * @param auctionId
	 * @return
	 * @throws SQLException
	 */
	public float getCurrentMaxBid(int auctionId) throws SQLException {
	    float currentMaxBid = 0;
	    String query = "SELECT MAX(offerPrice) as currentMaxBid FROM Offer WHERE aucID = ?";
	    try (PreparedStatement pstatement = con.prepareStatement(query)) {
	        pstatement.setInt(1, auctionId);
	        try (ResultSet result = pstatement.executeQuery()) {
	            if (result.next()) {
	                currentMaxBid = result.getFloat("currentMaxBid");
	            }
	        }
	    }
	    return currentMaxBid;
	}

	

	

}
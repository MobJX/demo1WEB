package it.polimi.tiw.projects.beans;

import java.util.Date;

public class WonAuction extends Auction {
    private float winningBid;
    private int bidderID;
    private String bidderName;
    private String bidderAddress;
    
    

    public WonAuction() {
		super();
	}

	

	public WonAuction(float winningBid, int bidderID, String bidderName, String bidderAddress) {
		super();
		this.winningBid = winningBid;
		this.bidderID = bidderID;
		this.bidderName = bidderName;
		this.bidderAddress = bidderAddress;
	}



	public int getBidderID() {
		return bidderID;
	}

	public void setBidderID(int bidderID) {
		this.bidderID = bidderID;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderAddress() {
		return bidderAddress;
	}

	public void setBidderAddress(String bidderAddress) {
		this.bidderAddress = bidderAddress;
	}

	public float getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(float winningBid) {
        this.winningBid = winningBid;
    }
}

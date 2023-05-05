package it.polimi.tiw.projects.beans;

import java.util.Date;

public class WonAuction extends Auction {
    private float winningBid = 0;

    public WonAuction() {
		super();
	}

	

	public WonAuction(float winningBid) {
		super();
		this.winningBid = winningBid;
	}

	public float getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(float winningBid) {
        this.winningBid = winningBid;
    }
}

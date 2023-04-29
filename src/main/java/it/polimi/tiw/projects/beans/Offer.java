package it.polimi.tiw.projects.beans;


import java.util.Date;

public class Offer {
	
	private int offerID;
	private int userID;
	private int aucID;
	private float offerPrice;
	private Date offerDayTime;
	
	public Offer() {
		super();
	}
	
	public Offer(int offerID, int userID, int aucID, float offerPrice, Date offerDayTime) {
		super();
		this.offerID = offerID;
		this.userID = userID;
		this.aucID = aucID;
		this.offerPrice = offerPrice;
		this.setOfferDayTime(offerDayTime);
	}

	public int getOfferID() {
		return offerID;
	}

	public void setOfferID(int offerID) {
		this.offerID = offerID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getAucID() {
		return aucID;
	}

	public void setAucID(int aucID) {
		this.aucID = aucID;
	}

	public float getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(float offerPrice) {
		this.offerPrice = offerPrice;
	}

	public Date getOfferDayTime() {
		return offerDayTime;
	}

	public void setOfferDayTime(Date offerDayTime) {
		this.offerDayTime = offerDayTime;
	}

	
	

}
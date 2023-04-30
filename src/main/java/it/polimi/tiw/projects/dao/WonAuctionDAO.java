package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Auction;
import it.polimi.tiw.projects.beans.WonAuction;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WonAuctionDAO {
    private Connection connection;

    public WonAuctionDAO(Connection connection) {
        this.connection = connection;
    }

    public List<WonAuction> findAllAuctionsWonByUser(int userID) throws SQLException {
        List<WonAuction> wonAuctions = new ArrayList<>();
        String query = "SELECT a.*, o.offerPrice "
        		+ "FROM Auction a "
        		+ "JOIN Offer o ON a.aucID = o.aucID "
        		+ "WHERE a.isClosed = 1 AND o.userID = ? "
        		+ "AND o.offerPrice = ( "
        		+ " SELECT MAX(offerPrice) "
        		+ " FROM Offer "
        		+ " WHERE aucID = a.aucID"
        		+ ")";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    WonAuction wonAuction = new WonAuction();
                    wonAuction.setAucID(resultSet.getInt("aucID"));
                    wonAuction.setOwnerID(resultSet.getInt("ownerID"));
                    wonAuction.setAucPrice(resultSet.getFloat("aucPrice"));
                    wonAuction.setMinUP(resultSet.getFloat("minUP"));
                    wonAuction.setExpiryDateTime(resultSet.getTimestamp("expiryDateTime"));
                    wonAuction.setClosed(resultSet.getBoolean("isClosed"));
                    wonAuction.setWinningBid(resultSet.getFloat("offerPrice"));
                    wonAuctions.add(wonAuction);
                }
            }
        }

        return wonAuctions;
    }
}

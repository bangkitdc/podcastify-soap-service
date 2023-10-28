package com.podcastify.repository;

import com.podcastify.model.SubscriberModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.sql.ResultSet;

public class SubscriberRepository extends Repository {

    public void addSubscriber(SubscriberModel sm) throws SQLException {
        String query = "INSERT INTO subscriptions (creator_id, subscriber_id) VALUES(?, ?)";

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            stmt.setInt(1, sm.getCreatorID());
            stmt.setInt(2, sm.getSubscriberID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                String message = "User with subscriber id: " + sm.getSubscriberID()
                        + " , has sent a subscription request to creator id: " + sm.getCreatorID();
                throw new SQLException(message);
            }
            this.conn.commit();
        }
    }

    public void updateSubscriptionStatus(SubscriberModel sm) throws SQLException {
        int statusId = 1;
        String query = "SELECT status_id FROM statuses WHERE name = ?";

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            stmt.setString(1, sm.getStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    statusId = rs.getInt("status_id");
                }
            }
        }

        query = "UPDATE subscriptions SET status_id = ?, updated_at = ? WHERE creator_id = ? and subscriber_id = ?";

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            Instant instant = Instant.now();
            Timestamp currentTimestamp = Timestamp.from(instant);

            stmt.setInt(1, statusId);
            stmt.setTimestamp(2, currentTimestamp);
            stmt.setInt(3, sm.getCreatorID());
            stmt.setInt(4, sm.getSubscriberID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                String message = "No subscription found with the provided creator_id and subscriber_id";
                throw new SQLException(message);
            }
            this.conn.commit();
        }
    }

    public String getSubscriptionStatus(SubscriberModel sm) throws SQLException {
        String query = "SELECT st.name status FROM subscriptions su INNER JOIN statuses st ON su.status_id = st.status_id WHERE su.creator_id = ? AND su.subscriber_id = ?";
        String status = null;

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            stmt.setInt(1, sm.getCreatorID());
            stmt.setInt(2, sm.getSubscriberID());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }

            if (status == null) {
                throw new SQLException("No data found for the given subscriber and creator IDs");
            }
            this.conn.commit();
        }

        return status;
    }

}

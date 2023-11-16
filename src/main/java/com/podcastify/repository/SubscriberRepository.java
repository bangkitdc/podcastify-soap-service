package com.podcastify.repository;

import com.podcastify.model.SubscriberModel;
import com.podcastify.constant.Status;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class SubscriberRepository extends Repository {

    public void addSubscriber(SubscriberModel sm) throws SQLException {
        String query = "INSERT INTO subscriptions (creator_id, subscriber_id, subscriber_name, creator_name) VALUES(?, ?, ?, ?)";

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            stmt.setInt(1, sm.getCreatorID());
            stmt.setInt(2, sm.getSubscriberID());
            stmt.setString(3, sm.getSubscriberName());
            stmt.setString(4, sm.getCreatorName());

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

        // Delete 'REJECTED' subscription to enable subscriber to subscribe again
        if (statusId == Status.REJECTED) {
            query = "DELETE FROM subscriptions WHERE creator_id = ? and subscriber_id = ?";
        } else {
            query = "UPDATE subscriptions SET status_id = ?, updated_at = ? WHERE creator_id = ? and subscriber_id = ?";
        }

        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            if (statusId == Status.REJECTED) {
                stmt.setInt(1, sm.getCreatorID());
                stmt.setInt(2, sm.getSubscriberID());
            } else {
                Instant instant = Instant.now();
                Timestamp currentTimestamp = Timestamp.from(instant);

                stmt.setInt(1, statusId);
                stmt.setTimestamp(2, currentTimestamp);
                stmt.setInt(3, sm.getCreatorID());
                stmt.setInt(4, sm.getSubscriberID());
            }

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

            this.conn.commit();
        }

        return status;
    }

    public List<SubscriberModel> getSubscriptionBySubscriberID(int subscriberID, String status) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT su.creator_id creator_id, su.creator_name creator_name, su.subscriber_name subscriber_name, st.name status, su.created_at, su.updated_at ")
                .append("FROM subscriptions su ")
                .append("INNER JOIN statuses st ON su.status_id = st.status_id ")
                .append("WHERE su.subscriber_id = ?");

        if (!status.equals("ALL")) {
            query.append(" AND st.name = ?");
        }

        List<SubscriberModel> subscribers = new ArrayList<>();
        try (PreparedStatement stmt = this.conn.prepareStatement(query.toString())) {
            stmt.setInt(1, subscriberID);
            if (!status.equals("ALL")) {
                stmt.setString(2, status);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SubscriberModel subscriber = new SubscriberModel();
                subscriber.setSubscriberID(subscriberID);
                subscriber.setSubscriberName(rs.getString("subscriber_name"));
                subscriber.setCreatorID(rs.getInt("creator_id"));
                subscriber.setCreatorName(rs.getString("creator_name"));
                subscriber.setStatus(rs.getString("status"));
                subscriber.setCreatedAt(rs.getTimestamp("created_at"));
                subscriber.setUpdatedAt(rs.getTimestamp("updated_at"));
                subscribers.add(subscriber);
            }
            this.conn.commit();
        }

        return subscribers;
    }

    public List<SubscriberModel> getSubscriptionByCreatorID(int creatorID, String status) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT su.creator_id creator_id, su.creator_name creator_name, su.subscriber_name subscriber_name, su.subscriber_id subscriber_id, st.name status, su.created_at, su.updated_at ")
                .append("FROM subscriptions su ")
                .append("INNER JOIN statuses st ON su.status_id = st.status_id ")
                .append("WHERE su.creator_id = ?");

        if (!status.equals("ALL")) {
            query.append(" AND st.name = ?");
        }

        List<SubscriberModel> subscribers = new ArrayList<>();
        try (PreparedStatement stmt = this.conn.prepareStatement(query.toString())) {
            stmt.setInt(1, creatorID);
            if (!status.equals("ALL")) {
                stmt.setString(2, status);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SubscriberModel subscriber = new SubscriberModel();
                subscriber.setCreatorID(creatorID);
                subscriber.setSubscriberName(rs.getString("subscriber_name"));
                subscriber.setCreatorName(rs.getString("creator_name"));
                subscriber.setStatus(rs.getString("status"));
                subscriber.setCreatedAt(rs.getTimestamp("created_at"));
                subscriber.setUpdatedAt(rs.getTimestamp("updated_at"));
                subscriber.setSubscriberID(rs.getInt("subscriber_id"));
                subscribers.add(subscriber);
            }
            this.conn.commit();
        }

        return subscribers;
    }

    public List<SubscriberModel> getAllSubscriptions() throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT su.creator_id creator_id, su.creator_name creator_name, su.subscriber_id subscriber_id, su.subscriber_name subscriber_name, st.name status, su.created_at, su.updated_at ")
                .append("FROM subscriptions su ")
                .append("INNER JOIN statuses st ON su.status_id = st.status_id ");

        List<SubscriberModel> subscribers = new ArrayList<>();
        try (PreparedStatement stmt = this.conn.prepareStatement(query.toString())) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SubscriberModel subscriber = new SubscriberModel();
                subscriber.setSubscriberID(rs.getInt("subscriber_id"));
                subscriber.setSubscriberName(rs.getString("subscriber_name"));
                subscriber.setCreatorID(rs.getInt("creator_id"));
                subscriber.setCreatorName(rs.getString("creator_name"));
                subscriber.setStatus(rs.getString("status"));
                subscriber.setCreatedAt(rs.getTimestamp("created_at"));
                subscriber.setUpdatedAt(rs.getTimestamp("updated_at"));
                subscribers.add(subscriber);
            }
            this.conn.commit();
        }

        return subscribers;
    }

}

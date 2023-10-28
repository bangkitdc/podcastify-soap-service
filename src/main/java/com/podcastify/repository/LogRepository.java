package com.podcastify.repository;

import com.podcastify.model.LogModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository extends Repository {

    public void addLog(LogModel log) throws SQLException {
        String query = "INSERT INTO logs (description, IP, endpoint, from_service) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = this.conn.prepareStatement(query)) {
            stmt.setString(1, log.getDescription());
            stmt.setString(2, log.getIP());
            stmt.setString(3, log.getEndpoint());
            stmt.setString(4, log.getFromService());
            stmt.execute();
            this.conn.commit();
        }
    }
}

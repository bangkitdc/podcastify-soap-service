package com.podcastify.repository;

import com.podcastify.db.Database;
import com.podcastify.model.LogModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository {
    private Database db;
    private Connection conn;

    public LogRepository() {
        this.db = new Database();
        this.conn = this.db.getConnection();
    }

    public void addLog(LogModel log) throws SQLException {
        String query = "INSERT INTO logs (description, IP, endpoint) VALUES(?, ?, ?)";
        PreparedStatement stmt = this.conn.prepareStatement(query);
        stmt.setString(1, log.getDescription());
        stmt.setString(2, log.getIP());
        stmt.setString(3, log.getEndpoint());
        stmt.execute();
        stmt.close();
        this.conn.commit();
    }
}

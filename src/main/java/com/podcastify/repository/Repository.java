package com.podcastify.repository;

import com.podcastify.db.Database;

import java.sql.Connection;

public abstract class Repository {
    protected Database db;
    protected Connection conn;

    public Repository() {
        this.db = new Database();
        this.conn = this.db.getConnection();
    }
}

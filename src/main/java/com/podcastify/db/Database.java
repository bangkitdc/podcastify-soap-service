package com.podcastify.db;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class Database {
    private Connection conn;
    private String MYSQL_USER;
    private String MYSQL_PASSWORD;
    private String MYSQL_HOST;
    private String MYSQL_DATABASE;
    private int MYSQL_PORT;

    public Database() {
        try {
            Dotenv dotenv = Dotenv.load();
            this.MYSQL_USER = dotenv.get("MYSQL_USER");
            this.MYSQL_PASSWORD = dotenv.get("MYSQL_PASSWORD");
            this.MYSQL_HOST = dotenv.get("MYSQL_HOST", "localhost");
            this.MYSQL_PORT = Integer.parseInt(dotenv.get("MYSQL_PORT", "3306"));
            this.MYSQL_DATABASE = dotenv.get("MYSQL_DATABASE");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setup connection
        try {
            String url = String.format(
                    "jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false&allowPublicKeyRetrieval=true",
                    this.MYSQL_HOST, this.MYSQL_PORT, this.MYSQL_DATABASE, this.MYSQL_USER, this.MYSQL_PASSWORD);
            this.conn = DriverManager.getConnection(url);
            this.conn.setAutoCommit(false);
        } catch (Exception e) {
            if (!(e instanceof CommunicationsException)) {
                System.out.println("Please check your env files! Exiting...");
                System.exit(1);
            }
        }
    }

    public Connection getConnection() {
        return this.conn;
    }
}

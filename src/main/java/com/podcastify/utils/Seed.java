package com.podcastify.utils;

import com.podcastify.db.Database;

import com.github.javafaker.Faker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Seed {
    private Database db;
    private Connection conn;

    public Seed() {
        this.db = new Database();
        this.conn = this.db.getConnection();
    }

    public void seedSubscriptions() {
        Faker faker = new Faker();
        Set<String> existingSubscriptions = new HashSet<>();

        String insertQuery = "INSERT INTO subscriptions (creator_id, creator_name, subscriber_id, subscriber_name, status_id) VALUES (?, ?, ?, ?, ?)";

        System.out.println("Seeding subscriptions table...");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);

            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    String subscriptionKey = i + "-" + j;
                    if (!existingSubscriptions.contains(subscriptionKey)) {
                        preparedStatement.setInt(1, i);
                        preparedStatement.setString(2, faker.name().fullName());
                        preparedStatement.setInt(3, j);
                        preparedStatement.setString(4, faker.name().fullName());
                        preparedStatement.setInt(5, faker.number().numberBetween(1, 4));
                        preparedStatement.executeUpdate();

                        existingSubscriptions.add(subscriptionKey);
                    }
                }
            }
            this.conn.commit();
            System.out.println("Seeding completed successfully.");
        } catch (SQLException e) {
            System.out.println("Error seeding data: " + e.getMessage());
        }
    }
}
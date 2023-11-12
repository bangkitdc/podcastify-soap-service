package com.podcastify.main;

import com.podcastify.implementor.SubscribeServiceImpl;
import com.podcastify.utils.Seed;

import javax.xml.ws.Endpoint;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        try {
            // Seeding
            Seed s = new Seed();
            s.seedSubscriptions();

            // Publish endpoint after done seeding
            Dotenv dotenv = Dotenv.load(); 
            String port = dotenv.get("PORT", "5555");

            // Subscription route
            Endpoint.publish("http://0.0.0.0:" + port + "/api/v1/subscription", new SubscribeServiceImpl());


            System.out.println("Listen and serve at: http://localhost:" + port + "/api/v1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

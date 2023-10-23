package com.podcastify.main;

import javax.xml.ws.Endpoint;

import com.podcastify.implementor.PodcastServiceImpl;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:5555/api/podcast/subscription", new PodcastServiceImpl());
            System.out.println("Listen and serve at: http://localhost:5555/api");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package main;

import javax.xml.ws.*;
import controller.SubscriptionController;

public class Main {
    public static void main(String[] args){
        try {
            Endpoint.publish("http://localhost:5555/api/subscription", new SubscriptionController());
            System.out.println("Server created at http://localhost:5555");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
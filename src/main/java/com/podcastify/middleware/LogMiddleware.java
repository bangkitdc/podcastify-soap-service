package com.podcastify.middleware;

import com.podcastify.model.LogModel;
import com.podcastify.implementor.LogServiceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;

import javax.xml.ws.handler.MessageContext;
import java.net.InetSocketAddress;

public class LogMiddleware {
    private LogServiceImpl lsi = new LogServiceImpl();
    
    public LogMiddleware(MessageContext mc, String description, String endpoint){
        HttpExchange httpExchange = (HttpExchange)mc.get(JAXWSProperties.HTTP_EXCHANGE);
        InetSocketAddress remoteAddress = httpExchange.getRemoteAddress();
        String IP = remoteAddress.getAddress().toString().substring(1);

        LogModel lm = new LogModel();
        lm.setIP(IP);
        lm.setDescription(description);
        lm.setEndpoint(endpoint);

        lsi.addLog(lm);
    }
}

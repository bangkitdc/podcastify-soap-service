package com.podcastify.middleware;

import com.podcastify.model.LogModel;
import com.podcastify.implementor.LogServiceImpl;
import com.podcastify.constant.ServiceConstants;

import io.github.cdimascio.dotenv.Dotenv;
import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;
import javax.xml.ws.handler.MessageContext;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;

public class LogMiddleware {
    private LogServiceImpl lsi = new LogServiceImpl();
    private Map<String, String> apiKeysToServices;
    private String incomingService;

    public LogMiddleware(MessageContext mc, String description, String endpoint) throws SecurityException {
        HttpExchange httpExchange = (HttpExchange) mc.get(JAXWSProperties.HTTP_EXCHANGE);

        // Initialize api keys & provided services
        Dotenv dotenv = Dotenv.load();
        this.apiKeysToServices = new HashMap<>();
        this.apiKeysToServices.put(dotenv.get("REST_API_KEY"), ServiceConstants.REST_SERVICE);
        this.apiKeysToServices.put(dotenv.get("APP_API_KEY"), ServiceConstants.APP_SERVICE);

        // Extract the API key from the request & validate
        String apiKey = httpExchange.getRequestHeaders().getFirst("x-api-key");
        String fromService = this.getServiceFromApiKey(apiKey);

        InetSocketAddress remoteAddress = httpExchange.getRemoteAddress();
        String IP = remoteAddress.getAddress().toString().substring(1);

        LogModel lm = new LogModel();
        lm.setIP(IP);
        lm.setDescription(description);
        lm.setEndpoint(endpoint);
        lm.setFromService(fromService);

        lsi.addLog(lm);
    }

    private String getServiceFromApiKey(String apiKey) throws SecurityException {
        String serviceName = apiKeysToServices.get(apiKey);
        this.incomingService = serviceName;

        if (serviceName == null) {
            throw new SecurityException("Invalid API Key!");
        }

        return serviceName;
    }

    public String getServiceName() {
        return this.incomingService;
    }
}

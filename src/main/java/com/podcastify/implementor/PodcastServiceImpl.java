package com.podcastify.implementor;

import com.podcastify.constant.Response;
import com.podcastify.service.PodcastService;
import com.podcastify.middleware.LogMiddleware;

import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;

@WebService(targetNamespace = "http://com.podcastify.service/", endpointInterface = "com.podcastify.service.PodcastService")
public class PodcastServiceImpl implements PodcastService {
   @Resource
   private WebServiceContext wsContext;

   @Override
   public int subscribe(String name) {
      String description = name + " is subscribing";
      MessageContext mc = wsContext.getMessageContext();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         // TODO: complete this implementation, current is just for logging purpose

         return Response.HTTP_STATUS_ACCEPTED;

      } catch (Exception e) {
         System.out.println("Security exception: " + e.getMessage());
         return Response.HTTP_STATUS_UNAUTHORIZED;
      }
   }
}

package com.podcastify.implementor;

import com.podcastify.constant.Response;
import com.podcastify.constant.ServiceConstants;
import com.podcastify.service.SubscribeService;
import com.podcastify.middleware.LogMiddleware;
import com.podcastify.repository.SubscriberRepository;
import com.podcastify.model.SubscriberModel;
import com.podcastify.model.BaseResponseModel;
import com.podcastify.model.ResponseModel;

import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.List;

@WebService(targetNamespace = "http://com.podcastify.service/", endpointInterface = "com.podcastify.service.SubscribeService")
public class SubscribeServiceImpl implements SubscribeService {
   @Resource
   private WebServiceContext wsContext;
   private SubscriberRepository sr;

   @Override
   public ResponseModel subscribe(int subscriberID, int creatorID) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }

      String description = "Subscriber ID: " + subscriberID + " is subscribing to creator ID: " + creatorID;
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.APP_SERVICE)) {
            SubscriberModel sm = new SubscriberModel();
            sm.setCreatorID(creatorID);
            sm.setSubscriberID(subscriberID);
            sr.addSubscriber(sm);

            return Response.createResponse(Response.HTTP_STATUS_ACCEPTED, "success");
         }

         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed");

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         return Response.createResponse(e);
      }
   }

   @Override
   public ResponseModel updateStatus(int subscriberID, int creatorID, String status) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }

      String sanitizedStatus = Jsoup.clean(status, Safelist.none());
      String description = "Updated status of Subscriber ID: " + subscriberID + " with creator ID: " + creatorID;
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.REST_SERVICE)) {
            SubscriberModel sm = new SubscriberModel();
            sm.setCreatorID(creatorID);
            sm.setSubscriberID(subscriberID);
            sm.setStatus(sanitizedStatus);
            sr.updateSubscriptionStatus(sm);

            return Response.createResponse(Response.HTTP_STATUS_OK, "success");
         }

         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed");

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         return Response.createResponse(e);
      }
   }

   @Override
   public BaseResponseModel getStatus(int subscriberID, int creatorID) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }

      String description = "Fetched status of Subscriber ID: " + subscriberID + " with creator ID: " + creatorID;
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.APP_SERVICE)) {
            SubscriberModel sm = new SubscriberModel();
            sm.setCreatorID(creatorID);
            sm.setSubscriberID(subscriberID);

            String status = sr.getSubscriptionStatus(sm);

            return Response.createResponse(Response.HTTP_STATUS_OK, "success", status);
         }

         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed", new Object());

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         return Response.createResponse(e, new Object());
      }
   }

   @Override
   public List<SubscriberModel> getSubscriptionByCreatorID(int creatorID, String status) {
      if (creatorID <= 0) {
         throw new IllegalArgumentException("Creator ID must be positive integer");
      }

      String description = "Fetched subscribers of Creator ID: " + creatorID;
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.REST_SERVICE)) {
            List<SubscriberModel> subscribers = sr.getSubscriptionByCreatorID(creatorID, status);

            return Response.createResponse(Response.HTTP_STATUS_OK, "success", subscribers);
         }

         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed", null);

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         return Response.createResponse(e, null);
      }
   }
}

package com.podcastify.implementor;

import com.podcastify.constant.Response;
import com.podcastify.constant.ServiceConstants;
import com.podcastify.service.SubscribeService;
import com.podcastify.middleware.LogMiddleware;
import com.podcastify.repository.SubscriberRepository;
import com.podcastify.model.SubscriberModel;
import com.podcastify.model.BaseResponseModel;
import com.podcastify.model.ResponseModel;
import com.podcastify.utils.Request;
import com.podcastify.utils.EmailGenerator;
import com.podcastify.utils.MethodList;

import io.github.cdimascio.dotenv.Dotenv;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import java.util.List;
import java.util.ArrayList;

@WebService(targetNamespace = "http://com.podcastify.service/", endpointInterface = "com.podcastify.service.SubscribeService")
public class SubscribeServiceImpl implements SubscribeService {
   @Resource
   private WebServiceContext wsContext;
   private SubscriberRepository sr;

   @Override
   public ResponseModel subscribe(int subscriberID, int creatorID, String subscriberName, String creatorName) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }
      String sanitizedSubscriberName = Jsoup.clean(subscriberName, Safelist.none());

      String description = "Subscriber ID    : " + subscriberID + "\n" +
                           "Creator ID       : " + creatorID + "\n" +
                           "Subscriber Name  : " + sanitizedSubscriberName + "\n" +
                           "Creator Name     : " + creatorName + "\n" +
                           "Method           : subscribe";
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.APP_SERVICE)) {
            SubscriberModel sm = new SubscriberModel();
            sm.setCreatorID(creatorID);
            sm.setSubscriberID(subscriberID);
            sm.setSubscriberName(sanitizedSubscriberName);
            sm.setCreatorName(creatorName);
            sr.addSubscriber(sm);

            // Send email to REST Admin
            Dotenv dotenv = Dotenv.load();
            String receiver = dotenv.get("RECEIVER_EMAIL");

            EmailServiceImpl emailServiceImpl = new EmailServiceImpl();

            String emailContent = EmailGenerator.generateEmail(sanitizedSubscriberName, creatorName);

            emailServiceImpl.sendEmail(receiver, "Podcastify - [New Subscription]", emailContent);

            MethodList.printProcessStatus(Response.HTTP_STATUS_ACCEPTED, "/subscription", "subscribe");
            return Response.createResponse(Response.HTTP_STATUS_ACCEPTED, "success");
         }

         MethodList.printProcessStatus(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "/subscription", "subscribe");
         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed");

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         MethodList.printProcessStatus(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, "/subscription", "subscribe");
         return Response.createResponse(e);
      }
   }

   @Override
   public ResponseModel updateStatus(int subscriberID, int creatorID, String creatorName, String status) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }

      String sanitizedStatus = Jsoup.clean(status, Safelist.none());
      String description = "Subscriber ID    : " + subscriberID + "\n" +
                           "Creator ID       : " + creatorID + "\n" +
                           "Creator Name     : " + creatorName + "\n" +
                           "Status           : " + status + "\n" +
                           "Method           : updateStatus";
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

            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("APP_URL") + "/subscription";
            Request request = new Request(url);

            // Set the HTTP method to POST
            request.setMethod("POST");

            // Add parameters
            request.addParam("subscriber_id", subscriberID);
            request.addParam("creator_id", creatorID);
            request.addParam("creator_name", creatorName);
            request.addParam("status", status);

            // Add headers
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");

            // Send request
            String response = request.send();

            MethodList.printProcessStatus(Response.HTTP_STATUS_OK, "/subscription", "updateStatus");
            return Response.createResponse(Response.HTTP_STATUS_OK, "success");
         }

         MethodList.printProcessStatus(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "/subscription", "updateStatus");
         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed");

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         MethodList.printProcessStatus(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, "/subscription", "updateStatus");
         return Response.createResponse(e);
      }
   }

   @Override
   public List<BaseResponseModel> getStatus(int subscriberID, int creatorID) {
      if (subscriberID <= 0 || creatorID <= 0) {
         throw new IllegalArgumentException("Subscriber ID and Creator ID must be positive integers");
      }

      String description = "Subscriber ID    : " + subscriberID + "\n" +
                           "Creator ID       : " + creatorID + "\n" +
                           "Method           : getStatus";
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.REST_SERVICE)) {
            SubscriberModel sm = new SubscriberModel();
            sm.setCreatorID(creatorID);
            sm.setSubscriberID(subscriberID);

            String status = sr.getSubscriptionStatus(sm);
            if (status == null) {
               return Response.createResponse(Response.HTTP_STATUS_OK, "success", "NOT_SUBSCRIBED");
            }

            MethodList.printProcessStatus(Response.HTTP_STATUS_OK, "/subscription", "getStatus");
            return Response.createResponse(Response.HTTP_STATUS_OK, "success", status);
         }

         MethodList.printProcessStatus(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "/subscription", "getStatus");
         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed",
               "method not allowed");

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         MethodList.printProcessStatus(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, "/subscription", "getStatus");
         return Response.createResponse(e, "an exception occured");
      }
   }

   @Override
   public List<SubscriberModel> getSubscriptionByCreatorID(int creatorID, String status) {
      if (creatorID <= 0) {
         throw new IllegalArgumentException("Creator ID must be positive integer");
      }

      String description = "Creator ID       : " + creatorID + "\n" +
                           "Status           : " + status + "\n" +
                           "Method           : getSubscriptionByCreatorID";
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.REST_SERVICE)) {
            List<SubscriberModel> subscribers = sr.getSubscriptionByCreatorID(creatorID, status);

            MethodList.printProcessStatus(Response.HTTP_STATUS_OK, "/subscription", "getSubscriptionByCreatorID");
            return Response.createResponse(Response.HTTP_STATUS_OK, "success", subscribers);
         }

         MethodList.printProcessStatus(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "/subscription", "getSubscriptionByCreatorID");
         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed",
               new ArrayList<>());

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         MethodList.printProcessStatus(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, "/subscription", "getSubscriptionByCreatorID");
         return Response.createResponse(e, new ArrayList<>());
      }
   }

   @Override
   public List<SubscriberModel> getAllSubscriptions() {

      String description = "Method           : getAllSubscriptions";
      MessageContext mc = wsContext.getMessageContext();
      this.sr = new SubscriberRepository();

      try {
         LogMiddleware loggingMiddleware = new LogMiddleware(mc, description, "/subscription");

         if (loggingMiddleware.getServiceName().equals(ServiceConstants.REST_SERVICE)) {
            List<SubscriberModel> subscribers = sr.getAllSubscriptions();

            MethodList.printProcessStatus(Response.HTTP_STATUS_OK, "/subscription", "getAllSubscriptions");
            return Response.createResponse(Response.HTTP_STATUS_OK, "success", subscribers);
         }

         MethodList.printProcessStatus(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "/subscription", "getAllSubscriptions");
         return Response.createResponse(Response.HTTP_STATUS_METHOD_NOT_ALLOWED, "method not allowed",
               new ArrayList<>());

      } catch (Exception e) {
         System.out.println("Exception: " + e.getMessage());

         MethodList.printProcessStatus(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, "/subscription", "getAllSubscriptions");
         return Response.createResponse(e, new ArrayList<>());
      }
   }
}

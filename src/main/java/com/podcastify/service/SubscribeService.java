package com.podcastify.service;

import com.podcastify.model.ResponseModel;
import com.podcastify.model.BaseResponseModel;
import com.podcastify.model.SubscriberModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface SubscribeService {
    @WebMethod
    ResponseModel subscribe(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID);

    @WebMethod
    ResponseModel updateStatus(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID, @WebParam(name = "status") String status);

    @WebMethod
    BaseResponseModel getStatus(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID);

    @WebMethod
    List<SubscriberModel> getSubscriptionByCreatorID(@WebParam(name = "creator_id") int creatorID, @WebParam(name = "status") String status);
}

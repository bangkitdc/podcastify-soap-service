package com.podcastify.service;

import com.podcastify.model.ResponseModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface SubscribeService {
    @WebMethod
    ResponseModel subscribe(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID);

    @WebMethod
    ResponseModel updateStatus(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID, @WebParam(name = "status") String status);

    @WebMethod
    ResponseModel getStatus(@WebParam(name = "subscriber_id") int subscriberID, @WebParam(name = "creator_id") int creatorID);
}

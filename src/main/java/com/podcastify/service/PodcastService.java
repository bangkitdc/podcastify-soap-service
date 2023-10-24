package com.podcastify.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface PodcastService {
    @WebMethod
    int subscribe(@WebParam(name = "name") String name);
}

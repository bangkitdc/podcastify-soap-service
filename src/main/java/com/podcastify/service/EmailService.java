package com.podcastify.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface EmailService {
  @WebMethod
  public void sendEmail(String to, String subject, String body);
}

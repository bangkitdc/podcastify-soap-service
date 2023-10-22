package controller;


import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class SubscriptionController {
    @WebMethod
    public String subscribe(String email) {
        // Implement subscription logic
        return "Subscribed with email: " + email;
    }
}

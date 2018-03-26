package service;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;

import java.awt.*;

public class MailService {
    public ClientResponse sendmessage(String email) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-ab7e9deb67d86ad5d02c6f855393da42"));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/sandbox2c96f46959c84fdd898b0ad77533a7d2.mailgun.org/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", "Mailgun Sandbox <postmaster@sandbox2c96f46959c84fdd898b0ad77533a7d2.mailgun.org>");
        formData.add("to", email);
        formData.add("subject", "Your OADTurk temporary password");
        formData.add("text", "Hello, your temporary password is 1kL3RRrW. Please change your password after you logged in!");
        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
    }
}


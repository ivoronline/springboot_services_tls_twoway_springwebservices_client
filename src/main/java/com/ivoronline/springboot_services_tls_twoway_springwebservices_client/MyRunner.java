package com.ivoronline.springboot_services_tls_twoway_springwebservices_client;

import com.ivoronline.soap.GetPersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MyRunner implements CommandLineRunner {

  //PROPERTIES
  String serverURL          = "https://localhost:8085/Hello";

  //CLIENT TRUST STORE
  String trustStoreName     = "ClientTrustStore.jks";
  String trustStorePassword = "mypassword";
  String trustStoreType     = "JKS";

  //CLIENT KEY STORE (For Two-Way TLS)
  String keyStoreName       = "ClientKeyStore.jks";
  String keyStorePassword   = "mypassword";
  String keyStoreType       = "JKS";

  @Autowired PersonClient personClient;

  //===============================================================================
  // RUN
  //===============================================================================
  @Override
  public void run(String... args) throws Exception {
    //restTemplate();
    webServiceTemplate();
  }

  //===============================================================================
  // WEB SERVICE TEMPLATE
  //===============================================================================
  public void webServiceTemplate() throws Exception {

    //SEND REQUEST
    GetPersonResponse response = personClient.getPerson(1);

    //PRINT RESPONSE
    System.out.println(response.getName());


  }

  //===============================================================================
  // REST TEMPLATE
  //===============================================================================
  public void restTemplate() throws Exception {

    //GET REQUEST FACTORY (for Two-Way TLS)
    HttpComponentsClientHttpRequestFactory requestFactory = UtilClientRestTemplate.getRequestFactoryForTwoWayTLS(
      trustStoreName, trustStorePassword, trustStoreType,
      keyStoreName  , keyStorePassword  , keyStoreType
    );

    //SEND REQUEST
    RestTemplate    restTemplate = new RestTemplate();
                    restTemplate.setRequestFactory(requestFactory);
    String result = restTemplate.getForObject(new URI(serverURL), String.class);

    //DISPLAY RESULT
    System.out.println(result);

  }

}

package com.ivoronline.springboot_services_tls_twoway_springwebservices_client;

import com.ivoronline.soap.GetPersonRequest;
import com.ivoronline.soap.GetPersonResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;

@Component
public class PersonClient extends WebServiceGatewaySupport {

  //PROPERTIES
  String generatedClasses   = "com.ivoronline.soap";
  String webServiceEndpoint = "https://localhost:8080/PersonWebService";

  //CLIENT TRUST STORE
  String trustStoreName     = "ClientTrustStore.jks";
  String trustStorePassword = "mypassword";
  String trustStoreType     = "JKS";

  //CLIENT KEY STORE (For Two-Way TLS)
  String keyStoreName       = "ClientKeyStore.jks";
  String keyStorePassword   = "mypassword";
  String keyStoreType       = "JKS";

  //==========================================================================
  // GET PERSON
  //==========================================================================
  // Input Parameters are only used to create Custom Request Object.
  // marshalSendAndReceive() marshals Request Object into XML Request and adds SOAP Envelope, Header & Body
  public GetPersonResponse getPerson(Integer id) throws Exception {

    //CREATE REQUEST OBJECT
    GetPersonRequest getPersonRequest = new GetPersonRequest();
                     getPersonRequest.setId(id);  //Set some Parameters

    //POINT MARSHALLER TO GENERATED CLASSES
    Jaxb2Marshaller  marshaller = new Jaxb2Marshaller();
                     marshaller.setContextPath(generatedClasses);

    //CONFIGURE CLIENT
    setDefaultUri  (webServiceEndpoint);
    setMarshaller  (marshaller);
    setUnmarshaller(marshaller);

    WebServiceMessageSender sender = UtilClientWebServiceTemplate.getWebServiceMessageSender(
      trustStoreName, trustStorePassword, trustStoreType,
      keyStoreName  , keyStorePassword  , keyStoreType
    );

    WebServiceTemplate template = getWebServiceTemplate();
                       template.setMessageSender(sender);

    //SEND REQUEST
    GetPersonResponse response = (GetPersonResponse) template.marshalSendAndReceive(getPersonRequest);

    //RETURN RESPONSE OBJECT
    return response;

  }

}
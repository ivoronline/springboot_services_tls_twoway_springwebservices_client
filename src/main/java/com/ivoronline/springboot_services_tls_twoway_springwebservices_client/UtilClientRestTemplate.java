package com.ivoronline.springboot_services_tls_twoway_springwebservices_client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

public class UtilClientRestTemplate {

  //=======================================================================================
  // GET REQUEST FACTORY FOR TWO WAY TLS
  //=======================================================================================
  // Difference is in SSLContext => TrustMaterial + KeyMaterial
  public static HttpComponentsClientHttpRequestFactory getRequestFactoryForTwoWayTLS(
    String trustStoreName,     //"/MyKeyStore.jks"
    String trustStorePassword, //"mypassword";
    String trustStoreType,     //"JKS"
    String keyStoreName,       //"/MyKeyStore.jks"
    String keyStorePassword,   //"mypassword";
    String keyStoreType        //"JKS"
  ) throws Exception {

    //LOAD STORES
    KeyStore trustStore = UtilKeys.getStore(trustStoreName, trustStorePassword, trustStoreType);//One-Way TLS
    KeyStore keyStore   = UtilKeys.getStore(keyStoreName  , keyStorePassword  , keyStoreType  );//Two-Way TLS

    //CONFIGURE REQUEST FACTORY
    SSLContext sslContext = new SSLContextBuilder()
      .loadTrustMaterial(trustStore, null)                         //For One-Way TLS
      .loadKeyMaterial  (keyStore, keyStorePassword.toCharArray()) //For Two-Way TLS
      .build();

    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
      sslContext,
      NoopHostnameVerifier.INSTANCE
    );

    CloseableHttpClient httpClient= HttpClients
      .custom()
      .setSSLSocketFactory(socketFactory)
      .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    //RETURN REQUEST FACTORY
    return requestFactory;

  }

}

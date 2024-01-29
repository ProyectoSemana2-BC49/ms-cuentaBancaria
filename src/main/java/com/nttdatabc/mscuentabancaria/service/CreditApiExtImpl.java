package com.nttdatabc.mscuentabancaria.service;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.REQUIRED_CREDIT_VIP;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.URL_CREDIT_CUSTOMER;

import com.nttdatabc.mscuentabancaria.model.CreditExt;
import com.nttdatabc.mscuentabancaria.model.CreditResponseExt;
import com.nttdatabc.mscuentabancaria.model.TypeCredit;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Clase http ms-credit.
 */
@Service
public class CreditApiExtImpl implements CreditApiExt {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void hasCreditCustomer(String customerId) throws ErrorResponseException {
    String apiUrl = URL_CREDIT_CUSTOMER + customerId;
    HttpHeaders httpHeaders = new HttpHeaders();
    HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<CreditResponseExt[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, CreditResponseExt[].class);

    if (!Objects.requireNonNull(response.getBody())[0].getBody().isEmpty()) {
      List<CreditExt> listCredit = response.getBody()[0].getBody();
      boolean hasCreditCard = listCredit.stream().anyMatch(creditExt ->
          creditExt.getType_credit().equalsIgnoreCase(TypeCredit.TARJETA.toString()));
      if (!hasCreditCard) {
        throw new ErrorResponseException(REQUIRED_CREDIT_VIP, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
      }
    } else {
      throw new ErrorResponseException(REQUIRED_CREDIT_VIP, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }
  }
}

package com.nttdatabc.mscuentabancaria.service;

import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;

/**
 * interface Credit api.
 */
public interface CreditApiExt {
  void hasCreditCustomer(String customerId) throws ErrorResponseException;
}

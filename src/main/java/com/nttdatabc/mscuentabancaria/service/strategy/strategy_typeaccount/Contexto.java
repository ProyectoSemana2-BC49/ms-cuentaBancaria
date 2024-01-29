package com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;

/**
 * Clase contexto.
 */
public class Contexto {
  private AccountConfigationStrategy accountConfigationStrategy;

  public Contexto(AccountConfigationStrategy accountConfigationStrategy) {
    this.accountConfigationStrategy = accountConfigationStrategy;
  }

  public void configurationAccount(Account account, CustomerExt customerExt) throws ErrorResponseException {
    accountConfigationStrategy.configureAccount(account, customerExt);
  }
}

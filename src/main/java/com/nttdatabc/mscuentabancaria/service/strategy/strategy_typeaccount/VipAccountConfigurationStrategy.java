package com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.EMPRESA_NOT_PERMITTED_VIP;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.MOUNT_INSUFICIENT_CREATE_VIP;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.MOUNT_MIN_OPEN_VIP;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.model.TypeCustomer;
import com.nttdatabc.mscuentabancaria.service.CreditApiExtImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import org.springframework.http.HttpStatus;

/**
 * Configuration for Accounts Vip.
 */
public class VipAccountConfigurationStrategy implements AccountConfigationStrategy {
  @Override
  public void configureAccount(Account account, CustomerExt customerExt) throws ErrorResponseException {
    if (customerExt.getType().equalsIgnoreCase(TypeCustomer.EMPRESA.toString())) {
      throw new ErrorResponseException(EMPRESA_NOT_PERMITTED_VIP,
          HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }

    if (account.getCurrentBalance().doubleValue() < MOUNT_MIN_OPEN_VIP) {
      throw new ErrorResponseException(MOUNT_INSUFICIENT_CREATE_VIP,
          HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }
  }

  @Override
  public void validateHasCredit(CreditApiExtImpl creditApiExt, String accountId) throws ErrorResponseException {
    AccountConfigationStrategy.super.validateHasCredit(creditApiExt, accountId);
  }
}

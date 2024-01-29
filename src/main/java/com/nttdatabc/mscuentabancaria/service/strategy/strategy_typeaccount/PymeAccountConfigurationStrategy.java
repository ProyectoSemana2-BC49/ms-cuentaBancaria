package com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.PERSONA_NOT_PERMITTED_VIP;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.model.TypeCustomer;
import com.nttdatabc.mscuentabancaria.service.CreditApiExtImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 * Class configuration Pyme.
 */
public class PymeAccountConfigurationStrategy implements AccountConfigationStrategy {
  @Override
  public void configureAccount(Account account, CustomerExt customerExt) throws ErrorResponseException {
    if (customerExt.getType().equalsIgnoreCase(TypeCustomer.PERSONA.toString())) {
      throw new ErrorResponseException(PERSONA_NOT_PERMITTED_VIP,
          HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }

  }

  @Override
  public void validateHasCredit(CreditApiExtImpl creditApiExt, String accountId) throws ErrorResponseException {
    AccountConfigationStrategy.super.validateHasCredit(creditApiExt, accountId);
  }

  @Override
  public void validateHasCorriente(List<Account> accountList) throws ErrorResponseException {
    AccountConfigationStrategy.super.validateHasCorriente(accountList);
  }
}

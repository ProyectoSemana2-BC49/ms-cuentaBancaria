package com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.REQUIRED_CUENTA_CORRIENTE;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.model.TypeAccountBank;
import com.nttdatabc.mscuentabancaria.service.CreditApiExtImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import java.util.List;
import org.springframework.http.HttpStatus;


/**
 * Configura la cuenta según la estrategia específica implementada.
 */
public interface AccountConfigationStrategy {
  void configureAccount(Account account, CustomerExt customerExt) throws ErrorResponseException;

  /**
   * Validate has credit.
   *
   * @param creditApiExt service.
   * @param accountId id acccount.
   * @throws ErrorResponseException error.
   */
  default void validateHasCredit(CreditApiExtImpl creditApiExt, String accountId) throws ErrorResponseException {
    creditApiExt.hasCreditCustomer(accountId);
  }

  /**
   * Validate required account corriente.
   *
   * @param accountList list.
   * @throws ErrorResponseException error.
   */
  default void validateHasCorriente(List<Account> accountList) throws ErrorResponseException {
    boolean hasCorriente = accountList.stream().anyMatch(account -> account.getTypeAccount()
        .equalsIgnoreCase(TypeAccountBank.CORRIENTE.toString()));
    if (!hasCorriente) {
      throw new ErrorResponseException(REQUIRED_CUENTA_CORRIENTE, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }
  }
}

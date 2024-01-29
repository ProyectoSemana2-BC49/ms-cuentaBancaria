package com.nttdatabc.mscuentabancaria.service;

import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.validateAccountEmpty;
import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.validateAccountsNoNulls;
import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.verifyCustomerExists;
import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.verifyTypeAccount;
import static com.nttdatabc.mscuentabancaria.utils.AccountValidator.verifyValues;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_NOT_FOUND_RECURSO;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.CustomerExt;
import com.nttdatabc.mscuentabancaria.model.TypeAccountBank;
import com.nttdatabc.mscuentabancaria.model.TypeCustomer;
import com.nttdatabc.mscuentabancaria.repository.AccountRepository;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_account.AccountValidationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_account.EmpresaAccountValidationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_account.PersonaAccountValidationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.AccountConfigationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.AhorroAccountConfigurationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.CorrienteAccountConfigurationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.PlazoFijoAccountConfigurationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.PymeAccountConfigurationStrategy;
import com.nttdatabc.mscuentabancaria.service.strategy.strategy_typeaccount.VipAccountConfigurationStrategy;
import com.nttdatabc.mscuentabancaria.utils.Utilitarios;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service de account.
 */
@Service
public class AccountServiceImpl implements AccountService {
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private CustomerApiExtImpl customerApiExtImpl;

  @Autowired
  private CreditApiExtImpl creditApiExt;

  @Override
  public Observable<List<Account>> getAllAccountsService() {
    return Observable.defer(() -> Observable.just(accountRepository.findAll()));
  }

  @Override
  public Completable createAccountService(Account account) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      validateAccountsNoNulls(account);
      validateAccountEmpty(account);
      verifyTypeAccount(account);
      verifyValues(account);
      CustomerExt customerFound = verifyCustomerExists(account.getCustomerId(), customerApiExtImpl);
      List<Account> listAccountByCustomer = getAccountsByCustomerIdService(account.getCustomerId()).blockingSingle();

      AccountValidationStrategy accountValidationStrategy = null;
      if (customerFound.getType().equalsIgnoreCase(TypeCustomer.PERSONA.toString())) {
        accountValidationStrategy = new PersonaAccountValidationStrategy();
        accountValidationStrategy.validateAccount(account, listAccountByCustomer);
      } else if (customerFound.getType().equalsIgnoreCase(TypeCustomer.EMPRESA.toString())) {
        accountValidationStrategy = new EmpresaAccountValidationStrategy();
        accountValidationStrategy.validateAccount(account, listAccountByCustomer);
      }

      AccountConfigationStrategy configationStrategy = null;
      if (account.getTypeAccount().equalsIgnoreCase(TypeAccountBank.AHORRO.toString())) {
        configationStrategy = new AhorroAccountConfigurationStrategy();
        configationStrategy.configureAccount(account, customerFound);
      } else if (account.getTypeAccount().equalsIgnoreCase(TypeAccountBank.CORRIENTE.toString())) {
        configationStrategy = new CorrienteAccountConfigurationStrategy();
        configationStrategy.configureAccount(account, customerFound);
      } else if (account.getTypeAccount().equalsIgnoreCase(TypeAccountBank.PLAZO_FIJO.toString())) {
        configationStrategy = new PlazoFijoAccountConfigurationStrategy();
        configationStrategy.configureAccount(account, customerFound);
      } else if (account.getTypeAccount().equalsIgnoreCase(TypeAccountBank.VIP.toString())) {
        configationStrategy = new VipAccountConfigurationStrategy();
        configationStrategy.validateHasCredit(creditApiExt, customerFound.get_id());
        configationStrategy.configureAccount(account, customerFound);
      } else if (account.getTypeAccount().equalsIgnoreCase(TypeAccountBank.PYME.toString())) {
        configationStrategy = new PymeAccountConfigurationStrategy();
        configationStrategy.validateHasCredit(creditApiExt, customerFound.get_id());
        configationStrategy.configureAccount(account, customerFound);
        configationStrategy.validateHasCorriente(accountRepository.findByCustomerId(customerFound.get_id()));
      }

      account.setId(Utilitarios.generateUuid());
      accountRepository.save(account);
    });
  }

  @Override
  public Completable updateAccountServide(Account account) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      validateAccountsNoNulls(account);
      validateAccountEmpty(account);
      verifyTypeAccount(account);
      Optional<Account> getAccountById = accountRepository.findById(account.getId());
      if (getAccountById.isEmpty()) {
        throw new ErrorResponseException(EX_NOT_FOUND_RECURSO,
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
      }
      Account accountFound = getAccountById.get();
      accountFound.setTypeAccount(account.getTypeAccount());
      accountFound.setCurrentBalance(account.getCurrentBalance());
      accountFound.setCustomerId(account.getCustomerId());
      accountFound.setHolders(account.getHolders());
      accountFound.setDateMovement(account.getDateMovement());
      accountFound.setLimitMaxMovements(account.getLimitMaxMovements());
      accountFound.setMaintenanceFee(account.getMaintenanceFee());
      accountRepository.save(accountFound);
    });

  }

  @Override
  public Completable deleteAccountByIdService(String accountId) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      Optional<Account> accountFindByIdOptional = accountRepository.findById(accountId);
      if (accountFindByIdOptional.isEmpty()) {
        throw new ErrorResponseException(EX_NOT_FOUND_RECURSO,
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
      }
      accountRepository.delete(accountFindByIdOptional.get());
    });
  }

  @Override
  public Single<Account> getAccountByIdService(String accountId) throws ErrorResponseException {
    return Single.defer(() -> {
      Optional<Account> account = accountRepository.findById(accountId);
      return Single.just(account.orElseThrow(() -> new ErrorResponseException(EX_NOT_FOUND_RECURSO, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND)));
    });
  }

  @Override
  public Observable<List<Account>> getAccountsByCustomerIdService(String customerId) throws ErrorResponseException {
    return Observable.defer(() -> {
      verifyCustomerExists(customerId, customerApiExtImpl);
      return Observable.just(accountRepository.findByCustomerId(customerId));
    });
  }

}

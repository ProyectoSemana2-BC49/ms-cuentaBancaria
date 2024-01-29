package com.nttdatabc.mscuentabancaria.controller;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.PREFIX_PATH;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.service.AccountServiceImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador de Account.
 */
@RestController
@RequestMapping(PREFIX_PATH)
@Slf4j
public class AccountController implements AccountControllerApi {

  @Autowired
  private AccountServiceImpl accountServiceImpl;

  @Override
  public Observable<ResponseEntity<List<Account>>> getAllAccounts() {
    return accountServiceImpl.getAllAccountsService()
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("getAllAccounts:: init"))
        .doOnComplete(() -> log.info("getAllAccounts: completed"))
        .map(ResponseEntity::ok)
        .onErrorReturn(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public Maybe<ResponseEntity<Object>> createAccount(Account account) throws ErrorResponseException {
    return accountServiceImpl.createAccountService(account)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("createAccount:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.CREATED).build()))
        .doOnSuccess(response -> log.info("createAccount:: completed"));
  }

  @Override
  public Maybe<ResponseEntity<Object>> updateAccount(Account account) throws ErrorResponseException {
    return accountServiceImpl.updateAccountServide(account)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("updateAccount:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.OK).build()))
        .doOnSuccess(response -> log.info("updateAccount:: completed"));
  }

  @Override
  public Maybe<ResponseEntity<Object>> deleteAccountById(String accountId) throws ErrorResponseException {
    return accountServiceImpl.deleteAccountByIdService(accountId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.info("deleteAccountById:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.OK).build()))
        .doOnSuccess(objectResponseEntity -> log.info("deleteAccountById:: completed"));
  }

  @Override
  public Single<ResponseEntity<Account>> getAccountById(String accountId) throws ErrorResponseException {
    return accountServiceImpl.getAccountByIdService(accountId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("getCustomerById:: init"))
        .map(ResponseEntity::ok)
        .doOnSuccess(cus -> log.debug("getCustomerById:: completed"));
  }

  @Override
  public Observable<ResponseEntity<List<Account>>> getAccountsByCustomerId(String customerId) throws ErrorResponseException {
    return accountServiceImpl.getAccountsByCustomerIdService(customerId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("getAllCustomers:: init"))
        .doOnComplete(() -> log.info("getAllCustomers: completed"))
        .map(ResponseEntity::ok)
        .onErrorReturn(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

  }
}

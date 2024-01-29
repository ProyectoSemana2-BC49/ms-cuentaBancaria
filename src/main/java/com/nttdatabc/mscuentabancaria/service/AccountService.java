package com.nttdatabc.mscuentabancaria.service;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

/**
 * Interface de accountService.
 */
public interface AccountService {
  Observable<List<Account>> getAllAccountsService();

  Completable createAccountService(Account account) throws ErrorResponseException;

  Completable updateAccountServide(Account account) throws ErrorResponseException;

  Completable deleteAccountByIdService(String accountId) throws ErrorResponseException;

  Single<Account> getAccountByIdService(String accountId) throws ErrorResponseException;

  Observable<List<Account>> getAccountsByCustomerIdService(String customerId) throws ErrorResponseException;
}

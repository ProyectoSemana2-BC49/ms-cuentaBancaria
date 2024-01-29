package com.nttdatabc.mscuentabancaria.service;

import com.nttdatabc.mscuentabancaria.model.BalanceAccounts;
import com.nttdatabc.mscuentabancaria.model.Movement;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

/**
 * Report service interface.
 */
public interface ReportService {
  Single<BalanceAccounts> getBalanceAverageService(String customerId);

  Observable<List<Movement>> getMovementsWithFee(String accountId);
}

package com.nttdatabc.mscuentabancaria.controller;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.PREFIX_PATH;

import com.nttdatabc.mscuentabancaria.model.BalanceAccounts;
import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.service.ReportServiceImpl;
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
 * Controller of Report.
 */
@RestController
@RequestMapping(PREFIX_PATH)
@Slf4j
public class ReportController implements ReportControllerApi {

  @Autowired
  private ReportServiceImpl reportServiceImpl;

  @Override
  public Single<ResponseEntity<BalanceAccounts>> getBalanceAccount(String customerId) {
    return reportServiceImpl.getBalanceAverageService(customerId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("getBalanceAccount:: init"))
        .map(ResponseEntity::ok)
        .doOnSuccess(cus -> log.debug("getBalanceAccount:: completed"));
  }

  @Override
  public Observable<ResponseEntity<List<Movement>>> getFeeByAccount(String accountId) {
    return reportServiceImpl.getMovementsWithFee(accountId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("getFeeByAccount:: init"))
        .doOnComplete(() -> log.info("getFeeByAccount: completed"))
        .map(ResponseEntity::ok)
        .onErrorReturn(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }
}

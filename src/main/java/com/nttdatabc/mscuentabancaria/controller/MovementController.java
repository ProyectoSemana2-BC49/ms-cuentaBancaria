package com.nttdatabc.mscuentabancaria.controller;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.PREFIX_PATH;

import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.service.MovementServiceImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Clase contoller Movement.
 */
@RestController
@RequestMapping(PREFIX_PATH)
@Slf4j
public class MovementController implements MovementControllerApi {
  @Autowired
  private MovementServiceImpl movementServiceImpl;

  @Override
  public Maybe<ResponseEntity<Object>> createMovementDeposit(Movement movement) throws ErrorResponseException {
    return movementServiceImpl.createMovementDepositService(movement)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("createMovementDeposit:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.CREATED).build()))
        .doOnSuccess(response -> log.info("createMovementDeposit:: completed"));
  }

  @Override
  public Maybe<ResponseEntity<Object>> createWithDraw(Movement movement) throws ErrorResponseException {
    return movementServiceImpl.createWithDrawService(movement)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("createWithDraw:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.CREATED).build()))
        .doOnSuccess(response -> log.info("createWithDraw:: completed"));
  }

  @Override
  public Observable<ResponseEntity<List<Movement>>> getMovementsByAccountId(String accountId) throws ErrorResponseException {
    return movementServiceImpl.getMovementsByAccountIdService(accountId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.info("getMovementsByAccountId:: init"))
        .doOnComplete(() -> log.info("getMovementsByAccountId:: completed"))
        .map(authorizedSigners -> ResponseEntity.ok().body(authorizedSigners));
  }

  @Override
  public Maybe<ResponseEntity<Object>> createTransfer(Movement movement) throws ErrorResponseException {
    return movementServiceImpl.createTransferService(movement)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> log.debug("createTransfer:: init"))
        .andThen(Maybe.just(ResponseEntity.status(HttpStatus.CREATED).build()))
        .doOnSuccess(response -> log.info("createTransfer:: completed"));
  }
}

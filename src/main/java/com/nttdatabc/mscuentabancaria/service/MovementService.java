package com.nttdatabc.mscuentabancaria.service;

import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;

/**
 * Interface movementService.
 */
public interface MovementService {
  Completable createMovementDepositService(Movement movement) throws ErrorResponseException;

  Completable createWithDrawService(Movement movement) throws ErrorResponseException;

  Observable<List<Movement>> getMovementsByAccountIdService(String accountId) throws ErrorResponseException;

  Completable createTransferService(Movement movement) throws ErrorResponseException;
}

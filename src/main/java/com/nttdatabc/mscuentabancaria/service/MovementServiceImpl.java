package com.nttdatabc.mscuentabancaria.service;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.FEE_LIMIT_TRANSACTION;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.TRANSACTION_FEE_FREE;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateAccountDestination;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateAccountRegister;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateCurrentBalance;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateMovementEmpty;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateMovementNoNulls;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateMovementTransferEmpty;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateMovementTransferNoNulls;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.validateMovements;
import static com.nttdatabc.mscuentabancaria.utils.MovementValidator.verifyValues;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.model.TypeMovement;
import com.nttdatabc.mscuentabancaria.repository.MovementRepository;
import com.nttdatabc.mscuentabancaria.utils.Utilitarios;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Clase service.
 */
@Service
public class MovementServiceImpl implements MovementService {
  @Autowired
  private MovementRepository movementRepository;
  @Autowired
  private AccountServiceImpl accountServiceImpl;

  @Override
  public Completable createMovementDepositService(Movement movement) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      validateMovementNoNulls(movement);
      validateMovementEmpty(movement);
      verifyValues(movement);
      validateAccountRegister(movement.getAccountId(), accountServiceImpl);

      movement.setId(Utilitarios.generateUuid());
      movement.setFecha(LocalDateTime.now().toString());
      movement.setTypeMovement(TypeMovement.DEPOSITO.toString());
      movement.setFee(BigDecimal.valueOf(TRANSACTION_FEE_FREE));

      List<Movement> listMovementByAccount = getMovementsByAccountIdService(movement.getAccountId()).blockingSingle();
      Account accountFound = accountServiceImpl.getAccountByIdService(movement.getAccountId()).blockingGet();
      validateMovements(accountFound, listMovementByAccount, movement);

      movementRepository.save(movement);

      //update mount in account bank
      double mountUpdate = movement.getMount().doubleValue();
      if (movement.getFee().doubleValue() == FEE_LIMIT_TRANSACTION) {
        double feeTransaction = movement.getMount().doubleValue() * FEE_LIMIT_TRANSACTION;
        mountUpdate = mountUpdate - feeTransaction;
      }
      Account accountUpdate = accountServiceImpl.getAccountByIdService(movement.getAccountId()).blockingGet();
      accountUpdate.setCurrentBalance(accountUpdate.getCurrentBalance().add(BigDecimal.valueOf(mountUpdate)));
      accountServiceImpl.updateAccountServide(accountUpdate).blockingAwait();
    });
  }

  @Override
  public Completable createWithDrawService(Movement movement) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      validateMovementNoNulls(movement);
      validateMovementEmpty(movement);
      verifyValues(movement);
      validateAccountRegister(movement.getAccountId(), accountServiceImpl);

      List<Movement> listMovementByAccount = getMovementsByAccountIdService(movement.getAccountId()).blockingSingle();
      Account accountFound = accountServiceImpl.getAccountByIdService(movement.getAccountId()).blockingGet();

      validateMovements(accountFound, listMovementByAccount, movement);
      validateCurrentBalance(accountServiceImpl, movement);

      movement.setId(Utilitarios.generateUuid());
      movement.setFecha(LocalDateTime.now().toString());
      movement.setTypeMovement(TypeMovement.RETIRO.toString());
      movement.setFee(BigDecimal.valueOf(TRANSACTION_FEE_FREE));

      movementRepository.save(movement);

      //update mount in account bank
      double mountUpdate = movement.getMount().doubleValue();
      if (movement.getFee().doubleValue() == FEE_LIMIT_TRANSACTION) {
        double feeTransaction = movement.getMount().doubleValue() * FEE_LIMIT_TRANSACTION;
        mountUpdate = mountUpdate + feeTransaction;
      }
      accountFound.setCurrentBalance(accountFound.getCurrentBalance().subtract(BigDecimal.valueOf(mountUpdate)));
      accountServiceImpl.updateAccountServide(accountFound).blockingAwait();
    });
  }

  @Override
  public Completable createTransferService(Movement movement) throws ErrorResponseException {
    return Completable.fromAction(() -> {
      validateMovementTransferNoNulls(movement);
      validateMovementEmpty(movement);
      validateMovementTransferEmpty(movement);
      verifyValues(movement);
      validateAccountRegister(movement.getAccountId(), accountServiceImpl);
      validateAccountDestination(movement.getDestination(), accountServiceImpl);
      validateCurrentBalance(accountServiceImpl, movement);

      movement.setId(Utilitarios.generateUuid());
      movement.setFecha(LocalDateTime.now().toString());
      movement.setTypeMovement(TypeMovement.TRANSFER.toString());
      movement.setFee(BigDecimal.valueOf(TRANSACTION_FEE_FREE));

      movementRepository.save(movement);

      // updates account origin transfer
      Account accountOrigin = accountServiceImpl.getAccountByIdService(movement.getAccountId()).blockingGet();
      accountOrigin.setCurrentBalance(accountOrigin.getCurrentBalance().subtract((movement.getMount())));
      accountServiceImpl.updateAccountServide(accountOrigin).blockingAwait();

      // updates account destination transfer
      Account accountDestination = accountServiceImpl.getAccountByIdService(movement.getDestination()).blockingGet();
      accountDestination.setCurrentBalance(accountDestination.getCurrentBalance().add((movement.getMount())));
      accountServiceImpl.updateAccountServide(accountDestination).blockingAwait();
    });
  }

  @Override
  public Observable<List<Movement>> getMovementsByAccountIdService(String accountId) throws ErrorResponseException {
    return Observable.defer(() -> {
      validateAccountRegister(accountId, accountServiceImpl);
      return Observable.just(movementRepository.findByAccountId(accountId));
    });
  }


}

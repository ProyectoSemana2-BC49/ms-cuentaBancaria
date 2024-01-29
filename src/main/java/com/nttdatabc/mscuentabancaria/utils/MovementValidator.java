package com.nttdatabc.mscuentabancaria.utils;

import static com.nttdatabc.mscuentabancaria.utils.Constantes.DAY_MOVEMENT_SELECTED;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_HAS_MOVEMENT_DAY;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_MOVEMENT_BALANCE_INSUFFICIENT;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_NOT_DAY_MOVEMENT;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_REQUEST;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_TYPE_MOVEMENT;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_ERROR_VALUE_MIN_MOVEMENT;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.EX_VALUE_EMPTY;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.FEE_LIMIT_TRANSACTION;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.LIMIT_MAX_MOVEMENTS;
import static com.nttdatabc.mscuentabancaria.utils.Constantes.VALUE_MIN_ACCOUNT_BANK;

import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.model.TypeAccountBank;
import com.nttdatabc.mscuentabancaria.model.TypeMovement;
import com.nttdatabc.mscuentabancaria.service.AccountServiceImpl;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;


/**
 * Clase MovementValidaror.
 */
public class MovementValidator {
  /**
   * Valida que los campos esenciales del movimiento no sean nulos.
   *
   * @param movement El movimiento que se va a validar.
   * @throws ErrorResponseException Si algún campo esencial es nulo.
   */
  public static void validateMovementNoNulls(Movement movement) throws ErrorResponseException {
    Optional.of(movement)
        .filter(c -> c.getAccountId() != null)
        .filter(c -> c.getMount() != null)
        .orElseThrow(() -> new ErrorResponseException(EX_ERROR_REQUEST,
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
  }

  /**
   * Valida que los campos del movimiento no estén vacíos.
   *
   * @param movement El movimiento que se va a validar.
   * @throws ErrorResponseException Si algún campo esencial está vacío.
   */
  public static void validateMovementEmpty(Movement movement) throws ErrorResponseException {
    Optional.of(movement)
        .filter(c -> !c.getAccountId().isBlank())
        .filter(c -> !c.getMount().toString().isBlank())
        .orElseThrow(() -> new ErrorResponseException(EX_VALUE_EMPTY,
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
  }

  /**
   * Verifica que el tipo de movimiento sea válido.
   *
   * @param movement El movimiento que se va a verificar.
   * @throws ErrorResponseException Si el tipo de movimiento no es válido.
   */
  public static void verifyTypeMovement(Movement movement) throws ErrorResponseException {
    Predicate<Movement> existTypeMovement = movementValidate -> movementValidate
        .getTypeMovement()
        .equalsIgnoreCase(TypeMovement.DEPOSITO.toString())
        || movementValidate.getTypeMovement().equalsIgnoreCase(TypeMovement.RETIRO.toString())
        || movementValidate.getTypeMovement().equalsIgnoreCase(TypeMovement.TRANSFER.toString());
    if (existTypeMovement.negate().test(movement)) {
      throw new ErrorResponseException(EX_ERROR_TYPE_MOVEMENT,
          HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Valida que una cuenta esté registrada.
   *
   * @param accountId          El ID de la cuenta.
   * @param accountServiceImpl Implementación del servicio de cuentas.
   * @throws ErrorResponseException Si la cuenta no está registrada.
   */
  public static void validateAccountRegister(String accountId, AccountServiceImpl accountServiceImpl) throws ErrorResponseException {
    accountServiceImpl.getAccountByIdService(accountId).blockingGet();
  }

  /**
   * Verifica que los valores del movimiento sean válidos.
   *
   * @param movement El movimiento que se va a verificar.
   * @throws ErrorResponseException Si los valores no son válidos.
   */
  public static void verifyValues(Movement movement) throws ErrorResponseException {
    if (movement.getMount().doubleValue() <= VALUE_MIN_ACCOUNT_BANK) {
      throw new ErrorResponseException(EX_ERROR_VALUE_MIN_MOVEMENT,
          HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Filtra la lista de movimientos por el mes actual.
   *
   * @param listaFound La lista de movimientos a filtrar.
   * @return Lista filtrada de movimientos del mes actual.
   */
  public static List<Movement> listMovementByMonth(List<Movement> listaFound) {
    int monthNow = LocalDate.now().getMonthValue();
    return listaFound.stream().filter(movement -> {
      LocalDateTime dateMovement = LocalDateTime.parse(movement.getFecha());
      return dateMovement.getMonthValue() == monthNow;
    }).collect(Collectors.toList());
  }

  /**
   * Verifica si hay algún movimiento registrado en el día actual.
   *
   * @param listaFound La lista de movimientos a verificar.
   * @return Verdadero si hay movimientos registrados hoy, falso de lo contrario.
   */
  public static Boolean hasMovementInDay(List<Movement> listaFound) {
    LocalDateTime dateTimeNow = LocalDateTime.now();
    int monthNow = dateTimeNow.getMonthValue();
    int dayNow = dateTimeNow.getDayOfMonth();
    return listaFound.stream().anyMatch(movement -> {
      LocalDateTime dateTimeMovement = LocalDateTime.parse(movement.getFecha());
      int monthMovement = dateTimeMovement.getMonthValue();
      int dayMovement = dateTimeMovement.getDayOfMonth();
      return monthNow == monthMovement && dayNow == dayMovement;
    });

  }

  /**
   * Verifica si el día actual es el día programado para movimientos.
   *
   * @return Verdadero si es el día programado, falso de lo contrario.
   */
  public static Boolean isDayMovement() {
    LocalDateTime dateTimeNow = LocalDateTime.now();
    int dayNow = dateTimeNow.getDayOfMonth();
    return dayNow == Integer.parseInt(DAY_MOVEMENT_SELECTED);
  }

  /**
   * Valida los movimientos según las reglas de la cuenta.
   *
   * @param accountFound          La cuenta asociada a los movimientos.
   * @param listMovementByAccount La lista de movimientos asociados a la cuenta.
   * @throws ErrorResponseException Si hay violaciones en las reglas de movimientos de la cuenta.
   */
  public static void validateMovements(Account accountFound, List<Movement> listMovementByAccount, Movement movement) throws ErrorResponseException {
    if (accountFound.getTypeAccount().equalsIgnoreCase(TypeAccountBank.AHORRO.toString())
        || accountFound.getTypeAccount().equalsIgnoreCase(TypeAccountBank.PLAZO_FIJO.toString())
        || accountFound.getTypeAccount().equalsIgnoreCase(TypeAccountBank.CORRIENTE.toString())) {
      List<Movement> listMovementByAccountByMonth = listMovementByMonth(listMovementByAccount);

      if (listMovementByAccountByMonth.size() >= LIMIT_MAX_MOVEMENTS) {
        // apply fee when limit max movements
        movement.setFee(BigDecimal.valueOf(FEE_LIMIT_TRANSACTION));
      }
    }

    if (accountFound.getTypeAccount().equalsIgnoreCase(TypeAccountBank.PLAZO_FIJO.toString())) {
      Boolean hasMovement = hasMovementInDay(listMovementByAccount);
      // Ya hizo el movimiento el día programado
      if (hasMovement) {
        throw new ErrorResponseException(EX_ERROR_HAS_MOVEMENT_DAY,
            HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
      }
      //No es el dia programado
      Boolean isDayMovement = isDayMovement();
      if (!isDayMovement) {
        throw new ErrorResponseException(EX_ERROR_NOT_DAY_MOVEMENT,
            HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
      }
    }
  }

  /**
   * Validate params not null.
   *
   * @param movement object movement.
   * @throws ErrorResponseException error.
   */
  public static void validateMovementTransferNoNulls(Movement movement) throws ErrorResponseException {
    Optional.of(movement)
        .filter(c -> c.getAccountId() != null)
        .filter(c -> c.getMount() != null)
        .filter(c -> c.getDestination() != null)
        .orElseThrow(() -> new ErrorResponseException(EX_ERROR_REQUEST,
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
  }

  /**
   * not empty.
   *
   * @param movement object.
   * @throws ErrorResponseException error.
   */
  public static void validateMovementTransferEmpty(Movement movement) throws ErrorResponseException {
    Optional.of(movement)
        .filter(c -> !c.getAccountId().isBlank())
        .filter(c -> !c.getMount().toString().isBlank())
        .filter(c -> !c.getDestination().isBlank())
        .orElseThrow(() -> new ErrorResponseException(EX_VALUE_EMPTY,
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
  }

  /**
   * Validate destination exist.
   *
   * @param accountId id.
   * @param accountServiceImpl service.
   * @throws ErrorResponseException error.
   */
  public static void validateAccountDestination(String accountId, AccountServiceImpl accountServiceImpl) throws ErrorResponseException {
    accountServiceImpl.getAccountByIdService(accountId).blockingGet();
  }

  /**
   * Balance insufficient.
   *
   * @param accountServiceImpl service.
   * @param movement object.
   * @throws ErrorResponseException err.
   */
  public static void validateCurrentBalance(AccountServiceImpl accountServiceImpl, Movement movement) throws ErrorResponseException {
    Account accountFound = accountServiceImpl.getAccountByIdService(movement.getAccountId()).blockingGet();
    if (accountFound.getCurrentBalance().doubleValue() < movement.getMount().doubleValue()) {
      throw new ErrorResponseException(EX_ERROR_MOVEMENT_BALANCE_INSUFFICIENT,
          HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT);
    }
  }

}

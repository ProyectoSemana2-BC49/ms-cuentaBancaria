package com.nttdatabc.mscuentabancaria.controller;

import com.nttdatabc.mscuentabancaria.api.ApiUtil;
import com.nttdatabc.mscuentabancaria.model.Account;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-26T11:40:00.550107500-05:00[America/Lima]")
@Validated
@Tag(name = "Cuentas Bancarias", description = "the Cuentas Bancarias API")
public interface AccountControllerApi {

  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /accounts : Agregar cuenta bancaria.
   *
   * @param account (optional).
   * @return Cuenta bancaria creada exitosamente (status code 201).
   * or Error en Request (status code 400).
   */
  @Operation(
      operationId = "createAccount",
      summary = "Agregar cuenta bancaria",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "201", description = "Cuenta bancaria creada exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en Request")
      }
  )
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/accounts",
      consumes = {"application/json"}
  )
  default Maybe<ResponseEntity<Object>> createAccount(
      @Parameter(name = "Account", description = "") @Valid @RequestBody(required = false) Account account
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * DELETE /accounts/{account_id} : Eliminar cuenta bancaria.
   *
   * @param accountId ID de la cuenta bancaria (required).
   * @return Cuenta bancaria eliminada exitosamente (status code 200).
   * or Error en request (status code 400).
   * or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "deleteAccountById",
      summary = "Eliminar cuenta bancaria",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Cuenta bancaria eliminada exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.DELETE,
      value = "/accounts/{account_id}"
  )
  default Maybe<ResponseEntity<Object>> deleteAccountById(
      @Parameter(name = "account_id", description = "ID de la cuenta bancaria", required = true, in = ParameterIn.PATH) @PathVariable("account_id") String accountId
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * GET /accounts/{account_id} : Obtener detalle de cuenta bancaria.
   *
   * @param accountId ID de la cuenta bancaria (required).
   * @return Detalle de cuenta bancaria obtenido exitosamente (status code 200).
   * or Error en request (status code 400).
   * or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "getAccountById",
      summary = "Obtener detalle de cuenta bancaria",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Detalle de cuenta bancaria obtenido exitosamente", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))
          }),
          @ApiResponse(responseCode = "400", description = "Error en request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/accounts/{account_id}",
      produces = {"application/json"}
  )
  default Single<ResponseEntity<Account>> getAccountById(
      @Parameter(name = "account_id", description = "ID de la cuenta bancaria", required = true, in = ParameterIn.PATH) @PathVariable("account_id") String accountId
  ) throws ErrorResponseException {
    getRequest().ifPresent(request -> {
      for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
        if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
          String exampleString = "{ \"date_movement\" : \"date_movement\", \"limit_max_movements\" : 6, \"type_account\" : \"type_account\", \"holders\" : [ { \"fullname\" : \"fullname\", \"dni\" : \"dni\" }, { \"fullname\" : \"fullname\", \"dni\" : \"dni\" } ], \"current_balance\" : 0.8008281904610115, \"_id\" : \"_id\", \"customer_id\" : \"customer_id\", \"maintenance_fee\" : 1.4658129805029452 }";
          ApiUtil.setExampleResponse(request, "application/json", exampleString);
          break;
        }
      }
    });
    return Single.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * GET /accounts/customer/{customer_id} : Obtener cuentas bancarias por cliente.
   *
   * @param customerId ID del cliente (required).
   * @return Detalle de cuenta bancaria obtenido exitosamente (status code 200).
   * or Error en request (status code 400).
   * or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "getAccountsByCustomerId",
      summary = "Obtener cuentas bancarias por cliente",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Detalle de cuenta bancaria obtenido exitosamente", content = {
              @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Account.class)))
          }),
          @ApiResponse(responseCode = "400", description = "Error en request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/accounts/customer/{customer_id}",
      produces = {"application/json"}
  )
  default Observable<ResponseEntity<List<Account>>> getAccountsByCustomerId(
      @Parameter(name = "customer_id", description = "ID del cliente", required = true, in = ParameterIn.PATH) @PathVariable("customer_id") String customerId
  ) throws ErrorResponseException {
    getRequest().ifPresent(request -> {
      for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
        if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
          String exampleString = "[ { \"date_movement\" : \"date_movement\", \"limit_max_movements\" : 6, \"type_account\" : \"type_account\", \"holders\" : [ { \"fullname\" : \"fullname\", \"dni\" : \"dni\" }, { \"fullname\" : \"fullname\", \"dni\" : \"dni\" } ], \"current_balance\" : 0.8008281904610115, \"_id\" : \"_id\", \"customer_id\" : \"customer_id\", \"maintenance_fee\" : 1.4658129805029452 }, { \"date_movement\" : \"date_movement\", \"limit_max_movements\" : 6, \"type_account\" : \"type_account\", \"holders\" : [ { \"fullname\" : \"fullname\", \"dni\" : \"dni\" }, { \"fullname\" : \"fullname\", \"dni\" : \"dni\" } ], \"current_balance\" : 0.8008281904610115, \"_id\" : \"_id\", \"customer_id\" : \"customer_id\", \"maintenance_fee\" : 1.4658129805029452 } ]";
          ApiUtil.setExampleResponse(request, "application/json", exampleString);
          break;
        }
      }
    });
    return Observable.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * GET /accounts : Obtener cuentas bancarias.
   *
   * @return Lista de cuentas bancarias obtenida exitosamente (status code 200).
   */
  @Operation(
      operationId = "getAllAccounts",
      summary = "Obtener cuentas bancarias",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Lista de cuentas bancarias obtenida exitosamente", content = {
              @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Account.class)))
          })
      }
  )
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/accounts",
      produces = {"application/json"}
  )
  default Observable<ResponseEntity<List<Account>>> getAllAccounts(

  ) {
    getRequest().ifPresent(request -> {
      for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
        if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
          String exampleString = "[ { \"date_movement\" : \"date_movement\", \"limit_max_movements\" : 6, \"type_account\" : \"type_account\", \"holders\" : [ { \"fullname\" : \"fullname\", \"dni\" : \"dni\" }, { \"fullname\" : \"fullname\", \"dni\" : \"dni\" } ], \"current_balance\" : 0.8008281904610115, \"_id\" : \"_id\", \"customer_id\" : \"customer_id\", \"maintenance_fee\" : 1.4658129805029452 }, { \"date_movement\" : \"date_movement\", \"limit_max_movements\" : 6, \"type_account\" : \"type_account\", \"holders\" : [ { \"fullname\" : \"fullname\", \"dni\" : \"dni\" }, { \"fullname\" : \"fullname\", \"dni\" : \"dni\" } ], \"current_balance\" : 0.8008281904610115, \"_id\" : \"_id\", \"customer_id\" : \"customer_id\", \"maintenance_fee\" : 1.4658129805029452 } ]";
          ApiUtil.setExampleResponse(request, "application/json", exampleString);
          break;
        }
      }
    });
    return Observable.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * PUT /accounts : Actualizar cuenta bancaria.
   *
   * @param account (optional).
   * @return Cuenta bancaria actualizada exitosamente (status code 200).
   * or Error en Request (status code 400).
   * or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "updateAccount",
      summary = "Actualizar cuenta bancaria",
      tags = {"Cuentas Bancarias"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Cuenta bancaria actualizada exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en Request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.PUT,
      value = "/accounts",
      consumes = {"application/json"}
  )
  default Maybe<ResponseEntity<Object>> updateAccount(
      @Parameter(name = "Account", description = "") @Valid @RequestBody(required = false) Account account
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }

}
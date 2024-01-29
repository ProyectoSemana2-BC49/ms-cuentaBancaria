package com.nttdatabc.mscuentabancaria.controller;

import com.nttdatabc.mscuentabancaria.api.ApiUtil;
import com.nttdatabc.mscuentabancaria.model.Movement;
import com.nttdatabc.mscuentabancaria.utils.exceptions.errors.ErrorResponseException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
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
@Tag(name = "Movimientos Cuenta Bancaria", description = "the Movimientos Cuenta Bancaria API")
public interface MovementControllerApi {

  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /movement/deposit : Crear depósito.
   *
   * @param movement  (optional).
   * @return Depósito creado exitosamente (status code 201).
   *         or Error en Request (status code 400).
   *         or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "createMovementDeposit",
      summary = "Crear depósito",
      tags = { "Movimientos Cuenta Bancaria" },
      responses = {
          @ApiResponse(responseCode = "201", description = "Depósito creado exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en Request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/movement/deposit",
      consumes = { "application/json" }
  )
  default Maybe<ResponseEntity<Object>> createMovementDeposit(
      @Parameter(name = "Movement", description = "") @Valid @RequestBody(required = false) Movement movement
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * POST /movement/withdraw : Crear retiro.
   *
   * @param movement  (optional).
   * @return Retiro creado exitosamente (status code 201).
   *         or Error en Request (status code 400).
   *         or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "createWithDraw",
      summary = "Crear retiro",
      tags = { "Movimientos Cuenta Bancaria" },
      responses = {
          @ApiResponse(responseCode = "201", description = "Retiro creado exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en Request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/movement/withdraw",
      consumes = { "application/json" }
  )
  default Maybe<ResponseEntity<Object>> createWithDraw(
      @Parameter(name = "Movement", description = "") @Valid @RequestBody(required = false) Movement movement
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }


  /**
   * GET /movement/{account_id} : Obtener movimientos de cuenta bancaria.
   *
   * @param accountId ID de la cuenta bancaria (required).
   * @return Lista de movimientos de cuenta bancaria obtenida exitosamente (status code 200).
   *         or Error en request (status code 400).
   *         or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "getMovementsByAccountId",
      summary = "Obtener movimientos de cuenta bancaria",
      tags = { "Movimientos Cuenta Bancaria" },
      responses = {
          @ApiResponse(responseCode = "200", description = "Lista de movimientos de cuenta bancaria obtenida exitosamente", content = {
              @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Movement.class)))
          }),
          @ApiResponse(responseCode = "400", description = "Error en request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/movement/{account_id}",
      produces = { "application/json" }
  )
  default Observable<ResponseEntity<List<Movement>>> getMovementsByAccountId(
      @Parameter(name = "account_id", description = "ID de la cuenta bancaria", required = true, in = ParameterIn.PATH) @PathVariable("account_id") String accountId
  ) throws ErrorResponseException {
    getRequest().ifPresent(request -> {
      for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
        if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
          String exampleString = "[ { \"type_movement\" : \"type_movement\", \"fecha\" : \"fecha\", \"account_id\" : \"account_id\", \"_id\" : \"_id\", \"mount\" : 0.8008281904610115 }, { \"type_movement\" : \"type_movement\", \"fecha\" : \"fecha\", \"account_id\" : \"account_id\", \"_id\" : \"_id\", \"mount\" : 0.8008281904610115 } ]";
          ApiUtil.setExampleResponse(request, "application/json", exampleString);
          break;
        }
      }
    });
    return Observable.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }

  /**
   * POST /movement/transfer : Crear transferencia.
   *
   * @param movement  (optional).
   * @return Transferencia creada exitosamente (status code 201).
   *         or Error en Request (status code 400).
   *         or Recurso no encontrado (status code 404).
   */
  @Operation(
      operationId = "createTransfer",
      summary = "Crear transferencia",
      tags = { "Movimientos Cuenta Bancaria" },
      responses = {
          @ApiResponse(responseCode = "201", description = "Transferencia creada exitosamente"),
          @ApiResponse(responseCode = "400", description = "Error en Request"),
          @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
      }
  )
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/movement/transfer",
      consumes = { "application/json" }
  )
  default Maybe<ResponseEntity<Object>> createTransfer(
      @Parameter(name = "Movement", description = "") @Valid @RequestBody(required = false) Movement movement
  ) throws ErrorResponseException {
    return Maybe.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));

  }

}


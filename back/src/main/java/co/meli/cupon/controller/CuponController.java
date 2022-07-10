package co.meli.cupon.controller;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;
import co.meli.cupon.dto.FavoritosResponseDTO;
import co.meli.cupon.service.CuponService;
import co.meli.cupon.util.ApplicationConstants;
import co.meli.cupon.util.BaseController;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API_VERSION + "/coupon")
public class CuponController extends BaseController {

  @Autowired private CuponService cuponService;

  @PostMapping(
      value = "",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "Cambiar cupon", notes = "Cambiar valor del cupón por ítems favoritos.")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<CuponResponseDTO> cambiarCupon(
      @ApiParam(value = "Parámetros para cambiar cupón", required = false) @RequestBody
          final CuponRequestDTO cuponRequestDTO) {

    CuponResponseDTO responseDTO = cuponService.usarCupon(cuponRequestDTO);

    cuponService.guardarFavoritos(cuponRequestDTO.getItem_ids());

    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  @GetMapping(
      value = "",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(
      value = "Obtener favoritos",
      notes = "Obtener listado de favoritos según parametros recibidos.")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<List<FavoritosResponseDTO>> obtenerFavoritos(
      @ApiParam(
              value = "Página a recuperar según la cantidad de registros a mostrar",
              example = "0")
          @RequestParam(required = false, name = "page")
          String page,
      @ApiParam(value = "Cantidad de registros a mostrar", example = "5")
          @RequestParam(required = false, name = "rows")
          String rows,
      @ApiParam(
              value = "Columna por la que se ordenaran los registros",
              example = "cantidadSolicitudesCompra")
          @RequestParam(required = false, name = "field")
          String field,
      @ApiParam(
              value = "Dirección de ordenamiento de los registros",
              allowableValues = "asc, desc",
              example = "desc")
          @RequestParam(required = false, name = "direction")
          String direction) {

    Pageable pageable = parsePageParams(page, rows, field, direction);

    List<FavoritosResponseDTO> responseListDTO = cuponService.obtenerFavoritos(pageable);

    return new ResponseEntity<>(responseListDTO, HttpStatus.OK);
  }
}

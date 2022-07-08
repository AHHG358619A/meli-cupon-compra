package co.meli.cupon.controller;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;
import co.meli.cupon.dto.FavoritosResponseDTO;
import co.meli.cupon.service.CuponService;
import co.meli.cupon.util.ApplicationConstants;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API_VERSION + "/coupon")
public class CuponController {

  @Autowired private CuponService cuponService;

  @PostMapping(
      value = "",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "cambiarCupon", notes = "Cambiar valor cupón por ítems favoritos.")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<CuponResponseDTO> cambiarCupon(
      @RequestHeader(value = "User-Agent") String userAgent,
      @ApiParam(value = "Parámetros para cambiar cupón", required = false) @RequestBody
          final CuponRequestDTO cuponRequestDTO) {

    CuponResponseDTO responseDTO = cuponService.usarCupon(cuponRequestDTO);

    cuponService.guardarFavoritos(cuponRequestDTO.getItem_ids());

    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  @GetMapping(
          value = "/favorites",
          produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "obtenerFavoritos", notes = "Obtener el top 5 de items usados en los cupones.")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<List<FavoritosResponseDTO>> obtenerFavoritos() {

    List<FavoritosResponseDTO> responseListDTO = cuponService.obtenerFavoritos();

    return new ResponseEntity<>(responseListDTO, HttpStatus.OK);
  }
}

package co.meli.cupon.service;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;
import co.meli.cupon.dto.FavoritosResponseDTO;

import java.util.List;

public interface CuponService {

  CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO);

  List<FavoritosResponseDTO> obtenerFavoritos();
}

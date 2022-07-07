package co.meli.cupon.service;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;

public interface CuponService {

  CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO);
}

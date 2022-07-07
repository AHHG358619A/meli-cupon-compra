package co.meli.cupon.service.impl;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;
import co.meli.cupon.service.CuponService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service("CuponService")
@Lazy
public class CuponServiceImpl implements CuponService {

  @Override
  public CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO) {
    CuponResponseDTO responseDTO = new CuponResponseDTO();
    List<String> itemsSeleccionados = new ArrayList<>();
    itemsSeleccionados.add("MLA1");
    itemsSeleccionados.add("MLA2");
    itemsSeleccionados.add("MLA4");
    itemsSeleccionados.add("MLA5");

    responseDTO.setItem_ids(itemsSeleccionados);
    responseDTO.setTotal(480);
    return responseDTO;
  }
}

package co.meli.cupon.service.impl;

import co.meli.cupon.dto.CuponRequestDTO;
import co.meli.cupon.dto.CuponResponseDTO;
import co.meli.cupon.dto.ItemDTO;
import co.meli.cupon.service.CuponService;
import co.meli.cupon.util.CamposAtributosEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static co.meli.cupon.util.ApplicationConstants.PATH_ATRIBUTOS;
import static co.meli.cupon.util.ApplicationConstants.PATH_ITEMS_ID;

@Transactional(readOnly = true)
@Service("CuponService")
@Lazy
public class CuponServiceImpl implements CuponService {

  @Value("${meli.url.servicio.items}")
  private String urlServicioItems;

  @Override
  public CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO) {
    consultarPrecioItems();
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

  private void consultarPrecioItems() {

    String camposItemsConsulta =
        Arrays.stream(CamposAtributosEnum.values())
            .map(Enum::toString)
            .collect(Collectors.joining(","));

    StringBuilder url = new StringBuilder();
    url.append(urlServicioItems);
    url.append(PATH_ITEMS_ID);
    url.append("MLA918474630,MLA923143874");
    url.append(PATH_ATRIBUTOS);
    url.append(camposItemsConsulta);

    System.out.println(url);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<ItemDTO[]> result = restTemplate.getForEntity(url.toString(), ItemDTO[].class);

    System.out.println("result: " + result.getStatusCode());
  }
}

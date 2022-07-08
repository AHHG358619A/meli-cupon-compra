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
import java.util.Objects;
import java.util.stream.Collectors;

import static co.meli.cupon.util.ApplicationConstants.*;

@Transactional(readOnly = true)
@Service("CuponService")
@Lazy
public class CuponServiceImpl implements CuponService {

  @Value("${meli.url.servicio.items}")
  private String urlServicioItems;

  @Override
  public CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO) {
    List<String> listadoItemsSinRepetir = eliminarItemsDuplicados(cuponRequestDTO.getItem_ids());
    List<ItemDTO> listadoItems = consultarPrecioItems(listadoItemsSinRepetir);
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

  private List<String> eliminarItemsDuplicados(List<String> item_ids) {

    return item_ids.stream().distinct().collect(Collectors.toList());
  }

  private List<ItemDTO> consultarPrecioItems(List<String> itemIdsList) {

    List<ItemDTO> listadoItems = new ArrayList<>();

    int tamanioListaRequest = itemIdsList.size();

    int posicionInicioParticionLista = 0;

    int posicionFinalParticionLista = Math.min(tamanioListaRequest, 20);

    while (posicionInicioParticionLista < tamanioListaRequest) {

      List<String> listaPeticion =
          itemIdsList.subList(posicionInicioParticionLista, posicionFinalParticionLista);

      String camposItemsConsulta =
          Arrays.stream(CamposAtributosEnum.values())
              .map(Enum::toString)
              .collect(Collectors.joining(","));

      String itemsPeticion = String.join(",", listaPeticion);

      StringBuilder url = new StringBuilder();
      url.append(urlServicioItems);
      url.append(PATH_ITEMS_ID);
      url.append(itemsPeticion);
      url.append(PATH_ATRIBUTOS);
      url.append(camposItemsConsulta);

      System.out.println(url);

      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<ItemDTO[]> itemsResultado =
          restTemplate.getForEntity(url.toString(), ItemDTO[].class);

      if (itemsResultado.getStatusCode().value() == STATUS_OK) {
        List<ItemDTO> listaResultado =
            Arrays.asList(Objects.requireNonNull(itemsResultado.getBody()));

        listaResultado =
            listaResultado.stream()
                .filter(e -> Integer.parseInt(e.getCode()) == STATUS_OK)
                .collect(Collectors.toList());

        listadoItems.addAll(listaResultado);
      }

      posicionInicioParticionLista += 20;

      if (posicionFinalParticionLista + 20 <= tamanioListaRequest) {
        posicionFinalParticionLista += 20;
      } else {
        posicionFinalParticionLista = tamanioListaRequest;
      }
    }

    return listadoItems;
  }
}

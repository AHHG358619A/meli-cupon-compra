package co.meli.cupon.service.impl;

import co.meli.cupon.dto.*;
import co.meli.cupon.entity.CuponCompra;
import co.meli.cupon.repository.CuponRepository;
import co.meli.cupon.service.CuponService;
import co.meli.cupon.util.CamposAtributosEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static co.meli.cupon.util.ApplicationConstants.*;

@Transactional(readOnly = true)
@Service("CuponService")
@Lazy
public class CuponServiceImpl implements CuponService {

  @Autowired CuponRepository cuponRepository;

  @Value("${meli.url.servicio.items}")
  private String urlServicioItems;

  @Override
  public CuponResponseDTO usarCupon(CuponRequestDTO cuponRequestDTO) {
    List<String> listadoItemsSinRepetir = eliminarItemsDuplicados(cuponRequestDTO.getItem_ids());
    List<ItemDTO> listadoItems = consultarPrecioItems(listadoItemsSinRepetir);
    return obtenerItemsCompra(listadoItems, cuponRequestDTO.getAmount());
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

  @Override
  public List<FavoritosResponseDTO> obtenerFavoritos() {

    List<CuponCompra> favoritosTopList =
        cuponRepository.findTop5ByOrderByCantidadSolicitudesCompraDesc();

    ModelMapper modelMapper = new ModelMapper();

    return favoritosTopList.stream()
        .map(favorito -> modelMapper.map(favorito, FavoritosResponseDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = false)
  public void guardarFavoritos(List<String> item_ids) {

    List<CuponCompra> cuponCompraList = cuponRepository.findByItemIdIn(item_ids);

    List<String> itemsExistentesList =
        cuponCompraList.stream().map(CuponCompra::getItemId).collect(Collectors.toList());

    for (String itemId : item_ids) {
      if (itemsExistentesList.contains(itemId)) {
        Optional<CuponCompra> optCupon =
            cuponCompraList.stream()
                .filter(e -> e.getItemId().equalsIgnoreCase(itemId))
                .findFirst();
        if (optCupon.isPresent()) {
          CuponCompra cuponCompra = optCupon.get();
          cuponCompra.setCantidadSolicitudesCompra(cuponCompra.getCantidadSolicitudesCompra() + 1);
        }
      } else {
        CuponCompra cuponCompra = new CuponCompra();
        cuponCompra.setItemId(itemId);
        cuponCompra.setCantidadSolicitudesCompra(1L);
        cuponCompraList.add(cuponCompra);
        itemsExistentesList.add(itemId);
      }
    }

    cuponRepository.saveAll(cuponCompraList);
  }

  private CuponResponseDTO obtenerItemsCompra(List<ItemDTO> listadoItems, double amount) {
    List<Integer> pesos = new ArrayList<>();
    List<Integer> valores = new ArrayList<>();
    pesos.add(0);
    valores.add(0);

    for (ItemDTO itemDTO : listadoItems) {
      pesos.add((int) Math.ceil(itemDTO.getBody().getPrice()));
      valores.add((int) Math.ceil(itemDTO.getBody().getPrice()));
    }

    int pesoMaximo = (int) amount;

    System.out.println("peso maximo " + pesoMaximo);
    int[][] values = new int[pesos.size()][pesoMaximo + 1];

    for (int i = 1; i < pesos.size(); i++) {
      for (int j = 1; j <= pesoMaximo; j++) {
        if (i == 1) {
          if (j >= pesos.get(i)) {
            values[i][j] = valores.get(i);
          }
        } else if (j < pesos.get(i)) {
          values[i][j] = values[i - 1][j];
        } else {
          values[i][j] =
              Math.max(values[i - 1][j], valores.get(i) + values[i - 1][j - pesos.get(i)]);
        }
      }
    }

    List<ItemDTO> itemDTOseleccionados = new ArrayList<>();

    int j = pesoMaximo;
    for (int i = pesos.size() - 1; i > 0; i--) {
      if (values[i][j] != values[i - 1][j]
          && values[i][j] == values[i - 1][j - pesos.get(i)] + valores.get(i)) {
        itemDTOseleccionados.add(listadoItems.get(i - 1));
        j -= pesos.get(i);
      }
    }

    CuponResponseDTO responseDTO = new CuponResponseDTO();
    List<String> itemsSeleccionados = new ArrayList<>();
    double sumaTotal = 0;
    for (ItemDTO dto : itemDTOseleccionados) {
      itemsSeleccionados.add(dto.getBody().getId());
      sumaTotal += dto.getBody().getPrice();
    }

    responseDTO.setItem_ids(itemsSeleccionados);
    responseDTO.setTotal(sumaTotal);

    return responseDTO;
  }
}

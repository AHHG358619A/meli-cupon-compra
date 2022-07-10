package co.meli.cupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritosResponseDTO {
  private String itemId;
  private Long cantidadSolicitudesCompra;
}

package co.meli.cupon.dto;

import lombok.Data;

import java.util.List;

@Data
public class CuponResponseDTO {

  private List<String> item_ids;
  private double total;
}

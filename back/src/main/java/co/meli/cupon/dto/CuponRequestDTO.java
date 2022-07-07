package co.meli.cupon.dto;

import lombok.Data;

import java.util.List;

@Data
public class CuponRequestDTO {
  private List<String> item_ids;
  private double amount;
}

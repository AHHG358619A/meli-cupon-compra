package co.meli.cupon.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class ItemBodyDTO {

  private String id;
  private double price;
  private String title;

  public void setPrice(Double price) {
    BigDecimal priceDecimal = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
    this.price = priceDecimal.doubleValue();
  }
}

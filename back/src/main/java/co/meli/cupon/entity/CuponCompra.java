package co.meli.cupon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CuponCompra {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String itemId;
  private Long cantidadSolicitudesCompra;

  public CuponCompra() {}

  public CuponCompra(Long id, String itemId, Long cantidadSolicitudesCompra) {
    this.id = id;
    this.itemId = itemId;
    this.cantidadSolicitudesCompra = cantidadSolicitudesCompra;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public Long getCantidadSolicitudesCompra() {
    return cantidadSolicitudesCompra;
  }

  public void setCantidadSolicitudesCompra(Long cantidadSolicitudesCompra) {
    this.cantidadSolicitudesCompra = cantidadSolicitudesCompra;
  }
}

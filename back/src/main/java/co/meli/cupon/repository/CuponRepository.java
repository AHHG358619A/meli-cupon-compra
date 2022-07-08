package co.meli.cupon.repository;

import co.meli.cupon.entity.CuponCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuponRepository extends JpaRepository<CuponCompra, Long> {

  List<CuponCompra> findByItemIdIn(List<String> itemIds);

  List<CuponCompra> findTop5ByOrderByCantidadSolicitudesCompraDesc();
}

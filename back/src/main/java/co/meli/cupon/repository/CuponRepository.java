package co.meli.cupon.repository;

import co.meli.cupon.entity.CuponCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuponRepository extends JpaRepository<CuponCompra, Long> {}

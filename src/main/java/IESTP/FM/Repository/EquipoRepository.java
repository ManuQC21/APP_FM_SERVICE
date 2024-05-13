package IESTP.FM.Repository;

import IESTP.FM.Entity.Equipo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipoRepository extends CrudRepository<Equipo, Integer> {
    Optional<Equipo> findByCodigoPatrimonial(String codigoPatrimonial);

}

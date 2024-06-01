package IESTP.FM.Repository;

import IESTP.FM.Entity.Equipo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface EquipoRepository extends CrudRepository<Equipo, Integer> {
    Optional<Equipo> findByCodigoPatrimonial(String codigoPatrimonial);

    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.responsable LEFT JOIN FETCH e.ubicacion WHERE e.id = :id")
    Equipo findByIdWithDetails(@Param("id") int id);


}

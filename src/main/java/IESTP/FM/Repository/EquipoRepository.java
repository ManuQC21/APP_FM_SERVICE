package IESTP.FM.Repository;

import IESTP.FM.Entity.Equipo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends CrudRepository<Equipo, Integer> {

    // Búsqueda de equipos por código patrimonial exacto
    Optional<Equipo> findByCodigoPatrimonial(String codigoPatrimonial);

    // Búsqueda de equipos por código de barra exacto
    Optional<Equipo> findByCodigoBarra(String codigoBarra);

    // Búsqueda de equipos por nombre con búsqueda parcial
    List<Equipo> findByNombreEquipoContaining(String nombre);

    // Búsqueda de equipos por código patrimonial con búsqueda parcial
    List<Equipo> findByCodigoPatrimonialContaining(String codigoPatrimonial);

    // Búsqueda de equipos por un rango de fechas de compra
    List<Equipo> findByFechaCompraBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Consulta nativa para obtener detalles completos por ID
    @Query(value = "SELECT e.* FROM Equipo e LEFT JOIN Empleado emp ON e.responsable_id = emp.id LEFT JOIN Ubicacion u ON e.ubicacion_id = u.id WHERE e.id = :id", nativeQuery = true)
    Equipo findByIdWithDetails(@Param("id") int id);
}

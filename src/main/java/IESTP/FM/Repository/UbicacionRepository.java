package IESTP.FM.Repository;

import IESTP.FM.Entity.Ubicacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends CrudRepository<Ubicacion, Integer> {
}

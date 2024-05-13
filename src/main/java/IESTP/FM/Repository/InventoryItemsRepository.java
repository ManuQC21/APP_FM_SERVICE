package IESTP.FM.Repository;

import IESTP.FM.Entity.InventoryItems;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemsRepository extends CrudRepository<InventoryItems, Integer> {
    Optional<InventoryItems> findByCodigoPatrimonial(String codigoPatrimonial);
    Optional<InventoryItems> findByCodigoBarra(String codigoBarra);

}

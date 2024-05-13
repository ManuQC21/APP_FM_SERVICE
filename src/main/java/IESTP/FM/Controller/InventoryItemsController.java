package IESTP.FM.Controller;

import IESTP.FM.Entity.InventoryItems;
import IESTP.FM.Service.InventoryItemsService;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/InventoryItems")
public class InventoryItemsController {

    @Autowired
    private InventoryItemsService inventoryItemsService;

    @PostMapping("/agregar")
    public GenericResponse<InventoryItems> addInformacion(@RequestBody InventoryItems inventoryItems) {
        return inventoryItemsService.saveInformacion(inventoryItems);
    }

    @GetMapping("/generarCodigoBarra/{codigoPatrimonial}")
    public ResponseEntity<byte[]> generarCodigoBarra(@PathVariable String codigoPatrimonial) {
        GenericResponse<byte[]> response = inventoryItemsService.generateBarcodeImageForPatrimonialCode(codigoPatrimonial);
        if (response.getRpta() == 1) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(response.getBody());
        } else {
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<GenericResponse<List<InventoryItems>>> listAllInformacion() {
        GenericResponse<List<InventoryItems>> response = inventoryItemsService.findAllInformacion();
        if (response.getRpta() == Global.RPTA_OK) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(500).body(response);
        }
    }
}

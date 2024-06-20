package IESTP.FM.Controller;

import IESTP.FM.Entity.Equipo;
import IESTP.FM.Service.EquipoService;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/equipo")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @PostMapping("/agregar")
    public ResponseEntity<GenericResponse<Equipo>> addEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.addEquipo(equipo));
    }

    @PutMapping("/modificar")
    public ResponseEntity<GenericResponse<Equipo>> updateEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.updateEquipo(equipo));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteEquipo(@PathVariable Integer id) {
        return ResponseEntity.ok(equipoService.deleteEquipo(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<GenericResponse<List<Equipo>>> listAllEquipos() {
        return ResponseEntity.ok(equipoService.findAllEquipos());
    }

    @PostMapping("/escanearCodigoBarra")
    public ResponseEntity<GenericResponse<Equipo>> scanBarcode(@RequestParam("file") MultipartFile file) {
        GenericResponse<Equipo> response = equipoService.scanAndCopyBarcodeData(file);
        return response.getRpta() == Global.RESPUESTA_OK ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/descargarReporte")
    public ResponseEntity<byte[]> downloadExcelReport() throws Exception {
        byte[] bytes = equipoService.generateExcelReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Reporte_De_Equipos.xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Equipo>> getEquipoById(@PathVariable int id) {
        return ResponseEntity.ok(equipoService.getEquipoById(id));
    }

    @GetMapping("/filtro/nombre")
    public ResponseEntity<GenericResponse<List<Equipo>>> filterByName(@RequestParam String nombreEquipo) {
        return ResponseEntity.ok(equipoService.filtroPorNombre(nombreEquipo));
    }

    @GetMapping("/filtro/codigoPatrimonial")
    public ResponseEntity<GenericResponse<List<Equipo>>> filterByPatrimonialCode(@RequestParam String codigoPatrimonial) {
        return ResponseEntity.ok(equipoService.filtroCodigoPatrimonial(codigoPatrimonial));
    }

    @GetMapping("/filtro/fechaCompra")
    public ResponseEntity<GenericResponse<List<Equipo>>> filterByPurchaseDate(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaFin) {
        return ResponseEntity.ok(equipoService.filtroFechaRevisionBetween(fechaInicio, fechaFin));
    }

    @GetMapping("/generarCodigoBarra/{codigoPatrimonial}")
    public ResponseEntity<byte[]> generateBarcode(@PathVariable String codigoPatrimonial) {
        GenericResponse<byte[]> response = equipoService.generateBarcodeImageForPatrimonialCode(codigoPatrimonial);
        if (response.getRpta() == Global.RESPUESTA_OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            return ResponseEntity.ok().headers(headers).body(response.getBody());
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

package IESTP.FM.Controller;

import IESTP.FM.Entity.Equipo;
import IESTP.FM.Service.EquipoService;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/equipo")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @PostMapping("/agregar")
    public GenericResponse<Equipo> addEquipo(@RequestBody Equipo equipo) {
        return equipoService.addEquipo(equipo);
    }

    @PutMapping("/modificar")
    public GenericResponse<Equipo> updateEquipo(@RequestBody Equipo equipo) {
        return equipoService.updateEquipo(equipo);
    }

    @DeleteMapping("/eliminar/{id}")
    public GenericResponse<Void> deleteEquipo(@PathVariable Integer id) {
        return equipoService.deleteEquipo(id);
    }

    @GetMapping("/listar")
    public GenericResponse<List<Equipo>> listAllEquipos() {
        return equipoService.findAllEquipos();
    }

    @GetMapping("/buscarPorCodigoPatrimonial/{codigoPatrimonial}")
    public ResponseEntity<GenericResponse<Equipo>> findEquipoByCodigoPatrimonial(@PathVariable String codigoPatrimonial) {
        GenericResponse<Equipo> response = equipoService.findEquipoByCodigoPatrimonial(codigoPatrimonial);
        return response.getRpta() == Global.RPTA_OK ? ResponseEntity.ok(response) : ResponseEntity.status(404).body(response);
    }

    @PostMapping("/escanearCodigoBarra")
    public ResponseEntity<GenericResponse<Equipo>> escanearCodigoBarra(@RequestParam("file") MultipartFile file) {
        GenericResponse<Equipo> response = equipoService.scanAndCopyBarcodeData(file);
        return response.getRpta() == Global.RPTA_OK ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/descargarReporte")
    public ResponseEntity<byte[]> downloadExcelReport() {
        try {
            byte[] report = equipoService.generateExcelReport();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipos.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Equipo>> getEquipoById(@PathVariable int id) {
        GenericResponse<Equipo> response = equipoService.getEquipoById(id);
        return ResponseEntity.status(response.getRpta() == Global.RPTA_OK ? 200 : 404).body(response);
    }
}

package IESTP.FM.Controller;

import IESTP.FM.Service.UbicacionService;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.Entity.Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ubicaciones")  // Asegurarse que la ruta es clara y correcta con el prefijo "/"
@Slf4j  // Lombok annotation for logging
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping("/listar")
    public ResponseEntity<GenericResponse<List<Ubicacion>>> listarTodasLasUbicaciones() {
        GenericResponse<List<Ubicacion>> response = ubicacionService.listarTodasLasUbicaciones();
        if (response.getBody() == null || response.getBody().isEmpty()) {
            log.info("No se encontraron ubicaciones en la base de datos.");
            return ResponseEntity.noContent().build();  // Retorna 204 No Content si la lista está vacía
        }
        log.info("Se recuperaron {} ubicaciones.", response.getBody().size());
        return ResponseEntity.ok(response);
    }
}

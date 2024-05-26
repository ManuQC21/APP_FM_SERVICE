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

@RestController
@RequestMapping("ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping("/listar")
    public ResponseEntity<GenericResponse<List<Ubicacion>>> listarTodasLasUbicaciones() {
        GenericResponse<List<Ubicacion>> response = ubicacionService.listarTodasLasUbicaciones();
        return ResponseEntity.ok(response);
    }
}

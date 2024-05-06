package IESTP.FM.Controller;

import IESTP.FM.Entity.Equipo;
import IESTP.FM.Service.EquipoService;
import IESTP.FM.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("equipo")
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
    // Endpoint para listar todos los equipos
    @GetMapping("/listar")
    public GenericResponse<List<Equipo>> listAllEquipos() {
        return equipoService.findAllEquipos();
    }

    // Endpoint para buscar un equipo por codigoP
    @GetMapping("/buscar/{codigoP}")
    public GenericResponse<Equipo> findEquipoByCodigoP(@PathVariable String codigoP) {
        return equipoService.findEquipoByCodigoP(codigoP);
    }

}

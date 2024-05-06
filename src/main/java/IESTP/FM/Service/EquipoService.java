package IESTP.FM.Service;

import IESTP.FM.Entity.Equipo;
import IESTP.FM.Repository.EquipoRepository;
import IESTP.FM.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    // Método para añadir un nuevo equipo sin validaciones
    public GenericResponse<Equipo> addEquipo(Equipo equipo) {
        try {
            // Guarda el equipo en la base de datos sin realizar validaciones previas
            Equipo savedEquipo = equipoRepository.save(equipo);
            // Retorna una respuesta exitosa con el equipo guardado
            return new GenericResponse<>("SUCCESS", 1, "Equipo registrado exitosamente.", savedEquipo);
        } catch (Exception e) {
            // Maneja excepciones generales y retorna un error
            return new GenericResponse<>("ERROR", 0, "Error al registrar el equipo: " + e.getMessage(), null);
        }
    }

    // Método para modificar un equipo existente
    public GenericResponse<Equipo> updateEquipo(Equipo equipo) {
        // Verifica si el id es 0, lo cual indica que no es un id válido
        if (equipo.getId() == 0 || !equipoRepository.existsById(equipo.getId())) {
            return new GenericResponse<>("ERROR", 0, "Equipo no encontrado.", null);
        }
        try {
            Equipo updatedEquipo = equipoRepository.save(equipo);
            return new GenericResponse<>("SUCCESS", 1, "Equipo actualizado exitosamente.", updatedEquipo);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al actualizar el equipo: " + e.getMessage(), null);
        }
    }

    // Método para eliminar un equipo
    public GenericResponse<Void> deleteEquipo(Integer id) {
        if (!equipoRepository.existsById(id)) {
            return new GenericResponse<>("ERROR", 0, "Equipo no encontrado.", null);
        }
        try {
            equipoRepository.deleteById(id);
            return new GenericResponse<>("SUCCESS", 1, "Equipo eliminado exitosamente.", null);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al eliminar el equipo: " + e.getMessage(), null);
        }
    }

    // Listar todos los equipos
    public GenericResponse<List<Equipo>> findAllEquipos() {
        try {
            List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
            return new GenericResponse<>("SUCCESS", 1, "Listado completo de equipos.", equipos);
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al obtener el listado de equipos.", null);
        }
    }

    // Buscar equipo por códigoP
    public GenericResponse<Equipo> findEquipoByCodigoP(String codigoP) {
        try {
            Optional<Equipo> equipo = equipoRepository.findByCodigoP(codigoP);
            if (equipo.isPresent()) {
                return new GenericResponse<>("SUCCESS", 1, "Equipo encontrado.", equipo.get());
            } else {
                return new GenericResponse<>("ERROR", 0, "No se encontró un equipo con el código proporcionado.", null);
            }
        } catch (Exception e) {
            return new GenericResponse<>("ERROR", 0, "Error al buscar el equipo.", null);
        }
    }

}

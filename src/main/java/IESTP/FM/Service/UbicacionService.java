package IESTP.FM.Service;

import IESTP.FM.Entity.Ubicacion;
import IESTP.FM.Repository.UbicacionRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public GenericResponse<List<Ubicacion>> listarTodasLasUbicaciones() {
        try {
            List<Ubicacion> ubicaciones = (List<Ubicacion>) ubicacionRepository.findAll();
            return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, Global.OPERACION_EXITOSA, ubicaciones);
        } catch (Exception e) {
            log.error("Error al listar ubicaciones", e);
            return new GenericResponse<>(Global.TIPO_RESULTADO, Global.RESPUESTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }
}

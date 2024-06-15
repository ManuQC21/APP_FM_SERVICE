package IESTP.FM.Service;

import IESTP.FM.Entity.Empleado;
import IESTP.FM.Repository.EmpleadoRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public GenericResponse<List<Empleado>> listarTodosLosEmpleados() {
        try {
            List<Empleado> empleados = (List<Empleado>) empleadoRepository.findAll();
            return new GenericResponse<>(Global.TIPO_DATOS, Global.RESPUESTA_OK, Global.OPERACION_EXITOSA, empleados);
        } catch (Exception e) {
            log.error("Error al listar empleados", e);
            return new GenericResponse<>(Global.TIPO_RESULTADO, Global.RESPUESTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }
}

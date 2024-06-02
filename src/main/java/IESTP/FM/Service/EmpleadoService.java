package IESTP.FM.Service;

import IESTP.FM.Entity.Empleado;
import IESTP.FM.Repository.EmpleadoRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public GenericResponse<List<Empleado>> listarTodosLosEmpleados() {
        try {
            List<Empleado> empleados = new ArrayList<>();
            empleadoRepository.findAll().forEach(empleados::add);
            return new GenericResponse<>(Global.TIPO_DATA, Global.RPTA_OK, Global.OPERACION_CORRECTA, empleados);
        } catch (Exception e) {
            return new GenericResponse<>(Global.TIPO_RESULT, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }
}

package IESTP.FM.Service;

import IESTP.FM.Entity.Usuario;
import IESTP.FM.Repository.UsuarioRepository;
import IESTP.FM.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    // Método para iniciar sesión
    public GenericResponse<Usuario> login(String correo, String clave) {
        Optional<Usuario> usuarioOpt = repository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getClave().equals(clave)) {
                return new GenericResponse<>("AUTH", 1, "Haz iniciado sesión correctamente", usuario);
            } else {
                return new GenericResponse<>("AUTH", 2, "Contraseña incorrecta", null);
            }
        }
        return new GenericResponse<>("AUTH", 2, "Usuario no encontrado", null);
    }
}

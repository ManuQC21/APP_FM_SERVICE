package IESTP.FM.Service;

import IESTP.FM.Entity.Usuario;
import IESTP.FM.Repository.UsuarioRepository;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public GenericResponse<Usuario> register(Usuario usuario) {
        try {
            Optional<Usuario> existingUser = repository.findByCorreo(usuario.getCorreo());
            if (existingUser.isPresent()) {
                return new GenericResponse<>(Global.TIPO_ADVERTENCIA, Global.RESPUESTA_ADVERTENCIA, Global.USUARIO_YA_EXISTE, null);
            }
            usuario.setClave(passwordEncoder.encode(usuario.getClave()));
            Usuario savedUsuario = repository.save(usuario);
            return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, Global.OPERACION_EXITOSA, savedUsuario);
        } catch (Exception e) {
            log.error("Error al registrar el usuario", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }

    public GenericResponse<Usuario> login(String correo, String clave) {
        try {
            Optional<Usuario> usuarioOpt = repository.findByCorreo(correo);
            if (usuarioOpt.isPresent() && passwordEncoder.matches(clave, usuarioOpt.get().getClave())) {
                return new GenericResponse<>(Global.TIPO_EXITO, Global.RESPUESTA_OK, Global.AUTENTICACION_EXITOSA, usuarioOpt.get());
            } else {
                return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ADVERTENCIA, Global.AUTENTICACION_FALLIDA, null);
            }
        } catch (Exception e) {
            log.error("Error al iniciar sesión", e);
            return new GenericResponse<>(Global.TIPO_ERROR, Global.RESPUESTA_ERROR, Global.OPERACION_ERRONEA, null);
        }
    }
}

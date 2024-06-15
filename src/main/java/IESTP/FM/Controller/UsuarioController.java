package IESTP.FM.Controller;

import IESTP.FM.Entity.Usuario;
import IESTP.FM.Service.UsuarioService;
import IESTP.FM.utils.GenericResponse;
import IESTP.FM.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/registro")
    public ResponseEntity<GenericResponse<Usuario>> register(@Valid @RequestBody Usuario usuario) {
        GenericResponse<Usuario> response = service.register(usuario);
        if (response.getRpta() == Global.RESPUESTA_OK) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<Usuario>> login(@RequestParam("correo") String correo, @RequestParam("clave") String clave) {
        GenericResponse<Usuario> response = service.login(correo, clave);
        if (response.getRpta() == Global.RESPUESTA_OK) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}

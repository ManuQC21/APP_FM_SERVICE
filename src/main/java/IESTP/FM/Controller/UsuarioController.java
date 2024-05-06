package IESTP.FM.Controller;

import IESTP.FM.Entity.Usuario;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import IESTP.FM.Service.UsuarioService;
import IESTP.FM.utils.GenericResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/login")
    public GenericResponse<Usuario> login(@RequestParam("correo") String correo, @RequestParam("clave") String clave) {
        return service.login(correo, clave);
    }
}
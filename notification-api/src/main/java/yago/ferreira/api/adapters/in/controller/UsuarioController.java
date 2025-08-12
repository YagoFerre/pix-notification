package yago.ferreira.api.adapters.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yago.ferreira.api.adapters.in.service.UsuarioService;
import yago.ferreira.api.adapters.out.dto.request.UsuarioRequest;
import yago.ferreira.api.adapters.out.dto.response.UsuarioResponse;

@RestController
@RequestMapping("api/v1/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> createUser(@RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioCreated = usuarioService.createUsuario(usuarioRequest);
        return ResponseEntity.ok(usuarioCreated);
    }
}

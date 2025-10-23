package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.PersonaDTO;
import pe.edu.unasam.activos.modules.personas.service.PersonaService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('USUARIOS_CREAR')")
    public ResponseEntity<PersonaDTO.PersonaUsuarioResponse> buscarPersonaPorDocumento(
            @RequestParam String numeroDocumento) {

        Optional<PersonaDTO.PersonaUsuarioResponse> personaResponse = personaService.findPersonaParaUsuario(numeroDocumento);

        return personaResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search-sin-usuario")
    public Page<PersonaDTO.Response> searchPersonasSinUsuario(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        return personaService.findPersonasSinUsuarioPaginado(query, pageable);
    }
}

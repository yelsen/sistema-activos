package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.PersonaDTO;
import pe.edu.unasam.activos.modules.personas.service.PersonaService;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/personas")
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping(value = "/buscar-usuario", params = "dni")
    public ResponseEntity<PersonaDTO.PersonaUsuarioResponse> buscarPersonaUsuarioPorDni(
            @RequestParam("dni") String dni) {
        return personaService.findPersonaParaUsuario(dni)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(
                        PersonaDTO.PersonaUsuarioResponse.builder()
                                .nombres("")
                                .apellidos("")
                                .email("")
                                .telefono("")
                                .direccion("")
                                .genero("")
                                .tieneUsuario(false)
                                .exists(false)
                                .build()));
    }

    @GetMapping(value = "/buscar-responsable", params = "dni")
    public ResponseEntity<PersonaDTO.PersonaResponsableResponse> buscarPersonaResponsablePorDni(
            @RequestParam("dni") String dni) {
        return personaService.findPersonaParaResponsable(dni)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "/buscar-tecnico", params = "dni")
    public ResponseEntity<PersonaDTO.PersonaTecnicoResponse> buscarPersonaTecnicoPorDni(
            @RequestParam("dni") String dni) {
        return personaService.findPersonaParaTecnico(dni)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "/buscar-usuario", params = "!dni")
    public Page<PersonaDTO.Response> buscarPersonasSinUsuario(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        return personaService.findPersonasSinUsuarioPaginado(query, pageable);
    }

    @GetMapping(value = "/buscar-responsable", params = "!dni")
    public Page<PersonaDTO.Response> buscarPersonasSinResponsable(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        return personaService.findPersonasSinResponsablePaginado(query, pageable);
    }

    @GetMapping(value = "/buscar-tecnico", params = "!dni")
    public Page<PersonaDTO.Response> buscarPersonasSinTecnico(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        return personaService.findPersonasSinTecnicoPaginado(query, pageable);
    }
}

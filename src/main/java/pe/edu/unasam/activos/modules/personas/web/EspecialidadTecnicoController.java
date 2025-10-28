package pe.edu.unasam.activos.modules.personas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.EspecialidadTecnicoDTO;
import pe.edu.unasam.activos.modules.personas.service.EspecialidadTecnicoService;

import java.util.List;

@RestController
@RequestMapping({"/mantenimientos/especialidades", "/personas/especialidades"})
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EspecialidadTecnicoController {

    private final EspecialidadTecnicoService especialidadService;

    @GetMapping
    @PreAuthorize("hasAuthority('ESPECIALIDADES_LEER')")
    public ResponseEntity<Page<EspecialidadTecnicoDTO.Response>> getAllEspecialidades(Pageable pageable) {
        return ResponseEntity.ok(especialidadService.getAllEspecialidades(pageable));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ESPECIALIDADES_LEER')")
    public ResponseEntity<List<EspecialidadTecnicoDTO.Response>> getAllEspecialidadesAsList() {
        return ResponseEntity.ok(especialidadService.getAllEspecialidadesAsList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ESPECIALIDADES_LEER')")
    public ResponseEntity<EspecialidadTecnicoDTO.Response> getEspecialidadById(@PathVariable Integer id) {
        return ResponseEntity.ok(especialidadService.getEspecialidadById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ESPECIALIDADES_CREAR')")
    public ResponseEntity<EspecialidadTecnicoDTO.Response> createEspecialidad(@Valid @RequestBody EspecialidadTecnicoDTO.Request request) {
        return new ResponseEntity<>(especialidadService.createEspecialidad(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ESPECIALIDADES_EDITAR')")
    public ResponseEntity<EspecialidadTecnicoDTO.Response> updateEspecialidad(@PathVariable Integer id, @Valid @RequestBody EspecialidadTecnicoDTO.Request request) {
        return ResponseEntity.ok(especialidadService.updateEspecialidad(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ESPECIALIDADES_ELIMINAR')")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Integer id) {
        especialidadService.deleteEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}

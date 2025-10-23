package pe.edu.unasam.activos.modules.personas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.ResponsableDTO;
import pe.edu.unasam.activos.modules.personas.service.ResponsableService;

@RestController
@RequestMapping("/personas/responsables")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ResponsableController {

    private final ResponsableService responsableService;

    @GetMapping
    @PreAuthorize("hasAuthority('RESPONSABLES_LEER')")
    public ResponseEntity<Page<ResponsableDTO.Response>> getAllResponsables(Pageable pageable) {
        return ResponseEntity.ok(responsableService.getAllResponsables(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_LEER')")
    public ResponseEntity<ResponsableDTO.Response> getResponsableById(@PathVariable Integer id) {
        return ResponseEntity.ok(responsableService.getResponsableById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESPONSABLES_CREAR')")
    public ResponseEntity<ResponsableDTO.Response> createResponsable(@Valid @RequestBody ResponsableDTO.Request request) {
        return new ResponseEntity<>(responsableService.createResponsable(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_EDITAR')")
    public ResponseEntity<ResponsableDTO.Response> updateResponsable(@PathVariable Integer id, @Valid @RequestBody ResponsableDTO.Request request) {
        return ResponseEntity.ok(responsableService.updateResponsable(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_ELIMINAR')")
    public ResponseEntity<Void> deleteResponsable(@PathVariable Integer id) {
        responsableService.deleteResponsable(id);
        return ResponseEntity.noContent().build();
    }
}

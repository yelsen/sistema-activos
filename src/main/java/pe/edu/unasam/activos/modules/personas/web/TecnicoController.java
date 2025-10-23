package pe.edu.unasam.activos.modules.personas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.TecnicoDTO;
import pe.edu.unasam.activos.modules.personas.service.TecnicoService;

@RestController
@RequestMapping("/personas/tecnicos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @GetMapping
    @PreAuthorize("hasAuthority('TECNICOS_LEER')")
    public ResponseEntity<Page<TecnicoDTO.Response>> getAllTecnicos(Pageable pageable) {
        return ResponseEntity.ok(tecnicoService.getAllTecnicos(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_LEER')")
    public ResponseEntity<TecnicoDTO.Response> getTecnicoById(@PathVariable Integer id) {
        return ResponseEntity.ok(tecnicoService.getTecnicoById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TECNICOS_CREAR')")
    public ResponseEntity<TecnicoDTO.Response> createTecnico(@Valid @RequestBody TecnicoDTO.Request request) {
        return new ResponseEntity<>(tecnicoService.createTecnico(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_EDITAR')")
    public ResponseEntity<TecnicoDTO.Response> updateTecnico(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO.Request request) {
        return ResponseEntity.ok(tecnicoService.updateTecnico(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_ELIMINAR')")
    public ResponseEntity<Void> deleteTecnico(@PathVariable Integer id) {
        tecnicoService.deleteTecnico(id);
        return ResponseEntity.noContent().build();
    }
}

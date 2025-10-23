package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personas/tipos-documento")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TipoDocumentoController {

    /*private final TipoDocumentoService tipoDocumentoService;

    @GetMapping
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_LEER')")
    public ResponseEntity<Page<TipoDocumentoDTO.Response>> getAllTiposDocumento(Pageable pageable) {
        return ResponseEntity.ok(tipoDocumentoService.getAllTiposDocumento(pageable));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_LEER')")
    public ResponseEntity<List<TipoDocumentoDTO.Response>> getAllTiposDocumentoAsList() {
        return ResponseEntity.ok(tipoDocumentoService.getAllTiposDocumentoAsList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_LEER')")
    public ResponseEntity<TipoDocumentoDTO.Response> getTipoDocumentoById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoDocumentoService.getTipoDocumentoById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_CREAR')")
    public ResponseEntity<TipoDocumentoDTO.Response> createTipoDocumento(@Valid @RequestBody TipoDocumentoDTO.Request request) {
        return new ResponseEntity<>(tipoDocumentoService.createTipoDocumento(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_EDITAR')")
    public ResponseEntity<TipoDocumentoDTO.Response> updateTipoDocumento(@PathVariable Integer id, @Valid @RequestBody TipoDocumentoDTO.Request request) {
        return ResponseEntity.ok(tipoDocumentoService.updateTipoDocumento(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOS_DOCUMENTO_ELIMINAR')")
    public ResponseEntity<Void> deleteTipoDocumento(@PathVariable Integer id) {
        tipoDocumentoService.deleteTipoDocumento(id);
        return ResponseEntity.noContent().build();
    }*/
}

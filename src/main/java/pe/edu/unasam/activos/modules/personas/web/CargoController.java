package pe.edu.unasam.activos.modules.personas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.personas.dto.CargoDTO;
import pe.edu.unasam.activos.modules.personas.service.CargoService;

import java.util.List;

@RestController
@RequestMapping("/personas/cargos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CargoController {
    private final CargoService cargoService;

    @GetMapping
    @PreAuthorize("hasAuthority('CARGOS_LEER')")
    public ResponseEntity<Page<CargoDTO.Response>> getAllCargos(Pageable pageable) {
        return ResponseEntity.ok(cargoService.getAllCargos(pageable));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CARGOS_LEER')")
    public ResponseEntity<List<CargoDTO.Response>> getAllCargosAsList() {
        return ResponseEntity.ok(cargoService.getAllCargosAsList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGOS_LEER')")
    public ResponseEntity<CargoDTO.Response> getCargoById(@PathVariable Integer id) {
        return ResponseEntity.ok(cargoService.getCargoById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CARGOS_CREAR')")
    public ResponseEntity<CargoDTO.Response> createCargo(@Valid @RequestBody CargoDTO.Request request) {
        return new ResponseEntity<>(cargoService.createCargo(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGOS_EDITAR')")
    public ResponseEntity<CargoDTO.Response> updateCargo(@PathVariable Integer id, @Valid @RequestBody CargoDTO.Request request) {
        return ResponseEntity.ok(cargoService.updateCargo(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGOS_ELIMINAR')")
    public ResponseEntity<Void> deleteCargo(@PathVariable Integer id) {
        cargoService.deleteCargo(id);
        return ResponseEntity.noContent().build();
    }
}

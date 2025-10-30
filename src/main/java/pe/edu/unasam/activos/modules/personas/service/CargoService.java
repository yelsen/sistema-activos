package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.Cargo;
import pe.edu.unasam.activos.modules.personas.dto.CargoDTO;
import pe.edu.unasam.activos.modules.personas.repository.CargoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public Page<CargoDTO.Response> getAllCargos(Pageable pageable) {
        return cargoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<CargoDTO.Response> getAllCargos(String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return cargoRepository
                    .findByNombreCargoContainingIgnoreCase(query.trim(), pageable)
                    .map(this::convertToDto);
        }
        return cargoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CargoDTO.Response> getAllCargosAsList() {
        return cargoRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CargoDTO.Response getCargoById(Integer id) {
        return cargoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + id));
    }

    public CargoDTO.Response createCargo(CargoDTO.Request request) {
        if (cargoRepository.existsByNombreCargo(request.getNombreCargo())) {
            throw new BusinessException("El nombre del cargo '" + request.getNombreCargo() + "' ya existe.");
        }
        Cargo cargo = new Cargo();
        cargo.setNombreCargo(request.getNombreCargo());
        return convertToDto(cargoRepository.save(cargo));
    }

    public CargoDTO.Response updateCargo(Integer id, CargoDTO.Request request) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + id));

        cargoRepository.findByNombreCargo(request.getNombreCargo()).ifPresent(existing -> {
            if (!existing.getIdCargo().equals(id)) {
                throw new BusinessException("El nombre del cargo '" + request.getNombreCargo() + "' ya existe.");
            }
        });

        cargo.setNombreCargo(request.getNombreCargo());
        return convertToDto(cargoRepository.save(cargo));
    }

    public void deleteCargo(Integer id) {
        if (!cargoRepository.existsById(id)) {
            throw new NotFoundException("Cargo no encontrado con ID: " + id);
        }
        cargoRepository.deleteById(id);
    }

    private CargoDTO.Response convertToDto(Cargo cargo) {
        return new CargoDTO.Response(cargo.getIdCargo(), cargo.getNombreCargo());
    }
}

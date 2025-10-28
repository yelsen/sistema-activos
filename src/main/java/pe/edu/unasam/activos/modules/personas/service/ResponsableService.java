package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import java.time.LocalDate;
import pe.edu.unasam.activos.modules.personas.domain.Cargo;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Responsable;
import java.util.List;
import pe.edu.unasam.activos.modules.personas.dto.ResponsableDTO;
import pe.edu.unasam.activos.modules.personas.repository.CargoRepository;
import pe.edu.unasam.activos.modules.personas.repository.ResponsableRepository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OficinaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ResponsableService {

    private final ResponsableRepository responsableRepository;
    private final CargoRepository cargoRepository;
    private final OficinaRepository oficinaRepository;
    private final PersonaService personaService;

    @Transactional(readOnly = true)
    public Page<ResponsableDTO.Response> getAllResponsables(Pageable pageable) {
        return responsableRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<ResponsableDTO.Response> getAllResponsables(String query, EstadoResponsable estado, Pageable pageable) {
        return responsableRepository.findAllWithFilters(query, estado, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public ResponsableDTO.Response getResponsableById(Integer id) {
        return responsableRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));
    }

    public ResponsableDTO.Response createResponsable(ResponsableDTO.Request request) {
        // Crear o ubicar Persona; si existe, actualizar atributos permitidos
        Persona persona = personaService.findOrCreatePersona(
                request.getDniPersona(),
                request.getNombres(),
                request.getApellidos(),
                request.getEmail(),
                request.getTelefono(),
                request.getDireccion(),
                request.getGenero());
        // Sincronizar datos en caso exista
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setEmail(request.getEmail());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
        persona.setGenero(request.getGenero());
        Cargo cargo = cargoRepository.findById(request.getIdCargo())
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + request.getIdCargo()));
        Oficina oficina = null;
        if (request.getIdOficina() != null) {
            oficina = oficinaRepository.findById(request.getIdOficina())
                    .orElseThrow(
                            () -> new NotFoundException("Oficina no encontrada con ID: " + request.getIdOficina()));
            // Regla: una oficina solo puede tener un responsable ACTIVO a la vez
            List<Responsable> ocupantes = responsableRepository.findByOficina_IdOficina(oficina.getIdOficina());
            boolean yaOcupada = ocupantes.stream()
                    .anyMatch(r -> r.getEstadoResponsable() == EstadoResponsable.ACTIVO && r.getFechaFinAsignacion() == null);
            if (yaOcupada) {
                throw new BusinessException("La oficina seleccionada ya tiene un responsable activo.");
            }
        }

        Responsable responsable = new Responsable();
        mapToEntity(responsable, request, persona, cargo, oficina);
        // Si se asigna oficina al crear y no hay fecha explícita, usar hoy
        if (oficina != null && responsable.getFechaAsignacion() == null) {
            responsable.setFechaAsignacion(LocalDate.now());
        }

        return convertToDto(responsableRepository.save(responsable));
    }

    @Transactional(readOnly = true)
    public ResponsableDTO.EditForm getEditForm(Integer id) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));

        Persona persona = responsable.getPersona();
        return ResponsableDTO.EditForm.builder()
                .idResponsable(responsable.getIdResponsable())
                .dniPersona(persona != null ? persona.getDni() : null)
                .nombres(persona != null ? persona.getNombres() : null)
                .apellidos(persona != null ? persona.getApellidos() : null)
                .email(persona != null ? persona.getEmail() : null)
                .telefono(persona != null ? persona.getTelefono() : null)
                .direccion(persona != null ? persona.getDireccion() : null)
                .genero(persona != null && persona.getGenero() != null ? persona.getGenero().name() : null)
                .idCargo(responsable.getCargo() != null ? responsable.getCargo().getIdCargo() : null)
                .idOficina(responsable.getOficina() != null ? responsable.getOficina().getIdOficina() : null)
                .build();
    }

    public ResponsableDTO.Response updateResponsable(Integer id, ResponsableDTO.Request request) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));
        // Usar la persona asociada (DNI es de solo lectura en el formulario)
        Persona persona = responsable.getPersona();
        if (persona == null) {
            // Si no hay persona asociada, crear o ubicar por DNI y datos provistos
            persona = personaService.findOrCreatePersona(
                    request.getDniPersona(),
                    request.getNombres(),
                    request.getApellidos(),
                    request.getEmail(),
                    request.getTelefono(),
                    request.getDireccion(),
                    request.getGenero());
        } else {
            // Mantener DNI inmutable durante edición
            if (request.getDniPersona() != null && !request.getDniPersona().equals(persona.getDni())) {
                throw new BusinessException("El DNI no puede ser modificado.");
            }
            // Actualizar datos de persona
            persona.setNombres(request.getNombres());
            persona.setApellidos(request.getApellidos());
            persona.setEmail(request.getEmail());
            persona.setTelefono(request.getTelefono());
            persona.setDireccion(request.getDireccion());
            persona.setGenero(request.getGenero());
        }
        Cargo cargo = cargoRepository.findById(request.getIdCargo())
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + request.getIdCargo()));
        Oficina oficina = null;
        if (request.getIdOficina() != null) {
            oficina = oficinaRepository.findById(request.getIdOficina())
                    .orElseThrow(
                            () -> new NotFoundException("Oficina no encontrada con ID: " + request.getIdOficina()));
        }

        // Detectar cambio de oficina para fijar fecha de asignación
        Oficina oldOficina = responsable.getOficina();
        // Si se quiere asignar una oficina (nueva o al crear) validar que no esté ocupada por otro
        if (oficina != null && (oldOficina == null ||
                (oldOficina.getIdOficina() != null && !oldOficina.getIdOficina().equals(oficina.getIdOficina())))) {
            List<Responsable> ocupantes = responsableRepository.findByOficina_IdOficina(oficina.getIdOficina());
            boolean yaOcupada = ocupantes.stream()
                    .anyMatch(r -> !r.getIdResponsable().equals(responsable.getIdResponsable())
                            && r.getEstadoResponsable() == EstadoResponsable.ACTIVO
                            && r.getFechaFinAsignacion() == null);
            if (yaOcupada) {
                throw new BusinessException("La oficina seleccionada ya está ocupada por otro responsable activo.");
            }
        }
        mapToEntity(responsable, request, persona, cargo, oficina);
        if (oficina != null && (oldOficina == null ||
                (oldOficina.getIdOficina() != null && !oldOficina.getIdOficina().equals(oficina.getIdOficina())))) {
            responsable.setFechaAsignacion(LocalDate.now());
            // Limpiar fecha fin si se re-asigna
            responsable.setFechaFinAsignacion(null);
            // Asegurar estado activo si estaba finalizado por error
            if (responsable.getEstadoResponsable() == null
                    || responsable.getEstadoResponsable() == EstadoResponsable.FINALIZADO) {
                responsable.setEstadoResponsable(EstadoResponsable.ACTIVO);
            }
        }

        return convertToDto(responsableRepository.save(responsable));
    }

    public void deleteResponsable(Integer id) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));
        // Al finalizar (soft delete), fijar fecha fin
        if (responsable.getFechaFinAsignacion() == null) {
            responsable.setFechaFinAsignacion(LocalDate.now());
            responsableRepository.save(responsable);
        }
        responsableRepository.deleteById(id);
    }

    public ResponsableDTO.Response toggleEstado(Integer id) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));
        EstadoResponsable estadoActual = responsable.getEstadoResponsable();
        if (estadoActual == EstadoResponsable.ACTIVO) {
            // Finalizar asignación: fijar fecha fin
            responsable.setEstadoResponsable(EstadoResponsable.FINALIZADO);
            if (responsable.getFechaFinAsignacion() == null) {
                responsable.setFechaFinAsignacion(LocalDate.now());
            }
        } else {
            // Reactivar asignación: limpiar fecha fin
            // Validar que la oficina no esté ocupada por otro responsable ACTIVO
            if (responsable.getOficina() != null && responsable.getOficina().getIdOficina() != null) {
                List<Responsable> ocupantes = responsableRepository
                        .findByOficina_IdOficina(responsable.getOficina().getIdOficina());
                boolean yaOcupada = ocupantes.stream()
                        .anyMatch(r -> !r.getIdResponsable().equals(responsable.getIdResponsable())
                                && r.getEstadoResponsable() == EstadoResponsable.ACTIVO
                                && r.getFechaFinAsignacion() == null);
                if (yaOcupada) {
                    throw new BusinessException("La oficina seleccionada ya está ocupada por otro responsable activo.");
                }
            }
            responsable.setEstadoResponsable(EstadoResponsable.ACTIVO);
            responsable.setFechaFinAsignacion(null);
            if (responsable.getFechaAsignacion() == null) {
                responsable.setFechaAsignacion(LocalDate.now());
            }
        }
        return convertToDto(responsableRepository.save(responsable));
    }

    private void mapToEntity(Responsable responsable, ResponsableDTO.Request request, Persona persona, Cargo cargo,
            Oficina oficina) {
        responsable.setPersona(persona);
        responsable.setCargo(cargo);
        responsable.setOficina(oficina);
        if (request.getFechaAsignacion() != null) {
            responsable.setFechaAsignacion(request.getFechaAsignacion());
        }
        if (request.getFechaFinAsignacion() != null) {
            responsable.setFechaFinAsignacion(request.getFechaFinAsignacion());
        }
        if (request.getEstadoResponsable() != null) {
            responsable.setEstadoResponsable(request.getEstadoResponsable());
        } else if (responsable.getIdResponsable() == null) {
            responsable.setEstadoResponsable(EstadoResponsable.ACTIVO);
        }
    }

    private ResponsableDTO.Response convertToDto(Responsable responsable) {
        return new ResponsableDTO.Response(
                responsable.getIdResponsable(),
                responsable.getPersona().getNombres() + " " + responsable.getPersona().getApellidos(),
                responsable.getPersona().getDni(),
                responsable.getCargo().getNombreCargo(),
                responsable.getOficina() != null ? responsable.getOficina().getNombreOficina() : null,
                responsable.getFechaAsignacion(),
                responsable.getFechaFinAsignacion(),
                responsable.getEstadoResponsable());
    }
}

package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.EspecialidadTecnico;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Tecnico;
import pe.edu.unasam.activos.modules.personas.dto.TecnicoDTO;
import pe.edu.unasam.activos.modules.personas.repository.EspecialidadTecnicoRepository;
import pe.edu.unasam.activos.modules.personas.repository.TecnicoRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TecnicoService {
    private final TecnicoRepository tecnicoRepository;
    private final PersonaService personaService;
    private final EspecialidadTecnicoRepository especialidadRepository;

    @Transactional(readOnly = true)
    public Page<TecnicoDTO.Response> getAllTecnicos(String query, EstadoTecnico estado, Pageable pageable) {
        return tecnicoRepository.findAllWithFilters(query, estado, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public TecnicoDTO.Response getTecnicoById(Integer id) {
        return tecnicoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Técnico no encontrado con ID: " + id));
    }

    public TecnicoDTO.Response createTecnico(TecnicoDTO.Request request) {
        if (tecnicoRepository.existsByPersona_Dni(request.getDniPersona())) {
            throw new BusinessException("La persona con DNI " + request.getDniPersona() + " ya es un técnico.");
        }

        Persona persona = personaService.findOrCreatePersona(request.getDniPersona(), request.getNombres(),
                request.getApellidos(), request.getEmail(), request.getTelefono(), request.getDireccion(),
                request.getGenero());



        EspecialidadTecnico especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new NotFoundException(
                        "Especialidad no encontrada con ID: " + request.getIdEspecialidad()));

        Tecnico tecnico = new Tecnico();
        tecnico.setPersona(persona);
        tecnico.setEspecialidadTecnico(especialidad);
        tecnico.setEstadoTecnico(EstadoTecnico.ACTIVO);

        return convertToDto(tecnicoRepository.save(tecnico));
    }

    public TecnicoDTO.Response updateTecnico(Integer id, TecnicoDTO.Request request) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Técnico no encontrado con ID: " + id));

        // Actualizar datos de la persona
        Persona persona = tecnico.getPersona();
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setEmail(request.getEmail());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
        persona.setGenero(request.getGenero());

        EspecialidadTecnico especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new NotFoundException(
                        "Especialidad no encontrada con ID: " + request.getIdEspecialidad()));

        tecnico.setEspecialidadTecnico(especialidad);
        if (request.getEstadoTecnico() != null) {
            tecnico.setEstadoTecnico(request.getEstadoTecnico());
        }

        return convertToDto(tecnicoRepository.save(tecnico));
    }

    public void deleteTecnico(Integer id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new NotFoundException("Técnico no encontrado con ID: " + id);
        }
        tecnicoRepository.deleteById(id);
    }

    public void toggleStatus(Integer id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Técnico no encontrado con ID: " + id));

        EstadoTecnico estado = tecnico.getEstadoTecnico();
        if (estado == EstadoTecnico.ACTIVO) {
            tecnico.setEstadoTecnico(EstadoTecnico.INACTIVO);
        } else if (estado == EstadoTecnico.INACTIVO) {
            tecnico.setEstadoTecnico(EstadoTecnico.ACTIVO);
        } else {
            // Para valores nulos/desconocidos, activar
            tecnico.setEstadoTecnico(EstadoTecnico.ACTIVO);
        }
        tecnicoRepository.save(tecnico);
    }

    private TecnicoDTO.Response convertToDto(Tecnico tecnico) {
        if (tecnico == null)
            return null;

        TecnicoDTO.Response.ResponseBuilder builder = TecnicoDTO.Response.builder();
        builder.idTecnico(tecnico.getIdTecnico())
                .estadoTecnico(tecnico.getEstadoTecnico());

        Optional.ofNullable(tecnico.getPersona()).ifPresent(p -> builder
                .dniPersona(p.getDni())
                .nombrePersona(p.getNombres() + " " + p.getApellidos())
                .emailPersona(p.getEmail())
                .telefonoPersona(p.getTelefono())
                .direccionPersona(p.getDireccion())
                .generoPersona(p.getGenero())
                // Para edición
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .email(p.getEmail())
                .telefono(p.getTelefono())
                .direccion(p.getDireccion())
                .genero(p.getGenero()));

        Optional.ofNullable(tecnico.getEspecialidadTecnico()).ifPresent(e -> builder
                .idEspecialidad(e.getIdEspecialidadTecnico())
                .nombreEspecialidad(e.getNombreEspecialidad()));

        return builder.build();
    }
}

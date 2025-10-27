package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoPersona;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.dto.PersonaDTO;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    // ==================== BÚSQUEDAS PARA USUARIOS ====================

    /**
     * Busca una persona por DNI y verifica si ya tiene usuario asignado
     * Exclusivo para el formulario de creación de usuarios
     */
    @Transactional(readOnly = true)
    public Optional<PersonaDTO.PersonaUsuarioResponse> findPersonaParaUsuario(String dni) {
        Optional<Persona> personaOpt = personaRepository.findById(dni);
        if (personaOpt.isEmpty()) {
            return Optional.empty();
        }

        Persona persona = personaOpt.get();
        boolean tieneUsuario = usuarioRepository.existsByPersona_Dni(dni);

        var response = PersonaDTO.PersonaUsuarioResponse.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero() != null ? persona.getGenero().name() : null)
                .tieneUsuario(tieneUsuario)
                .exists(true)
                .build();

        return Optional.of(response);
    }

    /**
     * Lista paginada de personas sin usuario asignado
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> findPersonasSinUsuarioPaginado(String query, Pageable pageable) {
        String searchTerm = (query == null) ? "" : query;
        return personaRepository.findPersonasSinUsuarioPaginado(searchTerm, pageable)
                .map(this::convertToDto);
    }

    // ==================== BÚSQUEDAS PARA RESPONSABLES ====================

    /**
     * Busca una persona por DNI y verifica si ya es responsable
     * Exclusivo para el formulario de asignación de responsables
     */
    @Transactional(readOnly = true)
    public Optional<PersonaDTO.PersonaResponsableResponse> findPersonaParaResponsable(String dni) {
        Optional<Persona> personaOpt = personaRepository.findById(dni);
        if (personaOpt.isEmpty()) {
            return Optional.empty();
        }

        Persona persona = personaOpt.get();

        boolean esResponsable = false; // Ajustar si cuentas con ResponsableRepository
        String departamento = null; // Ajustar para traer el departamento actual

        var response = PersonaDTO.PersonaResponsableResponse.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero() != null ? persona.getGenero().name() : null)
                .esResponsable(esResponsable)
                .departamentoActual(departamento)
                .build();

        return Optional.of(response);
    }

    /**
     * Lista paginada de personas sin responsable asignado
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> findPersonasSinResponsablePaginado(String query, Pageable pageable) {
        String searchTerm = (query == null) ? "" : query;
        return personaRepository.findPersonasSinResponsablePaginado(searchTerm, pageable)
                .map(this::convertToDto);
    }

    // ==================== BÚSQUEDAS PARA TÉCNICOS ====================

    /**
     * Busca una persona por DNI y verifica si ya es técnico
     * Exclusivo para el formulario de asignación de técnicos
     */
    @Transactional(readOnly = true)
    public Optional<PersonaDTO.PersonaTecnicoResponse> findPersonaParaTecnico(String dni) {
        Optional<Persona> personaOpt = personaRepository.findById(dni);
        if (personaOpt.isEmpty()) {
            return Optional.empty();
        }

        Persona persona = personaOpt.get();

        boolean esTecnico = false;
        String especialidad = null;

        var response = PersonaDTO.PersonaTecnicoResponse.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero() != null ? persona.getGenero().name() : null)
                .esTecnico(esTecnico)
                .especialidadActual(especialidad)
                .build();

        return Optional.of(response);
    }

    /**
     * Lista paginada de personas sin técnico asignado
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> findPersonasSinTecnicoPaginado(String query, Pageable pageable) {
        String searchTerm = (query == null) ? "" : query;
        return personaRepository.findPersonasSinTecnicoPaginado(searchTerm, pageable)
                .map(this::convertToDto);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    @Transactional
    public Persona findOrCreatePersona(UsuarioDTO.Request request) {
        return personaRepository.findById(request.getDniPersona())
                .orElseGet(() -> createNewPersonaFromUsuarioRequest(request));
    }

    private Persona createNewPersonaFromUsuarioRequest(UsuarioDTO.Request request) {
        if (!StringUtils.hasText(request.getNombres()) || !StringUtils.hasText(request.getApellidos())) {
            throw new BusinessException("Nombres y apellidos son requeridos para crear una nueva persona.");
        }

        Persona nuevaPersona = Persona.builder()
                .dni(request.getDniPersona())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .genero(request.getGenero())
                .estadoPersona(EstadoPersona.ACTIVO)
                .build();
        return personaRepository.save(nuevaPersona);
    }

    private PersonaDTO.Response convertToDto(Persona persona) {
        return PersonaDTO.Response.builder()
                .dni(persona.getDni())
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero())
                .estadoPersona(persona.getEstadoPersona())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> getAllPersonas(String query, Pageable pageable) {
        String processedQuery = (query != null && !query.trim().isEmpty()) ? query.trim().toLowerCase() : null;
        return personaRepository.findAllWithFilters(processedQuery, pageable)
                .map(this::convertToDto);
    }
}

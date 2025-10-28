package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoPersona;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.dto.PersonaDTO;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.repository.TecnicoRepository;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.personas.repository.ResponsableRepository;

import java.util.Optional;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ResponsableRepository responsableRepository;

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

    /**
     * Lista general de personas con búsqueda por término y filtro opcional por estado
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> getAllPersonas(String query, pe.edu.unasam.activos.common.enums.EstadoPersona estado, Pageable pageable) {
        String searchTerm = (query == null) ? "" : query;
        return personaRepository.findAllWithFilters(searchTerm, estado, pageable)
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
        var responsables = responsableRepository.findByPersona_Dni(dni);
        boolean esResponsable = !responsables.isEmpty();
        String departamento = responsables.stream()
                .filter(r -> r.getEstadoResponsable() == EstadoResponsable.ACTIVO)
                .map(r -> r.getOficina())
                .filter(Objects::nonNull)
                .map(of -> of.getDepartamento())
                .filter(Objects::nonNull)
                .map(dep -> dep.getNombreDepartamento())
                .findFirst()
                .orElse(null);

        var response = PersonaDTO.PersonaResponsableResponse.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero() != null ? persona.getGenero().name() : null)
                .esResponsable(esResponsable)
                .oficinaActual(departamento)
                .exists(true)
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
        boolean esTecnico = tecnicoRepository.existsByPersona_Dni(dni);
        String especialidadActual = tecnicoRepository.findByPersona_Dni(dni).stream()
                .filter(t -> t.getEstadoTecnico() == EstadoTecnico.ACTIVO)
                .map(t -> t.getEspecialidadTecnico())
                .filter(Objects::nonNull)
                .map(e -> e.getNombreEspecialidad())
                .findFirst()
                .orElse(null);

        var response = PersonaDTO.PersonaTecnicoResponse.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .genero(persona.getGenero() != null ? persona.getGenero().name() : null)
                .esTecnico(esTecnico)
                .especialidadActual(especialidadActual)
                .exists(true)
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
    public Persona findOrCreatePersona(String dni, String nombres, String apellidos, String email, String telefono, String direccion, Genero genero) {
        return personaRepository.findById(dni)
                .orElseGet(() -> createNewPersona(dni, nombres, apellidos, email, telefono, direccion, genero));
    }

    private Persona createNewPersona(String dni, String nombres, String apellidos, String email, String telefono, String direccion, Genero genero) {
        if (!StringUtils.hasText(nombres) || !StringUtils.hasText(apellidos)) {
            throw new BusinessException("Nombres y apellidos son requeridos para crear una nueva persona.");
        }

        Persona nuevaPersona = Persona.builder()
                .dni(dni)
                .nombres(nombres)
                .apellidos(apellidos)
                .email(email)
                .telefono(telefono)
                .direccion(direccion)
                .genero(genero)
                .estadoPersona(EstadoPersona.ACTIVO)
                .build();
        return personaRepository.save(nuevaPersona);
    }

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

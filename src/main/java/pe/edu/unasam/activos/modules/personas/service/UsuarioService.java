package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.common.enums.EstadoPersona;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;


import java.util.Optional;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO.Response> getAllUsuarios(String query, EstadoUsuario estado, Pageable pageable) {
        try {
            String processedQuery = StringUtils.hasText(query) ? query.trim().toLowerCase() : null;
            Page<Usuario> usuarios = usuarioRepository.findAllWithFilters(processedQuery, estado, pageable);
            return usuarios.map(this::convertToDto);
        } catch (DataAccessException e) {
            throw new BusinessException("Error de acceso a datos al obtener usuarios: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public UsuarioDTO.Response getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
    }

    public UsuarioDTO.Response createUsuario(UsuarioDTO.Request request) {
        // Validación específica para la creación: la contraseña es obligatoria.
        if (!StringUtils.hasText(request.getContrasena())) {
            throw new BusinessException("La contraseña es obligatoria para crear un usuario.");
        }

        // Validaciones comunes y obtención de entidades relacionadas
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + request.getIdRol()));

        // Validar unicidad del nombre de usuario
        if (usuarioRepository.existsByUsuario(request.getUsuario())) {
            throw new BusinessException("El nombre de usuario '" + request.getUsuario() + "' ya está en uso.");
        }

        Persona persona = findOrCreatePersona(request);
        // Validar que la persona no tenga ya un usuario
        if (usuarioRepository.existsByPersona_Dni(persona.getDni())) {
            throw new BusinessException(
                    "La persona con DNI '" + persona.getDni() + "' ya tiene un usuario asignado.");
        }

        // Mapear DTO a Entidad y encriptar contraseña
        Usuario usuario = new Usuario();
        mapToEntity(usuario, request, persona, rol); // Mapeo de campos comunes
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));

        // Guardar y convertir a DTO de respuesta
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDto(savedUsuario);
    }

    public UsuarioDTO.Response updateUsuario(Integer id, UsuarioDTO.Request request) {
        // Obtener entidad a actualizar
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        // Validaciones comunes y obtención de entidades relacionadas
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + request.getIdRol()));

        validateUniqueUsernameOnUpdate(request.getUsuario(), id);

        // Mapear DTO a Entidad y encriptar contraseña si se proporciona una nueva
        mapToEntity(usuario, request, usuario.getPersona(), rol); // Reutilizamos el mapeo
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        // Guardar y convertir a DTO de respuesta
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return convertToDto(updatedUsuario);
    }

    private Persona findOrCreatePersona(UsuarioDTO.Request request) {
        return personaRepository.findById(request.getDniPersona())
                .orElseGet(() -> createNewPersona(request));
    }

    private Persona createNewPersona(UsuarioDTO.Request request) {
        if (!StringUtils.hasText(request.getNombres()) || !StringUtils.hasText(request.getApellidos())) {
            throw new BusinessException("Nombres y apellidos son requeridos para crear una nueva persona.");
        }

        Persona nuevaPersona = Persona.builder()
                .dni(request.getDniPersona())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .estadoPersona(EstadoPersona.ACTIVO)
                .build();
        return personaRepository.save(nuevaPersona);
    }

    public void deleteUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        // El @SQLDelete se encargará del borrado lógico
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void toggleStatus(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        EstadoUsuario nuevoEstado = (usuario.getEstadoUsuarios() == EstadoUsuario.ACTIVO) ? EstadoUsuario.INACTIVO
                : EstadoUsuario.ACTIVO;
        usuario.setEstadoUsuarios(nuevoEstado);

        usuarioRepository.save(usuario);
    }

    private void validateUniqueUsernameOnUpdate(String username, Integer excludeId) {
        usuarioRepository.findByUsuario(username).ifPresent(existingUser -> {
            if (!existingUser.getIdUsuario().equals(excludeId)) {
                throw new BusinessException("El nombre de usuario '" + username + "' ya está en uso.");
            }
        });
    }

    private void mapToEntity(Usuario usuario, UsuarioDTO.Request dto, Persona persona, Rol rol) {
        usuario.setUsuario(dto.getUsuario());
        usuario.setEstadoUsuarios(dto.getEstadoUsuarios());
        usuario.setPersona(persona);
        usuario.setRol(rol);
    }

    private UsuarioDTO.Response convertToDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO.Response.ResponseBuilder builder = UsuarioDTO.Response.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .ultimoAcceso(usuario.getUltimoAcceso() != null
                        ? usuario.getUltimoAcceso().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        : "-")
                .intentosFallidos(usuario.getIntentosFallidos())
                .bloqueadoHasta(usuario.getBloqueadoHasta())
                .estadoUsuarios(usuario.getEstadoUsuarios());

        Optional<Persona> personaOpt = Optional.ofNullable(usuario.getPersona());
        builder.nombrePersona(personaOpt.map(p -> p.getNombres() + " " + p.getApellidos()).orElse("-"))
                .dniPersona(personaOpt.map(Persona::getDni).orElse("-"))
                .emailPersona(personaOpt.map(Persona::getEmail).filter(StringUtils::hasText).orElse("-"))
                .telefonoPersona(personaOpt.map(Persona::getTelefono).filter(StringUtils::hasText).orElse("-"))
                .direccionPersona(personaOpt.map(Persona::getDireccion).filter(StringUtils::hasText).orElse("-"))
                .generoPersona(personaOpt.map(Persona::getGenero).orElse(null));

        Optional<Rol> rolOpt = Optional.ofNullable(usuario.getRol());
        builder.nombreRol(rolOpt.map(Rol::getNombreRol).orElse("N/A"))
               .idRol(rolOpt.map(Rol::getIdRol).orElse(null));

        return builder.build();
    }
}
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
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;
import pe.edu.unasam.activos.modules.sistema.repository.SesionUsuarioRepository;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PersonaService personaService;
    private final PasswordEncoder passwordEncoder;
    private final SesionUsuarioRepository sesionUsuarioRepository;

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

        Persona persona = personaService.findOrCreatePersona(request);
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

        // Actualizar datos personales asociados
        Persona persona = usuario.getPersona();
        if (persona == null) {
            persona = personaService.findOrCreatePersona(request);
            usuario.setPersona(persona);
        } else {
            persona.setNombres(request.getNombres());
            persona.setApellidos(request.getApellidos());
            persona.setEmail(request.getEmail());
            persona.setTelefono(request.getTelefono());
            persona.setDireccion(request.getDireccion());
            persona.setGenero(request.getGenero());
        }

        // Mapear DTO a Entidad y encriptar contraseña si se proporciona una nueva
        mapToEntity(usuario, request, persona, rol); // Reutilizamos el mapeo
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        // Guardar y convertir a DTO de respuesta
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return convertToDto(updatedUsuario);
    }

    public void deleteUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario no encontrado con ID: " + id);
        }
        // El @SQLDelete se encargará del borrado lógico
        usuarioRepository.deleteById(id);
    }

    public void deleteUsuarioSafely(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        // 1) Prevenir auto-eliminación del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentUsername = authentication.getName();
            usuarioRepository.findByUsuario(currentUsername).ifPresent(currentUser -> {
                if (currentUser.getIdUsuario().equals(id)) {
                    throw new BusinessException("No puede eliminar su propia cuenta.");
                }
            });
        }

        // 2) Evitar eliminar el último administrador
        rolRepository.findByNombreRol("ADMIN_GENERAL").ifPresent(adminRol -> {
            if (usuario.getRol() != null && adminRol.getIdRol().equals(usuario.getRol().getIdRol())) {
                long totalAdmins = usuarioRepository.countByRol(adminRol);
                if (totalAdmins <= 1) {
                    throw new BusinessException("No se puede eliminar el último administrador del sistema.");
                }
            }
        });

        // 3) Bloquear eliminación si hay sesiones activas asociadas
        var sesionesActivas = sesionUsuarioRepository
                .findByUsuario_IdUsuarioAndEstadoSesion(id, EstadoSesion.ACTIVA);
        if (!sesionesActivas.isEmpty()) {
            throw new BusinessException("El usuario tiene sesiones activas. Cierre las sesiones antes de eliminar.");
        }

        // 4) Soft delete mediante @SQLDelete
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public void toggleStatus(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        if (usuario.getEstadoUsuarios() == EstadoUsuario.ACTIVO) {
            usuario.setEstadoUsuarios(EstadoUsuario.BLOQUEADO);
        } else if (usuario.getEstadoUsuarios() == EstadoUsuario.BLOQUEADO) {
            usuario.setEstadoUsuarios(EstadoUsuario.PENDIENTE);
        } else {
            // De PENDIENTE (u otros) a ACTIVO
            usuario.setEstadoUsuarios(EstadoUsuario.ACTIVO);
            usuario.setIntentosFallidos(0);
            usuario.setBloqueadoHasta(null);
        }

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
        if (dto.getEstadoUsuarios() != null) {
            usuario.setEstadoUsuarios(dto.getEstadoUsuarios());
        } else if (usuario.getEstadoUsuarios() == null) {
            usuario.setEstadoUsuarios(EstadoUsuario.ACTIVO);
        }
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
                .generoPersona(personaOpt.map(Persona::getGenero).orElse(null))
                // Campos extra para edición (coinciden con Request)
                .nombres(personaOpt.map(Persona::getNombres).orElse(null))
                .apellidos(personaOpt.map(Persona::getApellidos).orElse(null))
                .email(personaOpt.map(Persona::getEmail).orElse(null))
                .telefono(personaOpt.map(Persona::getTelefono).orElse(null))
                .direccion(personaOpt.map(Persona::getDireccion).orElse(null))
                .genero(personaOpt.map(Persona::getGenero).orElse(null));

        Optional<Rol> rolOpt = Optional.ofNullable(usuario.getRol());
        builder.nombreRol(rolOpt.map(Rol::getNombreRol).orElse("N/A"))
                .idRol(rolOpt.map(Rol::getIdRol).orElse(null));

        return builder.build();
    }

    // esto de aqui abajo nose si deve ir
    public String generateTemporaryPassword(Integer id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
        return generateRandomPassword(12);
    }

    private String generateRandomPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String specials = "!@#$%^&*()-_=+[]{}|;:,.<>/?";
        String all = upper + lower + digits + specials;

        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        // Garantizar al menos un carácter de cada tipo
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(specials.charAt(random.nextInt(specials.length())));

        for (int i = sb.length(); i < length; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }

        return sb.toString();
    }
}

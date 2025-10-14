package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO.Response> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioDTO.Response> getUsuarioById(Integer id) {
        return usuarioRepository.findById(id).map(this::convertToDto);
    }

    public UsuarioDTO.Response createUsuario(UsuarioDTO.Request request) {
        var persona = personaRepository.findById(request.getDocumentoPersona())
                .orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        var rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsuario(request.getUsuario());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setDebeCambiarPassword(request.getDebeCambiarPassword());
        usuario.setEstadoUsuarios(request.getEstadoUsuarios());
        usuario.setPersona(persona);
        usuario.setRol(rol);

        return convertToDto(usuarioRepository.save(usuario));
    }

    public UsuarioDTO.Response updateUsuario(Integer id, UsuarioDTO.Request request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        var persona = personaRepository.findById(request.getDocumentoPersona())
                .orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        var rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));

        usuario.setUsuario(request.getUsuario());
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }
        usuario.setDebeCambiarPassword(request.getDebeCambiarPassword());
        usuario.setEstadoUsuarios(request.getEstadoUsuarios());
        usuario.setPersona(persona);
        usuario.setRol(rol);

        return convertToDto(usuarioRepository.save(usuario));
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO.Response convertToDto(Usuario usuario) {
        return UsuarioDTO.Response.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .ultimoAcceso(usuario.getUltimoAcceso() != null ? usuario.getUltimoAcceso().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "-")
                .intentosFallidos(usuario.getIntentosFallidos())
                .bloqueadoHasta(usuario.getBloqueadoHasta())
                .debeCambiarPassword(usuario.getDebeCambiarPassword())
                .estadoUsuarios(usuario.getEstadoUsuarios())
                .nombrePersona(usuario.getPersona() != null ? usuario.getPersona().getNombres() + " " + usuario.getPersona().getApellidos() : "N/A")
                .documentoPersona(usuario.getPersona() != null ? usuario.getPersona().getDocumento() : "N/A")
                .nombreRol(usuario.getRol() != null ? usuario.getRol().getNombreRol() : "N/A")
                .idRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null)
                .build();
    }
}
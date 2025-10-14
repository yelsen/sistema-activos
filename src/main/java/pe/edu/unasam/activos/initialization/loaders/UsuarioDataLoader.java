package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;

import java.time.LocalTime;

@Component
@Order(8)
@RequiredArgsConstructor
public class UsuarioDataLoader extends AbstractDataLoader {
    
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    protected String getLoaderName() {
        return "Usuarios del Sistema";
    }
    
    @Override
    protected boolean shouldLoad() {
        return usuarioRepository.count() == 0;
    }
    
    @Override
    protected void loadData() {
        Persona adminPersona = personaRepository.findById("12345678")
                .orElseThrow(() -> new RuntimeException("Persona con documento 12345678 no encontrada"));
        Persona userPersona = personaRepository.findById("87654321")
                .orElseThrow(() -> new RuntimeException("Persona con documento 87654321 no encontrada"));
        Persona gestorPersona = personaRepository.findById("11223344")
                .orElseThrow(() -> new RuntimeException("Persona con documento 11223344 no encontrada"));
        
        Rol rolAdmin = rolRepository.findByNombreRol("ADMIN_GENERAL")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN_GENERAL no encontrado"));
        Rol rolEditor = rolRepository.findByNombreRol("EDITOR")
                .orElseThrow(() -> new RuntimeException("Rol EDITOR no encontrado"));
        Rol rolUsuario = rolRepository.findByNombreRol("USUARIO_CONSULTA")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO_CONSULTA no encontrado"));
        
        Usuario[] usuarios = {
            Usuario.builder()
                .usuario("superadmin")
                .contrasena(passwordEncoder.encode("admin123"))
                .rol(rolAdmin)
                .ultimoAcceso(LocalTime.now())
                .intentosFallidos(0)
                .bloqueadoHasta(null)
                .debeCambiarPassword(false)
                .estadoUsuarios(EstadoUsuario.ACTIVO)
                .persona(adminPersona)
                .build(),
            
            Usuario.builder()
                .usuario("jgarcia")
                .contrasena(passwordEncoder.encode("user123"))
                .rol(rolUsuario)
                .ultimoAcceso(LocalTime.now())
                .intentosFallidos(0)
                .bloqueadoHasta(null)
                .debeCambiarPassword(false)
                .estadoUsuarios(EstadoUsuario.ACTIVO)
                .persona(userPersona)
                .build(),
            
            Usuario.builder()
                .usuario("mrodriguez")
                .contrasena(passwordEncoder.encode("gestor123"))
                .rol(rolEditor)
                .ultimoAcceso(LocalTime.now())
                .intentosFallidos(0)
                .bloqueadoHasta(null)
                .debeCambiarPassword(false)
                .estadoUsuarios(EstadoUsuario.ACTIVO)
                .persona(gestorPersona)
                .build()
        };
        
        usuarioRepository.saveAll(java.util.Arrays.asList(usuarios));
    }
}

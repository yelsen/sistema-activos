package pe.edu.unasam.activos.modules.sistema.service;

import pe.edu.unasam.activos.modules.sistema.domain.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findByUsername(String username);

    Usuario save(Usuario usuario);
}

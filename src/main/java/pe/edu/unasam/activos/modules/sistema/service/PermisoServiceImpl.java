package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;

import java.util.List;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermisoServiceImpl implements PermisoService {

    private final UsuarioRepository usuarioRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;

    @Override
    public boolean usuarioTienePermiso(Integer usuarioId, String codigoPermiso, String tipoAcceso) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || usuario.getRol() == null) {
            return false;
        }

        Permiso permiso = permisoRepository.findByCodigoPermiso(codigoPermiso).orElse(null);
        if (permiso == null || permiso.getModuloSistema() == null || permiso.getAccion() == null) {
            return false;
        }

        if (!permiso.getAccion().getCodigoAccion().equalsIgnoreCase(tipoAcceso)) {
            return false;
        }

        List<RolPermiso> permisosDelRol = rolPermisoRepository.findByRol_IdRol(usuario.getRol().getIdRol());

        return permisosDelRol.stream()
                .filter(rp -> rp.getPermiso().getIdPermiso().equals(permiso.getIdPermiso()))
                .anyMatch(RolPermiso::isPermitido);
    }

    @Override
    public List<RolPermiso> getPermisosPorUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuario -> {
                    if (usuario.getRol() != null) {
                        return rolPermisoRepository.findByRol_IdRol(usuario.getRol().getIdRol());
                    }
                    return Collections.<RolPermiso>emptyList();
                })
                .orElse(Collections.emptyList());
    }
}

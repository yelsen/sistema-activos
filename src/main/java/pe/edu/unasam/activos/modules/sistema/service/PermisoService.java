package pe.edu.unasam.activos.modules.sistema.service;

import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;

import java.util.List;

public interface PermisoService {

    boolean usuarioTienePermiso(Integer usuarioId, String codigoPermiso, String tipoAcceso);

    List<RolPermiso> getPermisosPorUsuario(Integer idUsuario);
}

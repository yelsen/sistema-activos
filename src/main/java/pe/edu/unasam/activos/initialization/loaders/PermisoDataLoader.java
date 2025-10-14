package pe.edu.unasam.activos.initialization.loaders;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;

import java.util.*;

@Component
@Order(4)
@RequiredArgsConstructor
public class PermisoDataLoader extends AbstractDataLoader {

    private final PermisoRepository permisoRepository;
    private final ModuloSistemaRepository moduloSistemaRepository;
    private final AccionRepository accionRepository;
    private final PermisoService permisoService;

    @Override
    protected String getLoaderName() {
        return "Permisos del Sistema";
    }

    @Override
    protected boolean shouldLoad() {
        long modulosCount = moduloSistemaRepository.count();
        long accionesCount = accionRepository.count();
        long permisosEsperados = modulosCount * accionesCount;
        return permisoRepository.count() != permisosEsperados;
    }

    @Override
    @Transactional
    protected void loadData() {
        List<ModuloSistema> modulos = moduloSistemaRepository.findAll();
        List<Accion> acciones = accionRepository.findAll();
        List<Permiso> permisosAGenerar = new ArrayList<>();

        for (ModuloSistema modulo : modulos) {
            for (Accion accion : acciones) {
                String codigoPermiso = permisoService.generarCodigoPermiso(modulo.getNombreModulo(),
                        accion.getCodigoAccion());

                if (!permisoRepository.existsByCodigoPermiso(codigoPermiso)) {
                    Permiso nuevoPermiso = Permiso.builder()
                            .codigoPermiso(codigoPermiso)
                            .nombrePermiso(permisoService.generarNombrePermiso(modulo.getNombreModulo(),
                                    accion.getNombreAccion()))
                            .descripcionPermiso(permisoService.generarDescripcionPermiso(modulo.getNombreModulo(),
                                    accion.getNombreAccion()))
                            .moduloSistema(modulo)
                            .accion(accion)
                            .build();
                    permisosAGenerar.add(nuevoPermiso);
                }
            }
        }

        if (!permisosAGenerar.isEmpty()) {
            permisoRepository.saveAll(permisosAGenerar);
        }
    }
}

package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;

@Component
@Order(4)
@RequiredArgsConstructor
public class RolDataLoader extends AbstractDataLoader {

    private final RolRepository rolRepository;

    @Override
    protected String getLoaderName() {
        return "Roles del Sistema";
    }

    @Override
    protected boolean shouldLoad() {
        return rolRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        Rol[] roles = {
                Rol.builder()
                        .nombreRol("ADMIN_GENERAL")
                        .nivelAcceso(1)
                        .descripcionRol(
                                "Acceso completo a todas las funcionalidades del sistema, incluyendo configuración y seguridad.")
                        .colorRol("#dc3545")
                        .estadoRol(EstadoRol.ACTIVO)
                        .build(),

                Rol.builder()
                        .nombreRol("EDITOR")
                        .nivelAcceso(2)
                        .descripcionRol(
                                "Permite crear y modificar la mayoría de los registros del sistema, pero con acceso limitado a la configuración de seguridad.")
                        .colorRol("#0d6efd")
                        .estadoRol(EstadoRol.ACTIVO)
                        .build(),

                Rol.builder()
                        .nombreRol("USUARIO_CONSULTA")
                        .nivelAcceso(3)
                        .descripcionRol(
                                "Rol de solo lectura. Permite ver la información del sistema pero no puede realizar cambios.")
                        .colorRol("#198754")
                        .estadoRol(EstadoRol.ACTIVO)
                        .build()
        };

        for (Rol rol : roles) {
            rolRepository.save(rol);
        }
    }
}

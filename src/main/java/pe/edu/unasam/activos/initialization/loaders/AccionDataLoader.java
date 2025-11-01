package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;

@Component
@Order(3) // Debe ejecutarse antes que los permisos.
@RequiredArgsConstructor
public class AccionDataLoader extends AbstractDataLoader {

        private final AccionRepository accionRepository;

        @Override
        protected String getLoaderName() {
                return "Acciones del Sistema";
        }

        @Override
        protected boolean shouldLoad() {
                return accionRepository.count() == 0;
        }

        @Override
        protected void loadData() {
                Accion[] acciones = {
                                Accion.builder()
                                                .nombreAccion("Acceder")
                                                .codigoAccion("ACCEDER")
                                                .descripcionAccion("Permite acceder a un módulo o dashboard")
                                                .build(),

                                Accion.builder()
                                                .nombreAccion("Ver")
                                                .codigoAccion("VER")
                                                .descripcionAccion("Permite ver el detalle de un elemento")
                                                .build(),

                                Accion.builder()
                                                .nombreAccion("Crear")
                                                .codigoAccion("CREAR")
                                                .descripcionAccion("Permite añadir nuevos elementos")
                                                .build(),

                                Accion.builder()
                                                .nombreAccion("Editar")
                                                .codigoAccion("EDITAR")
                                                .descripcionAccion("Permite modificar elementos existentes")
                                                .build(),

                                Accion.builder()
                                                .nombreAccion("Eliminar")
                                                .codigoAccion("ELIMINAR")
                                                .descripcionAccion("Permite borrar elementos")
                                                .build(),

                                Accion.builder()
                                                .nombreAccion("Generar")
                                                .codigoAccion("GENERAR")
                                                .descripcionAccion("Permite generar reportes o archivos")
                                                .build(),

                };

                accionRepository.saveAll(java.util.Arrays.asList(acciones));
        }
}

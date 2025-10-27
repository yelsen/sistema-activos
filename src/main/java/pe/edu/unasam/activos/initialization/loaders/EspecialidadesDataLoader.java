package pe.edu.unasam.activos.initialization.loaders;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.personas.domain.EspecialidadTecnico;
import pe.edu.unasam.activos.modules.personas.repository.EspecialidadTecnicoRepository;

import java.util.Arrays;

@Component
@Order(10)
@RequiredArgsConstructor
public class EspecialidadesDataLoader extends AbstractDataLoader {

    private final EspecialidadTecnicoRepository especialidadRepository;

    @Override
    protected String getLoaderName() {
        return "Especialidades de Técnicos";
    }

    @Override
    protected boolean shouldLoad() {
        return especialidadRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        EspecialidadTecnico[] especialidades = {
                EspecialidadTecnico.builder().nombreEspecialidad("Soporte de Hardware").build(),
                EspecialidadTecnico.builder().nombreEspecialidad("Soporte de Software").build(),
                EspecialidadTecnico.builder().nombreEspecialidad("Seguridad Informática").build(),
                EspecialidadTecnico.builder().nombreEspecialidad("Redes y Telecomunicaciones").build(),
                EspecialidadTecnico.builder().nombreEspecialidad("Administración de Servidores").build()
        };
        especialidadRepository.saveAll(Arrays.asList(especialidades));
    }
}

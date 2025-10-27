package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Departamento;
import pe.edu.unasam.activos.modules.ubicaciones.repository.DepartamentoRepository;

import java.util.Arrays;

@Component
@Order(12)
@RequiredArgsConstructor
public class DepartamentosDataLoader extends AbstractDataLoader {

    private final DepartamentoRepository departamentoRepository;

    @Override
    protected String getLoaderName() {
        return "Departamentos y Facultades";
    }

    @Override
    protected boolean shouldLoad() {
        return departamentoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        Departamento[] departamentos = {
                // ÓRGANOS DE GOBIERNO
                Departamento.builder().nombreDepartamento("Asamblea Universitaria").build(),
                Departamento.builder().nombreDepartamento("Consejo Universitario").build(),
                Departamento.builder().nombreDepartamento("Rectorado").build(),

                // VICERRECTORADOS
                Departamento.builder().nombreDepartamento("Vicerrectorado Académico").build(),
                Departamento.builder().nombreDepartamento("Vicerrectorado de Investigación").build(),

                // ÓRGANOS DE CONTROL Y ASESORÍA
                Departamento.builder().nombreDepartamento("Oficina General de Control Institucional").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Asesoría Jurídica").build(),
                Departamento.builder().nombreDepartamento("Procuraduría Universitaria").build(),

                // SECRETARÍA GENERAL
                Departamento.builder().nombreDepartamento("Secretaría General").build(),

                // OFICINAS GENERALES DE APOYO
                Departamento.builder().nombreDepartamento("Oficina General de Planificación y Presupuesto").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Imagen Institucional").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Tecnologías de Información, Sistemas y Estadística").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Desarrollo Físico").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Pre Inversión").build(),

                // OFICINAS GENERALES ACADÉMICAS
                Departamento.builder().nombreDepartamento("Oficina General de Admisión").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Estudios").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Calidad Universitaria").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Servicios Académicos y Publicaciones").build(),
                Departamento.builder().nombreDepartamento("Oficina General de Responsabilidad Social Universitaria").build(),

                // DIRECCIÓN GENERAL DE ADMINISTRACIÓN
                Departamento.builder().nombreDepartamento("Dirección General de Administración").build(),
                Departamento.builder().nombreDepartamento("Dirección de Gestión Financiera").build(),
                Departamento.builder().nombreDepartamento("Dirección de Recursos Humanos").build(),
                Departamento.builder().nombreDepartamento("Dirección de Abastecimiento y Servicios Auxiliares").build(),
                Departamento.builder().nombreDepartamento("Dirección de Bienestar Universitario").build(),
                Departamento.builder().nombreDepartamento("Dirección de Gestión Ambiental y Bioseguridad").build(),

                // DIRECCIÓN GENERAL DE INVESTIGACIÓN
                Departamento.builder().nombreDepartamento("Dirección General de Institutos de Investigación y Experimentación").build(),

                // CENTROS ACADÉMICOS
                Departamento.builder().nombreDepartamento("Escuela de Post Grado").build(),
                Departamento.builder().nombreDepartamento("Centro de Idiomas").build(),
                Departamento.builder().nombreDepartamento("Centro Pre Universitario").build(),

                // FACULTADES (11 en total)
                Departamento.builder().nombreDepartamento("Facultad de Ciencias").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ciencias Agrarias").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ciencias del Ambiente").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ciencias Médicas").build(),
                Departamento.builder().nombreDepartamento("Facultad de Derecho y Ciencias Políticas").build(),
                Departamento.builder().nombreDepartamento("Facultad de Economía y Contabilidad").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ingeniería Civil").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ingeniería de Minas, Geología y Metalurgia").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ingeniería de Industrias Alimentarias").build(),
                Departamento.builder().nombreDepartamento("Facultad de Administración y Turismo").build(),
                Departamento.builder().nombreDepartamento("Facultad de Ciencias Sociales y Educación Comunicación").build()
        };
        departamentoRepository.saveAll(Arrays.asList(departamentos));
    }
}

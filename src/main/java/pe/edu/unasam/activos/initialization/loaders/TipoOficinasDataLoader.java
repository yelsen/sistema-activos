package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.ubicaciones.domain.TipoOficina;
import pe.edu.unasam.activos.modules.ubicaciones.repository.TipoOficinaRepository;

import java.util.Arrays;

@Component
@Order(13)
@RequiredArgsConstructor
public class TipoOficinasDataLoader extends AbstractDataLoader {

    private final TipoOficinaRepository tipoOficinaRepository;

    @Override
    protected String getLoaderName() {
        return "Tipos de Oficina";
    }

    @Override
    protected boolean shouldLoad() {
        return tipoOficinaRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        TipoOficina[] tipos = {
                // ========== ÓRGANOS DE GOBIERNO Y ALTA DIRECCIÓN ==========
                TipoOficina.builder().tipoOficina("Órgano de Gobierno").build(),
                TipoOficina.builder().tipoOficina("Rectorado").build(),
                TipoOficina.builder().tipoOficina("Vicerrectorado").build(),

                // ========== ÓRGANOS DE CONTROL Y ASESORÍA ==========
                TipoOficina.builder().tipoOficina("Oficina de Control").build(),
                TipoOficina.builder().tipoOficina("Oficina de Asesoría").build(),

                // ========== SECRETARÍA Y OFICINAS GENERALES ==========
                TipoOficina.builder().tipoOficina("Secretaría General").build(),
                TipoOficina.builder().tipoOficina("Oficina General").build(),

                // ========== DIRECCIONES ==========
                TipoOficina.builder().tipoOficina("Dirección General").build(),
                TipoOficina.builder().tipoOficina("Dirección").build(),

                // ========== UNIDADES OPERATIVAS ==========
                TipoOficina.builder().tipoOficina("Unidad").build(),

                // ========== FACULTADES Y ESCUELAS ==========
                TipoOficina.builder().tipoOficina("Decanato").build(),
                TipoOficina.builder().tipoOficina("Escuela Profesional").build(),
                TipoOficina.builder().tipoOficina("Escuela de Postgrado").build(),

                // ========== CENTROS ACADÉMICOS Y DE INVESTIGACIÓN ==========
                TipoOficina.builder().tipoOficina("Centro Académico").build(),
                TipoOficina.builder().tipoOficina("Centro de Investigación").build(),
                TipoOficina.builder().tipoOficina("Centro Experimental").build(),
                TipoOficina.builder().tipoOficina("Laboratorio").build(),

                // ========== CENTROS DE PRODUCCIÓN ==========
                TipoOficina.builder().tipoOficina("Centro de Producción").build(),

                // ========== OTROS ESPACIOS INSTITUCIONALES ==========
                TipoOficina.builder().tipoOficina("Auditorio").build(),
                TipoOficina.builder().tipoOficina("Almacén").build(),
                TipoOficina.builder().tipoOficina("Biblioteca").build()
        };

        tipoOficinaRepository.saveAll(Arrays.asList(tipos));
        System.out.println("✅ Se cargaron " + tipos.length + " tipos de oficina exitosamente.");
    }
}
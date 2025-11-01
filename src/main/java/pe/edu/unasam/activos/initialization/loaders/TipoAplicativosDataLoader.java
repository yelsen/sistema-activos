package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.aplicativos.domain.TipoAplicativo;
import pe.edu.unasam.activos.modules.aplicativos.repository.TipoAplicativoRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(21)
@RequiredArgsConstructor
public class TipoAplicativosDataLoader extends AbstractDataLoader {

    private final TipoAplicativoRepository tipoAplicativoRepository;

    @Override
    protected String getLoaderName() {
        return "Tipos de Aplicativo";
    }

    @Override
    protected boolean shouldLoad() {
        return tipoAplicativoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<TipoAplicativo> tipos = new ArrayList<>();

        tipos.add(TipoAplicativo.builder().tipoAplicativo("Sistema Operativo").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Base de Datos").requiereLicencia(false).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Ofimática").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Antivirus").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Navegador").requiereLicencia(false).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Utilidad").requiereLicencia(false).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Desarrollo").requiereLicencia(false).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Virtualización").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Seguridad").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Gestión Académica").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("ERP/Administrativo").requiereLicencia(true).build());
        tipos.add(TipoAplicativo.builder().tipoAplicativo("Middleware").requiereLicencia(false).build());

        tipoAplicativoRepository.saveAll(tipos);
    }
}
package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.Marca;
import pe.edu.unasam.activos.modules.equipos.repository.MarcaRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(23)
@RequiredArgsConstructor
public class MarcasDataLoader extends AbstractDataLoader {

    private final MarcaRepository marcaRepository;

    @Override
    protected String getLoaderName() {
        return "Marcas";
    }

    @Override
    protected boolean shouldLoad() {
        return marcaRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Marca> marcas = new ArrayList<>();

        // Marcas de Computadoras
        marcas.add(Marca.builder()
                .nombreMarca("Dell")
                .descripcionMarca("Fabricante estadounidense de computadoras y equipos empresariales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("HP")
                .descripcionMarca("Hewlett-Packard, líder en PCs y soluciones de impresión")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Lenovo")
                .descripcionMarca("Empresa china líder en computadoras personales y servidores")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Asus")
                .descripcionMarca("Fabricante taiwanés de placas madre y equipos informáticos")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Acer")
                .descripcionMarca("Marca taiwanesa de laptops y monitores")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Apple")
                .descripcionMarca("Fabricante de Mac, iPad y dispositivos premium")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("MSI")
                .descripcionMarca("Micro-Star International, especializada en gaming y workstations")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Toshiba")
                .descripcionMarca("Empresa japonesa de electrónica y computadoras")
                .build());

        // Marcas de Servidores
        marcas.add(Marca.builder()
                .nombreMarca("IBM")
                .descripcionMarca("International Business Machines, servidores empresariales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Supermicro")
                .descripcionMarca("Especialista en servidores y almacenamiento de alto rendimiento")
                .build());

        // Marcas de Networking
        marcas.add(Marca.builder()
                .nombreMarca("Cisco")
                .descripcionMarca("Líder mundial en equipos de red y telecomunicaciones")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("TP-Link")
                .descripcionMarca("Fabricante chino de equipos de red para hogares y empresas")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Ubiquiti")
                .descripcionMarca("Especialista en soluciones WiFi empresariales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("D-Link")
                .descripcionMarca("Marca taiwanesa de equipos de red")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Netgear")
                .descripcionMarca("Fabricante estadounidense de routers y switches")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("MikroTik")
                .descripcionMarca("Empresa letona especializada en routers y equipos ISP")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Huawei")
                .descripcionMarca("Empresa china de telecomunicaciones y networking")
                .build());

        // Marcas de Impresión
        marcas.add(Marca.builder()
                .nombreMarca("Epson")
                .descripcionMarca("Fabricante japonés de impresoras y proyectores")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Canon")
                .descripcionMarca("Empresa japonesa de impresoras y cámaras")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Brother")
                .descripcionMarca("Marca japonesa de impresoras y equipos de oficina")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Xerox")
                .descripcionMarca("Pionera en fotocopiadoras y soluciones documentales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Ricoh")
                .descripcionMarca("Empresa japonesa de fotocopiadoras multifuncionales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Samsung")
                .descripcionMarca("Conglomerado surcoreano de electrónica")
                .build());

        // Marcas de Monitores y Proyectores
        marcas.add(Marca.builder()
                .nombreMarca("LG")
                .descripcionMarca("Fabricante surcoreano de pantallas y electrodomésticos")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("BenQ")
                .descripcionMarca("Marca taiwanesa de monitores y proyectores")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("ViewSonic")
                .descripcionMarca("Especialista en monitores profesionales")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Sony")
                .descripcionMarca("Multinacional japonesa de electrónica y entretenimiento")
                .build());

        // Marcas de Componentes
        marcas.add(Marca.builder()
                .nombreMarca("Intel")
                .descripcionMarca("Líder mundial en procesadores y tecnología")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("AMD")
                .descripcionMarca("Advanced Micro Devices, procesadores y GPUs")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("NVIDIA")
                .descripcionMarca("Líder en tarjetas gráficas y AI")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Kingston")
                .descripcionMarca("Fabricante de memorias y almacenamiento")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Corsair")
                .descripcionMarca("Especialista en componentes gaming de alta gama")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Crucial")
                .descripcionMarca("Marca de memorias y SSDs de Micron")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Western Digital")
                .descripcionMarca("Fabricante de discos duros y SSDs")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Seagate")
                .descripcionMarca("Líder en almacenamiento magnético")
                .build());

        // Marcas de UPS y Energía
        marcas.add(Marca.builder()
                .nombreMarca("APC")
                .descripcionMarca("American Power Conversion, líder en UPS")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Eaton")
                .descripcionMarca("Soluciones de gestión de energía")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Tripp Lite")
                .descripcionMarca("Fabricante de UPS y protección eléctrica")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("CyberPower")
                .descripcionMarca("Especialista en sistemas UPS")
                .build());

        // Marcas de Seguridad
        marcas.add(Marca.builder()
                .nombreMarca("Hikvision")
                .descripcionMarca("Líder mundial en cámaras de seguridad")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Dahua")
                .descripcionMarca("Fabricante chino de videovigilancia")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("Axis")
                .descripcionMarca("Pionera en cámaras IP de seguridad")
                .build());

        // Marcas de Almacenamiento NAS
        marcas.add(Marca.builder()
                .nombreMarca("Synology")
                .descripcionMarca("Especialista en sistemas NAS para empresas")
                .build());
        marcas.add(Marca.builder()
                .nombreMarca("QNAP")
                .descripcionMarca("Fabricante taiwanés de NAS")
                .build());

        // Marcas Genéricas
        marcas.add(Marca.builder()
                .nombreMarca("Genérico")
                .descripcionMarca("Equipos sin marca específica o ensamblados")
                .build());

        marcaRepository.saveAll(marcas);
    }
}

package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.Modelo;
import pe.edu.unasam.activos.modules.equipos.repository.ModeloRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(26)
@RequiredArgsConstructor
public class ModelosDataLoader extends AbstractDataLoader {

    private final ModeloRepository modeloRepository;

    @Override
    protected String getLoaderName() {
        return "Modelos";
    }

    @Override
    protected boolean shouldLoad() {
        return modeloRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Modelo> modelos = new ArrayList<>();

        // Modelos Dell
        modelos.add(Modelo.builder()
                .nombreModelo("OptiPlex 7090")
                .descripcionModelo("PC de escritorio empresarial")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Latitude 5420")
                .descripcionModelo("Laptop corporativa 14 pulgadas")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Vostro 3510")
                .descripcionModelo("Laptop para pequeñas empresas")
                .anoLanzamiento(2022)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("PowerEdge R750")
                .descripcionModelo("Servidor rack empresarial 2U")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Precision 3660")
                .descripcionModelo("Workstation para diseño")
                .anoLanzamiento(2022)
                .build());

        // Modelos HP
        modelos.add(Modelo.builder()
                .nombreModelo("ProDesk 400 G7")
                .descripcionModelo("PC de escritorio compacta")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("EliteBook 840 G8")
                .descripcionModelo("Laptop premium empresarial")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ProBook 450 G9")
                .descripcionModelo("Laptop económica para negocios")
                .anoLanzamiento(2022)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ProLiant DL380 Gen10")
                .descripcionModelo("Servidor rack 2U escalable").anoLanzamiento(2020).build());
        modelos.add(Modelo.builder()
                .nombreModelo("Z2 Tower G9")
                .descripcionModelo("Workstation profesional")
                .anoLanzamiento(2022)
                .build());

        // Modelos Lenovo
        modelos.add(Modelo.builder()
                .nombreModelo("ThinkCentre M90t")
                .descripcionModelo("PC torre empresarial")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ThinkPad T14 Gen 3")
                .descripcionModelo("Laptop corporativa robusta")
                .anoLanzamiento(2022)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("IdeaPad 3")
                .descripcionModelo("Laptop básica para oficina")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ThinkSystem SR650")
                .descripcionModelo("Servidor rack 2U multiuso")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ThinkStation P360")
                .descripcionModelo("Workstation de alto rendimiento")
                .anoLanzamiento(2022)
                .build());

        // Modelos Asus
        modelos.add(Modelo.builder()
                .nombreModelo("ExpertCenter D500")
                .descripcionModelo("PC empresarial compacta")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Vivobook 15")
                .descripcionModelo("Laptop económica versátil")
                .anoLanzamiento(2022)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ZenBook 14")
                .descripcionModelo("Ultrabook delgada premium")
                .anoLanzamiento(2022)
                .build());

        // Modelos HP Impresoras
        modelos.add(Modelo.builder()
                .nombreModelo("LaserJet Pro M404dn")
                .descripcionModelo("Impresora láser monocromática")
                .anoLanzamiento(2019)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("LaserJet Pro MFP M428fdw")
                .descripcionModelo("Multifuncional láser")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("OfficeJet Pro 9025e")
                .descripcionModelo("Multifuncional inyección de tinta")
                .anoLanzamiento(2021)
                .build());

        // Modelos Epson
        modelos.add(Modelo.builder()
                .nombreModelo("EcoTank L3250")
                .descripcionModelo("Multifuncional con tanque de tinta")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("WorkForce Pro WF-C5790")
                .descripcionModelo("Multifuncional empresarial color")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("L805")
                .descripcionModelo("Impresora fotográfica con sistema continuo")
                .anoLanzamiento(2019)
                .build());

        // Modelos Canon
        modelos.add(Modelo.builder()
                .nombreModelo("imageRUNNER 2425")
                .descripcionModelo("Fotocopiadora multifuncional")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("PIXMA G3160")
                .descripcionModelo("Multifuncional con tanque de tinta")
                .anoLanzamiento(2021)
                .build());

        // Modelos Monitores
        modelos.add(Modelo.builder()
                .nombreModelo("P2422H")
                .descripcionModelo("Monitor Dell 24 pulgadas Full HD")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("24MK430H")
                .descripcionModelo("Monitor LG 24 pulgadas IPS")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("VG249Q")
                .descripcionModelo("Monitor gaming Asus 24 pulgadas")
                .anoLanzamiento(2020)
                .build());

        // Modelos Cisco Networking
        modelos.add(Modelo.builder()
                .nombreModelo("Catalyst 2960-X")
                .descripcionModelo("Switch 24 puertos gigabit")
                .anoLanzamiento(2019)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("ISR 4331")
                .descripcionModelo("Router empresarial modular")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Meraki MR46")
                .descripcionModelo("Access Point WiFi 6 en nube")
                .anoLanzamiento(2021)
                .build());

        // Modelos TP-Link
        modelos.add(Modelo.builder()
                .nombreModelo("TL-SG1024D")
                .descripcionModelo("Switch gigabit 24 puertos")
                .anoLanzamiento(2018)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Archer AX73")
                .descripcionModelo("Router WiFi 6 dual band")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("EAP660 HD")
                .descripcionModelo("Access Point WiFi 6 empresarial")
                .anoLanzamiento(2022)
                .build());

        // Modelos Ubiquiti
        modelos.add(Modelo.builder()
                .nombreModelo("UniFi Switch 24 PoE")
                .descripcionModelo("Switch administrable con PoE")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("UniFi AP AC Pro")
                .descripcionModelo("Access Point dual band")
                .anoLanzamiento(2019)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("EdgeRouter 4")
                .descripcionModelo("Router empresarial de alto rendimiento")
                .anoLanzamiento(2019)
                .build());

        // Modelos APC UPS
        modelos.add(Modelo.builder()
                .nombreModelo("Smart-UPS 1500VA")
                .descripcionModelo("UPS 1500VA con gestión")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Back-UPS Pro 900VA")
                .descripcionModelo("UPS 900VA para PCs")
                .anoLanzamiento(2019).build());

        // Modelos Proyectores
        modelos.add(Modelo.builder()
                .nombreModelo("EB-X41")
                .descripcionModelo("Proyector Epson 3600 lúmenes")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("MW550")
                .descripcionModelo("Proyector BenQ WXGA")
                .anoLanzamiento(2021)
                .build());

        // Modelos Cámaras
        modelos.add(Modelo.builder()
                .nombreModelo("DS-2CD2143G0-I")
                .descripcionModelo("Cámara IP domo 4MP Hikvision")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("IPC-HDW2431T-AS-S2")
                .descripcionModelo("Cámara IP turret 4MP Dahua")
                .anoLanzamiento(2021).build());

        // Modelos NAS
        modelos.add(Modelo.builder()
                .nombreModelo("DS920+")
                .descripcionModelo("NAS Synology 4 bahías")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("TS-453D")
                .descripcionModelo("NAS QNAP 4 bahías")
                .anoLanzamiento(2020)
                .build());

        // Modelos Tablets
        modelos.add(Modelo.builder()
                .nombreModelo("iPad 9th Gen")
                .descripcionModelo("Tablet Apple 10.2 pulgadas")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Galaxy Tab A8")
                .descripcionModelo("Tablet Samsung 10.5 pulgadas")
                .anoLanzamiento(2022)
                .build());

        // Componentes
        modelos.add(Modelo.builder()
                .nombreModelo("Core i5-12400")
                .descripcionModelo("Procesador Intel 12va gen")
                .anoLanzamiento(2022)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Ryzen 5 5600X")
                .descripcionModelo("Procesador AMD Zen 3")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("HyperX Fury 16GB")
                .descripcionModelo("Memoria RAM DDR4 3200MHz")
                .anoLanzamiento(2020)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("WD Blue 1TB")
                .descripcionModelo("Disco duro SATA 7200 RPM")
                .anoLanzamiento(2019)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("Samsung 980 PRO")
                .descripcionModelo("SSD NVMe 1TB Gen4")
                .anoLanzamiento(2021)
                .build());
        modelos.add(Modelo.builder()
                .nombreModelo("RTX 3060").descripcionModelo("Tarjeta gráfica Nvidia 12GB")
                .anoLanzamiento(2021)
                .build());

        modeloRepository.saveAll(modelos);
    }
}

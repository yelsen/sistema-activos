package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaComponente;
import pe.edu.unasam.activos.modules.equipos.domain.Componente;
import pe.edu.unasam.activos.modules.equipos.repository.CategoriaComponenteRepository;
import pe.edu.unasam.activos.modules.equipos.repository.ComponentesRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(26)
@RequiredArgsConstructor
public class ComponentesDataLoader extends AbstractDataLoader {

    private final ComponentesRepository componentesRepository;
    private final CategoriaComponenteRepository categoriaComponenteRepository;

    @Override
    protected String getLoaderName() {
        return "Componentes de Equipos y Servidores";
    }

    @Override
    protected boolean shouldLoad() {
        return componentesRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Componente> componentes = new ArrayList<>();

        // Obtener categorías
        CategoriaComponente so = getCategoria("Sistema Operativo");
        CategoriaComponente proc = getCategoria("Procesador");
        CategoriaComponente ram = getCategoria("Memoria RAM");
        CategoriaComponente alm = getCategoria("Almacenamiento");
        CategoriaComponente tmadre = getCategoria("Tarjeta Madre");
        CategoriaComponente gpu = getCategoria("Tarjeta de Video");
        CategoriaComponente pantalla = getCategoria("Pantalla");
        CategoriaComponente con = getCategoria("Conectividad");
        CategoriaComponente bat = getCategoria("Batería");
        CategoriaComponente puertos = getCategoria("Puertos");
        CategoriaComponente imp = getCategoria("Impresión");
        CategoriaComponente red = getCategoria("Red");
        CategoriaComponente ene = getCategoria("Energía");
        CategoriaComponente dim = getCategoria("Dimensiones");
        CategoriaComponente peso = getCategoria("Peso");

        // ==================== SISTEMAS OPERATIVOS ====================
        // Windows Desktop
        componentes.add(crearComponente("Windows 11 Home", "Sistema operativo Windows 11 Home", so));
        componentes.add(crearComponente("Windows 11 Pro", "Sistema operativo Windows 11 Pro", so));
        componentes.add(crearComponente("Windows 11 Enterprise", "Sistema operativo Windows 11 Enterprise", so));
        componentes.add(crearComponente("Windows 10 Home", "Sistema operativo Windows 10 Home", so));
        componentes.add(crearComponente("Windows 10 Pro", "Sistema operativo Windows 10 Pro", so));
        componentes.add(crearComponente("Windows 10 Enterprise", "Sistema operativo Windows 10 Enterprise", so));
        componentes.add(crearComponente("Windows 8.1 Pro", "Sistema operativo Windows 8.1 Professional", so));
        componentes.add(crearComponente("Windows 7 Professional", "Sistema operativo Windows 7 Professional", so));

        // Windows Server
        componentes.add(crearComponente("Windows Server 2022 Standard", "Windows Server 2022 Standard Edition", so));
        componentes.add(crearComponente("Windows Server 2022 Datacenter", "Windows Server 2022 Datacenter Edition", so));
        componentes.add(crearComponente("Windows Server 2019 Standard", "Windows Server 2019 Standard Edition", so));
        componentes.add(crearComponente("Windows Server 2019 Datacenter", "Windows Server 2019 Datacenter Edition", so));
        componentes.add(crearComponente("Windows Server 2016 Standard", "Windows Server 2016 Standard Edition", so));
        componentes.add(crearComponente("Windows Server 2016 Datacenter", "Windows Server 2016 Datacenter Edition", so));
        componentes.add(crearComponente("Windows Server 2012 R2", "Windows Server 2012 R2", so));

        // Ubuntu
        componentes.add(crearComponente("Ubuntu 24.04 LTS Desktop", "Ubuntu 24.04 LTS Noble Numbat Desktop", so));
        componentes.add(crearComponente("Ubuntu 24.04 LTS Server", "Ubuntu 24.04 LTS Noble Numbat Server", so));
        componentes.add(crearComponente("Ubuntu 22.04 LTS Desktop", "Ubuntu 22.04 LTS Jammy Jellyfish Desktop", so));
        componentes.add(crearComponente("Ubuntu 22.04 LTS Server", "Ubuntu 22.04 LTS Jammy Jellyfish Server", so));
        componentes.add(crearComponente("Ubuntu 20.04 LTS Desktop", "Ubuntu 20.04 LTS Focal Fossa Desktop", so));
        componentes.add(crearComponente("Ubuntu 20.04 LTS Server", "Ubuntu 20.04 LTS Focal Fossa Server", so));

        // Debian
        componentes.add(crearComponente("Debian 12 Bookworm", "Debian 12 Bookworm", so));
        componentes.add(crearComponente("Debian 11 Bullseye", "Debian 11 Bullseye", so));
        componentes.add(crearComponente("Debian 10 Buster", "Debian 10 Buster", so));

        // Otros Linux
        componentes.add(crearComponente("CentOS 9 Stream", "CentOS 9 Stream", so));
        componentes.add(crearComponente("Red Hat Enterprise Linux 9", "RHEL 9", so));
        componentes.add(crearComponente("Red Hat Enterprise Linux 8", "RHEL 8", so));
        componentes.add(crearComponente("Fedora Server", "Fedora Server", so));
        componentes.add(crearComponente("Rocky Linux 9", "Rocky Linux 9", so));
        componentes.add(crearComponente("AlmaLinux 9", "AlmaLinux 9", so));

        // Sin SO
        componentes.add(crearComponente("Sin Sistema Operativo", "Equipo sin SO preinstalado", so));
        componentes.add(crearComponente("FreeDOS", "Sistema operativo FreeDOS", so));

        // ==================== PROCESADORES INTEL ====================
        // Intel Core i9 (13va generación)
        componentes.add(crearComponente("Intel Core i9-13900K", "Procesador Intel Core i9-13900K 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i9-13900KF", "Procesador Intel Core i9-13900KF 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i9-13900", "Procesador Intel Core i9-13900 13va Gen", proc));

        // Intel Core i9 (12va generación)
        componentes.add(crearComponente("Intel Core i9-12900K", "Procesador Intel Core i9-12900K 12va Gen", proc));
        componentes.add(crearComponente("Intel Core i9-12900KF", "Procesador Intel Core i9-12900KF 12va Gen", proc));

        // Intel Core i9 (11va generación)
        componentes.add(crearComponente("Intel Core i9-11900K", "Procesador Intel Core i9-11900K 11va Gen", proc));

        // Intel Core i7 (13va generación)
        componentes.add(crearComponente("Intel Core i7-13700K", "Procesador Intel Core i7-13700K 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i7-13700KF", "Procesador Intel Core i7-13700KF 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i7-13700", "Procesador Intel Core i7-13700 13va Gen", proc));

        // Intel Core i7 (12va generación)
        componentes.add(crearComponente("Intel Core i7-12700K", "Procesador Intel Core i7-12700K 12va Gen", proc));
        componentes.add(crearComponente("Intel Core i7-12700", "Procesador Intel Core i7-12700 12va Gen", proc));

        // Intel Core i7 (11va generación)
        componentes.add(crearComponente("Intel Core i7-11700K", "Procesador Intel Core i7-11700K 11va Gen", proc));
        componentes.add(crearComponente("Intel Core i7-1165G7", "Procesador Intel Core i7-1165G7 11va Gen (Laptop)", proc));
        componentes.add(crearComponente("Intel Core i7-1185G7", "Procesador Intel Core i7-1185G7 11va Gen (Laptop)", proc));

        // Intel Core i7 (10ma generación)
        componentes.add(crearComponente("Intel Core i7-10700K", "Procesador Intel Core i7-10700K 10ma Gen", proc));
        componentes.add(crearComponente("Intel Core i7-10700", "Procesador Intel Core i7-10700 10ma Gen", proc));

        // Intel Core i5 (13va generación)
        componentes.add(crearComponente("Intel Core i5-13600K", "Procesador Intel Core i5-13600K 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i5-13600", "Procesador Intel Core i5-13600 13va Gen", proc));
        componentes.add(crearComponente("Intel Core i5-13400", "Procesador Intel Core i5-13400 13va Gen", proc));

        // Intel Core i5 (12va generación)
        componentes.add(crearComponente("Intel Core i5-12600K", "Procesador Intel Core i5-12600K 12va Gen", proc));
        componentes.add(crearComponente("Intel Core i5-12400", "Procesador Intel Core i5-12400 12va Gen", proc));

        // Intel Core i5 (11va generación)
        componentes.add(crearComponente("Intel Core i5-11600K", "Procesador Intel Core i5-11600K 11va Gen", proc));
        componentes.add(crearComponente("Intel Core i5-11400H", "Procesador Intel Core i5-11400H 11va Gen (Laptop)", proc));
        componentes.add(crearComponente("Intel Core i5-1135G7", "Procesador Intel Core i5-1135G7 11va Gen (Laptop)", proc));

        // Intel Core i5 (10ma generación)
        componentes.add(crearComponente("Intel Core i5-10400F", "Procesador Intel Core i5-10400F 10ma Gen", proc));
        componentes.add(crearComponente("Intel Core i5-10400", "Procesador Intel Core i5-10400 10ma Gen", proc));

        // Intel Core i3
        componentes.add(crearComponente("Intel Core i3-12100", "Procesador Intel Core i3-12100 12va Gen", proc));
        componentes.add(crearComponente("Intel Core i3-10100", "Procesador Intel Core i3-10100 10ma Gen", proc));
        componentes.add(crearComponente("Intel Core i3-1115G4", "Procesador Intel Core i3-1115G4 11va Gen (Laptop)", proc));

        // Intel Xeon (Servidores)
        componentes.add(crearComponente("Intel Xeon Gold 6248R", "Procesador Intel Xeon Gold 6248R (Servidor)", proc));
        componentes.add(crearComponente("Intel Xeon Gold 6238R", "Procesador Intel Xeon Gold 6238R (Servidor)", proc));
        componentes.add(crearComponente("Intel Xeon Silver 4214", "Procesador Intel Xeon Silver 4214 (Servidor)", proc));
        componentes.add(crearComponente("Intel Xeon Silver 4210", "Procesador Intel Xeon Silver 4210 (Servidor)", proc));
        componentes.add(crearComponente("Intel Xeon E-2388G", "Procesador Intel Xeon E-2388G (Servidor Entry)", proc));
        componentes.add(crearComponente("Intel Xeon E-2314", "Procesador Intel Xeon E-2314 (Servidor Entry)", proc));

        // ==================== PROCESADORES AMD ====================
        // AMD Ryzen 9 (7000 series)
        componentes.add(crearComponente("AMD Ryzen 9 7950X", "Procesador AMD Ryzen 9 7950X (Zen 4)", proc));
        componentes.add(crearComponente("AMD Ryzen 9 7900X", "Procesador AMD Ryzen 9 7900X (Zen 4)", proc));

        // AMD Ryzen 9 (5000 series)
        componentes.add(crearComponente("AMD Ryzen 9 5950X", "Procesador AMD Ryzen 9 5950X (Zen 3)", proc));
        componentes.add(crearComponente("AMD Ryzen 9 5900X", "Procesador AMD Ryzen 9 5900X (Zen 3)", proc));

        // AMD Ryzen 7 (7000 series)
        componentes.add(crearComponente("AMD Ryzen 7 7700X", "Procesador AMD Ryzen 7 7700X (Zen 4)", proc));
        componentes.add(crearComponente("AMD Ryzen 7 7600X", "Procesador AMD Ryzen 7 7600X (Zen 4)", proc));

        // AMD Ryzen 7 (5000 series)
        componentes.add(crearComponente("AMD Ryzen 7 5800X", "Procesador AMD Ryzen 7 5800X (Zen 3)", proc));
        componentes.add(crearComponente("AMD Ryzen 7 5700G", "Procesador AMD Ryzen 7 5700G con gráficos (Zen 3)", proc));
        componentes.add(crearComponente("AMD Ryzen 7 4800H", "Procesador AMD Ryzen 7 4800H (Laptop)", proc));

        // AMD Ryzen 5 (7000 series)
        componentes.add(crearComponente("AMD Ryzen 5 7600X", "Procesador AMD Ryzen 5 7600X (Zen 4)", proc));

        // AMD Ryzen 5 (5000 series)
        componentes.add(crearComponente("AMD Ryzen 5 5600X", "Procesador AMD Ryzen 5 5600X (Zen 3)", proc));
        componentes.add(crearComponente("AMD Ryzen 5 5600G", "Procesador AMD Ryzen 5 5600G con gráficos (Zen 3)", proc));
        componentes.add(crearComponente("AMD Ryzen 5 5500U", "Procesador AMD Ryzen 5 5500U (Laptop)", proc));

        // AMD Ryzen 3
        componentes.add(crearComponente("AMD Ryzen 3 3200G", "Procesador AMD Ryzen 3 3200G con gráficos", proc));
        componentes.add(crearComponente("AMD Ryzen 3 5300U", "Procesador AMD Ryzen 3 5300U (Laptop)", proc));

        // AMD EPYC (Servidores)
        componentes.add(crearComponente("AMD EPYC 7763", "Procesador AMD EPYC 7763 (Servidor)", proc));
        componentes.add(crearComponente("AMD EPYC 7543", "Procesador AMD EPYC 7543 (Servidor)", proc));
        componentes.add(crearComponente("AMD EPYC 7313", "Procesador AMD EPYC 7313 (Servidor)", proc));

        // ==================== MEMORIA RAM ====================
        // DDR5
        componentes.add(crearComponente("DDR5 64GB 5600MHz", "Memoria DDR5 64GB 5600MHz", ram));
        componentes.add(crearComponente("DDR5 32GB 5600MHz", "Memoria DDR5 32GB 5600MHz", ram));
        componentes.add(crearComponente("DDR5 16GB 5600MHz", "Memoria DDR5 16GB 5600MHz", ram));
        componentes.add(crearComponente("DDR5 32GB 6000MHz", "Memoria DDR5 32GB 6000MHz (Alta velocidad)", ram));

        // DDR4
        componentes.add(crearComponente("DDR4 64GB 3200MHz", "Memoria DDR4 64GB 3200MHz", ram));
        componentes.add(crearComponente("DDR4 32GB 3200MHz", "Memoria DDR4 32GB 3200MHz", ram));
        componentes.add(crearComponente("DDR4 16GB 3200MHz", "Memoria DDR4 16GB 3200MHz", ram));
        componentes.add(crearComponente("DDR4 8GB 3200MHz", "Memoria DDR4 8GB 3200MHz", ram));
        componentes.add(crearComponente("DDR4 32GB 2666MHz", "Memoria DDR4 32GB 2666MHz", ram));
        componentes.add(crearComponente("DDR4 16GB 2666MHz", "Memoria DDR4 16GB 2666MHz", ram));
        componentes.add(crearComponente("DDR4 8GB 2666MHz", "Memoria DDR4 8GB 2666MHz", ram));
        componentes.add(crearComponente("DDR4 4GB 2400MHz", "Memoria DDR4 4GB 2400MHz", ram));

        // DDR3
        componentes.add(crearComponente("DDR3 16GB 1600MHz", "Memoria DDR3 16GB 1600MHz", ram));
        componentes.add(crearComponente("DDR3 8GB 1600MHz", "Memoria DDR3 8GB 1600MHz", ram));
        componentes.add(crearComponente("DDR3 4GB 1600MHz", "Memoria DDR3 4GB 1600MHz", ram));
        componentes.add(crearComponente("DDR3 8GB 1333MHz", "Memoria DDR3 8GB 1333MHz", ram));
        componentes.add(crearComponente("DDR3 4GB 1333MHz", "Memoria DDR3 4GB 1333MHz", ram));
        componentes.add(crearComponente("DDR3 2GB 1333MHz", "Memoria DDR3 2GB 1333MHz", ram));

        // ==================== ALMACENAMIENTO ====================
        // SSD NVMe
        componentes.add(crearComponente("SSD NVMe 2TB PCIe 4.0", "Disco SSD NVMe 2TB PCIe Gen 4", alm));
        componentes.add(crearComponente("SSD NVMe 1TB PCIe 4.0", "Disco SSD NVMe 1TB PCIe Gen 4", alm));
        componentes.add(crearComponente("SSD NVMe 512GB PCIe 4.0", "Disco SSD NVMe 512GB PCIe Gen 4", alm));
        componentes.add(crearComponente("SSD NVMe 256GB PCIe 4.0", "Disco SSD NVMe 256GB PCIe Gen 4", alm));
        componentes.add(crearComponente("SSD NVMe 1TB PCIe 3.0", "Disco SSD NVMe 1TB PCIe Gen 3", alm));
        componentes.add(crearComponente("SSD NVMe 512GB PCIe 3.0", "Disco SSD NVMe 512GB PCIe Gen 3", alm));
        componentes.add(crearComponente("SSD NVMe 256GB PCIe 3.0", "Disco SSD NVMe 256GB PCIe Gen 3", alm));

        // SSD SATA
        componentes.add(crearComponente("SSD SATA 2TB", "Disco SSD SATA 2.5\" 2TB", alm));
        componentes.add(crearComponente("SSD SATA 1TB", "Disco SSD SATA 2.5\" 1TB", alm));
        componentes.add(crearComponente("SSD SATA 500GB", "Disco SSD SATA 2.5\" 500GB", alm));
        componentes.add(crearComponente("SSD SATA 256GB", "Disco SSD SATA 2.5\" 256GB", alm));
        componentes.add(crearComponente("SSD SATA 128GB", "Disco SSD SATA 2.5\" 128GB", alm));

        // HDD
        componentes.add(crearComponente("HDD 4TB 7200RPM", "Disco duro 4TB 7200RPM", alm));
        componentes.add(crearComponente("HDD 2TB 7200RPM", "Disco duro 2TB 7200RPM", alm));
        componentes.add(crearComponente("HDD 1TB 7200RPM", "Disco duro 1TB 7200RPM", alm));
        componentes.add(crearComponente("HDD 500GB 7200RPM", "Disco duro 500GB 7200RPM", alm));
        componentes.add(crearComponente("HDD 1TB 5400RPM", "Disco duro 1TB 5400RPM", alm));
        componentes.add(crearComponente("HDD 500GB 5400RPM", "Disco duro 500GB 5400RPM", alm));

        // ==================== TARJETAS DE VIDEO NVIDIA ====================
        // RTX 40 Series
        componentes.add(crearComponente("NVIDIA GeForce RTX 4090", "GPU NVIDIA RTX 4090 24GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 4080", "GPU NVIDIA RTX 4080 16GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 4070 Ti", "GPU NVIDIA RTX 4070 Ti 12GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 4070", "GPU NVIDIA RTX 4070 12GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 4060 Ti", "GPU NVIDIA RTX 4060 Ti 8GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 4060", "GPU NVIDIA RTX 4060 8GB", gpu));

        // RTX 30 Series
        componentes.add(crearComponente("NVIDIA GeForce RTX 3090", "GPU NVIDIA RTX 3090 24GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 3080", "GPU NVIDIA RTX 3080 10GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 3070", "GPU NVIDIA RTX 3070 8GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 3060 Ti", "GPU NVIDIA RTX 3060 Ti 8GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 3060", "GPU NVIDIA RTX 3060 12GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce RTX 3050", "GPU NVIDIA RTX 3050 8GB", gpu));

        // GTX Series
        componentes.add(crearComponente("NVIDIA GeForce GTX 1660 Ti", "GPU NVIDIA GTX 1660 Ti 6GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce GTX 1650", "GPU NVIDIA GTX 1650 4GB", gpu));
        componentes.add(crearComponente("NVIDIA GeForce GTX 1050 Ti", "GPU NVIDIA GTX 1050 Ti 4GB", gpu));

        // ==================== TARJETAS DE VIDEO AMD ====================
        // RX 7000 Series
        componentes.add(crearComponente("AMD Radeon RX 7900 XTX", "GPU AMD RX 7900 XTX 24GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 7900 XT", "GPU AMD RX 7900 XT 20GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 7800 XT", "GPU AMD RX 7800 XT 16GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 7700 XT", "GPU AMD RX 7700 XT 12GB", gpu));

        // RX 6000 Series
        componentes.add(crearComponente("AMD Radeon RX 6800 XT", "GPU AMD RX 6800 XT 16GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 6700 XT", "GPU AMD RX 6700 XT 12GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 6600 XT", "GPU AMD RX 6600 XT 8GB", gpu));
        componentes.add(crearComponente("AMD Radeon RX 6600", "GPU AMD RX 6600 8GB", gpu));

        // Gráficos Integrados
        componentes.add(crearComponente("Intel UHD Graphics 770", "Gráficos integrados Intel UHD 770", gpu));
        componentes.add(crearComponente("Intel Iris Xe Graphics", "Gráficos integrados Intel Iris Xe", gpu));
        componentes.add(crearComponente("AMD Radeon Graphics", "Gráficos integrados AMD Radeon", gpu));

        // ==================== PANTALLAS ====================
        // Laptops
        componentes.add(crearComponente("Pantalla 13.3\" Full HD", "Pantalla laptop 13.3\" (1920x1080)", pantalla));
        componentes.add(crearComponente("Pantalla 14\" Full HD", "Pantalla laptop 14\" (1920x1080)", pantalla));
        componentes.add(crearComponente("Pantalla 15.6\" Full HD", "Pantalla laptop 15.6\" (1920x1080)", pantalla));
        componentes.add(crearComponente("Pantalla 15.6\" HD", "Pantalla laptop 15.6\" (1366x768)", pantalla));
        componentes.add(crearComponente("Pantalla 17.3\" Full HD", "Pantalla laptop 17.3\" (1920x1080)", pantalla));

        // Monitores
        componentes.add(crearComponente("Monitor 21.5\" Full HD", "Monitor 21.5\" (1920x1080)", pantalla));
        componentes.add(crearComponente("Monitor 24\" Full HD", "Monitor 24\" (1920x1080)", pantalla));
        componentes.add(crearComponente("Monitor 27\" 4K UHD", "Monitor 27\" (3840x2160)", pantalla));
        componentes.add(crearComponente("Monitor 32\" 4K UHD", "Monitor 32\" (3840x2160)", pantalla));

        // ==================== CONECTIVIDAD ====================
        componentes.add(crearComponente("WiFi 6E (802.11ax)", "WiFi 6E de alta velocidad", con));
        componentes.add(crearComponente("WiFi 6 (802.11ax)", "WiFi 6 estándar", con));
        componentes.add(crearComponente("WiFi 5 (802.11ac)", "WiFi 5 estándar", con));
        componentes.add(crearComponente("WiFi 4 (802.11n)", "WiFi 4 básico", con));
        componentes.add(crearComponente("Bluetooth 5.3", "Bluetooth versión 5.3", con));
        componentes.add(crearComponente("Bluetooth 5.2", "Bluetooth versión 5.2", con));
        componentes.add(crearComponente("Bluetooth 5.0", "Bluetooth versión 5.0", con));
        componentes.add(crearComponente("Bluetooth 4.2", "Bluetooth versión 4.2", con));
        componentes.add(crearComponente("Ethernet Gigabit", "Puerto Ethernet 10/100/1000", con));
        componentes.add(crearComponente("Ethernet 10Gb", "Puerto Ethernet 10 Gigabit", con));

        // ==================== BATERÍA ====================
        componentes.add(crearComponente("Batería 90Wh Li-ion", "Batería litio-ion 90Wh", bat));
        componentes.add(crearComponente("Batería 80Wh Li-ion", "Batería litio-ion 80Wh", bat));
        componentes.add(crearComponente("Batería 70Wh Li-ion", "Batería litio-ion 70Wh", bat));
        componentes.add(crearComponente("Batería 56Wh Li-ion", "Batería litio-ion 56Wh", bat));
        componentes.add(crearComponente("Batería 45Wh Li-ion", "Batería litio-ion 45Wh", bat));
        componentes.add(crearComponente("Batería 42Wh Li-ion", "Batería litio-ion 42Wh", bat));

        // ==================== PUERTOS ====================
        componentes.add(crearComponente("Puerto USB 3.2 Gen 2", "Puerto USB 3.2 Gen 2 (10 Gbps)", puertos));
        componentes.add(crearComponente("Puerto USB 3.2 Gen 1", "Puerto USB 3.2 Gen 1 (5 Gbps)", puertos));
        componentes.add(crearComponente("Puerto USB 3.0", "Puerto USB 3.0 (5 Gbps)", puertos));
        componentes.add(crearComponente("Puerto USB-C", "Puerto USB Type-C", puertos));
        componentes.add(crearComponente("Puerto HDMI 2.1", "Puerto HDMI versión 2.1", puertos));
        componentes.add(crearComponente("Puerto HDMI 2.0", "Puerto HDMI versión 2.0", puertos));
        componentes.add(crearComponente("Puerto DisplayPort 1.4", "Puerto DisplayPort versión 1.4", puertos));
        componentes.add(crearComponente("Puerto Thunderbolt 4", "Puerto Thunderbolt 4 (40 Gbps)", puertos));
        componentes.add(crearComponente("Puerto Thunderbolt 3", "Puerto Thunderbolt 3 (40 Gbps)", puertos));
        componentes.add(crearComponente("Lector SD/microSD", "Lector de tarjetas SD", puertos));
        componentes.add(crearComponente("Puerto Audio 3.5mm", "Puerto de audio combo", puertos));
        componentes.add(crearComponente("Puerto RJ-45", "Puerto Ethernet RJ-45", puertos));

        // ==================== ENERGÍA ====================
        componentes.add(crearComponente("Fuente 850W 80+ Gold", "Fuente de poder 850W certificación Gold", ene));
        componentes.add(crearComponente("Fuente 750W 80+ Gold", "Fuente de poder 750W certificación Gold", ene));
        componentes.add(crearComponente("Fuente 650W 80+ Bronze", "Fuente de poder 650W certificación Bronze", ene));
        componentes.add(crearComponente("Fuente 500W 80+ Bronze", "Fuente de poder 500W certificación Bronze", ene));
        componentes.add(crearComponente("Cargador 65W USB-C", "Cargador 65W tipo USB-C", ene));
        componentes.add(crearComponente("Cargador 90W", "Cargador 90W estándar", ene));
        componentes.add(crearComponente("Cargador 135W", "Cargador 135W para equipos gaming", ene));
        componentes.add(crearComponente("UPS 1500VA", "UPS 1500VA interactivo", ene));
        componentes.add(crearComponente("UPS 3000VA", "UPS 3000VA online", ene));

        // ==================== TARJETAS MADRE ====================
        // ASUS
        componentes.add(crearComponente("ASUS ROG STRIX B550-F", "Motherboard ASUS ROG STRIX B550-F", tmadre));
        componentes.add(crearComponente("ASUS TUF Gaming B550M", "Motherboard ASUS TUF Gaming B550M", tmadre));
        componentes.add(crearComponente("ASUS Prime Z690-P", "Motherboard ASUS Prime Z690-P", tmadre));
        componentes.add(crearComponente("ASUS ROG MAXIMUS Z790", "Motherboard ASUS ROG MAXIMUS Z790", tmadre));

        // MSI
        componentes.add(crearComponente("MSI MAG B660M MORTAR", "Motherboard MSI MAG B660M MORTAR", tmadre));
        componentes.add(crearComponente("MSI MPG Z690 Carbon", "Motherboard MSI MPG Z690 Carbon", tmadre));
        componentes.add(crearComponente("MSI PRO B450M-A", "Motherboard MSI PRO B450M-A", tmadre));

        // Gigabyte
        componentes.add(crearComponente("Gigabyte Z690 AORUS", "Motherboard Gigabyte Z690 AORUS", tmadre));
        componentes.add(crearComponente("Gigabyte B550 AORUS Elite", "Motherboard Gigabyte B550 AORUS Elite", tmadre));
        componentes.add(crearComponente("Gigabyte H610M", "Motherboard Gigabyte H610M", tmadre));

        // ASRock
        componentes.add(crearComponente("ASRock B450M Steel Legend", "Motherboard ASRock B450M Steel Legend", tmadre));
        componentes.add(crearComponente("ASRock X570 Phantom Gaming", "Motherboard ASRock X570 Phantom Gaming", tmadre));
        componentes.add(crearComponente("ASRock Z690 Taichi", "Motherboard ASRock Z690 Taichi", tmadre));

        // ==================== IMPRESIÓN ====================
        // Láser Monocromáticas
        componentes.add(crearComponente("Impresora Láser Mono 20ppm", "Impresora láser monocromática 20 ppm", imp));
        componentes.add(crearComponente("Impresora Láser Mono 30ppm", "Impresora láser monocromática 30 ppm", imp));
        componentes.add(crearComponente("Impresora Láser Mono 40ppm", "Impresora láser monocromática 40 ppm", imp));

        // Láser Color
        componentes.add(crearComponente("Impresora Láser Color 20ppm", "Impresora láser a color 20 ppm", imp));
        componentes.add(crearComponente("Impresora Láser Color 27ppm", "Impresora láser a color 27 ppm", imp));

        // Inyección de Tinta
        componentes.add(crearComponente("Impresora Inyección Color", "Impresora inyección de tinta color", imp));
        componentes.add(crearComponente("Impresora Inyección Fotográfica", "Impresora para fotografías", imp));

        // Multifuncionales
        componentes.add(crearComponente("Multifuncional Láser Mono", "Multifuncional láser monocromática", imp));
        componentes.add(crearComponente("Multifuncional Láser Color", "Multifuncional láser a color", imp));
        componentes.add(crearComponente("Multifuncional Inyección", "Multifuncional inyección de tinta", imp));

        // Características
        componentes.add(crearComponente("Duplex Automático", "Función de impresión doble cara automática", imp));
        componentes.add(crearComponente("Resolución 1200dpi", "Resolución de impresión 1200x1200 dpi", imp));
        componentes.add(crearComponente("Resolución 600dpi", "Resolución de impresión 600x600 dpi", imp));

        // ==================== RED ====================
        // Switches No Administrables
        componentes.add(crearComponente("Switch 5 Puertos", "Switch no administrable 5 puertos", red));
        componentes.add(crearComponente("Switch 8 Puertos", "Switch no administrable 8 puertos", red));
        componentes.add(crearComponente("Switch 16 Puertos", "Switch no administrable 16 puertos", red));

        // Switches Administrables
        componentes.add(crearComponente("Switch 24P Gigabit Administrable", "Switch administrable 24 puertos Gigabit", red));
        componentes.add(crearComponente("Switch 48P Gigabit Administrable", "Switch administrable 48 puertos Gigabit", red));

        // Switches PoE
        componentes.add(crearComponente("Switch 8P PoE 802.3af", "Switch 8 puertos con PoE", red));
        componentes.add(crearComponente("Switch 24P PoE+ 802.3at", "Switch 24 puertos con PoE+", red));
        componentes.add(crearComponente("Switch 48P PoE+ 802.3at", "Switch 48 puertos con PoE+", red));

        // Routers
        componentes.add(crearComponente("Router WiFi 6 AX3000", "Router inalámbrico WiFi 6 AX3000", red));
        componentes.add(crearComponente("Router WiFi 6E AX5400", "Router inalámbrico WiFi 6E AX5400", red));
        componentes.add(crearComponente("Router Empresarial Dual WAN", "Router empresarial con dual WAN", red));

        // Access Points
        componentes.add(crearComponente("Access Point WiFi 6", "Punto de acceso WiFi 6", red));
        componentes.add(crearComponente("Access Point WiFi 6E", "Punto de acceso WiFi 6E", red));

        // Firewalls
        componentes.add(crearComponente("Firewall UTM", "Firewall con funciones UTM", red));
        componentes.add(crearComponente("Firewall Next-Gen", "Firewall de próxima generación", red));

        // ==================== DIMENSIONES ====================
        componentes.add(crearComponente("Dimensiones Ultraportátil", "Dimensiones equipo ultraportátil", dim));
        componentes.add(crearComponente("Dimensiones Laptop Estándar", "Dimensiones laptop estándar", dim));
        componentes.add(crearComponente("Dimensiones Desktop Tower", "Dimensiones PC torre", dim));
        componentes.add(crearComponente("Dimensiones Rack 1U", "Dimensiones servidor rack 1U", dim));
        componentes.add(crearComponente("Dimensiones Rack 2U", "Dimensiones servidor rack 2U", dim));

        // ==================== PESO ====================
        componentes.add(crearComponente("Peso Ultraportátil", "Peso equipo ultraportátil (menos de 1.5 kg)", peso));
        componentes.add(crearComponente("Peso Laptop Estándar", "Peso laptop estándar (1.5 - 2.5 kg)", peso));
        componentes.add(crearComponente("Peso Desktop", "Peso PC escritorio (5 - 15 kg)", peso));
        componentes.add(crearComponente("Peso Servidor", "Peso servidor (10 - 50 kg)", peso));

        componentesRepository.saveAll(componentes);
    }

    private Componente crearComponente(String nombre, String descripcion, CategoriaComponente categoria) {
        return Componente.builder()
                .nombreComponente(nombre)
                .descripcionComponente(descripcion)
                .categoriaComponente(categoria)
                .build();
    }

    private CategoriaComponente getCategoria(String nombre) {
        return categoriaComponenteRepository.findByCategoriaComponente(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
    }
}

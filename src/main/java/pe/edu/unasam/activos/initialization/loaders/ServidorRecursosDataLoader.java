package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.Unidad;
import pe.edu.unasam.activos.modules.equipos.repository.UnidadRepository;
import pe.edu.unasam.activos.modules.servidores.domain.Recurso;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import pe.edu.unasam.activos.modules.servidores.domain.ServidorRecurso;
import pe.edu.unasam.activos.modules.servidores.repository.RecursoRepository;
import pe.edu.unasam.activos.modules.servidores.repository.ServidorRecursoRepository;
import pe.edu.unasam.activos.modules.servidores.repository.ServidorRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(29)
@RequiredArgsConstructor
public class ServidorRecursosDataLoader extends AbstractDataLoader {

    private final ServidorRecursoRepository servidorRecursoRepository;
    private final ServidorRepository servidorRepository;
    private final RecursoRepository recursoRepository;
    private final UnidadRepository unidadRepository;

    @Override
    protected String getLoaderName() {
        return "Recursos de Servidores";
    }

    @Override
    protected boolean shouldLoad() {
        // Ignora filas inválidas previamente insertadas (campos nulos) y carga si no hay registros válidos
        return servidorRecursoRepository.countValid() == 0;
    }

    @Override
    protected void loadData() {
        // Buscar recursos
        Recurso cpuCores = recursoRepository.findByNombreRecurso("CPU Cores").orElseThrow();
        Recurso cpuFreq = recursoRepository.findByNombreRecurso("CPU Frequency").orElseThrow();
        Recurso cpuModel = recursoRepository.findByNombreRecurso("CPU Model").orElseThrow();
        Recurso ramTotal = recursoRepository.findByNombreRecurso("RAM Total").orElseThrow();
        Recurso ramType = recursoRepository.findByNombreRecurso("RAM Type").orElseThrow();
        Recurso storageTotal = recursoRepository.findByNombreRecurso("Storage Total").orElseThrow();
        Recurso storageType = recursoRepository.findByNombreRecurso("Storage Type").orElseThrow();
        Recurso networkSpeed = recursoRepository.findByNombreRecurso("Network Speed").orElseThrow();
        Recurso osVersion = recursoRepository.findByNombreRecurso("OS Version").orElseThrow();
        Recurso powerSupply = recursoRepository.findByNombreRecurso("Power Supply").orElseThrow();

        // Buscar unidades
        Unidad cores = unidadRepository.findByNombreUnidad("Núcleos").orElseThrow();
        Unidad ghz = unidadRepository.findByNombreUnidad("Gigahertz").orElseThrow();
        Unidad gbRam = unidadRepository.findByNombreUnidad("Gigabyte RAM").orElseThrow();
        Unidad tb = unidadRepository.findByNombreUnidad("Terabyte").orElseThrow();
        Unidad gbps = unidadRepository.findByNombreUnidad("Gigabits por segundo").orElseThrow();
        Unidad watts = unidadRepository.findByNombreUnidad("Watt").orElseThrow();

        List<ServidorRecurso> recursos = new ArrayList<>();

        // SRV-WEB-SIGA (Servidor físico web)
        Servidor srvWebSiga = servidorRepository.findByNombreServidor("SRV-WEB-SIGA").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(cpuModel).valor("Intel Xeon E-2236").build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(cpuCores).valor("6").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(cpuFreq).valor("3.4").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(ramTotal).valor("32").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(ramType).valor("DDR4 ECC").build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(storageTotal).valor("2").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(storageType).valor("SSD RAID 1").build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(networkSpeed).valor("1").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(osVersion).valor("Ubuntu Server 22.04 LTS").build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebSiga).recurso(powerSupply).valor("550").unidad(watts).build());

        // SRV-DB-PRINCIPAL (Base de datos crítica)
        Servidor srvDbPrincipal = servidorRepository.findByNombreServidor("SRV-DB-PRINCIPAL").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(cpuModel).valor("Intel Xeon Gold 5320").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(cpuCores).valor("26").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(cpuFreq).valor("2.2").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(ramTotal).valor("128").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(ramType).valor("DDR4 ECC Registered").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(storageTotal).valor("8").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(storageType).valor("NVMe SSD RAID 10").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(networkSpeed).valor("10").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(osVersion).valor("CentOS Stream 9").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbPrincipal).recurso(powerSupply).valor("1200").unidad(watts).build());

        // SRV-DB-ACADEMICO
        Servidor srvDbAcademico = servidorRepository.findByNombreServidor("SRV-DB-ACADEMICO").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(cpuModel).valor("AMD EPYC 7402P").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(cpuCores).valor("24").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(cpuFreq).valor("2.8").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(ramTotal).valor("64").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(ramType).valor("DDR4 ECC").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(storageTotal).valor("4").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(storageType).valor("SSD RAID 5").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(networkSpeed).valor("10").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDbAcademico).recurso(osVersion).valor("Ubuntu Server 22.04 LTS").build());

        // SRV-FILE-PRINCIPAL
        Servidor srvFilePrincipal = servidorRepository.findByNombreServidor("SRV-FILE-PRINCIPAL").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(cpuModel).valor("Intel Xeon E-2288G").build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(cpuCores).valor("8").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(cpuFreq).valor("3.7").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(ramTotal).valor("64").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(ramType).valor("DDR4 ECC").build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(storageTotal).valor("20").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(storageType).valor("HDD RAID 6").build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(networkSpeed).valor("10").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvFilePrincipal).recurso(osVersion).valor("Windows Server 2022").build());

        // SRV-VMHOST-01 (Virtualización)
        Servidor srvVmHost01 = servidorRepository.findByNombreServidor("SRV-VMHOST-01").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(cpuModel).valor("AMD EPYC 7543").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(cpuCores).valor("32").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(cpuFreq).valor("2.8").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(ramTotal).valor("256").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(ramType).valor("DDR4 ECC Registered").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(storageTotal).valor("10").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(storageType).valor("NVMe SSD RAID 10").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(networkSpeed).valor("25").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(osVersion).valor("VMware ESXi 8.0").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost01).recurso(powerSupply).valor("1600").unidad(watts).build());

        // SRV-VMHOST-02
        Servidor srvVmHost02 = servidorRepository.findByNombreServidor("SRV-VMHOST-02").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(cpuModel).valor("AMD EPYC 7543").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(cpuCores).valor("32").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(cpuFreq).valor("2.8").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(ramTotal).valor("256").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(ramType).valor("DDR4 ECC Registered").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(storageTotal).valor("10").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(storageType).valor("NVMe SSD RAID 10").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(networkSpeed).valor("25").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(osVersion).valor("VMware ESXi 8.0").build());
        recursos.add(ServidorRecurso.builder().servidor(srvVmHost02).recurso(powerSupply).valor("1600").unidad(watts).build());

        // SRV-DC-PRIMARY (Controlador de Dominio)
        Servidor srvDcPrimary = servidorRepository.findByNombreServidor("SRV-DC-PRIMARY").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(cpuModel).valor("Intel Xeon E-2274G").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(cpuCores).valor("4").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(cpuFreq).valor("4.0").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(ramTotal).valor("32").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(ramType).valor("DDR4 ECC").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(storageTotal).valor("1").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(storageType).valor("SSD RAID 1").build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(networkSpeed).valor("1").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvDcPrimary).recurso(osVersion).valor("Windows Server 2022").build());

        // SRV-BACKUP-VEEAM
        Servidor srvBackup = servidorRepository.findByNombreServidor("SRV-BACKUP-VEEAM").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(cpuModel).valor("Intel Xeon Silver 4214R").build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(cpuCores).valor("12").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(cpuFreq).valor("2.4").unidad(ghz).build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(ramTotal).valor("96").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(ramType).valor("DDR4 ECC").build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(storageTotal).valor("40").unidad(tb).build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(storageType).valor("HDD RAID 6 + SSD Cache").build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(networkSpeed).valor("10").unidad(gbps).build());
        recursos.add(ServidorRecurso.builder().servidor(srvBackup).recurso(osVersion).valor("Windows Server 2022").build());

        // Servidores virtuales - recursos más modestos
        // SRV-APP-SIAF
        Servidor srvAppSiaf = servidorRepository.findByNombreServidor("SRV-APP-SIAF").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvAppSiaf).recurso(cpuCores).valor("4").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvAppSiaf).recurso(ramTotal).valor("16").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvAppSiaf).recurso(storageTotal).valor("500").unidad(unidadRepository.findByNombreUnidad("Gigabyte").orElseThrow()).build());
        recursos.add(ServidorRecurso.builder().servidor(srvAppSiaf).recurso(storageType).valor("Virtual Disk").build());
        recursos.add(ServidorRecurso.builder().servidor(srvAppSiaf).recurso(osVersion).valor("Ubuntu Server 22.04 LTS").build());

        // SRV-WEB-PORTAL
        Servidor srvWebPortal = servidorRepository.findByNombreServidor("SRV-WEB-PORTAL").orElseThrow();
        recursos.add(ServidorRecurso.builder().servidor(srvWebPortal).recurso(cpuCores).valor("2").unidad(cores).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebPortal).recurso(ramTotal).valor("8").unidad(gbRam).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebPortal).recurso(storageTotal).valor("200").unidad(unidadRepository.findByNombreUnidad("Gigabyte").orElseThrow()).build());
        recursos.add(ServidorRecurso.builder().servidor(srvWebPortal).recurso(osVersion).valor("Ubuntu Server 22.04 LTS").build());

        servidorRecursoRepository.saveAll(recursos);
    }
}

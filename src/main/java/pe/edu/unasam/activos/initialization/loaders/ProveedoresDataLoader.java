package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.EstadoProveedor;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import pe.edu.unasam.activos.modules.proveedores.repository.ProveedorRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(19)
@RequiredArgsConstructor
public class ProveedoresDataLoader extends AbstractDataLoader {

    private final ProveedorRepository proveedorRepository;

    @Override
    protected String getLoaderName() {
        return "Proveedores";
    }

    @Override
    protected boolean shouldLoad() {
        return proveedorRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Proveedor> proveedores = new ArrayList<>();

        // Proveedores de Hardware
        proveedores.add(Proveedor.builder()
                .rucProveedor("20100070970")
                .nombreProveedor("Dell Perú")
                .razonSocial("Dell Computer de Peru S.A.C.")
                .direccion("Av. Victor Andres Belaunde 147, San Isidro, Lima")
                .telefono("01-2117700")
                .email("ventas.peru@dell.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20331066703")
                .nombreProveedor("HP Perú")
                .razonSocial("Hewlett Packard Peru S.R.L.")
                .direccion("Av. Jorge Basadre 592, San Isidro, Lima")
                .telefono("01-5136000")
                .email("contacto.peru@hp.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20508565934")
                .nombreProveedor("Lenovo Perú")
                .razonSocial("Lenovo Peru S.A.C.")
                .direccion("Av. República de Panamá 3591, San Isidro, Lima")
                .telefono("01-6157800")
                .email("ventas@lenovo.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20517912589")
                .nombreProveedor("Asus Perú")
                .razonSocial("ASUSTeK Computer Peru S.A.C.")
                .direccion("Calle Las Palmeras 235, San Isidro, Lima")
                .telefono("01-7056500")
                .email("info@asus.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20382036655")
                .nombreProveedor("Tottus")
                .razonSocial("Hipermercados Tottus S.A.")
                .direccion("Av. Angamos Este 1805, Surquillo, Lima")
                .telefono("01-6189999")
                .email("servicioalcliente@tottus.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Software
        proveedores.add(Proveedor.builder()
                .rucProveedor("20331038009")
                .nombreProveedor("Microsoft Perú")
                .razonSocial("Microsoft del Peru S.R.L.")
                .direccion("Av. República de Colombia 791, San Isidro, Lima")
                .telefono("01-7067000")
                .email("mslatam@microsoft.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20545738960")
                .nombreProveedor("Adobe Perú")
                .razonSocial("Adobe Systems Peru S.A.C.")
                .direccion("Av. Víctor Andrés Belaunde 214, San Isidro, Lima")
                .telefono("01-4213400")
                .email("adobe.peru@adobe.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20601582339")
                .nombreProveedor("Oracle Perú")
                .razonSocial("Oracle del Peru S.A.C.")
                .direccion("Av. El Derby 254, Santiago de Surco, Lima")
                .telefono("01-7115000")
                .email("oracle.latam@oracle.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Infraestructura de Red
        proveedores.add(Proveedor.builder()
                .rucProveedor("20269532933")
                .nombreProveedor("Cisco Perú")
                .razonSocial("Cisco Systems Peru S.A.C.")
                .direccion("Av. Camino Real 456, San Isidro, Lima")
                .telefono("01-7050000")
                .email("peru-info@cisco.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20517486836")
                .nombreProveedor("TP-Link Perú")
                .razonSocial("TP-Link Technologies Peru S.A.C.")
                .direccion("Av. Primavera 2160, Santiago de Surco, Lima")
                .telefono("01-7070707")
                .email("soporte@tp-link.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20602770801")
                .nombreProveedor("Ubiquiti Networks")
                .razonSocial("Ubiquiti Networks Peru S.A.C.")
                .direccion("Calle Los Negocios 151, Surquillo, Lima")
                .telefono("01-2420800")
                .email("peru@ui.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Seguridad
        proveedores.add(Proveedor.builder()
                .rucProveedor("20513076991")
                .nombreProveedor("ESET Latinoamérica")
                .razonSocial("ESET Latin America S.A.C.")
                .direccion("Av. Javier Prado Este 6210, La Molina, Lima")
                .telefono("01-5130700")
                .email("info@eset-la.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20508954123")
                .nombreProveedor("Kaspersky Perú")
                .razonSocial("Kaspersky Lab Peru S.A.C.")
                .direccion("Av. Benavides 1555, Miraflores, Lima")
                .telefono("01-6157777")
                .email("info@kaspersky.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Servicios Cloud
        proveedores.add(Proveedor.builder()
                .rucProveedor("20601523458")
                .nombreProveedor("Amazon Web Services")
                .razonSocial("Amazon Web Services Peru S.A.C.")
                .direccion("Av. La Encalada 1257, Santiago de Surco, Lima")
                .telefono("01-7001700")
                .email("aws-peru@amazon.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20545123789")
                .nombreProveedor("Google Cloud Perú")
                .razonSocial("Google Cloud Peru S.A.C.")
                .direccion("Av. Santo Toribio 143, San Isidro, Lima")
                .telefono("01-4129300")
                .email("cloud-peru@google.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Telecomunicaciones
        proveedores.add(Proveedor.builder()
                .rucProveedor("20100017491")
                .nombreProveedor("Claro Perú")
                .razonSocial("America Movil Peru S.A.C.")
                .direccion("Av. Benavides 1555, Miraflores, Lima")
                .telefono("0800-00200")
                .email("empresas@claro.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20100041700")
                .nombreProveedor("Movistar Perú")
                .razonSocial("Telefonica del Peru S.A.A.")
                .direccion("Av. Arequipa 1155, Lima")
                .telefono("0800-10123")
                .email("empresas@movistar.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20528745862")
                .nombreProveedor("Optical Networks")
                .razonSocial("Optical Networks Peru S.A.C.")
                .direccion("Av. Prolongación Iquitos 1840, Lince, Lima")
                .telefono("01-6309000")
                .email("ventas@opticalnetworks.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Suministros y Accesorios
        proveedores.add(Proveedor.builder()
                .rucProveedor("20512345678")
                .nombreProveedor("Intcomex Perú")
                .razonSocial("Intcomex Peru S.A.C.")
                .direccion("Av. Elmer Faucett 2823, Callao")
                .telefono("01-5186000")
                .email("peru@intcomex.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20100128056")
                .nombreProveedor("Distribuidora Crisol")
                .razonSocial("Tiendas Crisol S.A.C.")
                .direccion("Av. Petit Thouars 5225, Miraflores, Lima")
                .telefono("01-2422900")
                .email("ventas@crisol.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores de Impresión
        proveedores.add(Proveedor.builder()
                .rucProveedor("20259888934")
                .nombreProveedor("Canon Perú")
                .razonSocial("Canon del Peru S.A.")
                .direccion("Av. Caminos del Inca 1165, Santiago de Surco, Lima")
                .telefono("01-4135959")
                .email("info@canon.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20100169178")
                .nombreProveedor("Epson Perú")
                .razonSocial("Epson Peru S.A.")
                .direccion("Av. República de Panamá 3535, San Isidro, Lima")
                .telefono("01-7067000")
                .email("contacto@epson.com.pe")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        // Proveedores Locales de Soporte
        proveedores.add(Proveedor.builder()
                .rucProveedor("20401234567")
                .nombreProveedor("SysTech Solutions")
                .razonSocial("SysTech Solutions E.I.R.L.")
                .direccion("Jr. Fitzcarrald 456, Huaraz, Ancash")
                .telefono("043-422789")
                .email("contacto@systech-huaraz.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("20501876543")
                .nombreProveedor("CompuRepair Ancash")
                .razonSocial("CompuRepair Ancash S.A.C.")
                .direccion("Av. Luzuriaga 850, Huaraz, Ancash")
                .telefono("043-428900")
                .email("servicios@compurepair.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedores.add(Proveedor.builder()
                .rucProveedor("10439876542")
                .nombreProveedor("NetServices Huaraz")
                .razonSocial("Luis Alberto Rodriguez Martinez")
                .direccion("Jr. Sucre 321, Huaraz, Ancash")
                .telefono("043-425678")
                .email("netservices.hz@gmail.com")
                .estadoProveedor(EstadoProveedor.ACTIVO)
                .build());

        proveedorRepository.saveAll(proveedores);
    }
}

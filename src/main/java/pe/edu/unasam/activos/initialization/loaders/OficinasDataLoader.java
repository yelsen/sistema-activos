package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.EstadoOficina;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Departamento;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.domain.TipoOficina;
import pe.edu.unasam.activos.modules.ubicaciones.repository.DepartamentoRepository;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OficinaRepository;
import pe.edu.unasam.activos.modules.ubicaciones.repository.TipoOficinaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(14)
@RequiredArgsConstructor
public class OficinasDataLoader extends AbstractDataLoader {

    private final OficinaRepository oficinaRepository;
    private final DepartamentoRepository departamentoRepository;
    private final TipoOficinaRepository tipoOficinaRepository;

    @Override
    protected String getLoaderName() {
        return "Oficinas";
    }

    @Override
    protected boolean shouldLoad() {
        return oficinaRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        Map<String, Departamento> departamentos = departamentoRepository.findAll().stream()
                .collect(Collectors.toMap(Departamento::getNombreDepartamento, Function.identity()));
        Map<String, TipoOficina> tipoOficinas = tipoOficinaRepository.findAll().stream()
                .collect(Collectors.toMap(TipoOficina::getTipoOficina, Function.identity()));

        if (departamentos.isEmpty() || tipoOficinas.isEmpty()) {
            System.err.println(
                    "No se pudieron cargar las oficinas porque faltan dependencias (Departamentos o Tipos de Oficina).");
            return;
        }

        List<Oficina> oficinas = new ArrayList<>();

        // ================= ÓRGANOS DE GOBIERNO =================
        oficinas.add(crearOficina(
                "Asamblea Universitaria",
                "Ciudad Universitaria, Huaraz",
                "201",
                departamentos.get("Asamblea Universitaria"),
                tipoOficinas.get("Órgano de Gobierno")));

        oficinas.add(crearOficina(
                "Consejo Universitario",
                "Ciudad Universitaria, Huaraz",
                "202",
                departamentos.get("Consejo Universitario"),
                tipoOficinas.get("Órgano de Gobierno")));

        // ================= RECTORADO =================
        oficinas.add(crearOficina(
                "Oficina de Rectorado",
                "Pabellón Central, 2do Piso, Of. 201",
                "101",
                departamentos.get("Rectorado"),
                tipoOficinas.get("Rectorado")));

        // ================= VICERRECTORADOS =================
        oficinas.add(crearOficina(
                "Vicerrectorado Académico",
                "Pabellón Central, 2do Piso",
                "102",
                departamentos.get("Vicerrectorado Académico"),
                tipoOficinas.get("Vicerrectorado")));

        oficinas.add(crearOficina(
                "Vicerrectorado de Investigación",
                "Pabellón Central, 2do Piso",
                "103",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Vicerrectorado")));

        // Unidades bajo Vicerrectorado de Investigación
        oficinas.add(crearOficina(
                "Unidad de Cooperación Técnica",
                "Ciudad Universitaria, Huaraz",
                "104",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Investigación e Innovación",
                "Ciudad Universitaria, Huaraz",
                "105",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Unidad")));

        // Direcciones bajo Vicerrectorado Académico
        oficinas.add(crearOficina(
                "Dirección Académica de Estudios Generales",
                "Ciudad Universitaria, Huaraz",
                "106",
                departamentos.get("Vicerrectorado Académico"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Extensión Universitaria",
                "Ciudad Universitaria, Huaraz",
                "107",
                departamentos.get("Vicerrectorado Académico"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Proyección Social",
                "Ciudad Universitaria, Huaraz",
                "108",
                departamentos.get("Vicerrectorado Académico"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Seguimiento y Certificación del Egresado",
                "Ciudad Universitaria, Huaraz",
                "109",
                departamentos.get("Vicerrectorado Académico"),
                tipoOficinas.get("Dirección")));

        // Direcciones bajo Vicerrectorado de Investigación
        oficinas.add(crearOficina(
                "Dirección de Derechos de Autor y Patentes",
                "Ciudad Universitaria, Huaraz",
                "110",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Incubadoras",
                "Ciudad Universitaria, Huaraz",
                "111",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Repositorio",
                "Ciudad Universitaria, Huaraz",
                "112",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección del Instituto de Investigación",
                "Ciudad Universitaria, Huaraz",
                "113",
                departamentos.get("Vicerrectorado de Investigación"),
                tipoOficinas.get("Dirección")));

        // ================= ÓRGANOS DE CONTROL Y ASESORÍA =================
        oficinas.add(crearOficina(
                "Oficina General de Control Institucional",
                "Ciudad Universitaria, Huaraz",
                "120",
                departamentos.get("Oficina General de Control Institucional"),
                tipoOficinas.get("Oficina de Control")));

        oficinas.add(crearOficina(
                "Unidad de Acciones de Control",
                "Ciudad Universitaria, Huaraz",
                "121",
                departamentos.get("Oficina General de Control Institucional"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Actividades de Control",
                "Ciudad Universitaria, Huaraz",
                "122",
                departamentos.get("Oficina General de Control Institucional"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Asesoría Jurídica",
                "Ciudad Universitaria, Huaraz",
                "130",
                departamentos.get("Oficina General de Asesoría Jurídica"),
                tipoOficinas.get("Oficina de Asesoría")));

        oficinas.add(crearOficina(
                "Unidad de Asuntos Civiles y Penales",
                "Ciudad Universitaria, Huaraz",
                "131",
                departamentos.get("Oficina General de Asesoría Jurídica"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Procuraduría Universitaria",
                "Ciudad Universitaria, Huaraz",
                "140",
                departamentos.get("Procuraduría Universitaria"),
                tipoOficinas.get("Oficina de Asesoría")));

        // ================= SECRETARÍA GENERAL =================
        oficinas.add(crearOficina(
                "Secretaría General",
                "Pabellón Central, 2do Piso, Of. 202",
                "150",
                departamentos.get("Secretaría General"),
                tipoOficinas.get("Secretaría General")));

        oficinas.add(crearOficina(
                "Unidad de Actas y Resoluciones",
                "Pabellón Central, 2do Piso",
                "151",
                departamentos.get("Secretaría General"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Grados y Títulos",
                "Pabellón Central, 2do Piso",
                "152",
                departamentos.get("Secretaría General"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Trámite Documentario y Archivo Central",
                "Pabellón Central, 1er Piso",
                "153",
                departamentos.get("Secretaría General"),
                tipoOficinas.get("Unidad")));

        // ================= OFICINAS GENERALES DE APOYO =================
        oficinas.add(crearOficina(
                "Oficina General de Planificación y Presupuesto",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "160",
                departamentos.get("Oficina General de Planificación y Presupuesto"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Dirección de Planificación",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "161",
                departamentos.get("Oficina General de Planificación y Presupuesto"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Presupuesto",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "162",
                departamentos.get("Oficina General de Planificación y Presupuesto"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Desarrollo Institucional",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "163",
                departamentos.get("Oficina General de Planificación y Presupuesto"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Oficina General de Imagen Institucional",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "170",
                departamentos.get("Oficina General de Imagen Institucional"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Unidad de Comunicación y Relaciones Públicas",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "171",
                departamentos.get("Oficina General de Imagen Institucional"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Museo Universitario",
                "Ciudad Universitaria, Huaraz",
                "172",
                departamentos.get("Oficina General de Imagen Institucional"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Tecnologías de la Información, Sistemas y Estadística",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "180",
                departamentos.get("Oficina General de Tecnologías de Información, Sistemas y Estadística"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Unidad de Sistemas y Desarrollo de Software",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "181",
                departamentos.get("Oficina General de Tecnologías de Información, Sistemas y Estadística"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Soporte Técnico, Equipamiento y Comunicaciones",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "182",
                departamentos.get("Oficina General de Tecnologías de Información, Sistemas y Estadística"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Estadística",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "183",
                departamentos.get("Oficina General de Tecnologías de Información, Sistemas y Estadística"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Desarrollo Físico",
                "Ciudad Universitaria, Huaraz",
                "190",
                departamentos.get("Oficina General de Desarrollo Físico"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Unidad de Estudios, Obras y Mantenimiento de Infraestructura",
                "Ciudad Universitaria, Huaraz",
                "191",
                departamentos.get("Oficina General de Desarrollo Físico"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Supervisión y Liquidación de Obras",
                "Ciudad Universitaria, Huaraz",
                "192",
                departamentos.get("Oficina General de Desarrollo Físico"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad Formuladora de Mantenimiento y Reposición",
                "Ciudad Universitaria, Huaraz",
                "193",
                departamentos.get("Oficina General de Desarrollo Físico"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad Formuladora",
                "Ciudad Universitaria, Huaraz",
                "194",
                departamentos.get("Oficina General de Desarrollo Físico"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Pre Inversión",
                "Ciudad Universitaria, Huaraz",
                "195",
                departamentos.get("Oficina General de Pre Inversión"),
                tipoOficinas.get("Oficina General")));

        // ================= OFICINAS GENERALES ACADÉMICAS =================
        oficinas.add(crearOficina(
                "Oficina General de Admisión",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "200",
                departamentos.get("Oficina General de Admisión"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Oficina General de Estudios",
                "Ciudad Universitaria, Huaraz",
                "210",
                departamentos.get("Oficina General de Estudios"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Unidad de Registro y Certificación Académica",
                "Ciudad Universitaria, Huaraz",
                "211",
                departamentos.get("Oficina General de Estudios"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Control Académico",
                "Ciudad Universitaria, Huaraz",
                "212",
                departamentos.get("Oficina General de Estudios"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Calidad Universitaria",
                "Ciudad Universitaria, Huaraz",
                "220",
                departamentos.get("Oficina General de Calidad Universitaria"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Dirección de Gestión de la Calidad",
                "Ciudad Universitaria, Huaraz",
                "221",
                departamentos.get("Oficina General de Calidad Universitaria"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Dirección de Autoevaluación y Aseguramiento de la Calidad",
                "Ciudad Universitaria, Huaraz",
                "222",
                departamentos.get("Oficina General de Calidad Universitaria"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Oficina General de Servicios Académicos y Publicaciones",
                "Ciudad Universitaria, Huaraz",
                "230",
                departamentos.get("Oficina General de Servicios Académicos y Publicaciones"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Unidad de Biblioteca Central",
                "Ciudad Universitaria, Huaraz",
                "231",
                departamentos.get("Oficina General de Servicios Académicos y Publicaciones"),
                tipoOficinas.get("Biblioteca")));

        oficinas.add(crearOficina(
                "Unidad de Fondo Editorial Universitario",
                "Ciudad Universitaria, Huaraz",
                "232",
                departamentos.get("Oficina General de Servicios Académicos y Publicaciones"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Oficina General de Responsabilidad Social Universitaria",
                "Ciudad Universitaria, Huaraz",
                "240",
                departamentos.get("Oficina General de Responsabilidad Social Universitaria"),
                tipoOficinas.get("Oficina General")));

        oficinas.add(crearOficina(
                "Dirección de Responsabilidad Social",
                "Ciudad Universitaria, Huaraz",
                "241",
                departamentos.get("Oficina General de Responsabilidad Social Universitaria"),
                tipoOficinas.get("Dirección")));

        // ================= DIRECCIÓN GENERAL DE ADMINISTRACIÓN =================
        oficinas.add(crearOficina(
                "Dirección General de Administración",
                "Pabellón Central, 1er Piso",
                "250",
                departamentos.get("Dirección General de Administración"),
                tipoOficinas.get("Dirección General")));

        // --- Dirección de Gestión Financiera ---
        oficinas.add(crearOficina(
                "Dirección de Gestión Financiera",
                "Pabellón Central, 1er Piso",
                "260",
                departamentos.get("Dirección de Gestión Financiera"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Unidad de Tesorería",
                "Pabellón Central, 1er Piso",
                "261",
                departamentos.get("Dirección de Gestión Financiera"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Integración Contable",
                "Pabellón Central, 1er Piso",
                "262",
                departamentos.get("Dirección de Gestión Financiera"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Tributación",
                "Pabellón Central, 1er Piso",
                "263",
                departamentos.get("Dirección de Gestión Financiera"),
                tipoOficinas.get("Unidad")));

        // --- Dirección de Recursos Humanos ---
        oficinas.add(crearOficina(
                "Dirección de Recursos Humanos",
                "Ciudad Universitaria, Huaraz",
                "270",
                departamentos.get("Dirección de Recursos Humanos"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Unidad de Registro y Control de Personal",
                "Ciudad Universitaria, Huaraz",
                "271",
                departamentos.get("Dirección de Recursos Humanos"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Escalafón y Capacitación",
                "Ciudad Universitaria, Huaraz",
                "272",
                departamentos.get("Dirección de Recursos Humanos"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Remuneraciones y Pensiones",
                "Ciudad Universitaria, Huaraz",
                "273",
                departamentos.get("Dirección de Recursos Humanos"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Procesos de Selección",
                "Ciudad Universitaria, Huaraz",
                "274",
                departamentos.get("Dirección de Recursos Humanos"),
                tipoOficinas.get("Unidad")));

        // --- Dirección de Abastecimiento y Servicios Auxiliares ---
        oficinas.add(crearOficina(
                "Dirección de Abastecimiento y Servicios Auxiliares",
                "Ciudad Universitaria, Huaraz",
                "280",
                departamentos.get("Dirección de Abastecimiento y Servicios Auxiliares"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Unidad de Abastecimiento",
                "Ciudad Universitaria, Huaraz",
                "281",
                departamentos.get("Dirección de Abastecimiento y Servicios Auxiliares"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Patrimonio",
                "Ciudad Universitaria, Huaraz",
                "282",
                departamentos.get("Dirección de Abastecimiento y Servicios Auxiliares"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Servicios Auxiliares",
                "Ciudad Universitaria, Huaraz",
                "283",
                departamentos.get("Dirección de Abastecimiento y Servicios Auxiliares"),
                tipoOficinas.get("Unidad")));

        // --- Dirección de Bienestar Universitario ---
        oficinas.add(crearOficina(
                "Dirección de Bienestar Universitario",
                "Ciudad Universitaria, Huaraz",
                "290",
                departamentos.get("Dirección de Bienestar Universitario"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Unidad de Servicios Alimentarios",
                "Ciudad Universitaria, Huaraz",
                "291",
                departamentos.get("Dirección de Bienestar Universitario"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Salud",
                "Ciudad Universitaria, Huaraz",
                "292",
                departamentos.get("Dirección de Bienestar Universitario"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Servicio Social Universitario y Deportes",
                "Ciudad Universitaria, Huaraz",
                "293",
                departamentos.get("Dirección de Bienestar Universitario"),
                tipoOficinas.get("Unidad")));

        // --- Dirección de Gestión Ambiental y Bioseguridad ---
        oficinas.add(crearOficina(
                "Dirección de Gestión Ambiental y Bioseguridad",
                "Ciudad Universitaria, Huaraz",
                "300",
                departamentos.get("Dirección de Gestión Ambiental y Bioseguridad"),
                tipoOficinas.get("Dirección")));

        oficinas.add(crearOficina(
                "Unidad de Gestión Ambiental y Bioseguridad",
                "Ciudad Universitaria, Huaraz",
                "301",
                departamentos.get("Dirección de Gestión Ambiental y Bioseguridad"),
                tipoOficinas.get("Unidad")));

        oficinas.add(crearOficina(
                "Unidad de Defensa Civil",
                "Ciudad Universitaria, Huaraz",
                "302",
                departamentos.get("Dirección de Gestión Ambiental y Bioseguridad"),
                tipoOficinas.get("Unidad")));

        // ================= DIRECCIÓN GENERAL DE INVESTIGACIÓN =================
        oficinas.add(crearOficina(
                "Dirección General de Institutos de Investigación y Experimentación",
                "Ciudad Universitaria, Huaraz",
                "310",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Dirección General")));

        // Centros de Investigación y Experimentación
        oficinas.add(crearOficina(
                "Estación Experimental Pariacoto",
                "Pariacoto, Ancash",
                "311",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro Experimental")));

        oficinas.add(crearOficina(
                "Laboratorio de Calidad Ambiental",
                "Ciudad Universitaria, Huaraz",
                "312",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Laboratorio")));

        oficinas.add(crearOficina(
                "Instituto de Investigación Agropecuaria Santiago Antúnez de Mayolo - Tingua",
                "Tingua, Ancash",
                "313",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro de Investigación")));

        oficinas.add(crearOficina(
                "Centro Experimental Allpa Rumi",
                "Huaraz, Ancash",
                "314",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro Experimental")));

        oficinas.add(crearOficina(
                "Centro de Experimentación Ecológico Tuyu Ruri",
                "Huaraz, Ancash",
                "315",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro Experimental")));

        oficinas.add(crearOficina(
                "Centro de Investigación Agropecuaria Cañasbamba",
                "Cañasbamba, Ancash",
                "316",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro de Investigación")));

        oficinas.add(crearOficina(
                "Plantas Concentradoras de Minerales",
                "Ciudad Universitaria, Huaraz",
                "317",
                departamentos.get("Dirección General de Institutos de Investigación y Experimentación"),
                tipoOficinas.get("Centro de Producción")));

        // ================= CENTROS ACADÉMICOS =================
        oficinas.add(crearOficina(
                "Escuela de Post Grado",
                "Ciudad Universitaria, Huaraz",
                "320",
                departamentos.get("Escuela de Post Grado"),
                tipoOficinas.get("Escuela de Postgrado")));

        oficinas.add(crearOficina(
                "Centro de Idiomas",
                "Ciudad Universitaria, Huaraz",
                "330",
                departamentos.get("Centro de Idiomas"),
                tipoOficinas.get("Centro Académico")));

        oficinas.add(crearOficina(
                "Centro Pre Universitario",
                "Av. Centenario N.º 200, Independencia - Huaraz",
                "340",
                departamentos.get("Centro Pre Universitario"),
                tipoOficinas.get("Centro Académico")));



        // ================= FACULTADES Y ESCUELAS =================
        // --- Facultad de Ciencias ---
        Departamento facCiencias = departamentos.get("Facultad de Ciencias");
        oficinas.add(crearOficina(
                "Decanato de Ciencias",
                "Facultad de Ciencias, 2do Piso",
                "401",
                facCiencias,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Matemática",
                "Facultad de Ciencias, 1er Piso",
                "411",
                facCiencias,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Estadística e Informática",
                "Facultad de Ciencias, 1er Piso",
                "412",
                facCiencias,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Ing. de Sistemas e Informática",
                "Facultad de Ciencias, 1er Piso",
                "413",
                facCiencias,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ingeniería Civil ---
        Departamento facIngCivil = departamentos.get("Facultad de Ingeniería Civil");
        oficinas.add(crearOficina(
                "Decanato de Ingeniería Civil",
                "Facultad de Ing. Civil, 2do Piso",
                "501",
                facIngCivil,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería Civil",
                "Facultad de Ing. Civil, 1er Piso",
                "511",
                facIngCivil,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Arquitectura y Urbanismo",
                "Facultad de Ing. Civil, 1er Piso",
                "512",
                facIngCivil,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Derecho y Ciencias Políticas ---
        Departamento facDerecho = departamentos.get("Facultad de Derecho y Ciencias Políticas");
        oficinas.add(crearOficina(
                "Decanato de Derecho y Ciencias Políticas",
                "Local Central, 2do Piso",
                "601",
                facDerecho,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Derecho y Ciencias Políticas",
                "Local Central, 1er Piso",
                "611",
                facDerecho,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Administración y Turismo ---
        Departamento facAdmon = departamentos.get("Facultad de Administración y Turismo");
        oficinas.add(crearOficina(
                "Decanato de Administración y Turismo",
                "Facultad de ADTUR, 2do Piso",
                "701",
                facAdmon,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Administración",
                "Facultad de ADTUR, 1er Piso",
                "711",
                facAdmon,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Turismo",
                "Facultad de ADTUR, 1er Piso",
                "712",
                facAdmon,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ciencias Médicas ---
        Departamento facMedicina = departamentos.get("Facultad de Ciencias Médicas");
        oficinas.add(crearOficina(
                "Decanato de Ciencias Médicas",
                "Pabellón de Medicina, 2do Piso",
                "801",
                facMedicina,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Enfermería",
                "Pabellón de Medicina, 1er Piso",
                "811",
                facMedicina,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Obstetricia",
                "Pabellón de Medicina, 1er Piso",
                "812",
                facMedicina,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ciencias Agrarias ---
        Departamento facAgrarias = departamentos.get("Facultad de Ciencias Agrarias");
        oficinas.add(crearOficina(
                "Decanato de Ciencias Agrarias",
                "Pabellón de Agrarias, 2do Piso",
                "901",
                facAgrarias,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Agronomía",
                "Pabellón de Agrarias, 1er Piso",
                "911",
                facAgrarias,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería Agrícola",
                "Pabellón de Agrarias, 1er Piso",
                "912",
                facAgrarias,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ciencias del Ambiente ---
        Departamento facAmbiente = departamentos.get("Facultad de Ciencias del Ambiente");
        oficinas.add(crearOficina(
                "Decanato de Ciencias del Ambiente",
                "Pabellón de Ambiente, 2do Piso",
                "1001",
                facAmbiente,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería Ambiental",
                "Pabellón de Ambiente, 1er Piso",
                "1011",
                facAmbiente,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería Sanitaria",
                "Pabellón de Ambiente, 1er Piso",
                "1012",
                facAmbiente,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Economía y Contabilidad ---
        Departamento facEconomia = departamentos.get("Facultad de Economía y Contabilidad");
        oficinas.add(crearOficina(
                "Decanato de Economía y Contabilidad",
                "Pabellón de Economía, 2do Piso",
                "1101",
                facEconomia,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Economía",
                "Pabellón de Economía, 1er Piso",
                "1111",
                facEconomia,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Contabilidad",
                "Pabellón de Economía, 1er Piso",
                "1112",
                facEconomia,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ingeniería de Minas, Geología y Metalurgia ---
        Departamento facMinas = departamentos.get("Facultad de Ingeniería de Minas, Geología y Metalurgia");
        oficinas.add(crearOficina(
                "Decanato de FIGMM",
                "Pabellón de Minas, 2do Piso",
                "1201",
                facMinas,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería de Minas",
                "Pabellón de Minas, 1er Piso",
                "1211",
                facMinas,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ingeniería de Industrias Alimentarias ---
        Departamento facAlimentarias = departamentos.get("Facultad de Ingeniería de Industrias Alimentarias");
        oficinas.add(crearOficina(
                "Decanato de Ingeniería de Industrias Alimentarias",
                "Pabellón de FIIA, 2do Piso",
                "1301",
                facAlimentarias,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Ingeniería de Industrias Alimentarias",
                "Pabellón de FIIA, 1er Piso",
                "1311",
                facAlimentarias,
                tipoOficinas.get("Escuela Profesional")));
        oficinas.add(crearOficina(
                "Escuela de Ingeniería de Industrial",
                "Pabellón de FIIA, 1er Piso",
                "1312",
                facAlimentarias,
                tipoOficinas.get("Escuela Profesional")));

        // --- Facultad de Ciencias Sociales y Educación ---
        Departamento facSociales = departamentos.get("Facultad de Ciencias Sociales y Educación Comunicación");
        oficinas.add(crearOficina(
                "Decanato de Ciencias Sociales y Educación Comunicación",
                "Pabellón de Sociales, 2do Piso",
                "1401",
                facSociales,
                tipoOficinas.get("Decanato")));

        oficinas.add(crearOficina(
                "Escuela de Arqueología",
                "Pabellón de Sociales, 1er Piso",
                "1411",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Ciencias de la Comunicación",
                "Pabellón de Sociales, 1er Piso",
                "1413",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Comunicación lingüística y literatura",
                "Pabellón de Sociales, 1er Piso",
                "1414",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Educación Primaria Bilingüe Intercultural",
                "Pabellón de Sociales, 1er Piso",
                "1415",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Lengua extranjera Ingles",
                "Pabellón de Sociales, 1er Piso",
                "1416",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinas.add(crearOficina(
                "Escuela de Matemática e Informática",
                "Pabellón de Sociales, 1er Piso",
                "1417",
                facSociales,
                tipoOficinas.get("Escuela Profesional")));

        oficinaRepository.saveAll(oficinas.stream().filter(o -> o != null).collect(Collectors.toList()));
    }

    private Oficina crearOficina(String nombre, String direccion, String telefono, Departamento depto,
            TipoOficina tipo) {
        if (depto == null || tipo == null) {
            System.err.println("Advertencia: No se pudo crear la oficina '" + nombre
                    + "' porque el departamento o tipo es nulo (no se encontró en la base de datos).");
            return null;
        }
        return Oficina.builder()
                .nombreOficina(nombre)
                .direccionOficina(direccion)
                .telefonoOficina(telefono)
                .estadoOficina(EstadoOficina.ACTIVO)
                .departamento(depto)
                .tipoOficina(tipo)
                .build();
    }
}

package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.Cargo;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Responsable;
import pe.edu.unasam.activos.modules.personas.dto.ResponsableDTO;
import pe.edu.unasam.activos.modules.personas.repository.CargoRepository;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.personas.repository.ResponsableRepository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OficinaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ResponsableService {

    private final ResponsableRepository responsableRepository;
    private final PersonaRepository personaRepository;
    private final CargoRepository cargoRepository;
    private final OficinaRepository oficinaRepository;

    @Transactional(readOnly = true)
    public Page<ResponsableDTO.Response> getAllResponsables(Pageable pageable) {
        return responsableRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public ResponsableDTO.Response getResponsableById(Integer id) {
        return responsableRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));
    }

    public ResponsableDTO.Response createResponsable(ResponsableDTO.Request request) {
        Persona persona = personaRepository.findById(request.getDniPersona())
                .orElseThrow(() -> new NotFoundException("Persona no encontrada con DNI: " + request.getDniPersona()));
        Cargo cargo = cargoRepository.findById(request.getIdCargo())
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + request.getIdCargo()));
        Oficina oficina = oficinaRepository.findById(request.getIdOficina())
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + request.getIdOficina()));

        Responsable responsable = new Responsable();
        mapToEntity(responsable, request, persona, cargo, oficina);

        return convertToDto(responsableRepository.save(responsable));
    }

    public ResponsableDTO.Response updateResponsable(Integer id, ResponsableDTO.Request request) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado con ID: " + id));

        Persona persona = personaRepository.findById(request.getDniPersona())
                .orElseThrow(() -> new NotFoundException("Persona no encontrada con DNI: " + request.getDniPersona()));
        Cargo cargo = cargoRepository.findById(request.getIdCargo())
                .orElseThrow(() -> new NotFoundException("Cargo no encontrado con ID: " + request.getIdCargo()));
        Oficina oficina = oficinaRepository.findById(request.getIdOficina())
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + request.getIdOficina()));

        mapToEntity(responsable, request, persona, cargo, oficina);

        return convertToDto(responsableRepository.save(responsable));
    }

    public void deleteResponsable(Integer id) {
        responsableRepository.deleteById(id);
    }

    private void mapToEntity(Responsable responsable, ResponsableDTO.Request request, Persona persona, Cargo cargo, Oficina oficina) {
        responsable.setPersona(persona);
        responsable.setCargo(cargo);
        responsable.setOficina(oficina);
        responsable.setFechaAsignacion(request.getFechaAsignacion());
        responsable.setFechaFinAsignacion(request.getFechaFinAsignacion());
        responsable.setEstadoResponsable(request.getEstadoResponsable());
    }

    private ResponsableDTO.Response convertToDto(Responsable responsable) {
        return new ResponsableDTO.Response(
                responsable.getIdResponsable(),
                responsable.getPersona().getNombres() + " " + responsable.getPersona().getApellidos(),
                responsable.getPersona().getDni(),
                responsable.getCargo().getNombreCargo(),
                responsable.getOficina().getNombreOficina(),
                responsable.getFechaAsignacion(),
                responsable.getFechaFinAsignacion(),
                responsable.getEstadoResponsable()
        );
    }
}

package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.EspecialidadTecnico;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.domain.Tecnico;
import pe.edu.unasam.activos.modules.personas.dto.TecnicoDTO;
import pe.edu.unasam.activos.modules.personas.repository.EspecialidadTecnicoRepository;
import pe.edu.unasam.activos.modules.personas.repository.TecnicoRepository;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final PersonaRepository personaRepository;
    private final EspecialidadTecnicoRepository especialidadRepository;

    @Transactional(readOnly = true)
    public Page<TecnicoDTO.Response> getAllTecnicos(Pageable pageable) {
        return tecnicoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public TecnicoDTO.Response getTecnicoById(Integer id) {
        return tecnicoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Técnico no encontrado con ID: " + id));
    }

    public TecnicoDTO.Response createTecnico(TecnicoDTO.Request request) {
        if (!tecnicoRepository.findByPersona_Dni(request.getDniPersona()).isEmpty()) {
            throw new BusinessException(
                    "La persona con DNI " + request.getDniPersona() + " ya está registrada como técnico.");
        }

        Persona persona = personaRepository.findById(request.getDniPersona())
                .orElseThrow(() -> new NotFoundException(
                        "Persona no encontrada con documento: " + request.getDniPersona()));
        EspecialidadTecnico especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new NotFoundException(
                        "Especialidad no encontrada con ID: " + request.getIdEspecialidad()));

        Tecnico tecnico = new Tecnico();
        mapToEntity(tecnico, persona, especialidad);

        return convertToDto(tecnicoRepository.save(tecnico));
    }

    public TecnicoDTO.Response updateTecnico(Integer id, TecnicoDTO.Request request) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Técnico no encontrado con ID: " + id));

        // Validar que la persona no esté asignada a OTRO técnico
        tecnicoRepository.findByPersona_Dni(request.getDniPersona()).stream()
                .filter(t -> !t.getIdTecnico().equals(id))
                .findFirst()
                .ifPresent(t -> {
                    throw new BusinessException("La persona con DNI " + request.getDniPersona()
                            + " ya está registrada como otro técnico.");
                });

        Persona persona = personaRepository.findById(request.getDniPersona())
                .orElseThrow(() -> new NotFoundException(
                        "Persona no encontrada con documento: " + request.getDniPersona()));
        EspecialidadTecnico especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new NotFoundException(
                        "Especialidad no encontrada con ID: " + request.getIdEspecialidad()));

        mapToEntity(tecnico, persona, especialidad);
        return convertToDto(tecnicoRepository.save(tecnico));
    }

    public void deleteTecnico(Integer id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new NotFoundException("Técnico no encontrado con ID: " + id);
        }
        tecnicoRepository.deleteById(id);
    }

    private void mapToEntity(Tecnico tecnico, Persona persona, EspecialidadTecnico especialidad) {
        tecnico.setPersona(persona);
        tecnico.setEspecialidadTecnico(especialidad);
    }

    private TecnicoDTO.Response convertToDto(Tecnico tecnico) {
        return new TecnicoDTO.Response(
                tecnico.getIdTecnico(),
                tecnico.getPersona().getNombres() + " " + tecnico.getPersona().getApellidos(),
                tecnico.getPersona().getDni(),
                tecnico.getEspecialidadTecnico().getNombreEspecialidad());
    }
}

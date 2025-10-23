package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.personas.domain.EspecialidadTecnico;
import pe.edu.unasam.activos.modules.personas.dto.EspecialidadTecnicoDTO;
import pe.edu.unasam.activos.modules.personas.repository.EspecialidadTecnicoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EspecialidadTecnicoService {

    private final EspecialidadTecnicoRepository especialidadRepository;

    @Transactional(readOnly = true)
    public Page<EspecialidadTecnicoDTO.Response> getAllEspecialidades(Pageable pageable) {
        return especialidadRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<EspecialidadTecnicoDTO.Response> getAllEspecialidadesAsList() {
        return especialidadRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EspecialidadTecnicoDTO.Response getEspecialidadById(Integer id) {
        return especialidadRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada con ID: " + id));
    }

    public EspecialidadTecnicoDTO.Response createEspecialidad(EspecialidadTecnicoDTO.Request request) {
        if (especialidadRepository.existsByNombreEspecialidad(request.getNombreEspecialidad())) {
            throw new BusinessException("La especialidad '" + request.getNombreEspecialidad() + "' ya existe.");
        }
        EspecialidadTecnico especialidad = new EspecialidadTecnico();
        especialidad.setNombreEspecialidad(request.getNombreEspecialidad());
        return convertToDto(especialidadRepository.save(especialidad));
    }

    public EspecialidadTecnicoDTO.Response updateEspecialidad(Integer id, EspecialidadTecnicoDTO.Request request) {
        EspecialidadTecnico especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada con ID: " + id));

        especialidad.setNombreEspecialidad(request.getNombreEspecialidad());
        return convertToDto(especialidadRepository.save(especialidad));
    }

    public void deleteEspecialidad(Integer id) {
        especialidadRepository.deleteById(id);
    }

    private EspecialidadTecnicoDTO.Response convertToDto(EspecialidadTecnico especialidad) {
        return new EspecialidadTecnicoDTO.Response(especialidad.getIdEspecialidadTecnico(), especialidad.getNombreEspecialidad());
    }
}

package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.personas.domain.TipoDocumento;

import pe.edu.unasam.activos.modules.personas.repository.TipoDocumentoRepository;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Transactional(readOnly = true)
    public List<TipoDocumento> findAllTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }

}
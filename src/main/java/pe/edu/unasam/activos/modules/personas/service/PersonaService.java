package pe.edu.unasam.activos.modules.personas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.dto.PersonaDTO;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<PersonaDTO.Response> findPersonasSinUsuario() {
        List<String> documentosConUsuario = usuarioRepository.findAll().stream()
                .map(usuario -> usuario.getPersona().getNumeroDocumento())
                .collect(Collectors.toList());

        List<Persona> personasSinUsuario;
        if (documentosConUsuario.isEmpty()) {
            personasSinUsuario = personaRepository.findAll();
        } else {
            personasSinUsuario = personaRepository.findByNumeroDocumentoNotIn(documentosConUsuario);
        }

        return personasSinUsuario.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PersonaDTO.Response> findPersonasSinUsuarioPaginado(String query, Pageable pageable) {
        String searchTerm = (query == null) ? "" : query;
        return personaRepository.findPersonasSinUsuarioPaginado(searchTerm, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<PersonaDTO.PersonaUsuarioResponse> findPersonaParaUsuario(String numeroDocumento) {
        Optional<Persona> personaOpt = personaRepository.findById(numeroDocumento);
        if (personaOpt.isEmpty()) {
            return Optional.empty();
        }

        Persona persona = personaOpt.get();
        boolean tieneUsuario = usuarioRepository.existsByPersona_NumeroDocumento(numeroDocumento);

        var response = new PersonaDTO.PersonaUsuarioResponse(
                persona.getNombres(), persona.getApellidos(), persona.getEmail(), tieneUsuario
        );
        return Optional.of(response);
    }

    private PersonaDTO.Response convertToDto(Persona persona) {
        return PersonaDTO.Response.builder()
                .numeroDocumento(persona.getNumeroDocumento())
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .tipoDocumento(persona.getTipoDocumento() != null ? persona.getTipoDocumento().getTipoDocumento() : null)
                .email(persona.getEmail())
                .build();
    }

}
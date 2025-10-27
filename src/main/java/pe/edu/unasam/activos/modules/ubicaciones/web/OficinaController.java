package pe.edu.unasam.activos.modules.ubicaciones.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ubicaciones/oficinas")
public class OficinaController {

 
}

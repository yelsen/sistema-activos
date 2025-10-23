package pe.edu.unasam.activos.common.exception;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pe.edu.unasam.activos.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request, HttpServletRequest httpRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException ex, WebRequest request, HttpServletRequest httpRequest) {
        String requestURI = httpRequest.getRequestURI();
        boolean isApiCall = requestURI.startsWith("/api/") || "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));

        if (isApiCall) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.FORBIDDEN.value())
                    .error("Forbidden")
                    .message("No tiene permiso para acceder a este recurso.")
                    .path(request.getDescription(false))
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } else {
            // Para peticiones de navegador, es mejor mostrar una página de error 403.
            ModelAndView modelAndView = new ModelAndView("error/403"); // Asumiendo que tienes una vista error/403.html
            modelAndView.addObject("path", requestURI);
            modelAndView.setStatus(HttpStatus.FORBIDDEN);
            return modelAndView;
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ReferentialIntegrityException.class)
    public ResponseEntity<ErrorResponse> handleReferentialIntegrityException(ReferentialIntegrityException ex, WebRequest request) {
        log.warn("Conflicto de integridad referencial: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .error("Not Acceptable")
                .message("El tipo de medio no es aceptable. Se esperaba application/json.")
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, WebRequest webRequest, HttpServletRequest httpRequest) {
        boolean isDevelopment = environment.acceptsProfiles(org.springframework.core.env.Profiles.of("dev"));
        
        String errorMessage = "Ocurrió un error inesperado en el servidor. Por favor, contacte al administrador.";
        String debugInfo = null;

        if (isDevelopment) {
            errorMessage = ex.getMessage();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            debugInfo = sw.toString();
            log.error("Error inesperado capturado por GlobalExceptionHandler (DEV): ", ex);
        } else {
            log.error("Error inesperado capturado por GlobalExceptionHandler (PROD): {}", ex.getMessage());
        }

        // ¿El cliente quiere HTML o JSON?
        String acceptHeader = httpRequest.getHeader("Accept");
        boolean wantsHtml = acceptHeader != null && acceptHeader.contains("text/html");
        boolean isAjax = "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));

        // Si la petición es AJAX o pide JSON, siempre devolver JSON. Si no, devolver HTML.
        if (wantsHtml && !isAjax) {
            ModelAndView modelAndView = new ModelAndView("error/error");
            modelAndView.addObject("timestamp", LocalDateTime.now());
            modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            modelAndView.addObject("error", "Internal Server Error");
            modelAndView.addObject("message", errorMessage);
            modelAndView.addObject("path", webRequest.getDescription(false));
            modelAndView.addObject("trace", debugInfo); // La vista usará 'trace'
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return modelAndView;
        } else {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error("Internal Server Error")
                    .message(errorMessage)
                    .path(webRequest.getDescription(false))
                    .debugInfo(debugInfo)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

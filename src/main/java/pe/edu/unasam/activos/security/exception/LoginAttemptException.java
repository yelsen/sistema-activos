package pe.edu.unasam.activos.security.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginAttemptException extends AuthenticationException {
    public LoginAttemptException(String msg) {
        super(msg);
    }
}

/**
 * Session Timeout Handler
 *
 * Este script gestiona el cierre de sesión por inactividad del lado del cliente.
 * Muestra un contador, una advertencia modal y cierra la sesión si el usuario está inactivo.
 */
document.addEventListener('DOMContentLoaded', function () {
    const INACTIVITY_TIMEOUT_MINUTES = 15; // Debe coincidir con server.servlet.session.timeout
    const WARNING_TIME_SECONDS = 60; // Mostrar advertencia 1 minuto antes

    let countdownInterval;
    let remainingTime;

    const modalElement = document.getElementById('session-timeout-modal');
    if (!modalElement) {
        console.error('El modal con ID "session-timeout-modal" no se encontró en el DOM.');
        return;
    }
    const modal = new bootstrap.Modal(modalElement);
    const countdownElement = document.getElementById('session-countdown');
    const keepSessionBtn = document.getElementById('keep-session-btn');
    const headerTimerElement = document.getElementById('session-timer');
    const logoutBtn = document.getElementById('logout-now-btn');

    function startInactivityTimer() {
        clearTimeout(inactivityTimer);
        clearInterval(countdownTimer);

        inactivityTimer = setTimeout(showWarningModal, (INACTIVITY_TIMEOUT_MINUTES * 60 - WARNING_TIME_SECONDS) * 1000);
        remainingTime = INACTIVITY_TIMEOUT_MINUTES * 60;

        // Iniciar el contador visual en el header
        if (headerTimerElement) {
            updateHeaderCountdown();
            countdownTimer = setInterval(() => {
                remainingTime--;
                updateHeaderCountdown();
                if (remainingTime <= 0) {
                    // El temporizador principal se encargará del logout
                    clearInterval(countdownTimer);
                }
            }, 1000);
        }
    }

    function showWarningModal() {
        remainingTime = WARNING_TIME_SECONDS;
        updateCountdown();
        modal.show();

        let modalCountdown = setInterval(() => {
            remainingTime--;
            updateCountdown();
            if (remainingTime <= 0) {
                logout();
            }
        }, 1000);
    }

    function updateCountdown() {
        if (countdownElement) {
            countdownElement.textContent = remainingTime;
        }
    }

    function updateHeaderCountdown() {
        if (headerTimerElement) {
            const minutes = Math.floor(remainingTime / 60);
            const seconds = remainingTime % 60;
            headerTimerElement.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
        }
    }

    function resetInactivityTimer() {
        // El usuario interactuó, reiniciamos el temporizador.
        modal.hide();
        startInactivityTimer();
    }

    function stopInactivityTimer() {
        clearInterval(countdownInterval);
    }

    function keepSessionAlive() {
        // Realiza una llamada ligera al servidor para renovar la sesión.
        // Una llamada a un endpoint vacío es suficiente.
        fetch('/api/ping') // Necesitarás crear este endpoint
            .then(response => {
                if (response.ok) {
                    console.log('Sesión extendida.');
                    resetInactivityTimer();
                } else {
                    // Si la sesión ya expiró en el backend, redirigir al login
                    logout();
                }
            })
            .catch(() => {
                // En caso de error de red, es más seguro hacer logout
                console.error('Error al intentar mantener la sesión activa.');
                logout();
            });
    }

    function logout() {
        stopInactivityTimer();
        window.location.href = '/logout';
    }

    // Eventos que reinician el contador de inactividad
    ['click', 'mousemove', 'keydown', 'scroll', 'touchstart'].forEach(event => {
        document.addEventListener(event, resetInactivityTimer, { passive: true });
    });

    // Botones del modal
    if (keepSessionBtn) {
        keepSessionBtn.addEventListener('click', keepSessionAlive);
    }
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }

    // Iniciar el temporizador cuando la página carga
    startInactivityTimer();
});

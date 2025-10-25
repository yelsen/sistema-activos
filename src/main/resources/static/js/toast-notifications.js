/**
 * SISTEMA DE NOTIFICACIONES TOAST PERSONALIZADO
 * Sin dependencias externas - Solo JavaScript Vanilla
 */

// Crear el contenedor de toasts si no existe
function initToastContainer() {
    if (!document.getElementById('custom-toast-container')) {
        const container = document.createElement('div');
        container.id = 'custom-toast-container';
        container.className = 'custom-toast-container';
        document.body.appendChild(container);
    }
}

/**
 * Muestra una notificación toast
 * @param {string} message - El mensaje a mostrar
 * @param {string} type - Tipo: 'success', 'error', 'warning', 'info'
 * @param {number} duration - Duración en milisegundos (por defecto 5000)
 */
function showToast(message, type = 'info', duration = 5000) {
    initToastContainer();

    const styles = {
        success: {
            background: 'linear-gradient(135deg, #22c55e 0%, #16a34a 100%)',
            borderColor: '#22c55e',
            icon: `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>`,
            title: '¡Éxito!'
        },
        error: {
            background: 'linear-gradient(135deg, #ef4444 0%, #dc2626 100%)',
            borderColor: '#ef4444',
            icon: `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>`,
            title: 'Error'
        },
        warning: {
            background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)',
            borderColor: '#f59e0b',
            icon: `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>`,
            title: 'Advertencia'
        },
        info: {
            background: 'linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)',
            borderColor: '#3b82f6',
            icon: `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>`,
            title: 'Información'
        }
    };
    

    const style = styles[type] || styles.info;

    // Crear el elemento toast
    const toast = document.createElement('div');
    toast.className = 'custom-toast custom-toast-enter';
    toast.style.background = style.background;
    toast.style.border = `2px solid ${style.borderColor}`;

    // Escape y formato de saltos de línea para mensajes multilínea
    const escapeHtml = (str) => String(str).replace(/[&<>"']/g, (c) => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;','\'':'&#039;'}[c]));
    const messageHtml = escapeHtml(message).replace(/\n/g, '<br>');

    toast.innerHTML = `
        <div class="custom-toast-content">
            <div class="custom-toast-icon">${style.icon}</div>
            <div class="custom-toast-text">
                <div class="custom-toast-title">${style.title}</div>
                <div class="custom-toast-message">${messageHtml}</div>
            </div>
            <button class="custom-toast-close" aria-label="Cerrar">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
            </button>
        </div>
    `;

    
    // Agregar al contenedor
    const container = document.getElementById('custom-toast-container');
    container.appendChild(toast);

    // Animación de entrada
    setTimeout(() => {
        toast.classList.remove('custom-toast-enter');
        toast.classList.add('custom-toast-visible');
    }, 10);

    // Función para cerrar el toast
    const closeToast = () => {
        toast.classList.remove('custom-toast-visible');
        toast.classList.add('custom-toast-exit');
        setTimeout(() => {
            if (toast.parentElement) {
                toast.parentElement.removeChild(toast);
            }
        }, 300);
    };

    // Botón de cerrar
    const closeBtn = toast.querySelector('.custom-toast-close');
    closeBtn.addEventListener('click', closeToast);

    // Auto-cerrar después de la duración
    setTimeout(closeToast, duration);
}

// Atajos rápidos
const Toast = {
    success: (msg, duration) => showToast(msg, 'success', duration),
    error: (msg, duration) => showToast(msg, 'error', duration),
    warning: (msg, duration) => showToast(msg, 'warning', duration),
    info: (msg, duration) => showToast(msg, 'info', duration)
};

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initToastContainer);
} else {
    initToastContainer();
}

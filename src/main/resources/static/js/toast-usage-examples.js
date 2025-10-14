/**
 * ========================================
 * EJEMPLOS DE USO DEL SISTEMA DE TOASTS
 * ========================================
 * 
 * Este archivo contiene ejemplos de cómo usar el sistema de notificaciones toast.
 * NO es necesario incluir este archivo en producción, es solo referencia.
 */

// ============================================
// 1. USO BÁSICO - Función showToast()
// ============================================

// Éxito
showToast('Operación completada correctamente', 'success');

// Error
showToast('Ocurrió un error al procesar la solicitud', 'error');

// Advertencia
showToast('Por favor verifica los datos ingresados', 'warning');

// Información
showToast('Tienes 3 notificaciones nuevas', 'info');


// ============================================
// 2. USO CON ATAJOS - Objeto Toast
// ============================================

Toast.success('Usuario creado exitosamente');
Toast.error('No se pudo conectar al servidor');
Toast.warning('El formulario tiene campos vacíos');
Toast.info('Hay actualizaciones disponibles');


// ============================================
// 3. PERSONALIZAR POSICIÓN Y DURACIÓN
// ============================================

// Mostrar en esquina superior derecha por 3 segundos
showToast('Mensaje temporal', 'info', {
    gravity: 'top',
    position: 'right',
    duration: 3000
});

// Mostrar en el centro inferior
showToast('Mensaje centrado', 'success', {
    gravity: 'bottom',
    position: 'center',
    duration: 4000
});


// ============================================
// 4. CAMBIAR POSICIÓN GLOBAL
// ============================================

// Cambiar todas las notificaciones a top-right
setToastPosition('top', 'right');

// Cambiar todas las notificaciones a bottom-center
setToastPosition('bottom', 'center');

// Volver a bottom-left (configuración por defecto)
setToastPosition('bottom', 'left');


// ============================================
// 5. INTEGRACIÓN CON FORMULARIOS
// ============================================

// Ejemplo con un formulario
document.getElementById('myForm')?.addEventListener('submit', function(e) {
    e.preventDefault();
    
    // Validar campos
    const nombre = document.getElementById('nombre').value;
    if (!nombre) {
        Toast.warning('El campo nombre es requerido');
        return;
    }
    
    // Simular guardado
    fetch('/api/guardar', {
        method: 'POST',
        body: JSON.stringify({ nombre })
    })
    .then(response => {
        if (response.ok) {
            Toast.success('Datos guardados correctamente');
        } else {
            Toast.error('Error al guardar los datos');
        }
    })
    .catch(error => {
        Toast.error('Error de conexión con el servidor');
    });
});


// ============================================
// 6. MENSAJES DESDE EL SERVIDOR (Spring Boot)
// ============================================

/**
 * En tu controlador Spring Boot:
 * 
 * @PostMapping("/guardar")
 * public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
 *     try {
 *         usuarioService.guardar(usuario);
 *         redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
 *     } catch (Exception e) {
 *         redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario");
 *     }
 *     return "redirect:/usuarios";
 * }
 * 
 * En tu HTML (ya está implementado en index.html):
 * 
 * <div id="flash-messages" style="display: none;"
 *      th:attr="data-success=${success}, data-error=${error}, 
 *               data-warning=${warning}, data-info=${info}">
 * </div>
 * 
 * <script>
 *     document.addEventListener('DOMContentLoaded', function () {
 *         const flashContainer = document.getElementById('flash-messages');
 *         if (flashContainer) {
 *             if (flashContainer.dataset.success) showToast(flashContainer.dataset.success, 'success');
 *             if (flashContainer.dataset.error) showToast(flashContainer.dataset.error, 'error');
 *             if (flashContainer.dataset.warning) showToast(flashContainer.dataset.warning, 'warning');
 *             if (flashContainer.dataset.info) showToast(flashContainer.dataset.info, 'info');
 *         }
 *     });
 * </script>
 */


// ============================================
// 7. POSICIONES DISPONIBLES
// ============================================

/**
 * GRAVITY (Vertical):
 * - 'top'    → Parte superior de la pantalla
 * - 'bottom' → Parte inferior de la pantalla
 * 
 * POSITION (Horizontal):
 * - 'left'   → Lado izquierdo
 * - 'center' → Centro
 * - 'right'  → Lado derecho
 * 
 * COMBINACIONES POSIBLES:
 * - top + left     → Esquina superior izquierda
 * - top + center   → Centro superior
 * - top + right    → Esquina superior derecha
 * - bottom + left  → Esquina inferior izquierda (DEFAULT)
 * - bottom + center → Centro inferior
 * - bottom + right → Esquina inferior derecha
 */


// ============================================
// 8. CONFIGURACIÓN AVANZADA
// ============================================

// Acceder a la configuración global
console.log(ToastConfig);

// Modificar duración por defecto (en milisegundos)
ToastConfig.duration = 7000; // 7 segundos

// Modificar offset (margen desde el borde)
ToastConfig.offset.x = 30;
ToastConfig.offset.y = 30;

// Personalizar estilos de un tipo específico
ToastConfig.styles.success.title = "¡Perfecto!";
ToastConfig.styles.error.title = "¡Ups!";


// ============================================
// 9. BUENAS PRÁCTICAS
// ============================================

/**
 * ✅ DO - Hacer:
 * - Usar mensajes claros y concisos
 * - Usar el tipo correcto (success, error, warning, info)
 * - Mantener la posición consistente en toda la app
 * - Limitar la duración según la importancia del mensaje
 * 
 * ❌ DON'T - No hacer:
 * - Mostrar múltiples toasts simultáneamente innecesarios
 * - Usar mensajes demasiado largos
 * - Cambiar la posición frecuentemente
 * - Usar toast para información crítica (usar modales en su lugar)
 */


// ============================================
// 10. EJEMPLOS DE MENSAJES POR CONTEXTO
// ============================================

// CRUD - Crear
Toast.success('Registro creado exitosamente');

// CRUD - Actualizar
Toast.success('Los cambios se guardaron correctamente');

// CRUD - Eliminar
Toast.success('Registro eliminado correctamente');

// Validación
Toast.warning('Por favor completa todos los campos requeridos');

// Error de red
Toast.error('No se pudo conectar con el servidor. Verifica tu conexión');

// Error de permisos
Toast.error('No tienes permisos para realizar esta acción');

// Proceso en curso
Toast.info('Procesando tu solicitud, por favor espera...');

// Sesión
Toast.warning('Tu sesión expirará en 5 minutos');
Toast.error('Tu sesión ha expirado. Por favor inicia sesión nuevamente');

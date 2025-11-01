// Centralized toast handling from flash messages
(function () {
  document.addEventListener('DOMContentLoaded', function () {
    const flashContainer = document.getElementById('flash-messages');
    if (!flashContainer) return;

    const show = typeof window.showToast === 'function' ? window.showToast : null;
    if (!show) return;

    const { success, error, warning, info } = flashContainer.dataset;
    // Duraciones optimizadas por tipo
    const DURATION = {
      success: 4000,
      error: 7000,
      warning: 6000,
      info: 5000
    };
    if (success) show(success, 'success', DURATION.success);
    if (error) show(error, 'error', DURATION.error);
    if (warning) show(warning, 'warning', DURATION.warning);
    if (info) show(info, 'info', DURATION.info);
  });
})();
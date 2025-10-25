// Centralized toast handling from flash messages
(function () {
  document.addEventListener('DOMContentLoaded', function () {
    const flashContainer = document.getElementById('flash-messages');
    if (!flashContainer) return;

    const show = typeof window.showToast === 'function' ? window.showToast : null;
    if (!show) return;

    const { success, error, warning, info } = flashContainer.dataset;
    if (success) show(success, 'success');
    if (error) show(error, 'error');
    if (warning) show(warning, 'warning');
    if (info) show(info, 'info');
  });
})();
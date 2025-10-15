// Fix for aria-hidden accessibility issue with Bootstrap modals
document.addEventListener('DOMContentLoaded', function() {
    // Handle all modals to prevent aria-hidden focus conflicts
    const modals = document.querySelectorAll('.modal');
    
    modals.forEach(function(modal) {
        // When modal is fully shown, focus on first input if exists
        modal.addEventListener('shown.bs.modal', function() {
            // Remove aria-hidden when modal is shown to fix accessibility issue
            this.removeAttribute('aria-hidden');
            
            // Auto-focus on first input or textarea in modal (optional)
            const firstInput = this.querySelector('input:not([type="hidden"]), textarea, select');
            if (firstInput) {
                setTimeout(() => firstInput.focus(), 100);
            }
        });
        
        // When modal is being hidden, ensure focus is removed first
        modal.addEventListener('hide.bs.modal', function() {
            // Blur any focused element inside the modal before hiding
            const focusedElement = this.querySelector(':focus');
            if (focusedElement) {
                focusedElement.blur();
            }
        });
        
        // After modal is hidden, restore aria-hidden
        modal.addEventListener('hidden.bs.modal', function() {
            this.setAttribute('aria-hidden', 'true');
        });
    });
});

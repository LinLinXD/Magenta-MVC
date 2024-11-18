document.addEventListener('DOMContentLoaded', function() {
    // Manejar clics en las notificaciones
    document.querySelectorAll('.notification-content').forEach(notification => {
        notification.addEventListener('click', function(e) {
            e.preventDefault();
            const item = this.closest('.notification-item');
            const form = item.querySelector('form');

            // Añadir efecto visual
            item.style.opacity = '0.5';

            // Enviar formulario y redirigir
            if (form) {
                form.submit();
            }
        });
    });
});
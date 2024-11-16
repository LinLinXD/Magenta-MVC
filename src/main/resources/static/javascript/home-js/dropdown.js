document.addEventListener('DOMContentLoaded', function() {
    const profileTrigger = document.querySelector('.profile-trigger');
    const profileDropdown = document.querySelector('.profile-dropdown');
    const notificationItems = document.querySelectorAll('.notification-item');

    // Función para mostrar/ocultar el menú
    profileTrigger.addEventListener('click', function(e) {
        e.stopPropagation();
        profileDropdown.classList.toggle('active');
    });

    // Manejar clic en notificaciones individuales
    notificationItems.forEach(item => {
        item.addEventListener('click', function() {
            // Solo navegar al link si existe
            const link = this.getAttribute('data-link');
            if (link) {
                window.location.href = link;
            }
        });
    });

    // Cerrar el menú cuando se hace clic fuera de él
    document.addEventListener('click', function(e) {
        if (!profileDropdown.contains(e.target) && !profileTrigger.contains(e.target)) {
            profileDropdown.classList.remove('active');
        }
    });

    // Cerrar el menú con la tecla Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && profileDropdown.classList.contains('active')) {
            profileDropdown.classList.remove('active');
        }
    });

    // Prevenir que el menú se cierre al hacer click dentro
    profileDropdown.addEventListener('click', function(e) {
        if (!e.target.closest('a')) { // Solo si no es un enlace
            e.stopPropagation();
        }
    });
});
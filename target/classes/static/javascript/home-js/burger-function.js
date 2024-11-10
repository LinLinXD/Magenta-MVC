document.addEventListener('DOMContentLoaded', function() {
    const burgerIcon = document.querySelector('.burger-icon');
    const headerSections = document.querySelector('.header-sections');

    burgerIcon.addEventListener('click', function() {
        this.classList.toggle('active');
        headerSections.classList.toggle('active');
    });
});
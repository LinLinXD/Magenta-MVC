let $root = document.documentElement;

window.addEventListener('scroll', () => {
    const heroImageHeight = document.querySelector('.hero-image-container').offsetHeight;

    if (window.scrollY < heroImageHeight) {
        $root.style.setProperty('--header-opacity', '0');
    } else {
        $root.style.setProperty('--header-opacity', '1');
    }
});
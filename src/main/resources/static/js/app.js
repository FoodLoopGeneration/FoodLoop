document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".alert").forEach(a => {
        setTimeout(() => { a.style.display = 'none' }, 4000);
    });

    document.querySelectorAll("form[data-confirm]").forEach(form => {
        form.addEventListener("submit", (e) => {
            const msg = form.getAttribute("data-confirm") || "Sei sicuro?";
            if (!window.confirm(msg))
                e.preventDefault();
        });
    });

});

document.querySelectorAll("input[type=file]").forEach(input => {
    input.addEventListener("change", e => {
        const img = document.createElement("img");
        img.src = URL.createObjectURL(e.target.files[0]);
        img.width = 120;
        input.after(img);
    });
});

const track = document.querySelector(".carousel-track");
const slides = document.querySelectorAll(".carousel-track img");
const dotsContainer = document.querySelector(".carousel-dots");
const next = document.querySelector(".next");
const prev = document.querySelector(".prev");

if (track && slides.length > 0) {
    // 1. Crea i pallini dinamicamente
    slides.forEach((_, i) => {
        const dot = document.createElement("div");
        dot.classList.add("dot");
        if (i === 0) dot.classList.add("active");
        dot.addEventListener("click", () => {
            track.scrollTo({ left: slides[i].offsetLeft - (track.clientWidth / 10), behavior: 'smooth' });
        });
        dotsContainer.appendChild(dot);
    });

    const dots = document.querySelectorAll(".dot");

    // 2. Funzione per aggiornare il pallino attivo durante lo scroll
    const updateDots = () => {
        const scrollPosition = track.scrollLeft;
        const slideWidth = slides[0].clientWidth + 15;
        const activeIndex = Math.round(scrollPosition / slideWidth);
        
        dots.forEach((dot, i) => {
            dot.classList.toggle("active", i === activeIndex);
        });
    };

    // 3. Listener per i pulsanti e lo scroll manuale
    next.addEventListener("click", () => {
        track.scrollBy({ left: slides[0].clientWidth + 15, behavior: 'smooth' });
    });

    prev.addEventListener("click", () => {
        track.scrollBy({ left: -(slides[0].clientWidth + 15), behavior: 'smooth' });
    });

    track.addEventListener("scroll", updateDots);
};
function moveSlide(direction) {
    const track = document.querySelector('.carousel-track');
    const slides = track.querySelectorAll('.slide');
    const slideWidth = slides[0].clientWidth;
    
    // Calcola la posizione massima di scorrimento
    const maxScroll = track.scrollWidth - track.clientWidth;
    
    // Se clicchi "avanti" e sei alla fine, torna all'inizio
    if (direction === 1 && track.scrollLeft >= maxScroll - 5) {
        track.scrollTo({ left: 0, behavior: 'smooth' });
    } 
    // Se clicchi "indietro" e sei all'inizio, vai alla fine
    else if (direction === -1 && track.scrollLeft <= 5) {
        track.scrollTo({ left: maxScroll, behavior: 'smooth' });
    } 
    // Altrimenti scorri normalmente
    else {
        track.scrollBy({
            left: direction * slideWidth,
            behavior: 'smooth'
        });
    }
};
setInterval(() => {
    moveSlide(1);
}, 5000);

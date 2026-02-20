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

// Seleziona gli elementi
const track = document.querySelector(".carousel-track");
const slides = track ? track.children : []; 
const dotsContainer = document.querySelector(".carousel-dots");
const next = document.querySelector(".next");
const prev = document.querySelector(".prev");

if (track && slides.length > 0) {
    const gap = 15;

    // Crea i pallini
    Array.from(slides).forEach((_, i) => {
        const dot = document.createElement("div");
        dot.classList.add("dot");
        if (i === 0) dot.classList.add("active");
        dot.addEventListener("click", () => {
            const offset = slides[i].offsetLeft - track.offsetLeft;
            track.scrollTo({ left: offset, behavior: 'smooth' });
        });
        dotsContainer.appendChild(dot);
    });

    const dots = document.querySelectorAll(".dot");
    const moveSlide = (direction) => {
        const slideWidth = slides[0].offsetWidth + gap;
        const maxScroll = track.scrollWidth - track.clientWidth;

        if (direction === 1 && track.scrollLeft >= maxScroll - 5) {
            track.scrollTo({ left: 0, behavior: 'smooth' });
        } else if (direction === -1 && track.scrollLeft <= 5) {
            track.scrollTo({ left: maxScroll, behavior: 'smooth' });
        } else {
            track.scrollBy({ left: direction * slideWidth, behavior: 'smooth' });
        }
    };

    //Aggiorna i pallini
    track.addEventListener("scroll", () => {
        const slideWidth = slides[0].offsetWidth + gap;
        const activeIndex = Math.round(track.scrollLeft / slideWidth);
        dots.forEach((dot, i) => dot.classList.toggle("active", i === activeIndex));
    });

    // pulsanti
    next.addEventListener("click", () => moveSlide(1));
    prev.addEventListener("click", () => moveSlide(-1));

    // Auto-play
    let autoPlay = setInterval(() => moveSlide(1), 5000);

    // Ferma l'auto-play quando l'utente interagisce
    track.addEventListener("mouseenter", () => clearInterval(autoPlay));
    track.addEventListener("mouseleave", () => autoPlay = setInterval(() => moveSlide(1), 5000));
}


document.addEventListener("DOMContentLoaded", () => {
    // --- 1. Gestione Alert ---
    document.querySelectorAll(".alert").forEach(a => {
        setTimeout(() => { a.style.display = 'none' }, 4000);
    });

    // --- 2. Conferma Eliminazione ---
    document.querySelectorAll("form[data-confirm]").forEach(form => {
        form.addEventListener("submit", (e) => {
            const msg = form.getAttribute("data-confirm") || "Sei sicuro?";
            if (!window.confirm(msg)) e.preventDefault();
        });
    });

    // --- 3. Anteprima Immagine ---
    document.querySelectorAll("input[type=file]").forEach(input => {
        input.addEventListener("change", e => {
            const existingImg = input.parentElement.querySelector("img.preview-img");
            if (existingImg) existingImg.remove();

            if (e.target.files && e.target.files[0]) {
                const img = document.createElement("img");
                img.classList.add("preview-img");
                img.src = URL.createObjectURL(e.target.files[0]);
                img.width = 120;
                img.style.marginTop = "10px";
                img.style.borderRadius = "8px";
                img.style.display = "block";
                input.after(img);
            }
        });
    });

    // --- 4. Carosello ---
    const track = document.querySelector(".carousel-track");
    const slides = track ? track.children : [];
    const dotsContainer = document.querySelector(".carousel-dots");
    const next = document.querySelector(".next");
    const prev = document.querySelector(".prev");

    if (track && slides.length > 0) {
        const gap = 15;

        // Creazione dei pallini
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

        // Aggiorna lo stato attivo dei pallini durante lo scroll
        track.addEventListener("scroll", () => {
            const slideWidth = slides[0].offsetWidth + gap;
            const activeIndex = Math.round(track.scrollLeft / slideWidth);
            dots.forEach((dot, i) => dot.classList.toggle("active", i === activeIndex));
        });

        next.addEventListener("click", () => moveSlide(1));
        prev.addEventListener("click", () => moveSlide(-1));

        // Auto-play del carosello
        let autoPlay = setInterval(() => moveSlide(1), 5000);
        track.addEventListener("mouseenter", () => clearInterval(autoPlay));
        track.addEventListener("mouseleave", () => autoPlay = setInterval(() => moveSlide(1), 5000));
    }

    // --- 5. Logica Ingredienti (Binding Manuale per Backend) ---
    const recipeForm = document.querySelector("form");
    const selectIngredienti = document.getElementById("ingredientiSelect");
    const hiddenInput = document.getElementById("ingredientiString");

    if (recipeForm && selectIngredienti && hiddenInput) {
        recipeForm.addEventListener("submit", function () {
            const selectedOptions = Array.from(selectIngredienti.selectedOptions);
            const idsString = selectedOptions.map(option => option.value).join(',');

            hiddenInput.value = idsString;

            console.log("Dati ingredienti serializzati per il backend:", idsString);
        });
    }
});
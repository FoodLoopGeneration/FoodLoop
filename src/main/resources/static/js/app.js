document.addEventListener("DOMContentLoaded", () => {
    
    // --- 1. GESTIONE NOTIFICHE ---
    const closeAlerts = () => {
        const alerts = document.querySelectorAll(".alert, .alert-custom");
        alerts.forEach(alert => {
            setTimeout(() => {
                alert.style.transition = "all 0.5s ease";
                alert.style.opacity = "0";
                alert.style.transform = "translateY(-10px)";
                setTimeout(() => alert.remove(), 500);
            }, 4000);
        });
    };
    closeAlerts();

    // --- 2. ANTEPRIMA IMMAGINE E GESTIONE FILE ---
    const fileInput = document.querySelector("input[type=file]");
    if (fileInput) {
        fileInput.addEventListener("change", function(e) {
            const container = this.parentElement;
            let preview = container.querySelector(".preview-img");
            
            if (preview && preview.src.startsWith('blob:')) {
                URL.revokeObjectURL(preview.src);
            }

            if (this.files && this.files[0]) {
                const reader = new FileReader();
                
                if (!preview) {
                    preview = document.createElement("img");
                    preview.classList.add("preview-img");
                    Object.assign(preview.style, {
                        width: "120px",
                        height: "120px",
                        objectFit: "cover",
                        marginTop: "10px",
                        borderRadius: "12px",
                        border: "2px solid #2e7d32",
                        display: "block"
                    });
                    this.after(preview);
                }
                
                preview.src = URL.createObjectURL(this.files[0]);
            }
        });
    }

    // --- 3. LOGICA SINCRONIZZAZIONE INGREDIENTI ---

    const recipeForm = document.getElementById("recipeForm");
    const selectIngredienti = document.getElementById("ingredientiSelect");
    const hiddenInput = document.getElementById("ingredientiIds");

    if (recipeForm && selectIngredienti && hiddenInput) {
        recipeForm.addEventListener("submit", function(e) {
            const selectedOptions = Array.from(selectIngredienti.selectedOptions);
            const ids = selectedOptions.map(opt => opt.value).join(",");
            
            hiddenInput.value = ids;
            
            console.log("Submit: Mappatura ingredienti completata ->", ids);

            if (selectedOptions.length === 0) {
                if(!confirm("Non hai selezionato ingredienti. Continuare comunque?")) {
                    e.preventDefault();
                }
            }
        });
    }

    // --- 4. CAROSELLO HOMEPAGE ---
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
    

    // --- 5. Evidenzia i campi con errore al caricamento ---
    const errorInputs = document.querySelectorAll(".text-danger");
    errorInputs.forEach(error => {
        const input = error.parentElement.querySelector("input, textarea, select");
        if (input) {
            input.style.borderColor = "#dc3545";
            input.addEventListener('focus', () => {
                input.style.borderColor = ""; 
            }, { once: true });
        }
    });
});
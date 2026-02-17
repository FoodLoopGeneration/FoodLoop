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
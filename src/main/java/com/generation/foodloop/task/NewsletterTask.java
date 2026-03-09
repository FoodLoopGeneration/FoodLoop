package com.generation.foodloop.task;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.EmailService;
import com.generation.foodloop.services.IngredienteService;
import com.generation.foodloop.services.TipService;
import com.generation.foodloop.services.UtenteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsletterTask {

    private final UtenteService utenteService;

    private final IngredienteService ingredienteService;

    private final EmailService emailService;

    private final TipService tipService;

    @Scheduled(cron = "0 00 12 * * WED")
    public void inviaNewesletter() {

        log.info("Avvio del task newsletter");
        String tipSettimanale;

        try {
            tipSettimanale = tipService.getRandomTip();
        } catch (Exception e) {
            log.error("Impossibile recuperare tip da file: " + e.getMessage());
            tipSettimanale = "Pianifica i tuoi pasti per ridurre gli sprechi";
        }

        List<Utente> utenti = utenteService.getAll();

        for(Utente u : utenti) {

            List<Ingrediente> dispensa = ingredienteService.getByUtente(u.getId());
            List<Ingrediente> scadenza = dispensa.stream()
                                            .filter(i -> i.getScadenza() != null)
                                            .filter(this::scadenzaQuestaSettimana)
                                            .collect(Collectors.toList());
            
            if(!scadenza.isEmpty()) {

                inviaEmailUtente(u, scadenza, tipSettimanale);

            }

        }

    }

    private boolean scadenzaQuestaSettimana(Ingrediente i) {

        LocalDate oggi = LocalDate.now();

        LocalDate settimana = oggi.plusDays(7);

        LocalDate scadenza = i.getScadenza();

        return !scadenza.isBefore(oggi) && !scadenza.isAfter(settimana);
        
    }

    private void inviaEmailUtente(Utente u, List<Ingrediente> scadenza, String tip) {

        String oggetto = "FoodLoop: Prodotti in scadenza questa settimana!";

        StringBuilder corpoMail = new StringBuilder();

        corpoMail.append("Ciao ").append(u.getNome()).append(" ").append(u.getCognome()).append(",\n\n");
        corpoMail.append("Ecco la lista dei tuoi prodotti che scadranno questa settimana: \n\n");
        
        for(Ingrediente i : scadenza) {

            corpoMail.append("⛛ ").append(i.getNome()).append(" (SCADENZA: ").append(i.getScadenza()).append(") \n");

        }
        
        corpoMail.append("\n\n\n Consiglio anti spreco della settimana: \n").append(tip)
        .append("\n\n Il team di FoodLoop ti augura una buona cucina ed un buon risparmio!");

        try {
            
            emailService.sendMail(u.getEmail(), oggetto, corpoMail.toString());
            log.info("Email inviata con successo");

        } catch (Exception e) {

            log.error("Errore nell'invio della mail");

        }
    }
    
}

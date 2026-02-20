# Documento di Analisi Iniziale: Project FoodLoop

## 1. Descrizione del Progetto
**FoodLoop** è una piattaforma web gestionale dedicata all'ottimizzazione del consumo alimentare domestico. Il sistema permette agli utenti di monitorare la propria dispensa (ingredienti) e di ricevere suggerimenti intelligenti su quali ricette poter cucinare in base a ciò che hanno effettivamente a disposizione, riducendo gli sprechi e facilitando la pianificazione dei pasti.

## 2. Obiettivo
L'obiettivo primario è combattere lo **spreco alimentare**. FoodLoop trasforma il concetto di "cosa c'è in frigo?" in una risorsa creativa, automatizzando l'abbinamento tra scorte alimentari e database culinario, garantendo al contempo una gestione ordinata delle scadenze degli ingredienti.

## 3. Target Utenti
- **Single e Studenti:** Per ottimizzare la spesa e non dimenticare ingredienti aperti nel frigo.
- **Famiglie:** Per pianificare i pasti settimanali in modo rapido e vario.
- **Appassionati di cucina eco-sostenibile:** Utenti attenti all'impatto ambientale e alla riduzione degli sprechi.


## 4. Funzionalità
### Feature Principali 
- **Gestione Inventario:** Inserimento, modifica e cancellazione degli ingredienti con monitoraggio della quantità e della data di scadenza.
- **Ricettario Personale:** Creazione di ricette complete di descrizione, difficoltà, tempi di preparazione e foto.
- **Matching Intelligente:** Ricerca di ricette filtrata per gli ingredienti presenti in dispensa.

### Feature Opzionali 
- **Notifiche Scadenze:** Avvisi automatici quando un ingrediente è prossimo alla data di scadenza.
- **Social Sharing:** Possibilità di rendere pubbliche le proprie ricette per altri utenti della community.
- **Lista della Spesa Automatica:** Generazione di una lista basata sugli ingredienti mancanti per una specifica ricetta.


## 5. Flusso Principale 
Il diagramma logico del sistema segue questo percorso standard:

1. **Accesso:** L'utente effettua il login (o viene riconosciuto tramite cookie).
2. **Inventario:** L'utente popola la sua dispensa inserendo gli ingredienti (es: *Farina, 1kg, Scadenza 2026*).
3. **Esplorazione:** L'utente consulta il database delle ricette.
4. **Selezione:** Il sistema evidenzia le ricette realizzabili immediatamente con gli ingredienti posseduti.
5. **Esecuzione:** L'utente visualizza i dettagli della ricetta scelta e procede alla preparazione.


## 6. Architettura Tecnica (Sintesi)
- **Backend:** Spring Boot 3 (Java 21+).
- **Sicurezza:** Spring Security con `CookieFilter` personalizzato e gestione ruoli.
- **Persistenza:** Database relazionale (MySQL) gestito tramite Spring Data JPA.
- **Mappatura:** Utilizzo di DTO (Data Transfer Objects) e Mapper per separare la logica di database dalla logica di presentazione.


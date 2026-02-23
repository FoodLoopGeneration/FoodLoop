# 50 — Logging ed Error Handling

## Strategia Logging 
L'applicazione utilizza il framework **SLF4J** (tramite l'annotazione Lombok `@Slf4j`) per monitorare il ciclo di vita dei dati. La strategia di logging è distribuita su più livelli per facilitare il debug e il monitoraggio:

* **INFO (Tracciabilità Operativa)**:
* **GenericService**: Ogni operazione di persistenza viene loggata genericamente ("Salvataggio entità: [NomeClasse]").
* **FileStorageService**: Traccia l'inizio della procedura di salvataggio delle foto delle ricette.
* **UtenteService**: Logga i tentativi di registrazione e il successo del salvataggio nel database con il relativo ID generato.


* **WARN (Anomalie Gestite)**:
* Utilizzato in `UtenteService` per segnalare tentativi di registrazione con email già esistenti, evitando il crash ma segnalando l'evento anomalo.


* **ERROR (Fallimenti Critici)**:
* Cattura eccezioni durante il processo di hashing della password o errori di IO durante la scrittura dei file su disco nel `FileStorageService`.



## Validazione e Univocità
Il controllo degli errori non si limita ai DTO (già protetti da `@Valid`), ma prosegue nel layer Service per garantire l'integrità del database:

1. **Normalizzazione dei dati**: I service utilizzano un metodo privato `normNome()` che applica `.trim().toUpperCase()`. Questo garantisce che "Pasta", " pasta" e "PASTA" siano trattati come lo stesso record, prevenendo duplicati semantici.
2. **Unique Check**:
* **In Creazione**: I metodi `uniqueErrorsForCreate` verificano la presenza del nome nel repository prima di procedere.
* **In Aggiornamento**: I metodi `uniqueErrorsForUpdate` verificano l'univocità escludendo l'ID dell'entità corrente (`existsByNomeAndIdNot`), permettendo di rinominare un ingrediente senza conflitti con se stesso.


3. **Integrità Utente**: `UtenteService` verifica preventivamente l'esistenza dell'email nel DB prima di invocare il mapper, restituendo `false` se il vincolo di univocità è violato.

## Gestione Eccezioni e Robustezza
L'architettura è progettata per essere "Exception-Safe" grazie a diversi meccanismi:

* **Null-Safe Retrieval**: Grazie a `GenericService`, l'uso di `getByIdOrNull(id)` sostituisce il lancio diretto di `EntityNotFoundException`, permettendo ai controller di gestire la mancanza di un dato con un semplice redirect o un messaggio di feedback (es. in `delete` e `updateFromDto`).
* **Gestione Transazionale**: L'uso di `@Transactional` (sia nei singoli metodi dei Service che in `GenericService`) assicura che in caso di errore (es. il database non risponde dopo aver salvato un file immagine), venga effettuato il rollback della transazione, evitando dati "orfani".
* **Storage Fallback**: In `RicettaService`, il salvataggio della foto è protetto. Se l'upload fallisce o il file è vuoto, il sistema è istruito per restituire `null` o gestire l'eccezione IO senza necessariamente corrompere il salvataggio della parte testuale della ricetta (a seconda della logica di business scelta).



**FoodLoop** è una piattaforma web gestionale dedicata all'ottimizzazione del consumo alimentare domestico. Il sistema permette di monitorare la propria dispensa e ricevere suggerimenti su quali ricette cucinare in base agli ingredienti effettivamente disponibili, riducendo gli sprechi.


## Stack Tecnologico
* **Backend:** Spring Boot 4.0.3 (Java 21).
* **Security:** Spring Security con gestione ruoli (ADM/USR) e `CookieFilter` personalizzato.
* **Frontend:** Thymeleaf con integrazione Spring Security 6.
* **Database:** MySQL con Spring Data JPA.
* **Utilities:** Project Lombok per la riduzione del boilerplate code.

## Struttura del Progetto
Il progetto segue un'architettura modulare per separare le responsabilità:
- `com.generation.foodloop.entities`: Classi di persistenza (JPA Entities) come `Utente`, `Ingrediente`, `Categoria`, `Ricetta`.
- `com.generation.foodloop.repositories`: Interfacce per l'accesso ai dati (Spring Data JPA).
- `com.generation.foodloop.services`: Logica di business e gestione delle transazioni.
- `com.generation.foodloop.controllers`: Gestione delle rotte web e interazione con la UI.
- `com.generation.foodloop.dto` & `utils`: Data Transfer Objects e Mapper per la separazione tra presentazione e database.
- `com.generation.foodloop.security`: Configurazione filtri, rotte protette e gestione autenticazione.


## Setup e Installazione

### 1. Prerequisiti
- **Java 21+** installato.
- **Maven** (o utilizzo del `mvnw` incluso).
- **MySQL Server** attivo.

### 2. Database Configuration
Crea un database denominato `Food` sul tuo server MySQL locale. Lo schema verrà generato automaticamente (tramite Hibernate) o può essere inizializzato tramite lo script `schema.sql` fornito.

### 3. Avvio dell'applicazione
Dalla root del progetto, esegui il comando:

```bash
./mvnw spring-boot:run

```

L'applicazione sarà disponibile all'indirizzo locale: `http://localhost:8080`


## Credenziali Demo
L'applicazione può essere inizializzata con un utente amministratore tramite il file `data.sql`:

- **Email:** `admin@foodloop.com`
- **Password:** `admin` (hash: `$2a$12$yVYfGVDAgOp2..j.cPcmiulBZAkifzYHs4/HVmmQh2t7vakcm/eeW`)
- **Ruolo:** `ADM`


## Comandi Utili (Maven)

* **Pulizia e Compilazione:**
```bash
./mvnw clean compile

```


* **Esecuzione Test:**
```bash
./mvnw test

```


* **Packaging (JAR):**
```bash
./mvnw package

```


## Flusso Principale dell'Utente
1. **Accesso:** Login tramite form o riconoscimento cookie.
2. **Inventario:** Popolamento della dispensa con ingredienti, quantità e scadenze.
3. **Matching:** Consultazione delle ricette filtrata per ciò che è presente in frigo.
4. **Cucina:** Visualizzazione dettagli ricetta e preparazione.

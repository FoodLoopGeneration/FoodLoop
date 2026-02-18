# 10 — Architettura

## Struttura package (esempio)
- `controller/` (MVC + mapping rotte)

    * AppController – endpoint di utilità e funzionalità di sistema

    * IngredienteController – esposizione delle API per la gestione degli ingredienti

    * LoginController – gestione del processo di autenticazione

    * RegisterController – gestione della registrazione degli utenti

    * RicettaController – esposizione delle API per la gestione delle ricette

I controller rappresentano il livello di ingresso dell’applicazione.
Gestiscono esclusivamente gli aspetti HTTP (routing, request/response) e delegano completamente la logica applicativa ai service.
Non contengono logica di business né accedono direttamente al livello di persistenza.

- `service/` (logica applicativa)

    * CategoriaService – gestione della logica applicativa relativa alle categorie

    * FileStorageService – gestione delle operazioni di salvataggio e recupero file

    * GenericService – funzionalità comuni e riutilizzabili tra più service

    * IngredienteService – logica applicativa per la gestione degli ingredienti

    * RicettaService – logica applicativa per la gestione delle ricette

    * UtenteService – gestione della logica applicativa legata agli utenti

Il livello service incapsula la business logic dell’applicazione.
Coordina le operazioni sui repository, applica le regole di dominio e definisce i flussi applicativi.
Rappresenta il punto centrale dell’architettura.

- `repository/` (Spring Data JPA)

    * CategoriaRepository – accesso ai dati delle categorie

    * IngredienteRepository – accesso ai dati degli ingredienti

    * RicettaRepository – accesso ai dati delle ricette

    * RuoloRepository – accesso ai dati dei ruoli

    * UtenteRepository – accesso ai dati degli utenti

I repository si occupano esclusivamente dell’accesso ai dati tramite Spring Data JPA.
Non contengono logica di business e forniscono un’astrazione del livello di persistenza.

- `entity/` (JPA entities)

    * Categoria – entità di dominio per le categorie

    * Ingrediente – entità di dominio per gli ingredienti

    * Ricetta – entità di dominio per le ricette

    * Ruolo – entità che rappresenta i ruoli utente

    * TipoRuolo – enum che definisce il tipo di ruolo (User, Admin)

    * UnitaMisura – enum per la rappresentazione delle unità di misura

    * Utente – entità che rappresenta l’utente del sistema

Le entity rappresentano il modello dati persistente e sono mappate tramite JPA/Hibernate.
Non vengono mai esposte direttamente attraverso le API.

- `dto/` (request/response)

    * CategoriaDTO – DTO per la gestione delle categorie

    * IngredienteDTO – DTO per la gestione degli ingredienti

    * RicettaDTO – DTO per la gestione delle ricette

    * UtenteDTO – DTO per la gestione degli utenti

I DTO sono utilizzati per lo scambio di dati tra client e server.
Consentono di:

    1. separare il modello interno dall’esposizione esterna

    2. applicare la validazione dell’input

    3. controllare i dati esposti dalle API

- `security/` (config, user details, handlers)

    * filters/CookieFilter – filtro per l’intercettazione e la validazione delle richieste

    * ApiRoutes – definizione centralizzata delle rotte pubbliche e protette

    * LoginSuccessHandler – gestione delle operazioni post-login

    * LogoutHandler – gestione del processo di logout

    * SecurityConfiguration – configurazione di Spring Security

    * UserPasswordAuthProvider – provider per l’autenticazione username/password

    * Questo package gestisce tutti gli aspetti legati alla sicurezza dell’applicazione, inclusi autenticazione, autorizzazione e protezione delle API.

- `config/` (profili, bean, init)

    * EncoderContext – configurazione degli encoder per la gestione delle password

    * EntityContext – configurazione del contesto JPA

    * MvcConfig – configurazione MVC dell’applicazione

Il package contiene configurazioni infrastrutturali e bean condivisi, separati dalla logica applicativa.

## Pattern minimi
- Controller “thin”, Service “fat”

I controller si limitano alla gestione delle request HTTP, mentre tutta la logica di business risiede nei service.

- Validazione input con `@Valid`

La validazione viene applicata sui DTO utilizzati come input delle API.

- Mapping Entity ↔ DTO (manuale o mapper semplice)

Il mapping tra entity e DTO è esplicito (manuale o tramite mapper dedicato).
Le entity non vengono mai esposte direttamente verso l’esterno.

## Diagramma a livelli
L’applicazione segue un flusso a livelli con dipendenze unidirezionali:

Controller → Service → Repository → MySQL

Ogni livello ha una responsabilità ben definita e non è consentito accedere direttamente a un livello inferiore saltando quelli intermedi.

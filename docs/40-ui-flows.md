# 40 — UI Flows

Inserisci i flussi principali prima, poi come extra gli altri

## Flusso 1: Login
1. GET /login (form)
2. POST /login (submit)
3. success → redirect a home / fail → messaggio errore

ESEMPIO diagramma di sequenza:
```mermaid
sequenceDiagram
    autonumber
    actor Utente
    participant Browser
    participant Server
    participant DB

    Utente->>Browser: Apre /login
    Browser->>Server: GET /login
    Server-->>Browser: Ritorna form di login

    Utente->>Browser: Inserisce credenziali e clicca Submit
    Browser->>Server: POST /login (email, password)
    Server->>DB: Verifica credenziali
    DB-->>Server: Risultato query

    alt Credenziali valide
        Server-->>Browser: Redirect a Home
        Browser-->>Utente: Mostra Home Page
    else Credenziali non valide
        Server-->>Browser: Messaggio di errore
        Browser-->>Utente: Mostra errore di login
    end
```

## Flusso 2: CRUD principale (ADMIN)
- Lista → crea → salva → conferma → modifica → salva → elimina/disabilita

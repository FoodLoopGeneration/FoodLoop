# 30 — Security

## Requisiti minimi
- **Login custom**: Implementato tramite una pagina Thymeleaf dedicata raggiungibile all'endpoint `/login`. La gestione del processo di autenticazione è configurata in `SecurityConfiguration` tramite `formLogin`.
- **Password hash BCrypt**: La verifica delle credenziali utilizza `BCryptPasswordEncoder` per confrontare la password fornita con quella archiviata nel database.
- **Protezione rotte**: La sicurezza è gestita tramite `SecurityFilterChain` definita in `SecurityConfiguration`:
- **Rotte Pubbliche**: Accesso libero a `/`, `/login`, `/register` e risorse statiche (`/css/**`, `/js/**`, `/res/**`).
- **Rotte ADMIN**: Gli endpoint sotto `/admin/**` e `/gestione/**` sono riservati esclusivamente agli utenti con ruolo `ADM`.
- **Rotte USER/Autenticato**: Gli endpoint `/user/**`, `/ingredienti/**`, `/ricette/**` e `/categoria/**` sono accessibili sia ai ruoli `USR` che `ADM`.
- **Altre rotte**: Qualsiasi altra richiesta richiede l'autenticazione generica.

## Meccanismo di Autenticazione
- **Authentication Provider**: È presente un `UserPasswordAuthProvider` custom che valida l'utente tramite email e verifica la password tramite `BCrypt`.
- **Cookie-Based Auth**: Un filtro personalizzato (`CookieFilter`) intercetta le richieste per validare un cookie specifico (definito dalla proprietà `app.cookie.nome`).
- Se il cookie è valido, estrae l'ID utente, recupera l'entità dal database tramite `UtenteService` e imposta l'autenticazione nel `SecurityContextHolder`.

## Sessione e logout
- **Logout attivo**: Configurato per rispondere all'endpoint `/logout` (metodo GET).
- **Gestione Logout**: Il sistema invalida la sessione HTTP, pulisce il contesto di autenticazione e rimuove i cookie `JSESSIONID` e `FOODLOOP_AUTH`.
- **Redirect dopo login**: Gestito tramite un `LoginSuccessHandler` custom che determina la destinazione dell'utente dopo un'autenticazione corretta.

## Gestione errori
- **Fallimento Login**: In caso di credenziali errate, l'utente viene reindirizzato a `/login?error=true`.
- **Accesso Negato (403)**: Sebbene il requisito extra richieda una pagina dedicata, la configurazione attuale delega la protezione dei permessi ai metodi `hasRole` e `hasAnyRole` all'interno della catena di filtri.

# 01 — Requisiti e Scope

> Compila questa sezione PRIMA di implementare.

## 1. Tema del progetto
- Titolo: FoodLoop
- Descrizione: FoodLoop è una piattaforma web per la gestione intelligente della dispensa domestica e la riduzione dello spreco alimentare. Gli utenti possono catalogare gli ingredienti in possesso monitorandone scadenze e quantità. Il sistema suggerisce ricette compatibili con le scorte attuali o permette la creazione di un ricettario personale. L'obiettivo è trasformare le eccedenze in pasti creativi attraverso un matching immediato tra inventario e database culinario.
- Utenti target: Single, famiglie e studenti fuori sede che desiderano ottimizzare la spesa ed evitare di buttare cibo scaduto.

## 2. Funzionalità (MVP - Minimum Viable Product)
### USER
- [ ] Registrazione / Login
- [ ] Area personale
- [ ] Lista oggetti principali (es. corsi, prodotti, ticket…)
- [ ] Dettaglio oggetto
- [ ] Operazione chiave (es. prenota/aggiungi/crea richiesta) 
- [ ] Storico (Extra)

### ADMIN (Extra)
- [ ] CRUD entità principali
- [ ] Gestione utenti/ruoli (minimo: vedere lista, abilitare/disabilitare o cambiare ruolo)
- [ ] Accesso a dashboard/area admin protetta

## 3. Vincoli tecnici
- Thymeleaf
- MySQL + JPA/Hibernate
- Password con BCrypt
- Rotte protette per ruolo

## 4. Validazioni e regole di business
- Regole input (min/max, formati, unicità):
- Regole stato (es. non si può cancellare se…):

## 5. Non-funzionali (base)
- Avvio seguendo README
- Errori gestiti (403/404/500)
- Nessuna credenziale in chiaro nel codice

# 10 — Architettura

## Struttura package (esempio)
- `controller/` (MVC + mapping rotte)
- `service/` (logica applicativa)
- `repository/` (Spring Data JPA)
- `entity/` (JPA entities)
- `dto/` (request/response)
- `security/` (config, user details, handlers)
- `config/` (profili, bean, init)

## Pattern minimi
- Controller “thin”, Service “fat”
- Validazione input con `@Valid`
- Mapping Entity ↔ DTO (manuale o mapper semplice)

## Diagramma a livelli
Inserire un diagramma o descrizione:

Controller → Service → Repository → MySQL

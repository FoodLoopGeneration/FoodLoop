# 30 — Security

## Requisiti minimi
- Login custom (pagina Thymeleaf)
- Password hash BCrypt
- Protezione rotte:
  - `/admin/**` → ADMIN
  - altre rotte protette → USER o autenticato

## Sessione e logout
- Logout attivo
- Redirect dopo login

## Gestione errori (Extra)
- 403: accesso negato con pagina dedicata

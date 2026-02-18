-- Inserimento Ruoli (se non esistono)
INSERT IGNORE INTO ruoli (id, nome) VALUES (1, 'ADM');
INSERT IGNORE INTO ruoli (id, nome) VALUES (2, 'USR');

-- Inserimento Utente ADMIN demo
INSERT IGNORE INTO utenti (id, nome, cognome, email, password) 
VALUES (1, 'Demo', 'Admin', 'admin@foodloop.com', '$2a$12$yVYfGVDAgOp2..j.cPcmiulBZAkifzYHs4/HVmmQh2t7vakcm/eeW');

-- Associazione Utente-Ruolo
INSERT IGNORE INTO utenti_ruoli (id_utente, id_ruolo) VALUES (1, 1);
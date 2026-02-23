SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS ricette_ingredienti;
DROP TABLE IF EXISTS utenti_ruoli;
DROP TABLE IF EXISTS ingredienti;
DROP TABLE IF EXISTS ricette;
DROP TABLE IF EXISTS categorie;
DROP TABLE IF EXISTS ruoli;
DROP TABLE IF EXISTS utenti;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Tabella Utenti
CREATE TABLE utenti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cognome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- 2. Tabella Ruoli
CREATE TABLE ruoli (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT uk_nome_ruolo UNIQUE (nome)
) ENGINE=InnoDB;

-- 3. Tabella Categorie
CREATE TABLE categorie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    id_utente BIGINT,
    CONSTRAINT fk_categoria_utente FOREIGN KEY (id_utente) REFERENCES utenti(id)
) ENGINE=InnoDB;

-- 4. Tabella Ingredienti
CREATE TABLE ingredienti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    scadenza DATE,
    quantita DOUBLE,
    unita_misura VARCHAR(10) DEFAULT 'Kg',
    posizione VARCHAR(255),
    id_utente BIGINT,
    id_categoria BIGINT,
    CONSTRAINT fk_ingrediente_utente FOREIGN KEY (id_utente) REFERENCES utenti(id),
    CONSTRAINT fk_ingrediente_categoria FOREIGN KEY (id_categoria) REFERENCES categorie(id)
) ENGINE=InnoDB;

-- 5. Tabella Ricette
CREATE TABLE ricette (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    foto VARCHAR(255),
    descrizione TEXT,
    difficolta INT,
    porzioni INT,
    tempo INT,
    valutazione INT,
    id_utente BIGINT,
    CONSTRAINT fk_ricetta_utente FOREIGN KEY (id_utente) REFERENCES utenti(id)
) ENGINE=InnoDB;

-- 6. Tabella di Join Utenti-Ruoli
CREATE TABLE utenti_ruoli (
    id_utente BIGINT NOT NULL,
    id_ruolo BIGINT NOT NULL,
    PRIMARY KEY (id_utente, id_role),
    CONSTRAINT fk_join_utente FOREIGN KEY (id_utente) REFERENCES utenti(id),
    CONSTRAINT fk_join_ruolo FOREIGN KEY (id_ruolo) REFERENCES ruoli(id)
) ENGINE=InnoDB;

-- 7. Tabella di Join Ricette-Ingredienti
CREATE TABLE ricette_ingredienti (
    id_ricetta BIGINT NOT NULL,
    id_ingrediente BIGINT NOT NULL,
    PRIMARY KEY (id_ricetta, id_ingrediente),
    CONSTRAINT fk_join_ricetta FOREIGN KEY (id_ricetta) REFERENCES ricette(id),
    CONSTRAINT fk_join_ingrediente FOREIGN KEY (id_ingrediente) REFERENCES ingredienti(id)
) ENGINE=InnoDB;
# 20 — Database

## Schema
- Nome DB: Food
- Tabelle principali: utenti,ricette,ingredienti,categorie
- Relazioni (1:N, N:M): utenti-categorie(1:N), utenti-ricette(1:N), utenti-ingradienti(1:N), ingredienti-ricette(N:M)

## ER Diagram
![Diagramma ER](diagrams/ER.png)

## Note JPA
- Strategie di chiave primaria
- Gestione relazioni (fetch, cascade) con criterio (senza complicare)

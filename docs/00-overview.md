# 00 — Overview

## Obiettivo
Realizzare un applicativo **Java + Spring Boot + JPA/Hibernate + MySQL** con interfaccia **Thymeleaf (SSR)**, autenticazione e ruoli (USER/ADMIN), e documentazione tecnica completa.

## Stack obbligatorio
- Java 17+ (o versione indicata dal corso)
- Spring Boot (Web, Thymeleaf, Validation, Data JPA, Security)
- Hibernate (via JPA)
- MySQL
- Template engine: Thymeleaf (server-side rendering)

## Principi architetturali minimi
- Architettura a livelli: Controller → Service → Repository → DB
- DTO per input/output, validazioni con Bean Validation
- Gestione errori base (pagine 403/404/500) e messaggi utente

## Ruoli
- USER: operazioni consentite definite nei requisiti
- ADMIN: gestione completa e/o gestione utenti/ruoli (Extra)

## Output (deploy)
- Repo ordinato + istruzioni di avvio riproducibili
- DB inizializzabile (schema + dati demo + admin seed) (in una cartella configuration/ da rilasciare con il file .jar)
- Documentazione compilata in /docs
- File .jar eseguibile (da creare tramite maven)
- Consegnare il tutto in una cartella release-GRUPPO_N.zip strutturata in questo modo:
```
release-GRUPPO_N/
├── configuration/
├── docs/
└── fileeseguibile.jar
```

## Utils
Installare l'estensione mermaid per VSCode (https://marketplace.visualstudio.com/items?itemName=MermaidChart.vscode-mermaid-chart) cosi da poter visualizzare o esportare immagini di diagrammi direttamente nei file .md (markdown)
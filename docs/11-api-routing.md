# 11 — Rotte MVC e Pagine (Thymeleaf)

| Pagina | Rotta | Controller#method | Ruolo | Note |
|---|---|---|---|---|
| Home | GET / | AppController#index | Public | |

| Login | GET /login | LoginController#login | Public |Form Login |
| Login | POST /login | LoginController#login | Public |Gestito da Security|
| Registrazione | GET /register | LoginController#registerForm | Public |Form utente |
| Registrazione | POST /register | LoginController#register | Public |Valid. + Salvataggio|
| Logout | GET/| /AppController#index | User/Admin | |

| ListaIngredienti | GET/ingredienti|IngredienteController#listaIngredienti | User/Admin | |
| FormNuovoIngr.| GET/ingredienti/new|IngredienteController#createForm| User/Admin |mode==create |
| CreaNuovoIngr.| Post/ingredienti|IngredienteController#create| User/Admin | |
| FormModificaIngr.| GET/ingredienti/{id}/edit|IngredienteController#editForm| User/Admin |mode==update |
| AggiornaIngr.| POST/ingredienti/{id}|IngredienteController#update| User/Admin ||
| EliminaIngr.| POST/ingredienti/{id}/delete|IngredienteController#delete| User/Admin ||

> Compilare la tabella con i routing rilevanti del progetto.

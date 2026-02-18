# 11 — Rotte MVC e Pagine (Thymeleaf)

| Pagina | Rotta | Controller#method | Ruolo | Note |
|---|---|---|---|---|
| Home | GET / | AppController#index | Public | |

* |Login|GET /login|LoginController#login|Public|Form Login|
* |Login|POST/login|LoginController#login|Public|Gestito da Security|
* |Registrazione|GET /register|LoginController#registerForm|Public||
* |Registrazione|POST /register|LoginController#register|Public||
* |Logout|GET/|AppController#index|User/Admin| |

* |ListaIngredienti|GET/ingredienti|IngredienteController#listaIngredienti| User/Admin | |
* |FormNuovoIngr.|GET/ingredienti/new|IngredienteController#createForm|User/ Admin|mode==create|
* |CreaNuovoIngr.|Post/ingredienti|IngredienteController#create|User/Admin | |
* |FormModificaIngr.|GET/ingredienti/{id}/edit|IngredienteController#editForm|User/Admin |mode==update|
* |AggiornaIngr.|POST/ingredienti/{id}|IngredienteController#update|User/Admin||
* |EliminaIngr.|POST/ingredienti/{id}/delete|IngredienteController#delete| User/Admin||

* |ListaRicette|GET/ricette|RicetteController#listaRicette|User/Admin| |
* |ListaRicettePersonali|GET/ricette/mie|RicetteController#listaRicette| User/Admin | |
* |DettaglioRicette|GET/ricette/{id}|RicetteController#details|User/Admin| |
* |FormNuovaRicetta|GET/ricette/new|RicetteController#createForm|User/Admin| mode==create|
* |CreaNuovaRicetta|Post/ricette|RicetteController#create|User/Admin| |
* |FormModificaRicetta|GET/ricette/{id}/edit|RicetteController#editForm| User/Admin|mode==update |
* |AggiornaRicetta| POST/ricette/{id}|RicetteController#update| User/Admin ||
* |EliminaRicetta| POST/ingredienti/{id}/delete|RicetteController#delete| User/Admin||

* |FormNuovaCategoria|GET/categoria/new|CategoriaController#createForm|User/Admin|mode==create|
* |CreaCategoria|Post/ricette|CategoriaController#create|User/Admin | |


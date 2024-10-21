Funkcijski zahtjevi
//TODO

Nefunkcijski zahtjevi
//TODO

Struktura (inicijalno)

README.md -> opis projekta
.env -> varijable okoline
docker-compose.yml -> pokretanje multi-container Docker aplikacija
.github/ -> specifične konfiguracije za GitHub

backend/
  src/main/java/com/eduhub/
    controllers/ -> REST API
    services/ -> Business logic
    repositories/ -> repozitoriji baze podataka
    models -> entiteti
    utils -> utility klase
  src/main/resources
    application.properties -> Spring Boot config
  src/test/java/com/eduhub -> testni slučaji
  build.gradle -> build datoteka
  Dockerfile -> Docker konfiguracija za backend

frontend/
  public/ -> public stavke
  src/
    components/ -> ponovno iskoristive komponente
    pages -> stranice aplikacije
    services -> API pozivi backendu
    context -> context API
    hooks
    styles -> CSS
    App.js
    index.js 
  package.json
  Dockerfile -> Docker konfiguracija za frontend

database/
  migrations/ -> SQL skripte za schemu baze podataka
  seeds/ -> podaci za inicijalnu bazu
  db_config.sql //

docs/
  architecture.md
  api.md
  setup.md
  design/ -> Figma...

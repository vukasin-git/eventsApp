# Events App

Events App je Android aplikacija za pregled, kreiranje i upravljanje događajima. Korisnicima omogućava registraciju i prijavu, pregled događaja po kategorijama, prijavljivanje na događaje, čuvanje zainteresovanih događaja, ocenjivanje prošlih događaja i upravljanje korisničkim profilom.

Aplikacija koristi lokalnu SQLite bazu podataka za čuvanje i rad sa podacima na uređaju, kao i serversku bazu podataka kojoj pristupa putem REST API-ja. Android klijent komunicira sa Node.js i Express serverom, dok se serverski podaci čuvaju u MongoDB bazi. Lokalni i serverski podaci se sinhronizuju pomoću jedinstvenih serverskih identifikatora.

## Funkcionalnosti

### Registracija i prijava korisnika

Korisnik može da:

- kreira novi nalog
- prijavi se postojećim nalogom
- odjavi se iz aplikacije
- pregleda korisničko ime i email adresu
- promeni lozinku

Lozinke se ne čuvaju kao običan tekst, već kao heširane vrednosti.

## Pregled događaja

Na glavnom ekranu prikazuje se lista dostupnih događaja.

Za svaki događaj prikazuju se:

- slika
- naziv
- kategorija
- lokacija
- datum i vreme
- oznaka za promoted događaj
- broj slobodnih mesta kod promoted događaja

Promoted događaji su vizuelno izdvojeni i prikazuju se na vrhu liste.

## Filtriranje događaja

Događaji mogu da se filtriraju prema kategorijama:

- Party
- Festival
- Stand-Up & Theater
- Concert
- Exhibition

Aktivni filter je vizuelno označen, a kategorije se učitavaju iz baze podataka.

## Detalji događaja

Klikom na događaj otvara se ekran sa detaljnim informacijama:

- naziv događaja
- opis
- kategorija
- lokacija
- datum i vreme
- slika
- slobodna mesta
- prosečna ocena
- broj pristiglih ocena

Korisnik sa tog ekrana može da označi događaj kao zainteresovan ili da potvrdi prisustvo.

Kod promoted događaja sistem proverava da li ima slobodnih mesta pre prijave korisnika.

## Zainteresovani događaji

Korisnik može da označi događaj kao zainteresovan.

Svi takvi događaji prikazuju se u posebnoj listi u okviru ekrana `Interested Events`.

Jedan korisnik može imati samo jedan zapis za isti događaj.

## Događaji kojima korisnik prisustvuje

U okviru ekrana `Attending Events`, događaji su podeljeni na:

- predstojeće događaje
- prošle događaje

Podela se vrši na osnovu datuma i vremena događaja.

Za prošle događaje dostupna je opcija za ocenjivanje.

## Ocenjivanje događaja

Korisnik može da oceni prošli događaj ocenom od 1 do 5 zvezdica.

Aplikacija:

- vizuelno prikazuje izabranu ocenu
- sprečava potvrdu bez izabrane ocene
- sprečava da isti korisnik više puta oceni isti događaj
- ažurira prosečnu ocenu događaja
- ažurira ukupan broj ocena

## Kreiranje događaja

Korisnik može da kreira novi događaj unosom:

- naziva
- opisa
- lokacije
- datuma i vremena
- kategorije
- promoted statusa
- kapaciteta za promoted događaj

Forma sadrži validaciju obaveznih polja i kapaciteta.

Novi događaj se čuva u bazi i odmah postaje vidljiv u listi događaja.

## Korisnički profil

Profil korisnika prikazuje:

- korisničko ime
- email adresu

Sa profila korisnik može da:

- promeni lozinku
- završi sesiju

## Lokalna baza podataka

Aplikacija koristi SQLite bazu `EventsApp.db`.

Lokalna baza sadrži tabele:

- `users`
- `events`
- `attendance`
- `ratings`

SQLite baza omogućava lokalno čuvanje i brzo prikazivanje podataka na uređaju.

## Serverska baza i API

Aplikacija komunicira sa udaljenim serverom putem REST API-ja.

Serverski deo koristi:

- Node.js
- Express
- MongoDB

Android aplikacija šalje HTTP zahteve pomoću klase zasnovane na `HttpURLConnection`.

Server obrađuje:

- registraciju i prijavu korisnika
- događaje
- prijave i zainteresovanost korisnika
- ocene
- ažuriranje broja prisutnih
- prosečne ocene i broj ocena

Server predstavlja glavni izvor podataka, dok se podaci potrebni za rad aplikacije čuvaju i lokalno u SQLite bazi.

## Sinhronizacija podataka

Podaci između lokalne SQLite baze i servera povezuju se pomoću serverskih `_id` vrednosti.

Aplikacija sinhronizuje:

- korisnike
- događaje
- zainteresovane događaje
- prijavljena prisustva
- prosečne ocene
- broj ocena
- broj prisutnih

Na ovaj način aplikacija može da koristi lokalne podatke za prikaz, dok serverska baza čuva centralno i aktuelno stanje.

## Tehnologije

- Java
- Android Studio
- XML
- SQLite
- MongoDB
- Node.js
- Express
- REST API
- HttpURLConnection
- Fragments
- ListView
- BaseAdapter
- ViewHolder pattern
- Factory pattern
- Singleton pattern
- Intent i Bundle
- Password hashing

## Android komponente

Aplikacija koristi više Activity i Fragment komponenti za organizaciju ekrana.

Glavne komponente su:

- `LoginActivity`
- `EventsActivity`
- `EventDetailsActivity`
- `InterestedEventsActivity`
- `AttendingEventsActivity`
- `CreateEventActivity`
- `RatingActivity`
- `ProfileActivity`
- `PasswordActivity`
- `EventsFragment`
- `MyEventsFragment`


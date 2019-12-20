# Projet Android Pointage

> Dans le cadre professionnel, cette application Android permet à un employé d'avertir son employeur, à l'aide de l'envoi d'un SMS, de son arrivée sur le lieu de travail, ou de son départ.

## Participants :
- **CAPOU Marie-Joseph** *- Partie connexion de l'utilisateur (base de données)*
- **PAQUEREAU Yoann** *- Gestion de la localisation*
- **THIBAULT Loïc** *- Gestion de l'envoi du SMS*

## Utilisation
#### Lieu de travail de base : INSSET - St-Quentin
Pour le modifier, modifier dans la classe **Work_Place** les coordonnées
```
this.lat_no = 49.838197;
this.lon_no = 3.296236;

this.lat_se = 49.835814;
this.lon_se = 3.305382;
```
![alt text](https://i.imgur.com/qk3tI5H.png)

#### Modifier le n° de téléphone
Pour la réception du SMS sur votre téléphone (lors des tests) dans la classe **EmployeCompanies** (L.158).

`smsManager.sendTextMessage("0668475292", null, msg, null, null);`

#### Identifiants de connexion pour l'utilisateur :
```
Login: Paquereau001
Password: Paq001
```

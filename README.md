
Registrazione e Login utente

Per questa web app ho utilizzato spring boot versione 3.0.2 come framework e tutte le sue librerie.
In particolare ho importato:
la dipendenza per spring-boot-starter-data-jpa per la persistenza dei dati sul db relazionale
La libreria spring-boot-starter-security per gestire l'autenticazione e criptare la password
La libreria spring-boot-starter-thymeleaf per l'autenticazione e la sicurezza lato FE

Ho usato twilio per creare un numero di telefono virtuale da cui inviare messaggi per le notifiche e per l'otp
Mentre per l'invio di mail ho utilizzato la libreria spring-boot-starter-mail
Per l'invio di messaggi su slack ho utilizzato la libreria slack-api-client

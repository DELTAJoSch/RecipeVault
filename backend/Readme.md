# Backend Template for SEPM Group Phase

## How to run it

### Start the backed

`mvn spring-boot:run`

### Start the backed with test data

If the database is not clean, the test data won't be inserted

`mvn spring-boot:run -Dspring-boot.run.profiles=generateData`

the following two testusers will be inserted:
email: `admin@example.com` and apassword `password`
email: `user@example.com` and apassword `password`

### Administration

#### h2-console

is reachable at: localhost:8080/h2-console

Saved Settings: `Generic H2 (Embedded)`
Setting Name: `Generic H2 (E)`
Driver Class: `org.h2.Driver`
JDBC URL: `jdbc:h2:file:./database/db`
User Name: `admin`
password: `password`


## OCR
Prerequesites: needs to have tesseract installed
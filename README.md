A simple microservice to retrieve contacts using Spring Boot, JPA and Postgres

It stores contact in this format

```
{
    "firstName": "TiloTTAMA",
    "lastName": "GAAT",
    "phoneNumber": "5103303218",
    "address": "Fremont,CA,94536",
    "lastContacted": 1420001966000
}
```

address is expected to be comma separated values
lastContacted is milliseconds from the epoch
phonenumber is a string.

Endpoints:

```
GET /v1/contacts/{id}  retrieves one contact with id
GET /v1/contacts/search/phoneNumber?areaCode=540 retrieves all contacts with phone number having the area code
GET /v1/contacts/search/address?state=CA retrieves all contacts in the state CA
GET /v1/contacts/search/lastContacted?start=1388552366000&end=1420001966000 retrieves all contacts
contacted within the date range. Start and end are milliseconds from epoch
```

```

To compile and run tests:
mvn clean install

To run:
mvn spring-boot:run
```







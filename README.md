Money Transfers Application
===========
This Money Transfer application contains two `api`:
1. For money transfer between two existing accounts;
2. For retrieve account details with current balance.

How start the application
===========

- Clone the project from the repo
- Navigate to the source root folder `transfers`
- Execute `./mvnw clean package`. This will create the fat jar at `/target` folder.
- Execute `docker compose up` to run local container of PostgreSQL and the app.

```
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
c.bsfdv.transfers.TransfersApplication   : Started TransfersApplication in 7.244 seconds (JVM running for 8.085)
```

- App will start at default port `8080` and the apis can be accessed via
  `http://localhost:8080/<api path>` or via Swagger URL `http://localhost:8080/swagger-ui.html`

API Details
===========

## Account ##

**Fetch details on a single account** - ```GET``` -
```http://localhost:8080/v1/account/1000341```

## Transfer ##

**Make a transfer** - ```POST``` -
```http://localhost:8080/v1/account/transfer-money```

Error Handling
=============
- The data validation error code starts with 400
- The transaction error starts with 402
- Other server run time exception is 500

Below are the two sample error scenario response.

```
{
    "errorCode": 402,
    "message": "Transfer amount exceeds available balance",
    "type": "INVALID_ACCOUNT_STATUS"
}
```

```
{
    "errorCode": 404,
    "message": "Account doesn't exists",
    "type": "ACCOUNT_NOT_FOUND"
}
```
<h1 align='center'>
Currency exchange project.
</h1>

## Overview

This project provides a REST API for managing currencies and exchange rates.
The API allows users to:
- get exchange rates between different currencies;
- add new currencies and their corresponding symbols;
- add or update exchange rates between currency pairs;
- validate currency codes and handle errors gracefully using appropriate HTTP status codes (e.g., 404 for not found, 400 for bad requests).

Key HTTP methods used:
- GET: Retrieve exchange rates or currency details.
- POST: Add new currencies or exchange rates.
- PATCH: Update existing exchange rates.

The API is designed following the MVC(S) architecture, ensuring separation of concerns and a structured approach to handling requests.

## Technologies / tools used:

- REST API
- Javax servlets
- JDBC
- SQLite
- Tomcat 9
- Gson
- Maven

## Database

#### Table `Currencies`

<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>ID</td>
        <td>Int</td>
        <td>Auto Increment, Primary Key</td>
    </tr>
    <tr>
        <td>Code</td>
        <td>Varchar</td>
        <td>Unique</td>
    </tr>
    <tr>
        <td>Fullname</td>
        <td>Varchar</td>
        <td>Full name of the currency</td>
    </tr>
    <tr>
        <td>Sign</td>
        <td>Varchar</td>
        <td>Currency sign</td>
    </tr>
</table>

Example
<table>
    <tr>
        <th>ID</th>
        <th>Code</th>
        <th>FullName</th>
        <th>Sign</th>
     </tr>
     <tr>
        <td>1</td>
        <td>AUD</td>
        <td>Australian dollar</td>
        <td>A$</td>
    </tr>
</table>

#### Table `ExchangeRates`
<table>
    <tr>
        <th>Column</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>ID</td>
        <td>Int</td>
        <td>Auto Increment, Primary Key</td>
    </tr>
    <tr>
        <td>BaseCurrencyId</td>
        <td>Int</td>
        <td>References to Currencies.ID</td>
    </tr>
    <tr>
        <td>TargetCurrencyId</td>
        <td>Int</td>
        <td>References to Currencies.ID</td>
    </tr>
    <tr>
        <td>Rate</td>
        <td>Decimal(6)</td>
        <td>The exchange rate of the base currency unit to the target currency unit</td>
    </tr>
</table>

Unique Index for a pair of fields BaseCurrencyId, TargetCurrencyId

## REST API


### Currencies

GET /currencies
```[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "ˆ"
    }
]
```

Status codes:
- Success - 200
- Error - 500

GET /currency/EUR
```{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "ˆ"
}
```

Status codes:
- Success - 200
- Currency code not specified - 400
- Currency not found - 404
- Error - 500

POST /currencies?name=Euro&code=EUR&sign=ˆ
```{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "ˆ"
}
```

Status codes:
- Success - 200
- Any required field not specified - 400
- Currency code already exists - 409
- Error - 500

### Exchange rates

GET /exchangeRates
```[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "ˆ"
        },
        "rate": 0.99
    }
]
```

Status codes:
- Success - 200
- Error - 500

GET /exchangeRate/USDRUB
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "ˆ"
    },
    "rate": 0.99
}
```

Status codes:
- Success - 200
- Any currency code not specified - 400
- Exchange rate not found - 404
- Error - 500

POST /exchangeRates/USDRUB?baseCurrencyCode=USD&targetCurrencyCode=EUR&rate=0.99
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "ˆ"
    },
    "rate": 0.99
}
```

Status codes:
- Success - 200
- Any required field not specified - 400
- Exchange rate already exists - 409
- Error - 500

PATCH /exchangeRate/USDRUB?rate=0.99
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "ˆ"
    },
    "rate": 0.99
}
```

Status codes:
- Success - 200
- Any required field not specified - 400
- Exchange rate not found - 404
- Error - 500

### Currency exchange

GET /exchange?from=USD&to=AUD&amount=10
```{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "Aˆ"
    },
    "rate": 1.45,
    "amount": 10.00
    "convertedAmount": 14.50
}
```

Status codes:
- Success - 200
- Any required field not specified - 400
- Exchange rate not found - 404
- Error - 500


## What I learned from this project?
- design REST API;
- how to work with HTTP methods like GET, POST and PATCH;
- gained a deeper understanding of MVC(S) architecture.
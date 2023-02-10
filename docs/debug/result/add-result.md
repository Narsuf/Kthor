# Post Result
Stores a new result in database.

## Endpoint
`/result`

First option, with already existing party:
```json
{
  "partyId": 2,
  "electionId": 3,
  "elects": 169,
  "votes": 11289335
}
```

Second option, create the party too:
```json
{
  "electionId": 3,
  "elects": 169,
  "votes": 11289335,
  "party": {
    "name": "PSOE",
    "color": "E20025"
  }
}
```

### Method
`POST`

### Response
```json
{
  "id": 1,
  "partyId": 2,
  "electionId": 3,
  "elects": 169,
  "votes": 11289335,
  "party": {
    "id": 2,
    "name": "PSOE",
    "color": "E20025"
  }
}
```
# Post Result
Updates a result in database.

## Endpoint
`/result/{id}`
```json
{
  "elects": 169,
  "votes": 11289335
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
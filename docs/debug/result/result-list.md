# GET Results list
Returns all the results stored in database.

## Endpoint
`/result`

### Method
`GET`

### Response
```json
{
  "results": [
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
  ]
}
```
# GET Result details
Returns all the information from one specific result.

## Endpoint
`/result/{id}`

### Method
`GET`

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
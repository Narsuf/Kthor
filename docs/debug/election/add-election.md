# Post Election
Stores a new election in database.

## Endpoint
`/election`
```json
{
  "date": "2008",
  "name": "Generales",
  "place": "España",
  "chamberName": "Congreso",
  "totalElects": 350,
  "scrutinized": 100.0,
  "validVotes": 25900439,
  "abstentions": 9172740,
  "blankVotes": 286182,
  "nullVotes": 165576,
  "results": [               // Optional     
    {
      "elects": 169,
      "votes": 11289335,
      "party": {
        "name": "PSOE",
        "color": "E20025"
      }
    }
  ]
}
```

### Method
`POST`

### Response
```json
{
  "id": 1,
  "date": "2008",
  "name": "Generales",
  "place": "España",
  "chamberName": "Congreso",
  "totalElects": 350,
  "scrutinized": 100.0,
  "validVotes": 25900439,
  "abstentions": 9172740,
  "blankVotes": 286182,
  "nullVotes": 165576,
  "results": [
    {
      "id": 2,
      "partyId": 3,
      "electionId": 1,
      "elects": 169,
      "votes": 11289335,
      "party": {
        "id": 3,
        "name": "PSOE",
        "color": "E20025"
      }
    }
  ]
}
```
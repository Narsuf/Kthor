# Post Election
Updates an election in database.

## Endpoint
`/election/{id}`
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
  "nullVotes": 165576
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
  "results": []
}
```
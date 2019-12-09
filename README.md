## How to Run:
mvn compile exec:java

#localhost:8080/balance/1 GET

#localhost:8080/transference POST
{
 "to_account_id":"2",
 "from_account_id":"1",
 "amount":1
}

## Architecture
The architecture consists on a core Domain, agnostic of the technologies we could use for
persistence (at the moment memory) or as delivery mechanism (http). As it should have little
reasons for changes due to changes in persistence, I decided to already define async interfaces
for the repository even when in memory is not that useful, because the repository interface lives in
the Domain package its stability should be high.
The Balance is calculated by going through all the events. As it's an O(n) operation a quick improvement for the future would be to store checkpoints and count from there.

## Web framework
I wanted to use a framework to solve the web side, as it can bring good benefit as long as it does
not pollute the Domain code. In this case is Vert.x but could have been any other at this scale.

## Dummy Data
The Main class stores two dummy Accounts (Ids 1 and 2), one with one CREDIT event to be able to make a transference.
I assumed that creating the Account and depositing intial money would be out of scope as they are easier than
the transfer flow and then less valuable for this exercise.

## Error messages and Security
Depends on the security standard, error messages could be more or less descriptive, I chose
to keep it simple and give little details.

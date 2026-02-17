# my-westpac-app

Spring Boot REST API for basic account management. Uses an in-memory H2 database and seeds sample data on startup.

**Features**
- Create accounts with an initial deposit
- Deposit and withdraw funds
- Retrieve account details
- Fetch latest account user info or full history

**Tech Stack**
- Java 17
- Spring Boot 3.2.2
- Spring Web, Spring Data JPA, Validation
- H2 (in-memory)
- Lombok
- JUnit 5, Mockito

**Requirements**
- JDK 17
- Maven 3.8+

**Run Locally**
```bash
mvn spring-boot:run
```

Base URL: `http://localhost:9090`

**Endpoints**
- `POST /api/accounts/create` Create a new account.
- `POST /api/accounts/{id}/deposit` Deposit funds.
- `POST /api/accounts/{id}/withdraw` Withdraw funds (amount must be greater than 10.00).
- `GET /api/accounts/{id}/account-info` Get account details.
- `GET /api/accounts/{id}/user-info` Get latest user info.
- `GET /api/accounts/{id}/user-info/history` Get user info history.

**Example Requests**
Create account:
```bash
curl -X POST http://localhost:9090/api/accounts/create \
  -H "Content-Type: application/json" \
  -d "{\"accountHolderName\": \"Alice\", \"amount\": 50.00}"
```

Withdraw:
```bash
curl -X POST http://localhost:9090/api/accounts/10000000000/withdraw \
  -H "Content-Type: application/json" \
  -d "{\"amount\": 20.00}"
```

**Validation Rules**
- Deposits must be positive.
- Withdrawals must be positive and greater than 10.00.
- Withdrawals cannot exceed the current balance.

**H2 Console**
- URL: `http://localhost:9090/h2-console`
- JDBC URL: `jdbc:h2:mem:bankdb`
- User: `sa` (default)
- Password: empty (default)

**Sample Data**
- Seeded from `src/main/resources/data.sql` on startup.
- Account IDs start at `10000000000`.

**Run Tests**
```bash
mvn test
```

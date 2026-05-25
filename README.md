# Mongo Book Online

Enterprise event-driven ecommerce book ordering sample.

## Architecture

- `frontend/`: Angular storefront and admin-style screens.
- `backend/book-service`: Spring Boot REST service for books.
- `backend/user-service`: Spring Boot REST service for users.
- `backend/order-service`: Spring Boot REST service for ordering, MongoDB persistence, and Kafka events.
- Kafka topic: `book.orders.events`.
- MongoDB: `192.168.1.83:27017`, user `root`.

## Services

| Service | Port | API |
| --- | ---: | --- |
| Book Service | `8081` | `/api/books` |
| User Service | `8082` | `/api/users` |
| Order Service | `8083` | `/api/orders` |
| Angular Frontend | `4200` | browser app |

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 20+
- Angular CLI 18+
- Docker, for local Kafka
- Reachable MongoDB at `192.168.1.83:27017`

## Start Kafka

```powershell
docker compose up -d
```

## Run Backend Services

Open three terminals:

```powershell
cd backend\book-service
mvn spring-boot:run
```

```powershell
cd backend\user-service
mvn spring-boot:run
```

```powershell
cd backend\order-service
mvn spring-boot:run
```

## Run Frontend

```powershell
cd frontend
npm install
npm start
```

Then open `http://localhost:4200`.

## Example API Calls

Create a book:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8081/api/books -ContentType application/json -Body '{"isbn":"9780134685991","title":"Effective Java","author":"Joshua Bloch","price":54.99,"stockQuantity":12}'
```

Create a user:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8082/api/users -ContentType application/json -Body '{"email":"reader@example.com","fullName":"Reader One","shippingAddress":"100 Main St"}'
```

Create an order:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8083/api/orders -ContentType application/json -Body '{"userId":"reader@example.com","items":[{"bookId":"9780134685991","title":"Effective Java","quantity":1,"unitPrice":54.99}]}'
```


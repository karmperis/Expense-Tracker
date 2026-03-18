# ExpenseTracker - Spring Boot Backend API (Introductory Project)

Expense Tracker API built with Spring Boot 3 and Azure SQL Database. This project is a sandbox intended to master core backend architecture patterns, specifically the **Controller-Service-Repository** layered architecture and **DTO mapping** (Entities <-> DTOs).

## Security Note (Deliberately Excluded)
To keep the focus purely on core architectural patterns and data flow, this version **explicitly excludes**:
- **Security:** Spring Security, JWT, and OAuth2 are not implemented.
- **RBAC:** Role-Based Access Control is not implemented.

This is intentionally designed so the project remains a clean foundation for later thesis-grade expansion.

## Frontend Status
There is **no frontend yet**. This is a **backend-only REST API** project.

## Tech Stack
- Java 17+ (Maven compiler in this repository is configured to a newer Java version; Java 17+ is expected)
- Spring Boot 3
- Spring Data JPA
- Azure SQL Database (cloud persistence layer)
- Lombok
- MapStruct (included as a target mapping approach; current mapping is implemented via dedicated mapper components)
- Jakarta Validation

## Architecture (Layered Controller-Service-Repository)
The codebase follows a simple three-layer structure:

1. **Controller layer (REST API)**
   - Receives HTTP requests and query parameters.
   - Performs input validation using **Jakarta Validation** (`@Valid`).
   - Returns DTOs (never exposing JPA entities directly).

2. **Service layer (Business logic)**
   - Contains the application’s business rules (e.g., when creating/updating domain objects).
   - Coordinates calls to repositories and mapper components.
   - Produces read-only DTOs for safe API responses.

3. **Repository layer (Persistence)**
   - Encapsulates data access using **Spring Data JPA** repositories.
   - Performs CRUD operations and supports entity retrieval used by the service layer.

### DTO Mapping
Entities are converted to and from DTOs to:
- protect internal domain structures,
- keep API contracts stable,
- simplify testing and future refactoring.

## API Base Path
All endpoints are exposed under:
`/api/v1`

## Main API Endpoints
### Users
- `POST /api/v1/users/register` - Register a new user
- `GET /api/v1/users?page=0&size=10` - Get paginated users
- `GET /api/v1/users/{uuid}` - Get a user by UUID
- `GET /api/v1/users/search?username={username}` - Find a user by username

### Categories
- `POST /api/v1/categories` - Create a category
- `GET /api/v1/categories?page=0&size=10` - Get paginated categories
- `PUT /api/v1/categories/{uuid}` - Update a category
- `DELETE /api/v1/categories/{uuid}` - Soft delete (deactivate) a category

### Accounts
- `POST /api/v1/accounts` - Create an account
- `GET /api/v1/accounts?page=0&size=10` - Get paginated accounts
- `GET /api/v1/accounts/{uuid}` - Get an account by UUID
- `PUT /api/v1/accounts/{uuid}` - Update an account
- `DELETE /api/v1/accounts/{uuid}` - Soft delete (deactivate) an account

### Payment Methods
- `POST /api/v1/payment-methods` - Create a payment method
- `GET /api/v1/payment-methods?page=0&size=10` - Get paginated payment methods
- `GET /api/v1/payment-methods/{uuid}` - Get a payment method by UUID
- `DELETE /api/v1/payment-methods/{uuid}` - Soft delete (deactivate) a payment method

### Transactions
- `POST /api/v1/transactions` - Record a new transaction
- `GET /api/v1/transactions?page=0&size=10` - Get paginated transactions
- `GET /api/v1/transactions/{uuid}` - Get a transaction by UUID
- `DELETE /api/v1/transactions/{uuid}` - Soft delete (and revert related effects, where applicable)

## OpenAPI / Swagger UI (Interactive Docs)
The project includes Swagger UI (OpenAPI 3) for interactive API documentation and testing.

Local URL:
`http://localhost:8080/swagger-ui/index.html`

Use this page to explore available endpoints, inspect request/response models, and execute calls directly from your browser.

## How to Run
### Prerequisites
1. **JDK 17+** and **Maven**
2. **Azure SQL Database** set up and reachable from your network
   - Ensure Azure SQL firewall rules allow your IP (or allow Azure services).
3. Configure database credentials and connection settings in `src/main/resources/application.properties`

### Azure SQL Setup
1. Create an Azure SQL logical server and a database (the project expects `expense_tracker_db`).
2. Configure firewall rules / networking so the application can connect.

### Application Properties Configuration
Update `src/main/resources/application.properties` with your values:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Note: This repository contains connection placeholders/values for development. For real environments, keep secrets out of version control and prefer environment variables or a secret manager.

Schema synchronization is handled via:
- `spring.jpa.hibernate.ddl-auto=update`

### Start the Application
From the project root:
- Windows:
  - `.\mvnw.cmd spring-boot:run`
- Or using Maven directly:
  - `mvn spring-boot:run`

After startup, verify Swagger UI is available at:
`http://localhost:8080/swagger-ui/index.html`

## Future Enhancements
This backend serves as the foundation for my **Spring Boot learning journey**. 
Planned enhancements to transition this from a sandbox to a production-grade application 
include:
- Implementing **JWT Security** and full authentication/authorization using **Spring Security**
- Adding **OAuth2** integration (where applicable)
- Introducing **Role-Based Access Control (RBAC)**
- Building a **React** or **Angular** frontend that consumes this API
# Expense Tracker - Spring Boot Backend API (Introductory Project)
Expense Manager using Java, Spring Boot, and Azure SQL Database.

To maintain a clear focus on core architectural patterns (**Controller-Service-Repository**), this initial version **deliberately excludes**:
* **Authentication & Authorization:** No Spring Security, JWT, or Role-Based Access Control.
* **Complex Permissions:** User capabilities and restricted access are not implemented.

The primary goal was to isolate and master the data flow between layers, DTO mapping, and Cloud Database integration without the added complexity of security layers at this stage.

---

## 🛠️ Key Technical Learnings
Through this sandbox project, I focused on:
* **Decoupling Logic:** Separating business rules (Service) from data access (Repository).
* **DTO Pattern:** Mastering the conversion between Database Entities and Data Transfer Objects to protect internal data structures.
* **Global Error Handling:** Implementing a unified response system for exceptions (e.g., EntityNotFound, AlreadyExists).
* **Cloud Infrastructure:** Managing firewall rules and connection strings for **Azure SQL Database**.
* **Validation:** Using Jakarta Bean Validation to ensure data integrity at the entry point (API).

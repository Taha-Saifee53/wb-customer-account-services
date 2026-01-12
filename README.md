# wb-customer-account-services

**Account Management Service**

**Overview**

The Account Management Service is a Spring Boot–based microservice responsible for managing customer accounts.
It follows an Event-Driven Architecture (EDA) and consumes account creation events published via RabbitMQ from the
Customer Service.

The service enforces business rules such as:

1. Maximum number of accounts per customer
2. Only one SALARY account per customer
3. Generation of unique account IDs

**Architecture**

<pre>
Customer Service
       |
       |  (AccountCreateEvent)
       v
RabbitMQ Exchange
       |
       v
Account Service Consumer
       |
       v
Account Service
       |
       v
Relational Database (H2 / RDBMS)

</pre>

**Technology Stack**

1. Java 21
2. Spring Boot 4.0.1
3. Spring Data JPA
4. RabbitMQ
5. Hibernate
6. H2 Database (for local/testing)
7. JUnit 5 & Mockito
8. Lombok
9. Swagger / OpenAPI

**Database Design**

| Column Name    | Type          | Description                |
|----------------|---------------|----------------------------|
| `account_id`   | VARCHAR(10)   | Primary key                |
| `customer_id`  | VARCHAR(7)    | Foreign key (Customer)     |
| `account_type` | VARCHAR(20)   | SAVINGS / SALARY / CURRENT |
| `currency`     | VARCHAR(3)    | ISO currency code          |
| `balance`      | DECIMAL(15,2) | Account balance            |
| `status`       | VARCHAR(20)   | ACTIVE / DORMANT           |
| `created_at`   | TIMESTAMP     | Creation time              |
| `updated_at`   | TIMESTAMP     | Last update time           |

**Relationship**

<pre>

Customer (1) ────────< Account (N)

</pre>

1. One customer can have multiple accounts
2. Each account belongs to one customer
3. Indexed on customer_id for performance

**Design Decisions**

**Event-Driven Architecture (EDA)**

1. Account creation is triggered via RabbitMQ events
2. Loose coupling between Customer and Account services
3. Improves scalability and resilience

**Business Rule Enforcement at Service Layer**

1. Account limits and validations are handled inside AccountService
2. Keeps controllers thin and logic centralized

**Many-to-One Relationship with Customer**

1. Proper relational mapping using JPA
2. Enables efficient joins and future reporting use cases

**Deterministic Account ID Generation**

1. Account ID = customerId + sequence
2. Ensures uniqueness without DB sequences (simple & predictable)

**Testing Strategy**

1. JUnit 5 + Mockito
2. 100% coverage for: </br>
   a. Service layer </br>
   b. RabbitMQ consumers </br>
   c. Configuration classes </br>
3. External dependencies (RabbitMQ, DB) are mocked
4. No embedded brokers used for unit tests

**Environment Variables Configuration**

To avoid committing sensitive information such as database usernames and passwords into source control, this service uses environment variables for all credentials.

**Setting Environment Variables**

<pre>
set DB_USERNAME=sa (default password of H2 for development local purpose)
set DB_PASSWORD= (default password of H2 for development local purpose)
set RABBITMQ_USERNAME=guest (default password of RABBITMQ for development local purpose)
set RABBITMQ_PASSWORD=guest (default password of RABBITMQ for development local purpose)
</pre>

**Assumptions**

1. Customer already exists before account creation
2. Customer ID is always 7 digits
3. Only one SALARY account is allowed per customer
4. Maximum 10 accounts per customer
5. Account creation is asynchronous
6. Currency validation is handled upstream
7. H2 is used only for development/testing

**Shortcomings & Limitations**

1. H2 Database

   a. Not suitable for production </br>
   b. Shared DB across services is only for demo purposes

2. No Idempotency Handling

    a. Duplicate RabbitMQ messages could create duplicate accounts </br>
    b. Needs event deduplication or idempotent keys

3. No Retry / Dead Letter Queue

   a. Failed message processing is not retried </br>
   b. DLQ should be added for robustness

4. Synchronous Validation Dependency

   a. Account service assumes customer data consistency </br>
   b. Could be improved using local cache or read model

5. Basic Logging

   a. Distributed tracing (e.g. Zipkin) not implemented

**Future Improvements**

1. Add Dead Letter Queues (DLQ)
2. Introduce idempotent event processing
3. Use PostgreSQL / Oracle for production
4. Add Saga / Compensation logic
5. Implement distributed tracing

**API Documentation**

<pre>

http://localhost:{port}/swagger-ui.html

</pre>


**Conclusion**

The Account Service demonstrates a clean, scalable, and event-driven design, aligned with modern microservice best
practices.
It prioritizes decoupling, maintainability, and testability, while leaving room for future enterprise-grade
enhancements.

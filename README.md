# Beneficiary Account Management System

This project is a system for managing beneficiary accounts using three CSV files: beneficiaries, accounts, and transactions. It provides REST APIs to retrieve beneficiary information, accounts, transactions, and balances, and to find the largest withdrawal for a beneficiary within the last month.
## Table of Contents

- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Dependencies](#dependencies)
- [Usage](#usage)


## Getting Started

### Prerequisites

- Java 17
- Maven
- H2 Database (Embedded)

### Setup

1. Clone the repository
   
   git clone https://github.com/stamnik23/transactionapi.git
   
2. Build the project

  mvn clean install
  
3.Run the application

  mvn spring-boot:run
  
4.The application will start on http://localhost:8080.

Project Structure
src/main/java/com/example/interview: Main application source code.

controller: REST controllers.
exception: Custom exceptions and exception handlers.
model: Entity models.
repository: Repository interfaces.
service: Service layer containing business logic.
loader: Data loader for reading CSV files.
src/test/java/com/example/interview: Test source code.



API Endpoints
Get Beneficiary Information
URL: /beneficiaries/{id}
Method: GET
Description: Retrieves information about a beneficiary by ID.
Get Beneficiary Accounts
URL: /beneficiaries/{id}/accounts
Method: GET
Description: Retrieves accounts associated with a beneficiary.
Get Beneficiary Transactions
URL: /beneficiaries/{id}/transactions
Method: GET
Description: Retrieves transactions for a beneficiary.
Get Beneficiary Balance
URL: /beneficiaries/{id}/balance
Method: GET
Description: Retrieves the balance for a beneficiary.
Get Largest Withdrawal (Last Month)
URL: /beneficiaries/{id}/largest-withdrawal
Method: GET
Description: Retrieves the largest withdrawal for a beneficiary.
Get Largest Withdrawal (Specific Month)
URL: /beneficiaries/{id}/largest-withdrawal-month
Method: GET
Description: Retrieves the largest withdrawal for a beneficiary in the last month.


Running Tests
To run the tests, use the following command:

mvn test
Dependencies
Spring Boot: Framework for building the application.
Spring Data JPA: For database operations.
H2 Database: Embedded database for testing.
OpenCSV: For reading CSV files


Usage
Get Beneficiary Information


curl -X GET http://localhost:8080/beneficiaries/{id}

Get Beneficiary Accounts


curl -X GET http://localhost:8080/beneficiaries/{id}/accounts

Get Beneficiary Transactions


curl -X GET http://localhost:8080/beneficiaries/{id}/transactions

Get Beneficiary Balance

curl -X GET http://localhost:8080/beneficiaries/{id}/balance

Get Largest Withdrawal for Beneficiary

curl -X GET http://localhost:8080/beneficiaries/{id}/largest-withdrawal

Get Largest Withdrawal (Last Month)

curl -X GET http://localhost:8080/beneficiaries/{id}/largest-withdrawal-month


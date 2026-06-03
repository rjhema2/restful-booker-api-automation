# Booking API Automation

REST Assured API automation framework for testing the Restful Booker API.

## Objective
 Automate the CRUD booking lifecycle of the Restful Booker API 
 and verify the data persistence across operations using REST Assured,
testNG,Jackson and Allure reporting

## Tech Stack
- Java 17
- REST Assured 5.4.0
- TestNG 7.9.0
- Jackson Databind 2.17.1
- Allure Reports 2.29.0
- Maven

## Framework Structure
src/test/java/com/hemalatha/bookingapi/
├── base/          # RequestSpecFactory, ResponseSpecFactory
├── endpoints/     # ApiEndPoints
├── models/        # Booking, BookingDates POJOs
├── tests/         # BookingApiTest
└── resources/     # allure.properties    

## Framework Feature
- RequestSpecBuilder
- ResponseSpecBuilder
- Jackson POJO mapping
- Allure reporting
- Request/Response logging
- CRUD booking lifecycle validation

## Test Scenarios
- Create a new booking (POST)
- Get booking and verify data persistence (GET)
- Update booking (PUT)
- Partial update booking (PATCH)
- Delete booking (DELETE)
- Verify booking deletion returns 404 (GET)

## How to Run Tests
mvn clean test

## How to Generate Allure Report
mvn allure:serve

## Application Under Test
https://restful-booker.herokuapp.com

## Scenerio Covered
CRUD booking lifecycle with data persistance validation
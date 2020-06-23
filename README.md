![CO2 Monitoring API pipepline](https://github.com/emmanuelsilva/co2-monitoring-service/workflows/CO2%20Monitoring%20API%20pipepline/badge.svg)

# CO2 Monitoring API

Restful API to collect data from sensors to monitoring CO2 and create alerts when concentrations reach critical levels.

# Architecture

TBD

## Project organization

```yaml
│  README.md
│  build.gradle
│  settings.gradle
└─ src
|  └─ business
|  └─ controller
|  └─ domain
|  |  └─ entity
|  |  └─ repository
|  └─ dto
|  └─ event
|  └─ exception
|  └─ service 
└─ tests
   └─ business
   └─ service
```

### packages responsability

#### business

This package contains all business rules. All business rules must be written in a pure function approach, that meaning no side effect is allowed in this layer. 

The main reason to keep business rules as a pure function is to write easier unit testing for all business logic of the project.

The service package is the only layer able to access this business package.

#### service

This layer is responsible to orchestrate the calling to all business rules to complete a specific use case of the application. 

Are responsible for this layer:

- Manage transactions
- Dependency Injection
- Business rules orchestration

#### controller

This layer is responsible to up the HTTP Restful endpoints, convert the input and call the service layer.

#### entity

The entities used to complete a specific business. All entities should be immutable to keep data consistent. All entities should be create or updated in the business layer.

#### repository

The repository is an interface that should used to fetch or persist an entity on databases.

#### dto

Data transfer objects are the protocol to communicates the controller and service layer.

#### event

All application events. An event must be an immutable object.

#### exception

Custom application exception.

# Tests

- ### business

As business rules are business functions, mocks are not allowed here, so just testing individual and focused tests for each isolated function.

- ### services

To testing this layer, mock is allowed to simulate the integration with the external resources, such as repositories, event-bus, etc.

## Pre requisites

Make sure you have all the following requisites before run:

- Java 14 or later

# How to build

```sh
./gradlew compileJava
```

# How to test

```sh
./gradlew test
```

# How to running

```sh
./gradlew bootRun
```

# masterDoc v4

## Summary

> A console-based Java application for managing **Person**, **Car**, and **Dog** entities with support for car transactions between people. The application follows a **Domain-Driven Design (DDD)** structure with separated layers: `controller`, `service`, `repository`, `model`, and `utils`. 
>
> **Major update in v4**: Complete uniform **ResultOperation** return type across **all** Service methods. No more raw `boolean`, entity, or `ArrayList` returns. Every operation (CRUD for Person/Car/Dog, read-only CarTransaction, and `buyCar`) now returns a `ResultOperation` containing `success`, `message`, `type`, `timestamp`, and optional `updatedObject`. The Controller was updated to consistently capture and print every `ResultOperation`. `updatePerson` served as the canonical example for the refactor. This change improves consistency, error reporting, and prepares the design for future API/response patterns.

## Project

- **Name:** PersonCarDog
- **Package:** `org.example`
- **Entry point:** `App.main()`
- **Build:** Maven (`pom.xml`)
- **Version:** 1.0-SNAPSHOT

Project structure (DDD layered architecture):

```
src/
└── main/
    └── java/
        └── org/
            └── example/
                ├── App.java
                ├── controller/
                │   └── Controller.java
                ├── model/
                │   ├── Car.java
                │   ├── CarTransaction.java
                │   ├── Dog.java
                │   └── Person.java
                ├── repository/
                │   └── Repository.java
                ├── service/
                │   └── Service.java
                └── utils/
                    ├── DataSeeder.java
                    ├── ResultOperation.java
                    └── Utils.java
```

## DDD Layers

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Controller** | `controller` | Console menu loop + submenus; user input handling; delegates all logic to Service; captures every `ResultOperation` |
| **Service** | `service` | All business logic and use cases (CRUD + buyCar); **every method returns `ResultOperation`** |
| **Repository** | `repository` | In-memory data access (fake database) |
| **Model** | `model` | Pure domain entities: `Person`, `Car`, `Dog`, `CarTransaction` |
| **Utils** | `utils` | UI helpers + `DataSeeder` + `ResultOperation` (standardized service result wrapper) |

## Classes

| Class | Fields / Methods | Description |
|---|---|---|
| **App** | — | Main class; delegates to `Controller.run()` |
| **Controller** | `run()`, submenus | Full menu-driven UI; every Service call now stores and prints `ResultOperation` |
| **Service** | All methods return `ResultOperation` | Implements full CRUD for Person/Car/Dog; read-only for CarTransaction; `buyCar(...)`; uniform `ResultOperation` returns |
| **Repository** | `id`, `people`, `cars`, `dogs`, `carTransactions` | In-memory fake database |
| **Person** | `id`, `name`, `age`, `car` | Represents a person who can own a car |
| **Car** | `id`, `make`, `model`, `year` | Represents a car |
| **Dog** | `id`, `name`, `breed`, `age` | Represents a dog |
| **CarTransaction** | `id`, `buyer`, `seller`, `date`, `car`, `contract` | Records a car sale between two people (read-only) |
| **Utils** | menus + input helpers | `mainMenu()`, `personMenu()`, `carMenu()`, `dogMenu()`, `carTransactionMenu()`, `askMenuOption(Scanner)`, `askString()`, `askInt()` |
| **DataSeeder** | `seedRepository(Repository)` | Uses Java Faker to populate initial data (3 cars, 3 people, 2 dogs, 2 transactions) |
| **ResultOperation** | `type, timestamp, success, message, updatedObject` + `setter(...)` overloads | Standardized return object for every Service operation. Provides success status, message, entity type, timestamp, and optional payload. |

### Repository — In-Memory Database

`Repository` acts as a **fake database** that simulates persistent storage entirely in memory.

| ArrayList (table) | Stores | Equivalent SQL table |
|---|---|---|
| `people` | `Person` objects | `PERSON` |
| `cars` | `Car` objects | `CAR` |
| `dogs` | `Dog` objects | `DOG` |
| `carTransactions` | `CarTransaction` objects | `CAR_TRANSACTION` |

Each table exposes four CRUD methods:

| Method | SQL equivalent | Description |
|---|---|---|
| `add<Entity>(entity)` | `INSERT` | Appends the object |
| `get<Entity>ById(id)` | `SELECT WHERE id = ?` | Returns matching object or `null` |
| `getAll<Entities>()` | `SELECT *` | Returns the entire list |
| `remove<Entity>(id)` | `DELETE WHERE id = ?` | Removes using `removeIf` |

> **Limitations:** Data is lost when the application stops. No indexes or constraints.

## Implemented Use Cases

| Use Case | Status | Notes |
|----------|--------|-------|
| **Person CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete — all return `ResultOperation` |
| **Dog CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete — all return `ResultOperation` |
| **Car CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete — all return `ResultOperation` |
| **CarTransaction (read-only)** | Implemented | List all + Find by ID — return `ResultOperation` |
| **Buy a car (person to person)** | Fully Implemented | `Service.buyCar(...)` — returns `ResultOperation` |

### Service Methods (v4) — All Return ResultOperation

#### Person
- `ResultOperation createPerson(String name, int age, Repository)`
- `ResultOperation getPersonById(String id, Repository)`
- `ResultOperation getAllPeople(Repository)`
- `ResultOperation updatePerson(String id, String newName, int newAge, Repository)`
- `ResultOperation deletePerson(String id, Repository)`

#### Car
- `ResultOperation createCar(String make, String model, int year, Repository)`
- `ResultOperation getCarById(String id, Repository)`
- `ResultOperation getAllCars(Repository)`
- `ResultOperation updateCar(String id, String newMake, String newModel, int newYear, Repository)`
- `ResultOperation deleteCar(String id, Repository)`

#### Dog
- `ResultOperation createDog(String name, String breed, int age, Repository)`
- `ResultOperation getDogById(String id, Repository)`
- `ResultOperation getAllDogs(Repository)`
- `ResultOperation updateDog(String id, String newName, String newBreed, int newAge, Repository)`
- `ResultOperation deleteDog(String id, Repository)`

#### CarTransaction (read-only)
- `ResultOperation getCarTransactionById(String id, Repository)`
- `ResultOperation getAllCarTransactions(Repository)`

#### Business Use Case
- `ResultOperation buyCar(Person buyer, Person seller, int price, Repository)`

### ResultOperation Contract

`ResultOperation` is created inside each Service method and populated via the fluent `setter(...)` helpers:

```java
ResultOperation result = new ResultOperation();
// success path (with payload)
return result.setter(true, "Person created successfully.", "Person", System.currentTimeMillis(), p);
// failure path
return result.setter(false, "Name cannot be empty.", "Person", System.currentTimeMillis());
```

Fields:
- `success: boolean`
- `message: String`
- `type: String` (e.g. "Person", "Car", "Dog", "CarTransaction")
- `timestamp: long`
- `updatedObject: Object` (payload on success: created/updated entity for create/getById/update/buyCar; list for getAll*; transaction for buyCar; `null` on failure or successful delete)

**Note:** `updatedObject` is `null` for successful delete operations, as no entity is returned in this case. For read operations (`get*ById`, `getAll*`), `updatedObject` contains the retrieved entity or list of entities.

Controller usage pattern (example):
```java
ResultOperation res = Service.createPerson(name, age, repo);
System.out.println("resultOperation: " + res);
```

### buyCar Behavior (v4)

1. Validates buyer exists in repository
2. Validates seller exists in repository
3. Seller must have a car
4. Buyer must not have a car
5. Transfers ownership (`buyer.setCar(car)`, `seller.setCar(null)`)
6. Creates and persists `CarTransaction`
7. Returns `ResultOperation` (success with transaction as `updatedObject`, or failure with explanatory message)

## Data Seeding (unchanged since v3)

On startup, `Controller` calls:
```java
DataSeeder.seedRepository(repo);
```

This uses **Java Faker** to create:
- 3 Cars
- 3 People (with strategic car assignments to enable `buyCar`)
- 2 Dogs
- 2 historical `CarTransaction` records

Total: **10 instances** preloaded for immediate use of all features.

## Console UI Structure (v4)

```
===== MAIN MENU =====
1. Person operations
2. Dog operations
3. Car operations
4. Buy a car (person to person)
5. View car transactions (read only)
6. Quit
```

Each of 1–3 opens a full submenu:
```
===== PERSON MENU =====
1. Create person
2. List all people
3. Find person by ID
4. Update person
5. Delete person
6. Back
```

Option 5 opens a read-only submenu for `CarTransaction`.

Every menu action now prints the resulting `ResultOperation`.

## UML

### Domain Model (unchanged)

```
┌─────────────────┐       ┌─────────────────┐
│    Person       │       │     Car         │
├─────────────────┤       ├─────────────────┤
│ - id: String    │       │ - id: String    │
│ - name: String  │       │ - make: String  │
│ - age: int      │       │ - model: String │
│ - car: Car      │──────>│ - year: int     │
└──────┬──────────┘       └─────────────────┘
       │
       │ buyer / seller
       ▼
┌─────────────────────┐
│   CarTransaction    │
├─────────────────────┤
│ - id: String        │
│ - buyer: Person     │
│ - seller: Person    │
│ - date: Date        │
│ - car: Car          │
│ - contract: String  │
└─────────────────────┘

┌─────────────────┐
│     Dog         │
├─────────────────┤
│ - id: String    │
│ - name: String  │
│ - breed: String │
│ - age: int      │
└─────────────────┘
```

### Layer Responsibilities (v4) — Service always returns ResultOperation

```
┌──────────────┐
│     App      │
└──────┬───────┘
       │
       ▼
┌─────────────────────┐
│     Controller      │
│  + run()            │
│  + personSubMenu()  │   // captures ResultOperation
│  + carSubMenu()     │   // prints resultOperation
│  + dogSubMenu()     │
│  + carTransactionSubMenu()   │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐       ┌─────────────────────────┐
│      Service        │       │     Repository          │
│  ALL methods return │──────>│ - people / cars / dogs  │
│  ResultOperation    │       │ - carTransactions       │
│  (create/get/update │       │ + add/get/remove        │
│   /delete/buyCar)   │       │                         │
└─────────────────────┘       └─────────────────────────┘
```

### Data Seeding Flow (unchanged)

```
App.main()
  └── Controller.run()
        ├── new Repository()
        ├── DataSeeder.seedRepository(repo)   // Faker data
        └── while(true) { main menu + submenus (all print ResultOperation) }
```

## Tech Stack

- **Language:** Java 21
- **Build tool:** Maven
- **Dependencies:**
  - JUnit 3.8.1 (test)
  - Java Faker 1.0.2 (data seeding)
- **Data storage:** In-memory (ArrayList)
- **UI:** Console (Scanner + menu system)
- **Result wrapper:** `ResultOperation` (custom)
- **IDE:** IntelliJ IDEA 2026.2.3

## Teaching Purpose (Spring Boot Preparation)

This version is designed as a **pre-Spring Boot teaching artifact**:

| Java SE (v4)                                      | Spring Boot Mapping                              |
|---------------------------------------------------|--------------------------------------------------|
| `Service.*` methods                               | `@Service` + dedicated service classes           |
| `Repository.add/get/remove`                       | Spring Data JPA Repository                       |
| `Controller` submenus                             | `@RestController` + `@RequestMapping`            |
| Manual validation + prints                        | Bean Validation + `@Valid` + exceptions          |
| `DataSeeder`                                      | `@PostConstruct` or `CommandLineRunner`          |
| `buyCar` business logic                           | `@Transactional` service method                  |
| `ResultOperation` uniform return                  | `ResponseEntity<ApiResponse<T>>` / DTO pattern   |
| Controller capturing `ResultOperation`            | ControllerAdvice + consistent response handling  |

## ResultOperation Refactor Notes (v4)

- Prior to v4, Service methods returned mixed types: `Person`, `Car`, `ArrayList<T>`, `boolean`.
- Refactor used `updatePerson` as the template.
- Every method now:
  1. Instantiates `ResultOperation result = new ResultOperation();`
  2. On validation/entity failure → `return result.setter(false, "...", "Type", timestamp);`
  3. On success (with or without payload) → `return result.setter(true, "...", "Type", timestamp, payload);`
- Controller updated in all submenus (person, dog, car, car-transaction) and in `runBuyCarDemo`.
- `runBuyCarDemo` fallback creation now safely extracts entities via `res.isSuccess() ? (Type) res.getUpdatedObject() : null`.
- Note: Successful read operations (`get*ById`, `getAll*`) and `buyCar` populate `updatedObject`; create/update return the modified entity; deletes on success return no payload (only status + message).

---

**Document version:** v4  
**Last major change:** Service + Controller refactor — all Service methods return `ResultOperation`; no more boolean/entity/list returns; Controller uniformly captures and logs results

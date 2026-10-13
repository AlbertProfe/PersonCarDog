# masterDoc v3

## Summary

> A console-based Java application for managing **Person**, **Car**, and **Dog** entities with support for car transactions between people. The application follows a **Domain-Driven Design (DDD)** structure with separated layers: `controller`, `service`, `repository`, `model`, and `utils`. 
>
> **Major update in v3**: Complete **CRUD** (Create, Read, Update, Delete) operations implemented for `Person`, `Car`, and `Dog`. `CarTransaction` is read-only. The `buyCar` use case remains fully functional. The application now includes dynamic data seeding using Java Faker and a complete menu-driven console UI.

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
                    └── Utils.java
```

## DDD Layers

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Controller** | `controller` | Console menu loop + submenus; user input handling; delegates all logic to Service |
| **Service** | `service` | All business logic and use cases (CRUD + buyCar) |
| **Repository** | `repository` | In-memory data access (fake database) |
| **Model** | `model` | Pure domain entities: `Person`, `Car`, `Dog`, `CarTransaction` |
| **Utils** | `utils` | UI helpers (`mainMenu`, submenus, `askString`, `askInt`) + `DataSeeder` |

## Classes

| Class | Fields / Methods | Description |
|---|---|---|
| **App** | — | Main class; delegates to `Controller.run()` |
| **Controller** | `run()`, submenus | Full menu-driven UI with separate submenus for Person, Dog, Car, and read-only CarTransaction |
| **Service** | CRUD + buyCar | Implements full CRUD for Person/Car/Dog; read-only for CarTransaction; `buyCar(...)` |
| **Repository** | `id`, `people`, `cars`, `dogs`, `carTransactions` | In-memory fake database |
| **Person** | `id`, `name`, `age`, `car` | Represents a person who can own a car |
| **Car** | `id`, `make`, `model`, `year` | Represents a car |
| **Dog** | `id`, `name`, `breed`, `age` | Represents a dog |
| **CarTransaction** | `id`, `buyer`, `seller`, `date`, `car`, `contract` | Records a car sale between two people (read-only) |
| **Utils** | menus + input helpers | `mainMenu()`, `personMenu()`, `carMenu()`, `dogMenu()`, `carTransactionMenu()`, `askString()`, `askInt()` |
| **DataSeeder** | `seedRepository(Repository)` | Uses Java Faker to populate initial data (3 cars, 3 people, 2 dogs, 2 transactions) |

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
| **Person CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete via submenu |
| **Dog CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete via submenu |
| **Car CRUD** | Fully Implemented | Create, List, Find by ID, Update, Delete via submenu |
| **CarTransaction (read-only)** | Implemented | List all + Find by ID only |
| **Buy a car (person to person)** | Fully Implemented | `Service.buyCar(...)` — menu option 4 |

### Service Methods (v3)

#### Person
- `createPerson(String name, int age, Repository)`
- `getPersonById(String id, Repository)`
- `getAllPeople(Repository)`
- `updatePerson(String id, String newName, int newAge, Repository)`
- `deletePerson(String id, Repository)`

#### Car
- `createCar(String make, String model, int year, Repository)`
- `getCarById(String id, Repository)`
- `getAllCars(Repository)`
- `updateCar(String id, String newMake, String newModel, int newYear, Repository)`
- `deleteCar(String id, Repository)`

#### Dog
- `createDog(String name, String breed, int age, Repository)`
- `getDogById(String id, Repository)`
- `getAllDogs(Repository)`
- `updateDog(String id, String newName, String newBreed, int newAge, Repository)`
- `deleteDog(String id, Repository)`

#### CarTransaction (read-only)
- `getCarTransactionById(String id, Repository)`
- `getAllCarTransactions(Repository)`

#### Business Use Case
- `buyCar(Person buyer, Person seller, int price, Repository)` — unchanged from v2

### buyCar Behavior (unchanged)
1. Validates buyer and seller exist
2. Seller must have a car
3. Buyer must not have a car
4. Transfers ownership
5. Creates and persists `CarTransaction`
6. Returns `true` / `false`

## Data Seeding (new in v3)

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

## Console UI Structure (v3)

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

### Layer Responsibilities (v3)

```
┌──────────────┐
│     App      │
└──────┬───────┘
       │
       ▼
┌─────────────────────┐
│     Controller      │
│  + run()            │
│  + personSubMenu()  │
│  + carSubMenu()     │
│  + dogSubMenu()     │
│  + carTxSubMenu()   │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐       ┌─────────────────────────┐
│      Service        │       │     Repository          │
│ + create/get/update │──────>│ - people / cars / dogs  │
│   /delete per entity│       │ - carTransactions       │
│ + buyCar(...)       │       │ + add/get/remove        │
└─────────────────────┘       └─────────────────────────┘
```

### Data Seeding Flow

```
App.main()
  └── Controller.run()
        ├── new Repository()
        ├── DataSeeder.seedRepository(repo)   // Faker data
        └── while(true) { main menu + submenus }
```

## Tech Stack

- **Language:** Java 21
- **Build tool:** Maven
- **Dependencies:**
  - JUnit 3.8.1 (test)
  - Java Faker 1.0.2 (data seeding)
- **Data storage:** In-memory (ArrayList)
- **UI:** Console (Scanner + menu system)
- **IDE:** IntelliJ IDEA 2026.2.3

## Teaching Purpose (Spring Boot Preparation)

This version is designed as a **pre-Spring Boot teaching artifact**:

| Java SE (v3)                     | Spring Boot Mapping                     |
|----------------------------------|-----------------------------------------|
| `Service.createPerson(...)`      | `@Service` + `PersonService`            |
| `Repository.add/get/remove`      | Spring Data JPA Repository              |
| `Controller` submenus            | `@RestController` + `@RequestMapping`   |
| Manual validation + prints       | Bean Validation + `@Valid` + exceptions |
| `DataSeeder`                     | `@PostConstruct` or `CommandLineRunner` |
| `buyCar` business logic          | `@Transactional` service method         |

---

**Document version:** v3  
**Last major change:** Full CRUD for all entities + DataSeeder + complete console UI

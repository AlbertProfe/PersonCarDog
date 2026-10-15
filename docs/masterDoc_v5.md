# masterDoc v5

## Summary

> A console-based Java application for managing **Person**, **Car**, and **Dog** entities with support for car transactions between people and dog adoptions. The application follows a **Domain-Driven Design (DDD)** structure with separated layers: `controller`, `service`, `repository`, `model`, and `utils`.
>
> **Major update in v5**: New **Dog Adoption** use case. A `Person` can now adopt a `Dog` through `Service.adoptDog(...)`, which links both entities **bidirectionally** (`Person.dog` ↔ `Dog.owner`) and records a read-only **`DogAdoption`** (same role as `CarTransaction` for `buyCar`). The model gains two relation fields and one new entity; `Repository` gains a `dogAdoptions` table; `deletePerson` / `deleteDog` cascade to keep both sides of the relation consistent; the console UI adds "Adopt a dog" and a read-only "Dog adoptions" submenu; `DataSeeder` pre-adopts the two seeded dogs. All new Service methods follow the v4 `ResultOperation` contract.

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
                │   ├── DogAdoption.java        <- new in v5
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
| **Controller** | `controller` | Console menu loop + submenus; user input handling; delegates all logic to Service; captures every `ResultOperation`; **new `runAdoptDog()` + `dogAdoptionSubMenu()`** |
| **Service** | `service` | All business logic and use cases (CRUD + `buyCar` + **`adoptDog`**); every method returns `ResultOperation` |
| **Repository** | `repository` | In-memory data access (fake database); **new `dogAdoptions` table** |
| **Model** | `model` | Pure domain entities: `Person`, `Car`, `Dog`, `CarTransaction`, **`DogAdoption`** |
| **Utils** | `utils` | UI helpers + `DataSeeder` + `ResultOperation`; **new `dogAdoptionMenu()`** |

## Classes

| Class | Fields / Methods | Description |
|---|---|---|
| **App** | — | Main class; delegates to `Controller.run()` |
| **Controller** | `run()`, submenus, `runBuyCarDemo()`, **`runAdoptDog()`**, **`dogAdoptionSubMenu()`** | Full menu-driven UI; every Service call stores and prints `ResultOperation` |
| **Service** | All methods return `ResultOperation` | CRUD for Person/Car/Dog; read-only CarTransaction + **DogAdoption**; `buyCar(...)`; **`adoptDog(...)`** |
| **Repository** | `id`, `people`, `cars`, `dogs`, `carTransactions`, **`dogAdoptions`** | In-memory fake database |
| **Person** | `id`, `name`, `age`, `car`, **`dog`** | A person who can own a car and adopt a dog |
| **Car** | `id`, `make`, `model`, `year` | Represents a car |
| **Dog** | `id`, `name`, `breed`, `age`, **`owner`** | A dog that may have an owner (back-reference to `Person`) |
| **CarTransaction** | `id`, `buyer`, `seller`, `date`, `car`, `contract` | Records a car sale between two people (read-only) |
| **DogAdoption** | **`id`, `adopter`, `dog`, `date`, `fee`, `contract`** | **Records a dog adoption (read-only); created only by `adoptDog`** |
| **Utils** | menus + input helpers | `mainMenu()`, `personMenu()`, `carMenu()`, `dogMenu()`, `carTransactionMenu()`, **`dogAdoptionMenu()`**, `askMenuOption()`, `askString()`, `askInt()` |
| **DataSeeder** | `seedRepository(Repository)` | Java Faker data: 3 cars, 3 people, 2 dogs, 2 transactions, **2 adoptions** |
| **ResultOperation** | `type, timestamp, success, message, updatedObject` + `setter(...)` | Standardized return object for every Service operation |

### Repository — In-Memory Database

| ArrayList (table) | Stores | Equivalent SQL table |
|---|---|---|
| `people` | `Person` objects | `PERSON` |
| `cars` | `Car` objects | `CAR` |
| `dogs` | `Dog` objects | `DOG` |
| `carTransactions` | `CarTransaction` objects | `CAR_TRANSACTION` |
| **`dogAdoptions`** | **`DogAdoption` objects** | **`DOG_ADOPTION`** |

Each table exposes four CRUD methods (`add`, `getById`, `getAll`, `remove`). The new table follows the same pattern: `addDogAdoption`, `getDogAdoptionById`, `getAllDogAdoptions`, `removeDogAdoption`.

> **Limitations:** Data is lost when the application stops. No indexes or constraints. Relation consistency (`Person.dog` ↔ `Dog.owner`) is enforced only by `Service`, not by the repository.

## Implemented Use Cases

| Use Case | Status | Notes |
|----------|--------|-------|
| **Person CRUD** | Fully Implemented | `deletePerson` now cascades to `dog.owner` |
| **Dog CRUD** | Fully Implemented | `deleteDog` now cascades to `owner.dog` |
| **Car CRUD** | Fully Implemented | Unchanged |
| **CarTransaction (read-only)** | Implemented | Unchanged |
| **Buy a car (person to person)** | Fully Implemented | Unchanged |
| **Adopt a dog (person <- dog)** | **New in v5** | `Service.adoptDog(...)` — menu option 6 |
| **DogAdoption (read-only)** | **New in v5** | List all + Find by ID — menu option 7 |

### Service Methods (v5) — New / Changed

#### DogAdoption (read-only)
- `ResultOperation getDogAdoptionById(String id, Repository)`
- `ResultOperation getAllDogAdoptions(Repository)`

#### Business Use Case
- `ResultOperation adoptDog(Person adopter, Dog dog, int fee, Repository)`

#### Changed (cascade)
- `deletePerson(...)`: if `p.getDog() != null` → `dog.setOwner(null)` before removal.
- `deleteDog(...)`: if `d.getOwner() != null` → `owner.setDog(null)` before removal.

All other Service methods are unchanged from v4.

### adoptDog Behavior (v5)

Validation chain — each failure returns `ResultOperation(success=false, type="DogAdoption")`:

1. Adopter is not `null` and exists in `repo.getAllPeople()`
2. Dog is not `null` and exists in `repo.getAllDogs()`
3. `adopter.getDog() == null` — one dog per person
4. `dog.getOwner() == null` — one owner per dog
5. `adopter.getAge() >= 18` — only adults can adopt
6. `fee >= 0`

On success:

7. Set **both** sides: `adopter.setDog(dog)` and `dog.setOwner(adopter)`
8. Create and persist `DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee)`
9. Return `ResultOperation(success=true, updatedObject=adoption)`

### buyCar vs adoptDog

| Aspect | `buyCar` | `adoptDog` |
|---|---|---|
| Parties | 2 people (buyer, seller) | 1 person + 1 dog |
| Relation | Unidirectional (`Person.car`) | **Bidirectional** (`Person.dog` ↔ `Dog.owner`) |
| Transfer | Car moves seller → buyer | Dog gets an owner (no previous owner allowed) |
| Extra rules | — | Adult check, fee ≥ 0 |
| Record | `CarTransaction` | `DogAdoption` |
| Controller selection | Hardcoded `people.get(0/1)` (demo) | **User picks person and dog by ID** |

### Bidirectional Relation — Design Notes

- `Dog.toString()` prints only `owner.getName()`, never the full `Person`, to avoid infinite recursion (`Person` → `Dog` → `Person` …).
- `Person.toString()` is unchanged and does not print the dog.
- Any code that sets one side **must** set the other (`adoptDog`, `deletePerson`, `deleteDog`, `DataSeeder`).

## Data Seeding (v5)

```java
DataSeeder.seedRepository(repo);
```

Java Faker creates:
- 3 Cars
- 3 People (ages 18–80, so they all pass the adult rule)
- 2 Dogs — **both pre-adopted** by people 0 and 1 (both sides of the relation set)
- 2 historical `CarTransaction` records
- **2 `DogAdoption` records**

Total: **12 instances**. The pre-adopted dogs let users immediately test the "Dog already has an owner" rule; person 2 has no dog and can adopt a newly created one.

## Console UI Structure (v5)

```
===== MAIN MENU =====
1. Person operations
2. Dog operations
3. Car operations
4. Buy a car (person to person)
5. View car transactions (read only)
6. Adopt a dog (person <- dog)
7. View dog adoptions (read only)
8. Quit
```

Option 6 flow (`runAdoptDog`):
```
Enter adopter (person) ID:
Enter dog ID:
Enter adoption fee:
```
The Controller resolves both objects via `repo.getPersonById` / `repo.getDogById` and delegates all validation to `Service.adoptDog`.

Option 7 opens:
```
===== DOG ADOPTIONS (READ ONLY) =====
1. List all adoptions
2. Find adoption by ID
3. Back
```

## UML

### Domain Model (v5)

```
┌─────────────────┐       ┌─────────────────┐
│    Person       │       │     Car         │
├─────────────────┤       ├─────────────────┤
│ - id: String    │       │ - id: String    │
│ - name: String  │       │ - make: String  │
│ - age: int      │       │ - model: String │
│ - car: Car      │──────>│ - year: int     │
│ - dog: Dog      │◄──┐   └─────────────────┘
└──────┬──────────┘   │
       │              │ owner / dog (bidirectional)
       │ buyer/seller │
       ▼              │
┌─────────────────────┐   ┌─────────────────┐
│   CarTransaction    │   │     Dog         │
├─────────────────────┤   ├─────────────────┤
│ - id: String        │   │ - id: String    │
│ - buyer: Person     │   │ - name: String  │
│ - seller: Person    │   │ - breed: String │
│ - date: Date        │   │ - age: int      │
│ - car: Car          │   │ - owner: Person │──┘
│ - contract: String  │   └────────┬────────┘
└─────────────────────┘            │
                                   │ adopter / dog
                                   ▼
                          ┌─────────────────────┐
                          │    DogAdoption      │
                          ├─────────────────────┤
                          │ - id: String        │
                          │ - adopter: Person   │
                          │ - dog: Dog          │
                          │ - date: Date        │
                          │ - fee: int          │
                          │ - contract: String  │
                          └─────────────────────┘
```

### Layer Responsibilities (v5)

```
┌──────────────┐
│     App      │
└──────┬───────┘
       │
       ▼
┌──────────────────────────┐
│       Controller         │
│  + run()                 │
│  + personSubMenu()       │
│  + carSubMenu()          │
│  + dogSubMenu()          │
│  + carTransactionSubMenu()│
│  + runBuyCarDemo()       │
│  + runAdoptDog()         │   // new: by ID
│  + dogAdoptionSubMenu()  │   // new: read-only
└──────┬───────────────────┘
       │
       ▼
┌─────────────────────┐       ┌─────────────────────────┐
│      Service        │       │     Repository          │
│  ALL methods return │──────>│ - people / cars / dogs  │
│  ResultOperation    │       │ - carTransactions       │
│  + CRUD             │       │ - dogAdoptions   (new)  │
│  + buyCar(...)      │       │ + add/get/getAll/remove │
│  + adoptDog(...)    │       │                         │
└─────────────────────┘       └─────────────────────────┘
```

### adoptDog Flow

```
Controller.runAdoptDog()
  ├── askString(personId), askString(dogId), askInt(fee)
  ├── adopter = repo.getPersonById(personId)
  ├── dog     = repo.getDogById(dogId)
  └── Service.adoptDog(adopter, dog, fee, repo)
        ├── 6 validations (exists x2, free x2, adult, fee)
        ├── adopter.setDog(dog); dog.setOwner(adopter)
        ├── repo.addDogAdoption(new DogAdoption(...))
        └── return ResultOperation(success, adoption)
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

| Java SE (v5)                                      | Spring Boot Mapping                              |
|---------------------------------------------------|--------------------------------------------------|
| `Service.*` methods                               | `@Service` + dedicated service classes           |
| `Repository.add/get/remove`                       | Spring Data JPA Repository                       |
| `Controller` submenus                             | `@RestController` + `@RequestMapping`            |
| Manual validation + prints                        | Bean Validation + `@Valid` + exceptions          |
| `DataSeeder`                                      | `@PostConstruct` or `CommandLineRunner`          |
| `buyCar` / `adoptDog` business logic              | `@Transactional` service method                  |
| `Person.dog` ↔ `Dog.owner`                        | `@OneToOne` + `mappedBy` (JPA)                   |
| `DogAdoption` read-only record                    | Entity written only by the service layer         |
| `ResultOperation` uniform return                  | `ResponseEntity<ApiResponse<T>>` / DTO pattern   |
| `runAdoptDog` (select by ID)                      | `@PostMapping("/adoptions")` with IDs in body    |

## Dog Adoption Notes (v5)

- Exercise spec: `docs/pra/exercise-adopt-dog.md`.
- `DogAdoption` is **never** created, updated or deleted from the UI; only `adoptDog` writes to it.
- `deletePerson` and `deleteDog` are the only places (besides `adoptDog`) that touch the relation, and both clear **both** sides.
- `runAdoptDog` deliberately differs from `runBuyCarDemo`: it asks for IDs instead of picking `people.get(0)` / `get(1)`, so students can target any person/dog pair and trigger every validation rule from the console.
- Verified: `mvn compile` passes; seeding reports `Adoptions: 2`; listing dogs shows `owner='<name>'` with no recursion.

---

**Document version:** v5  
**Last major change:** Dog Adoption use case — `Person.dog` ↔ `Dog.owner` bidirectional relation, new `DogAdoption` entity + repository table, `Service.adoptDog` with 6-rule validation, cascade on delete, new console menu options 6–7, DataSeeder pre-adopts 2 dogs

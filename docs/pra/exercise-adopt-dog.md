# Exercise: Adopt a Dog (Java SE / Pre-Spring Boot)

## Objective

Implement a new use case **`adoptDog`** that links a `Person` and a `Dog`, and records the operation as a read-only **`DogAdoption`**.

This mirrors the existing `buyCar(...)` → `CarTransaction` flow, but adds two new challenges:
- The relation **does not exist yet** in the model (`Person` has no `Dog`, `Dog` has no owner).
- The relation is **bidirectional**: both sides must stay in sync.

All business logic must live in `Service.java`.  
All user interaction (menus, input, output) must live in `Controller.java` + `Utils.java`.

## Context

- This is a **plain Java SE** project (no Spring yet).
- Architecture follows a simple DDD style:
  - `model/` → domain objects (no business logic)
  - `repository/` → in-memory storage (fake DB)
  - `service/` → all business rules and operations
  - `controller/` + `utils/` → console UI
- `buyCar(...)` in `Service` is the reference implementation for a use case.
- `Service` methods return a `ResultOperation` (see `utils/ResultOperation.java`). Follow the same pattern.
- `DogAdoption` is created only as a side effect of `adoptDog`. You must **not** allow direct creation, update or deletion of `DogAdoption` through the UI.

## Business Rules

A shelter gives a dog to a person.

- A person can own **at most one dog**.
- A dog can have **at most one owner**.
- Only adults (`age >= 18`) can adopt.
- The adoption fee must be `>= 0`.
- Every successful adoption is recorded as a `DogAdoption`.

## Requirements

### 1. Model

#### `Person`
- Add field `private Dog dog;` with getter/setter.

#### `Dog`
- Add field `private Person owner;` with getter/setter.
- Be careful with `toString()`: do **not** print the full `owner` object (infinite recursion risk if `Person.toString()` prints the dog). Print only the owner's name or id.

#### New: `DogAdoption` (in `model/`)
Same shape as `CarTransaction`:
- `String id` (UUID, generated in constructor)
- `Person adopter`
- `Dog dog`
- `Date date`
- `int fee`
- `String contract`
- Empty constructor + full constructor, getters/setters, `toString()`.

### 2. Repository

Add to `Repository.java`:
- `private ArrayList<DogAdoption> dogAdoptions;` (initialize in constructor)
- `addDogAdoption(DogAdoption adoption)`
- `getDogAdoptionById(String id)` → `DogAdoption`
- `getAllDogAdoptions()` → `ArrayList<DogAdoption>`
- `removeDogAdoption(String id)` → `boolean`

### 3. Service Layer

#### Use case

```java
public static ResultOperation adoptDog(Person adopter, Dog dog, int fee, Repository repo)
```

Validation chain — each failure returns `result.setter(false, "<message>", "DogAdoption", System.currentTimeMillis())` and prints the message, exactly as `buyCar` does:

1. `adopter` exists in `repo.getAllPeople()`
2. `dog` exists in `repo.getAllDogs()`
3. `adopter.getDog() == null` → otherwise "Adopter already owns a dog."
4. `dog.getOwner() == null` → otherwise "Dog already has an owner."
5. `adopter.getAge() >= 18` → otherwise "Adopter must be an adult."
6. `fee >= 0` → otherwise "Fee cannot be negative."

On success:
- `adopter.setDog(dog)`
- `dog.setOwner(adopter)`
- Create `DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee)`
- `repo.addDogAdoption(adoption)`
- Print the adoption
- Return `result.setter(true, "Dog adopted successfully.", "DogAdoption", System.currentTimeMillis(), adoption)`

#### Read-only methods
- `getDogAdoptionById(String id, Repository repo)` → `ResultOperation`
- `getAllDogAdoptions(Repository repo)` → `ResultOperation`

Copy the pattern from the `CarTransaction` read-only section.

#### Cascade on delete (update existing methods)
- `deletePerson`: if the person owns a dog, set `dog.setOwner(null)` before removing.
- `deleteDog`: if the dog has an owner, set `owner.setDog(null)` before removing.

Same idea as `deleteCar` clearing the car reference from its owner.

### 4. Utils — UI Helpers

Extend `Utils.java` with:

- `dogAdoptionMenu()` (read-only options, print only)
- Update `mainMenu()` with the new options (see below)

### 5. Controller — Menu-Driven UI

Update `Controller.java`:

- New main-menu option **Adopt a dog**:
  - Ask the person ID (`Utils.askString`)
  - Ask the dog ID
  - Ask the fee (`Utils.askInt`)
  - Resolve both objects via `repo.getPersonById` / `repo.getDogById`
  - Call `Service.adoptDog(...)`
- New main-menu option **View dog adoptions (read only)** opening a submenu.
- Unlike `runBuyCarDemo`, which hardcodes `people.get(0)` / `get(1)`, the user **must pick person and dog by ID**.

**Example main menu after changes:**
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

Dog adoption submenu (read-only):
```
===== DOG ADOPTIONS (READ ONLY) =====
1. List all adoptions
2. Find adoption by ID
3. Back
```

### 6. DataSeeder (optional but recommended)

- Seed at least **2 dogs already adopted** using `javafaker`, so the "already has an owner" rule can be tested from the console.
- Make sure the adopters are adults.

### 7. Non-Functional / Style Requirements

- Keep the same coding style as `buyCar`.
- All messages should be clear for students debugging.
- Do **not** put business logic inside `Controller` or `Utils`.
- `DogAdoption` must remain read-only from the UI.
- Both sides of the relation (`Person.dog` and `Dog.owner`) must **always** be consistent.

## Acceptance Criteria

- [ ] `Person` has a `dog` field; `Dog` has an `owner` field.
- [ ] `DogAdoption` model exists with the same shape as `CarTransaction`.
- [ ] `Repository` stores and retrieves `DogAdoption`.
- [ ] `Service.adoptDog` validates all 6 rules and returns `ResultOperation`.
- [ ] `Service` contains only read methods for `DogAdoption`.
- [ ] `deletePerson` / `deleteDog` keep the relation consistent.
- [ ] Controller lets the user adopt by selecting person and dog **by ID**.
- [ ] Controller allows viewing (list + by id) of `DogAdoption`.
- [ ] `buyCar` and existing CRUD continue to work unchanged.
- [ ] `Dog.toString()` does not cause infinite recursion.

## Optional Extensions

- `returnDog(Dog dog, Repository repo)`: reverse the adoption, keep the `DogAdoption` record, append `" - RETURNED"` to its contract.
- Instead of trusting only `adopter.getDog()`, check `getAllDogAdoptions()` for an active adoption by that person.
- Add a `status` enum to `DogAdoption` (`ACTIVE`, `RETURNED`).

## Preparation for Spring Boot

| Java SE (this project)              | Spring Boot equivalent                              |
|-------------------------------------|-----------------------------------------------------|
| `Person.dog` / `Dog.owner`          | `@OneToOne` + `mappedBy` (JPA)                      |
| `Service.adoptDog(...)`             | `@Service` method, `@Transactional`                 |
| `Repository.addDogAdoption(...)`    | `DogAdoptionRepository` (Spring Data JPA)           |
| `ResultOperation`                   | `ResponseEntity<T>` / custom API response DTO       |
| Controller "Adopt a dog" option     | `@PostMapping("/adoptions")` in a `@RestController` |

## Tips

- Start with the model (`Person`, `Dog`, `DogAdoption`).
- Then the repository list + methods.
- Then `adoptDog` in `Service`, testing each rule from the console.
- Then the cascade in `deletePerson` / `deleteDog`.
- Finally wire the menus in `Utils` and `Controller`.
- Use `buyCar` as a template for validation order and message style.

---

**Exercise file location:** `docs/pra/exercise-adopt-dog.md`

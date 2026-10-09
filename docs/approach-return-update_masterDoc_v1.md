# Approach: Return Update Wrapper (`ResultUpdateOperation`)

## Purpose

`ResultUpdateOperation` is a lightweight **return-value wrapper** designed to pack structured result information from CRUD update operations (and potentially other mutating operations) into a single object.

Instead of returning only `boolean` or the updated entity (or `null`), the wrapper carries:

- Operation outcome (`success`)
- Human-readable message
- Entity type tag
- Timestamp
- The actual updated entity (when successful)

This approach enables the caller (typically `Controller`) to:
- Know whether the operation succeeded without relying on `null` checks alone
- Display a meaningful message
- Optionally render or log the updated entity
- Keep a uniform return contract across future update methods

## Location

`src/main/java/org/example/utils/ResultUpdateOperation.java`

## Class Structure

| Field | Type | Description |
|-------|------|-------------|
| `type` | `String` | Logical entity type (e.g. `"Person"`, `"Car"`, `"Dog"`) |
| `timestamp` | `long` | `System.currentTimeMillis()` at result creation |
| `success` | `boolean` | `true` if operation completed successfully |
| `message` | `String` | Short outcome description for UI/logs |
| `updatedObject` | `Object` | The mutated entity on success; `null` on failure |

## Why `Object`?

`updatedObject` is typed as `java.lang.Object` to allow the wrapper to carry **any domain entity** without introducing a common base class or using generics at this stage.

**Rationale:**

- The project currently has independent model classes: `Person`, `Car`, `Dog`, `CarTransaction`.
- No shared supertype or interface exists for all entities.
- Using `Object` keeps the wrapper simple and non-intrusive.
- Callers cast (or use `instanceof`) when they need the concrete type.
- This is an intentional, minimal design for a pre-Spring teaching codebase.

Trade-offs:
- Loses compile-time type safety for the payload.
- Requires a cast at the call site if the concrete type is needed.
- Keeps the wrapper reusable across all entities without extra abstraction layers.

## Example Implementation — Service Layer

`Service.updatePerson(...)` is the first method converted to return `ResultUpdateOperation`.

```java
// src/main/java/org/example/service/Service.java
public static ResultUpdateOperation updatePerson(String id, String newName, int newAge, Repository repo) {
    System.out.println("Welcome to UPDATE PERSON");

    ResultUpdateOperation result = new ResultUpdateOperation();

    if (id == null || id.trim().isEmpty()) {
        System.out.println("ID cannot be empty.");
        return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
    }

    Person personFromDB = repo.getPersonById(id.trim());
    if (personFromDB == null) {
        System.out.println("Person not found.");
        return result.setter(false, "Person not found.", "Person", System.currentTimeMillis());
    }

    if (newName != null && !newName.trim().isEmpty()) {
        personFromDB.setName(newName.trim());
    }
    if (newAge >= 0) {
        personFromDB.setAge(newAge);
    }

    System.out.println("Person updated: " + personFromDB);
    return result.setter(true, "Person updated successfully.", "Person", System.currentTimeMillis(), personFromDB);
}
```

Key patterns:
- Always create a `ResultUpdateOperation` (even for failure paths).
- Use the convenience `setter(...)` fluent-style method.
- On validation or lookup failure → `success=false`, `updatedObject=null`.
- On success → `success=true` and attach the mutated entity.

## Example Usage — Controller Layer

Controller menu option 1 → Person submenu → option 4 demonstrates consumption.

```java
// src/main/java/org/example/controller/Controller.java
case "4":
    String uid = Utils.askString(scan, "Enter person ID to update: ");
    String newName = Utils.askString(scan, "New name (leave blank to keep): ");
    int newAge = Utils.askInt(scan, "New age (-1 to keep): ");
    ResultUpdateOperation resultUpdateOperation = Service.updatePerson(uid, newName, newAge, repo);
    System.out.println("resultUpdateOperation: " + resultUpdateOperation);
    break;
```

The controller:
- Receives the wrapper directly from Service.
- Prints the `toString()` representation for immediate feedback.
- Could later inspect `isSuccess()`, `getMessage()`, or cast `getUpdatedObject()`.

## API Surface (Current)

| Method | Description |
|--------|-------------|
| `ResultUpdateOperation()` | No-arg constructor |
| `ResultUpdateOperation(String type, long timestamp, boolean success, String message, Object updatedObject)` | Full constructor |
| `setter(boolean, String, String, long, Object)` | Sets all fields and returns `this` (fluent) |
| `setter(boolean, String, String, long)` | Overload that sets `updatedObject=null` |
| Getters / Setters | Standard accessors for each field |
| `toString()` | Debug-friendly representation |

## Design Notes / Future Considerations

- Currently used only for `updatePerson`.
- Other update methods (`updateCar`, `updateDog`) still return `boolean` (legacy pattern).
- The wrapper can be generalized for create/delete results or other mutating operations.
- If a common domain interface is later introduced, the field could be narrowed (or made generic).
- For Spring Boot teaching mapping, this mirrors returning a `ResponseEntity` body or a custom result DTO.

## References

- `src/main/java/org/example/utils/ResultUpdateOperation.java`
- `src/main/java/org/example/service/Service.java` → `updatePerson(...)`
- `src/main/java/org/example/controller/Controller.java` → `personSubMenu()` case `"4"`

---
**Document version:** approach-return-update v1  
**Last major change:** Introduction of `ResultUpdateOperation` wrapper for update results (Person example)

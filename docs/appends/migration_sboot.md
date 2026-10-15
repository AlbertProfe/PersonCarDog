# Spring Boot Migration from v3

## Summary

**Overall: Moderately easy to migrate structurally, but moderately difficult to do it *well*.**

## Why it's not trivial

| Aspect | Difficulty | Why |
|--------|------------|-----|
| **Size** | Easy | Only ~10 source files, no complex build |
| **Current architecture** | Hard | Heavy use of `static` methods, manual `new Repository()` passing, no interfaces |
| **Entry point** | Moderate | CLI + `Scanner` + infinite menu loops. Spring Boot expects either web or `CommandLineRunner` |
| **Repository** | Moderate-Hard | Custom in-memory `ArrayList` store. Real Spring style would use `@Repository` + either `Map` or Spring Data JPA |
| **Service layer** | Hard | All static, does validation + printing + side effects. Needs to become proper `@Service` beans |
| **No Spring deps yet** | Easy | Clean slate — just add Spring Boot parent + starters |
| **Models** | Easy | Plain POJOs, minimal effort to annotate or keep as-is |

## Realistic effort levels

- **Minimal migration** (just boot it as a Spring console app): <mark>Easy</mark> (few hours). Wrap `Controller.run()` in a `CommandLineRunner`.
- **Proper Spring migration** (DI, layered beans, no statics): <mark>Moderate</mark> (1-3 days).
- **Migrate to web/REST app**: <mark>Moderate-Hard</mark> (significant rewrite of Controller + Service + possibly Repository).

## Main blockers to a clean migration

1. Remove all `static` usage in `Service` and `Controller`
2. Convert manual repo passing into Spring-managed beans
3. Decide: keep CLI or move to `HTTP`? API Rest?
4. Replace `javafaker` (unmaintained) and ancient JUnit 3 to JUnit 5

**Bottom line**: The code volume is small, but the design is very "pre-Spring". 

> The migration is mostly about re-architecting the wiring and control flow rather than porting complex logic.

## Migration Approach (v3 → Spring Boot)

**Service location:** `src/main/java/org/example/service`

### Key Decisions

- **In-memory storage (arrays):** Keep the existing `ArrayList`-based `Repository`. No database or Spring Data JPA migration at this stage.
- **Existing Controller:** Do **not** remove the current `Controller`. It remains as the **backoffice** CLI interface.
- **REST API:** Add new `@RestController`(s) to expose the domain via HTTP (REST) alongside the CLI backoffice.
- **javafaker:** Keep `javafaker` for data seeding via `DataSeeder`.

### Target Architecture (minimal structural change)

- `Repository` — in-memory `ArrayList` storage, registered as a Spring bean (`@Repository` or `@Component`).
- `Service` — move from static methods to instance methods; annotated `@Service`; receives `Repository` via constructor injection.
- `Controller` (CLI) — kept for backoffice operations; can be adapted to use the injected `Service`.
- New `RestController` — `@RestController` layer for API endpoints (CRUD + buyCar flows).
- `DataSeeder` — unchanged usage of `javafaker` to populate the repository on startup or on demand.
- `App` — becomes `@SpringBootApplication`; CLI can run via `CommandLineRunner` or `ApplicationRunner` if desired.

### What changes (high level)

1. Remove `static` from `Service` methods; convert to proper Spring beans (CI).
2. Inject `Repository` (and later `Service`) instead of passing manually.
3. Add Spring Boot parent + starters (`spring-boot-starter`, optionally `spring-boot-starter-web` for REST).
4. Introduce `@RestController` classes (new files or package) without altering the existing CLI `Controller`.
5. <mark>Keep all current models</mark> and in-memory lists.

### Scope / non-goals for this approach

- No switch to a real database.
- No removal or major rewrite of the CLI backoffice.
- javafaker retained (despite being unmaintained).
- JUnit 3 → JUnit 5 upgrade is optional / deferred.

> **Outcome:** Dual interface (CLI backoffice + REST API) over the same in-memory service layer.

### Realistic minimal path (pom + tiny code)

1. **pom.xml** (necessary):
    - Spring Boot parent BOM.
    - `spring-boot-maven-plugin`.
    - `spring-boot-starter`.

2. **Code** (unavoidable for actual Spring Boot execution):
    - Annotate [App](https://github.com/AlbertProfe/PersonCarDog/blob/8ec69afbefc6ef29a2f2909fd26a89725db37e79/src/main/java/org/example/App.java) with `@SpringBootApplication`.
    - Change its [main](cci:9://file:///home/albert/MyProjects/Sandbox/PersonCarDog/src/main:0:0-0:0) to [SpringApplication.run(App.class, args);](https://github.com/AlbertProfe/PersonCarDog/blob/8ec69afbefc6ef29a2f2909fd26a89725db37e79/src/main/java/org/example/App.java#L9).




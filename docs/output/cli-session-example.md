# CLI Session Example – Approach Summary

## Goal

Provide a realistic, reproducible example of the running `PersonCarDog` application (v3) showing:

- Startup + data seeding
- Full CRUD for Person, Dog, Car
- Read-only access to CarTransaction
- Execution of the `buyCar` use case

> This helps users app and documentation readers see exactly what the console UI looks like without having to run the app themselves.

## Files

| File | Role | Location |
|------|------|----------|
| **Input script** | The sequence of menu choices and data entry (what the user "types") | `/tmp/v3_demo_input.txt` |
| **Output capture** | The actual console output produced by the application | `docs/output/output_v3.txt` |

## Approach (How it was generated)

1. Created a plain text file (`/tmp/v3_demo_input.txt`) containing the exact sequence of numbers and text that a user would enter in the menus.
2. Ran the application feeding that file as standard input:
   ```bash
   java -cp "target/classes:..." org.example.App < /tmp/v3_demo_input.txt > docs/output/output_v3.txt 2>&1
   ```
3. The resulting `output_v3.txt` contains the complete terminal session as it would appear to a user.

This technique produces an authentic record because:
- It uses the real running code.
- All `UUIDs`, messages, and formatting come from the actual execution.
- The same input file can be reused to regenerate the output later.

## What this specific session demonstrates

The input script (`v3_demo_input.txt`) performs a compact but representative flow:

1. Lists seeded data (Dogs, Cars, People).
2. Creates one new **Person**.
3. Creates one new **Dog**.
4. Creates one new **Car**.
5. Views **Car Transactions** (read-only).
6. Executes **Buy a car** (option 4) with price 250.
7. Views transactions again (to see the new record).
8. Exits.

The captured output (`output_v3.txt`, 214 lines) shows:
- The initial seeding banner ("DATA SEEDING COMPLETE").
- All menus and submenus.
- "Welcome to ..." messages from the Service layer.
- Created objects with real UUIDs.
- Existing `CarTransaction` records (read-only).
- The effect of a successful `buyCar`.

## Notes

- Not every menu option is exercised (by design it was a simple example, not a full test of the app).
- The trailing `NoSuchElementException` at the very end is normal when the input script ends while the program is still waiting for input in a submenu.
- The same approach can be used for other exercises or future versions (just change the input script and re-capture).

## References

- Input that was fed to the program: `/tmp/v3_demo_input.txt`
- Resulting console session: `docs/output/output_v3.txt`

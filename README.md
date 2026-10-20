# CSSD2101-Lab2
Lab 2 group. README.md pending.


## Testing

We used JUnit 5 and Maven for testing the bookstore application.

Tests included pricing, shipping, generation of e-books, catalogue methods, and tests for bad inputs. We did boundary case testing to ensure correct calculations.

**Test results:**
- Tests: 38
- Failed: 0
- Errors: 0
- Skipped: 0

All 38 tests were successful when we ran the project on Java 25.

## Limitations

Our bookstore application is a basic one. There are no payment systems, shipping services, or persistent storage.

Our system supports only five material types that are predefined. Additional material types need modifications of the current code.

Catalogue is not designed to allow simultaneous access from multiple threads.

Despite the fact that all 38 tests were successful, it does not mean that there are no bugs in the program.
ss
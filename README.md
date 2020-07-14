## Idea behind the solution:

**POST /transactions** - makes 2 operations: adding to transactions list and calculating current statistics.

**GET /statistics** - makes 1 operation: returns current statistics object with all required statistics.

There are 2 services in the app:

-   *TransactionService* - taking care of validation of input transaction and adding transaction to the transaction list inside TransactionRepository class.
-   *StatisticsService* - taking care of calculating statistics. There is also scheduled method which updates statistics once every 1000 ms, because calculation only on adding transaction is not enough to always have proper statistics.

## **Tests:**

-   Unit tests for *StatisticCalculator* methods
-   MockMvc tests covering the main use cases of application.

## Main tools:

Java 8, Spring Boot, Maven

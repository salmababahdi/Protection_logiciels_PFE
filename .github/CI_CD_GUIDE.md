# CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment.

## Pipeline Overview

The CI/CD pipeline consists of the following stages:

### 1. Build and Test (`build-and-test`)
- Sets up JDK 17
- Builds the application
- Runs unit tests (excluding integration tests)
- Uploads test results and reports as artifacts

### 2. Integration Test (`integration-test`)
- Runs integration tests that require a database connection
- Uses H2 in-memory database for testing
- Uploads test results and reports as artifacts

### 3. Code Quality (`code-quality`)
- Runs code quality checks
- Depends on successful build and test

### 4. Package (`package`)
- **Only runs on pushes to `main` branch**
- Creates the executable JAR file
- Uploads the JAR as an artifact

## Running Tests Locally

### Unit Tests Only
```bash
./gradlew test
```

### Integration Tests Only
```bash
./gradlew integrationTest
```

### All Tests
```bash
./gradlew check
```

### Build Application
```bash
./gradlew build
```

## Test Configuration

### Unit Tests
- Located in `src/test/java/`
- Use Mockito for mocking dependencies
- Do not require a database connection
- Run with `@ExtendWith(MockitoExtension.class)`

### Integration Tests
- Located in `src/test/java/`
- Use `@SpringBootTest` annotation
- Require H2 database (configured in `src/test/resources/application-test.properties`)
- Run with `@ActiveProfiles("test")`

## Artifacts

After a successful run, the following artifacts are available:
- `unit-test-results`: Raw JUnit XML results
- `unit-test-reports`: HTML test reports
- `integration-test-results`: Raw integration test XML results
- `integration-test-reports`: HTML integration test reports
- `application-jar`: The executable JAR file (only on main branch pushes)

## Branch Protection

Recommended branch protection rules for `main`:
- Require pull request reviews before merging
- Require status checks to pass before merging
- Required status checks: `build-and-test`, `integration-test`, `code-quality`

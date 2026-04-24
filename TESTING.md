# Testing Strategy & Coverage Documentation

## Overview
This document describes the testing infrastructure and coverage strategy for the GestionDeProjets application.

## Backend Testing (Java/Spring Boot)

### Setup
- **Framework**: JUnit 5
- **Mocking**: Mockito
- **Coverage Tool**: JaCoCo
- **Configuration**: Maven via pom.xml

### Key Components Tested
1. **Services** (`src/test/java/com/projetjee/backend/service/`)
   - ProjectService: CRUD operations, validation, resource assignment
   - TaskService: Task management, project linking
   - EmployeeService: Employee management, email uniqueness

2. **Security** (`src/test/java/com/projetjee/backend/security/`)
   - JwtService: Token generation, validation, user extraction

### Running Backend Tests

**Run all tests:**
```bash
cd backend
mvn clean test
```

**Generate coverage report:**
```bash
mvn clean test
# Report available at: target/site/jacoco/index.html
```

**Run specific test class:**
```bash
mvn test -Dtest=ProjectServiceTest
```

### Coverage Reports
- **Location**: `backend/target/site/jacoco/`
- **Open in browser**: Open `index.html` for interactive coverage analysis
- **Key metrics**: Line coverage, branch coverage by package

### Test Examples

#### Service Unit Test
```java
@Test
void testRecupererTousLesProjets_Success() {
    List<Project> projects = List.of(testProject);
    when(projectRepository.findAll()).thenReturn(projects);
    
    List<Project> result = projectService.recupererTousLesProjets();
    
    assertEquals(1, result.size());
    verify(projectRepository, times(1)).findAll();
}
```

#### Security Unit Test
```java
@Test
void testGenerateToken_Success() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setRole(Role.ADMIN);
    
    String token = jwtService.generateToken(user);
    
    assertNotNull(token);
    assertTrue(jwtService.isTokenValid(token, "test@example.com"));
}
```

## Frontend Testing (Angular/TypeScript)

### Setup
- **Test Runner**: Vitest (with Angular build support)
- **Coverage Tool**: Istanbul/nyc
- **Configuration**: Angular CLI + package.json scripts

### Running Frontend Tests

**Run tests:**
```bash
cd frontend
npm test
```

**Run tests with coverage:**
```bash
npm run test:coverage
```

**Watch mode:**
```bash
npm test -- --watch
```

### Coverage Reports
- **Location**: `frontend/coverage/`
- **Open in browser**: Open `coverage/index.html` for detailed analysis

## Coverage Goals

### Target Coverage Thresholds
- **Lines**: > 80%
- **Branches**: > 75%
- **Functions**: > 80%
- **Statements**: > 80%

### Current Phase (Phase 1)
- Backend unit tests established for core services
- Backend security (JWT) tests implemented
- Coverage infrastructure configured
- Frontend test structure ready
- Coverage reports auto-generated on each build

## Testing Best Practices

### 1. Naming Convention
```
- Test class: {ClassName}Test.java
- Test method: test{MethodName}_{Scenario}
- Example: testRecupererTousLesProjets_Success
```

### 2. Test Structure (AAA Pattern)
```java
// Arrange
List<Project> projects = List.of(testProject);
when(projectRepository.findAll()).thenReturn(projects);

// Act
List<Project> result = projectService.recupererTousLesProjets();

// Assert
assertEquals(1, result.size());
verify(projectRepository, times(1)).findAll();
```

### 3. Mock External Dependencies
- Use @Mock for repository/service dependencies
- Use @InjectMocks for service under test
- Avoid testing infrastructure (database, HTTP calls) in unit tests

### 4. Integration Tests
- Test controller endpoints with MockMvc
- Verify proper HTTP status codes
- Validate response structure with hamcrest matchers

## CI/CD Integration

### Build Verification
- Tests run automatically on each Maven/npm build
- Coverage reports generated with test results
- Build fails if critical services cannot be tested

### GitHub Actions (Phase 9)
- Automated test execution on PR
- Coverage reports published
- Quality gate checks enforced

## Next Steps to Reach 80% Coverage

### Backend
1. Add integration tests for all controllers
2. Test error scenarios and validation
3. Test edge cases (null values, empty lists)
4. Add converter and utility tests

### Frontend
1. Create service unit tests (auth, project, task, employee)
2. Test guard functionality (auth, role)
3. Test interceptor behavior
4. Component testing for critical flows

## Troubleshooting

### Backend Tests Fail
- **Issue**: Missing mockito stubs
- **Solution**: Verify all @Mock fields are properly configured

### Coverage Below 80%
- **Issue**: Untested code paths
- **Solution**: Add tests for error handling, edge cases, and critical paths

### Frontend Tests Not Running
- **Issue**: Vitest not properly configured
- **Solution**: Ensure package.json test script and vitest.config.ts are correct

## Resources
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Angular Testing Guide](https://angular.io/guide/testing)
- [Vitest Documentation](https://vitest.dev/)

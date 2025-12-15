# WebService 최종 프로젝트

본 프로젝트는 사용자, 도서, 리뷰 및 주문 관리를 위해 Spring Boot로 구축된 포괄적인 RESTful 웹 서비스입니다. 현대적인 아키텍처 패턴, 강력한 보안 기능, 데이터 유효성 검사 및 명확한 API 문서를 통합합니다.

## 🚀 사용 기술 스택

*   **프레임워크:** Spring Boot 3.3.5
*   **언어:** Java 17
*   **데이터베이스:** MySQL
*   **ORM:** Spring Data JPA, Hibernate
*   **빌드 도구:** Maven
*   **인증:** JWT (JSON Web Tokens)
*   **인가:** 역할 기반 접근 제어 (RBAC)
*   **유효성 검사:** Jakarta Bean Validation
*   **마이그레이션 도구:** Flyway
*   **API 문서화:** Springdoc OpenAPI (Swagger UI)
*   **로깅:** SLF4J/Logback

## ✨ 주요 기능

### 1. 핵심 리소스 (5개) 및 CRUD 작업

서비스는 5개의 핵심 리소스를 정의하며, `auth`를 제외한 각 리소스는 포괄적인 CRUD (생성, 읽기, 업데이트, 삭제) 기능을 제공합니다.

*   **`users` (사용자):** 사용자 등록, 프로필 관리(조회/업데이트), 계정 삭제. 사용자 목록 조회, 비활성화, 역할 변경을 위한 관리자 기능.
*   **`auth` (인증):** 사용자 로그인, JWT 토큰 재발급, 로그아웃.
*   **`book` (도서):** 도서 생성, 상세 조회, 검색/페이지네이션/정렬을 통한 목록 조회, 업데이트 및 삭제. 관리자는 모든 도서를 삭제할 수 있습니다.
*   **`review` (리뷰):** 도서 리뷰 생성, 조회, 업데이트 및 삭제. 관리자는 모든 리뷰를 삭제할 수 있습니다.
*   **`order` (주문):** (여러 항목을 포함한) 주문 생성, 사용자 자신의 주문 조회, 사용자 주문 목록 조회, 주문 상태 업데이트(예: 취소) 및 삭제. 관리자는 모든 주문을 조회하고 삭제할 수 있습니다.

### 2. 광범위한 HTTP 엔드포인트

*   **총 30개 이상:** 기본 CRUD 및 검색, 페이지네이션, 정렬, 관계형 하위 리소스와 같은 고급 기능을 포함하여 30개 이상의 고유한 HTTP 엔드포인트가 구현되었습니다.

### 3. 인증 및 인가

*   **JWT 기반 인증:** JWT를 사용한 안전한 사용자 인증.
    *   `POST /auth/login`: 액세스 토큰 및 리프레시 토큰 발급.
    *   `POST /auth/refresh`: 리프레시 토큰을 사용하여 액세스 토큰 재발급.
    *   `POST /auth/logout`: 토큰 무효화.
    *   만료되거나 위조된 토큰 처리.
*   **역할 기반 접근 제어 (RBAC):**
    *   `ROLE_USER`, `ROLE_ADMIN` 역할 정의.
    *   **관리자 전용 엔드포인트 (6개 이상):** `/admin/**` 경로 아래의 엔드포인트는 `ROLE_ADMIN` 역할을 요구하여 보호됩니다. 예시: `/admin/users`, `/admin/users/{id}/deactivate`, `/admin/users/{id}/role`, `/admin/reviews/{id}`, `/admin/orders`, `/admin/books/{id}`.

### 4. 요청 유효성 검사 및 통합된 에러 응답

*   **입력 유효성 검사:** 들어오는 모든 요청은 DTO 및 Jakarta Bean Validation 어노테이션을 사용하여 필드 타입, 범위, 필수 값에 대해 유효성 검사가 이루어집니다.
*   **통합된 에러 응답:** API 전반에 걸쳐 일관된 JSON 에러 응답 형식을 사용합니다.
    ```json
    {
      "isSuccess": false,
      "timestamp": "2025-03-05T12:00:00Z",
      "path": "/api/users/invalid",
      "status": 404,
      "code": "USER_NOT_FOUND",
      "message": "User not found",
      "details": null
    }
    ```
*   **에러 코드:** 다양한 시나리오에 대해 20개 이상의 특정 에러 코드가 정의되어 있습니다 (예: `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `409 Conflict`, `500 Internal Server Error`).

### 5. 목록 조회 공통 규격

*   **페이지네이션:** `page` (기본값 0), `size` (기본값 10, 최대 100) 쿼리 파라미터를 지원합니다.
*   **정렬:** `sort=필드명,ASC|DESC` 형식의 쿼리 파라미터를 지원합니다 (예: `sort=createdAt,DESC`).
*   **검색/필터:** 여러 조건(예: `keyword`, `genre`, `status`)을 지원합니다.

### 6. 데이터베이스 관리

*   **MySQL:** 데이터 영속화를 위해 MySQL을 활용합니다.
*   **외래 키 및 인덱스:** 데이터 무결성 및 쿼리 성능을 위해 관련 필드에 적절하게 설계된 외래 키 관계 및 인덱스가 정의되어 있습니다.
*   **마이그레이션 도구 (Flyway):** 데이터베이스 스키마 변경 사항을 관리합니다. 초기 스키마 및 시드 데이터 스크립트가 플레이스홀더로 제공됩니다.
*   **N+1 방지:** ORM 관계에서 N+1 쿼리 문제를 방지하기 위해 효율적인 페칭 전략(예: `FetchType.LAZY`)이 구현되었습니다.

### 7. 보안 및 성능 향상

*   **환경 변수 사용:** 민감한 정보(DB 자격 증명, JWT 비밀 키)는 환경 변수에서 로드되도록 구성되어, 레포지토리에 하드코딩되는 것을 방지합니다. (`.env.example` 파일이 제공됩니다.)
*   **비밀번호 해싱:** 사용자 비밀번호는 BCrypt 해싱을 사용하여 안전하게 저장됩니다.
*   **CORS 설정:** (개발/테스트 목적을 위해) 모든 출처의 요청을 허용하도록 기본 CORS(Cross-Origin Resource Sharing)가 활성화되어 있습니다.
*   **요청 크기 제한:** 잠재적인 서비스 거부 공격을 완화하기 위해 최대 요청 본문 크기는 10MB로 제한됩니다.
*   **헬스 체크:** `GET /health` 엔드포인트는 인증 없이 애플리케이션 상태(HTTP 200 OK)를 제공하며, 애플리케이션 버전 및 빌드 시간을 포함합니다.

### 8. 로깅 및 예외 추적

*   **요청/응답 요약 로깅:** 사용자 정의 `LoggingFilter`가 들어오는 요청 메서드, 경로, 응답 상태 코드 및 처리 시간을 로깅합니다.
*   **에러 스택 트레이스:** 서버 로그는 에러 발생 시 전체 스택 트레이스를 캡처합니다(민감한 정보는 공개 API 에러 응답에 노출되지 않습니다).

## 🛠️ 설정 및 애플리케이션 실행

### 전제 조건

*   Java Development Kit (JDK) 17 이상
*   Apache Maven 3.6.0 이상
*   MySQL 8.0 이상
*   Google Cloud SDK (Cloud Run 배포 시)
*   Docker (Cloud Run 배포 시)

### 환경 변수

`.env.example` 파일을 기반으로 프로젝트 루트에 `.env` 파일을 생성합니다.

```bash
# .env 파일 예시 내용
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=a_very_long_and_secure_jwt_secret_key_that_is_at_least_32_characters_long
JWT_ACCESS_TTL_SEC=1800
JWT_REFRESH_TTL_SEC=1209600
```

**참고:** `JWT_SECRET`은 강력하고 무작위로 생성된 키여야 합니다. 제공된 예시는 플레이스홀더입니다.

### 데이터베이스 설정

1.  **MySQL 데이터베이스 생성:** 애플리케이션을 위한 새 MySQL 데이터베이스(예: `webservicefinal`)를 생성합니다.
2.  **Flyway 마이그레이션:** Flyway는 애플리케이션 시작 시 자동으로 마이그레이션을 실행합니다.
    *   **중요:** `src/main/resources/db/migration/V1__initial_schema.sql` 파일은 데이터베이스 스키마의 실제 DDL(Data Definition Language)로 채워져야 합니다. `README.md`의 지침에 따라 이 파일을 업데이트하십시오.
    *   `src/main/resources/db/migration/V2__seed_data.sql` 파일에는 최소 200개 이상의 시드 데이터가 채워져 있습니다.

### 빌드 및 실행

1.  **프로젝트 빌드:**
    ```bash
    ./mvnw clean install
    ```
2.  **애플리케이션 실행:**
    ```bash
    java -jar target/webservice-0.0.1-SNAPSHOT.jar
    ```
    또는 Maven으로 직접 실행:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📚 API 문서화 및 접속 정보

애플리케이션이 실행 중인 경우, API 문서(Swagger UI)는 다음 주소에서 확인 가능합니다.

*   **로컬 환경:** `http://localhost:8080/swagger-ui.html`
*   **JCloud (Cloud Run) 환경:** `https://[YOUR_CLOUD_RUN_URL]/swagger-ui.html` (배포 후 `[YOUR_CLOUD_RUN_URL]`을 실제 Cloud Run 서비스 URL로 대체)

API 루트 주소는 위 Swagger UI 주소에서 `/swagger-ui.html` 부분을 제외한 부분입니다.

## 🚧 테스트 환경 설정 및 문제 해결

본 프로젝트의 테스트는 `application-test.properties` 설정을 사용하여 외부 MySQL 데이터베이스에 연결하도록 구성되어 있습니다. `mvnw clean install` 중 테스트 단계에서 발생하는 일반적인 문제에 대한 해결책은 다음과 같습니다.

### 1. 테스트 데이터베이스 연결 및 권한 문제

테스트를 외부 MySQL 인스턴스에 연결하기 위해 SSH 터널링을 사용하는 경우, 다음 사항을 확인해야 합니다.

*   **SSH 터널 설정:** MySQL 서버의 3306 포트가 로컬 `localhost:3307`로 포워딩되도록 SSH 터널을 설정해야 합니다. (`-L 3307:127.0.0.1:3306`과 같은 옵션 사용).
*   **환경 변수 설정:** 테스트 실행 전에 `DB_USERNAME` 및 `DB_PASSWORD` 환경 변수가 터미널 세션에 올바르게 설정되어야 합니다.
    *   `application-test.properties` 파일은 `spring.datasource.url=jdbc:mysql://localhost:3307/webservicefinal...` 와 같이 `localhost:3307`을 사용하도록 구성되어 있습니다.
    *   `DB_USERNAME`은 데이터베이스에 접속 가능한 사용자(예: `abc`)로 설정되어야 합니다.
    *   Windows PowerShell 예시: `$env:DB_USERNAME="abc"; $env:DB_PASSWORD="your_password_here"; ./mvnw clean install`
    *   Windows Command Prompt 예시: `set DB_USERNAME=guest && set DB_PASSWORD=your_password_here && mvnw clean install`

### 2. 테스트 간 데이터 무결성 문제 (Foreign Key 제약 조건 및 중복 엔트리)

이 프로젝트의 통합 테스트는 실제 데이터베이스에 대해 실행될 때 데이터 독립성을 보장하기 위해 각 테스트 메서드 전에 데이터베이스를 정리합니다. 이 과정에서 발생하는 `DataIntegrityViolationException` 또는 `Cannot delete or update a parent row`와 같은 오류는 주로 다음 수정 사항으로 해결되었습니다.

*   **테스트 클래스 트랜잭션 관리:** `AdminUserControllerTest.java` 및 `BookControllerTest.java`와 같은 통합 테스트 클래스에서 `@Transactional` 어노테이션을 제거했습니다. 이는 `@BeforeEach` 메서드에서 수행되는 데이터 정리 작업(`deleteAll()`)이 테스트 트랜잭션의 롤백에 의해 취소되지 않고 실제 데이터베이스에 즉시 반영되어, 테스트 간 데이터 독립성을 보장하기 위함입니다.
*   **종속성 역순 데이터 삭제:** `setUp()` 메서드 내에서 외래 키 제약 조건 위반을 방지하기 위해 다음 순서로 모든 엔티티의 데이터를 명시적으로 삭제하도록 리포지토리를 사용합니다.
    1.  `reviewRepository.deleteAll()`
    2.  `orderItemRepository.deleteAll()`
    3.  `orderRepository.deleteAll()`
    4.  `bookRepository.deleteAll()`
    5.  `userRepository.deleteAll()`
    이러한 변경은 `AdminUserControllerTest.java` 및 `BookControllerTest.java` 모두에 적용되었습니다.

### 3. `WebserviceApplicationTests`의 프로필 활성화

`WebserviceApplicationTests.java`는 기본적으로 "default" 프로필로 실행되어 `application-test.properties`에 정의된 데이터베이스 설정을 인식하지 못하는 문제가 있었습니다. 이 문제를 해결하기 위해 해당 클래스에 `@ActiveProfiles("test")` 어노테이션을 추가하여 테스트용 프로필을 명시적으로 활성화했습니다.

---

## 📬 Postman 컬렉션

Postman 컬렉션(`Postman_Collection.json`)은 API 테스트를 용이하게 하기 위해 별도로 제공될 것입니다. 이는 효율적인 워크플로우(예: 토큰 관리, 응답 유효성 검사)를 위한 환경 변수 및 Pre-request/Test 스크립트를 포함할 것입니다.
 
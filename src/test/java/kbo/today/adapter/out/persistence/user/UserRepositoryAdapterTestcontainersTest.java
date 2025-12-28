package kbo.today.adapter.out.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kbo.today.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

// Testcontainers 사용 시 주석 해제
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Testcontainers를 사용한 통합 테스트
 * 
 * 사용 방법:
 * 1. Docker가 실행 중이어야 함
 * 2. build.gradle에 testcontainers 의존성이 추가되어 있어야 함
 * 3. 아래 주석을 해제하고 사용
 * 
 * 이 테스트는 실제 PostgreSQL 컨테이너를 띄워서 테스트하므로 느립니다.
 * CI/CD 환경에서만 실행하거나, 로컬에서 선택적으로 실행하세요.
 */
// @DataJpaTest
// @Import(UserRepositoryAdapter.class)
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Testcontainers
@ActiveProfiles("test")
@DisplayName("UserRepositoryAdapter Testcontainers 통합 테스트 (선택적)")
class UserRepositoryAdapterTestcontainersTest {

    // Testcontainers 사용 시 주석 해제
    /*
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    @DisplayName("실제 PostgreSQL에서 도메인 엔티티를 저장하고 조회한다")
    void saveAndFindById_WithRealPostgreSQL_Success() {
        // given
        User domainUser = User.create("test@example.com", "encodedPassword", "테스트유저");

        // when
        User saved = userRepositoryAdapter.save(domainUser);
        Optional<User> found = userRepositoryAdapter.findById(saved.getId());

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getNickname()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("실제 PostgreSQL에서 이메일로 사용자를 조회한다")
    void findByEmail_WithRealPostgreSQL_Success() {
        // given
        User domainUser = User.create("test@example.com", "encodedPassword", "테스트유저");
        User saved = userRepositoryAdapter.save(domainUser);

        // when
        Optional<User> found = userRepositoryAdapter.findByEmail("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }
    */
    
    @Test
    @DisplayName("Testcontainers 테스트는 선택적으로 사용 (Docker 필요)")
    void placeholder() {
        // Testcontainers를 사용하려면 위 주석을 해제하고 Docker를 실행하세요
        assertThat(true).isTrue();
    }
}


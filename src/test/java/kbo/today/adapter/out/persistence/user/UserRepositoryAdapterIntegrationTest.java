package kbo.today.adapter.out.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kbo.today.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(UserRepositoryAdapter.class)
@ActiveProfiles("test")
@DisplayName("UserRepositoryAdapter 통합 테스트")
class UserRepositoryAdapterIntegrationTest {

    @Autowired
    private UserRepositoryAdapter userRepositoryAdapter;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("도메인 엔티티를 JPA 엔티티로 변환하여 저장하고 조회한다")
    void saveAndFindById_Success() {
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
        assertThat(found.get().getPassword()).isEqualTo("encodedPassword");

        // JPA 엔티티도 확인
        UserJpaEntity jpaEntity = userJpaRepository.findById(saved.getId()).get();
        assertThat(jpaEntity.toDomain().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("이메일로 사용자를 조회한다")
    void findByEmail_Success() {
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

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 빈 Optional을 반환한다")
    void findByEmail_NotFound_ReturnsEmpty() {
        // when
        Optional<User> found = userRepositoryAdapter.findByEmail("notfound@example.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("도메인 엔티티와 JPA 엔티티 간 변환이 정확하게 이루어진다")
    void domainToJpaEntityConversion_IsCorrect() {
        // given
        User domainUser = User.create("test@example.com", "encodedPassword", "테스트유저");

        // when
        User saved = userRepositoryAdapter.save(domainUser);
        UserJpaEntity jpaEntity = userJpaRepository.findById(saved.getId()).get();

        // then - 도메인 엔티티 검증
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getNickname()).isEqualTo("테스트유저");
        assertThat(saved.getPassword()).isEqualTo("encodedPassword");

        // then - JPA 엔티티 검증
        User domainFromJpa = jpaEntity.toDomain();
        assertThat(domainFromJpa.getEmail()).isEqualTo("test@example.com");
        assertThat(domainFromJpa.getNickname()).isEqualTo("테스트유저");
        assertThat(domainFromJpa.getPassword()).isEqualTo("encodedPassword");
    }
}


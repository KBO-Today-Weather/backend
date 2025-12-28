package kbo.today.adapter.out.persistence.stadium;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kbo.today.domain.stadium.Stadium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({StadiumRepositoryAdapter.class, StadiumQueryRepository.class})
@ActiveProfiles("test")
@DisplayName("StadiumRepositoryAdapter 통합 테스트")
class StadiumRepositoryAdapterIntegrationTest {

    @Autowired
    private StadiumRepositoryAdapter stadiumRepositoryAdapter;

    @Test
    @DisplayName("모든 구장 목록을 조회한다")
    void findAll_Success() {
        // when
        List<Stadium> stadiums = stadiumRepositoryAdapter.findAll();

        // then
        assertThat(stadiums).isNotNull();
        // 실제 데이터가 있으면 검증 가능
    }

    @Test
    @DisplayName("존재하지 않는 구장 ID로 조회 시 빈 Optional을 반환한다")
    void findById_NotFound_ReturnsEmpty() {
        // when
        var result = stadiumRepositoryAdapter.findById(999L);

        // then
        assertThat(result).isEmpty();
    }
}


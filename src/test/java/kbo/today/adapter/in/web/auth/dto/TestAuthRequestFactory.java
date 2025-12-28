package kbo.today.adapter.in.web.auth.dto;

import java.lang.reflect.Field;

/**
 * 테스트 전용 Request DTO 팩토리 클래스
 * 프로덕션 코드의 DTO는 불변성을 유지하고, 테스트에서만 사용하는 헬퍼 클래스
 */
public class TestAuthRequestFactory {

    private TestAuthRequestFactory() {
        // 유틸리티 클래스
    }

    public static CreateUserRequest createUserRequest(String email, String password, String nickname) {
        CreateUserRequest request = new CreateUserRequest();
        setField(request, "email", email);
        setField(request, "password", password);
        setField(request, "nickname", nickname);
        return request;
    }

    public static LoginRequest loginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        setField(request, "email", email);
        setField(request, "password", password);
        return request;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}


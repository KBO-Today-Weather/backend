package kbo.today.common.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {
    
    @PostMapping("/test/business-exception")
    public void throwBusinessException() {
        throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
    }

    @PostMapping("/test/validation")
    public void testValidation(@Valid @RequestBody TestRequest request) {
        // validation 테스트용
    }

    @PostMapping("/test/illegal-argument")
    public void throwIllegalArgumentException() {
        throw new IllegalArgumentException("잘못된 인자입니다.");
    }

    @PostMapping("/test/generic-exception")
    public void throwGenericException() {
        throw new RuntimeException("예상치 못한 오류");
    }

    public record TestRequest(
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String email,
        @NotBlank(message = "이름은 필수입니다")
        String name
    ) {}
}


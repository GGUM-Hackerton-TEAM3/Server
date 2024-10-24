package GGUM_Team3.Server.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldResponse> errors;

    private ErrorResponse(final ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = Collections.emptyList();
    }
    private ErrorResponse(final ErrorCode code, final List<FieldResponse> errors) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = errors;
    }
    private ErrorResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
        this.errors = Collections.emptyList();
    }
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldResponse.of(bindingResult));
    }
    public static ErrorResponse of(final String code, final String message) {
        return new ErrorResponse(code, message);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldResponse {

        private String field;
        private String reason;

        private static FieldResponse of(final String field, final String reason) {
            return new FieldResponse(field, reason);
        }

        private FieldResponse(final String field, final String reason) {
            this.field = field;
            this.reason = reason;
        }

        private static List<FieldResponse> of(final BindingResult bindingResult) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> FieldResponse.of(
                            error.getField(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}

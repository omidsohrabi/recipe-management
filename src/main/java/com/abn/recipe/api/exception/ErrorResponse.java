package com.abn.recipe.api.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private Object errorDetails;
}

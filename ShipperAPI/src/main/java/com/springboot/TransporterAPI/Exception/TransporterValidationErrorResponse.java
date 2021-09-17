package com.springboot.TransporterAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class TransporterValidationErrorResponse extends TransporterSubErrorResponse{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    TransporterValidationErrorResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }
}

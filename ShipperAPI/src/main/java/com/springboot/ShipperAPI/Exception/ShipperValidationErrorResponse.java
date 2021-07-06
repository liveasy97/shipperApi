package com.springboot.ShipperAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ShipperValidationErrorResponse extends ShipperSubErrorResponse{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ShipperValidationErrorResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }
}

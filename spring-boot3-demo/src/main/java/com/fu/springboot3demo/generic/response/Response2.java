package com.fu.springboot3demo.generic.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @since 2024-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Response2 extends BaseResponse<Response2> {
    private String username;
}

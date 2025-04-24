package com.fu.springboot3demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {
    @NotNull(message = "不能为NULL")
    private Integer id;
    @NotBlank(message = "不能为空")
    private String name;
}

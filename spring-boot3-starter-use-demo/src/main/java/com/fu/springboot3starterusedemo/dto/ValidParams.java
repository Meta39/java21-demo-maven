package com.fu.springboot3starterusedemo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidParams {
    @NotBlank(message = "不能为空")
    private String name;
    private Integer age;

}

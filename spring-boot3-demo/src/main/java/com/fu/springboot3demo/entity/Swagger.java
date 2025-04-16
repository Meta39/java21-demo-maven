package com.fu.springboot3demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "实体类描述或VO描述")
@Data
public class Swagger {

    @Schema(title = "实体类变量描述")
    private String swagger;

}

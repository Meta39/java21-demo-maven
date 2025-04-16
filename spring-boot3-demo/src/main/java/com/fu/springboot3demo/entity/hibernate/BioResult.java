package com.fu.springboot3demo.entity.hibernate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BioResult {
    private String bioId;
    private String bioName;
    private AntiResults antiResults;
}

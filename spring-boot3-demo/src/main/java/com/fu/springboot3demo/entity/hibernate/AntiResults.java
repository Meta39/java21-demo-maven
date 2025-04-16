package com.fu.springboot3demo.entity.hibernate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AntiResults {
    private String antiExamMethod;
    private String antiResultUnit;
    private List<AntiResult> antiResult;
}

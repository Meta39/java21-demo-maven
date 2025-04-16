package com.fu.springboot3demo.entity.hibernate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InspectionTestRecord {
    private String visitOrganization;
    private String visitOrganizationName;
    private String patientType;
    private String name;
    private List<TestResult> testResults;
    private List<PlantResult> plantResults;
    private List<BioResult> bioResults;

}

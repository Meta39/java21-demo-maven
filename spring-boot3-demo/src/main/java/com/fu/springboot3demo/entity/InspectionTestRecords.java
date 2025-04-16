package com.fu.springboot3demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("inspection_test_records")
public class InspectionTestRecords {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String visitOrganization;//visit_organization
    private String visitOrganizationName;//visit_organization_name
    private String patientType;//patient_type
    private String name;//
    private String testId;//test_id
    private String testName;//test_name
    private String plantTestId;//plant_test_id
    private String plantTestName;//plant_test_name
    private String bioId;//bio_id
    private String bioName;//bio_name
    private String antiExamMethod;//anti_exam_method
    private String antiResultUnit;//anti_result_unit
    private String antiResultBioId;//anti_result_bio_id
    private String etestResult;//etest_result
}

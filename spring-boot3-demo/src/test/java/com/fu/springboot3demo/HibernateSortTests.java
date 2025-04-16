package com.fu.springboot3demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.springboot3demo.entity.BSxml;
import com.fu.springboot3demo.entity.DynamicTransformer;
import com.fu.springboot3demo.entity.DynamicTransformerXml;
import com.fu.springboot3demo.entity.InspectionTestRecords;
import com.fu.springboot3demo.entity.hibernate.*;
import com.fu.springboot3demo.mapper.InspectionTestRecordsMapper;
import com.fu.springboot3demo.util.DateTimeUtils;
import com.fu.springboot3demo.util.JacksonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * 当使用Hibernate时，没有办法像MyBatis那样能多层级嵌套，实现只查询一次，循环一次实现返回多层级嵌套json数据。
 */
@Slf4j
@SpringBootTest
public class HibernateSortTests {
    @Resource
    private InspectionTestRecordsMapper inspectionTestRecordsMapper;

    // 模拟数据生成方法
    private static List<Map<String, Object>> fetchData() {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        record.put("SourcePatientId", 1);
        record.put("MedicalCardType", 1);
        record.put("MedicalCardID", "000001");
        record.put("VisitDateTime", "2025-03-11 00:00:00");
        record.put("DeptCode", 1758);
        record.put("DeptName", "全科");
        record.put("DoctorId", "5013999");
        record.put("DoctorName", "管理员");
        record.put("TotalCost", "10");
        record.put("VisitId", 1);
        record.put("Remark", "备注");
        record.put("CostDate", new Date());
        record.put("CostItemId", 1);
        record.put("CostItemName", "西药");
        record.put("CostItemCount", "1");
        record.put("FeeNo", 1);
        record.put("CostId", 1);
        record.put("CostName", "布洛芬缓释胶囊");
        record.put("DrugSpecifications", "30片/盒");
        record.put("CostNumber", 1);
        record.put("NumberUnit", "盒");
        record.put("CostPrice", "10");
        records.add(record);

        Map<String, Object> record2 = new HashMap<>();
        record2.put("SourcePatientId", 1);
        record2.put("MedicalCardType", 1);
        record2.put("MedicalCardID", "000001");
        record2.put("VisitDateTime", "2025-03-12 00:00:00");
        record2.put("DeptCode", 1758);
        record2.put("DeptName", "全科");
        record2.put("DoctorId", "5013999");
        record2.put("DoctorName", "管理员");
        record2.put("TotalCost", "10");
        record2.put("VisitId", 2);
        record2.put("Remark", "备注2");
        record2.put("CostDate", new Date());
        record2.put("CostItemId", 2);
        record2.put("CostItemName", "西药");
        record2.put("CostItemCount", "2");
        record2.put("FeeNo", 2);
        record2.put("CostId", 2);
        record2.put("CostName", "感冒灵");
        record2.put("DrugSpecifications", "1袋/盒");
        record2.put("CostNumber", 2);
        record2.put("NumberUnit", "盒");
        record2.put("CostPrice", "5");
        records.add(record2);
        return records;
    }

    /**
     * 适用于通用模板多层嵌套返回JSON格式（并且数据库返回的结果是List<Map<String, Object>>类型时使用，不适用于对象）
     * 1、支持替换数据库别名为其它变量名
     * 2、支持变量值进行格式化。如：日期格式化、枚举值转文本、金额元转分（即：金额乘100）
     */
    @Test
    public void testJson() throws JsonProcessingException {
        // 模拟数据库查询出来的数据
        List<Map<String, Object>> records = fetchData();

        //修改返回的变量名
        Map<String, String> costItemListsParams = new HashMap<>();
        costItemListsParams.put("DeptCode", "KSDM");//修改 DeptCode 变量名为 KSDM

        // 使用构建器配置转换规则
        List<Map<String, Object>> result = new DynamicTransformer
                .Builder()
                .addLevel("MedicalInformations", null, "SourcePatientId", "MedicalCardType", "MedicalCardID") // 第一级
                .addLevel("CostItemLists", costItemListsParams, "VisitDateTime", "DeptCode", "DeptName", "DoctorId", "DoctorName", "TotalCost", "VisitId", "Remark") // 第二级
                .addLevel("DetailsItemLists", null, "CostDate", "CostItemId", "CostItemName", "CostItemCount", "FeeNo") // 第三级
                .addLevel(null, null, "CostId", "CostName", "DrugSpecifications", "CostNumber", "NumberUnit", "CostPrice") // 第四级
                .addFieldFormatter("SourcePatientId", value -> Integer.parseInt(String.valueOf(value)) + 1)//修改字段值【时间格式化、枚举转换等】。如：每个值的SourcePatientId都进行 + 1
                .addFieldFormatter("CostDate", value -> DateTimeUtils.dateToString((Date) value, DateTimeUtils.DEFAULT_DATE_TIME_FORMAT))//日期格式化
                .build()//结束构建链
                .transform(records);//传入原始数据，进行数据处理

        log.info("对象（值为null也输出）{}", result);
        //JacksonUtils配置了值为null，则不序列化
        log.info("JSON(值为null，不序列化)：{}", JacksonUtils.JSON.writeValueAsString(result));
    }

    /**
     * 适用于通用模板多层嵌套返回XML格式（并且数据库返回的结果是List<Map<String, Object>>类型时使用，不适用于对象）
     * 1、支持集合内部名称
     * 2、支持替换数据库别名为其它变量名
     * 3、支持变量值进行格式化。如：日期格式化、枚举值转文本、金额元转分（即：金额乘100）
     */
    @Test
    public void testXml() throws JsonProcessingException {
        // 模拟数据库查询出来的数据
        List<Map<String, Object>> records = fetchData();

        //修改返回的变量名
        Map<String, String> costItemListsParams = new HashMap<>();
        costItemListsParams.put("DeptCode", "KSDM");//修改 DeptCode 变量名为 KSDM

        // 使用构建器配置转换规则
        List<Map<String, Object>> result = new DynamicTransformerXml
                .Builder()
                .addLevel("MedicalInformations", "MedicalInformation", null, "SourcePatientId", "MedicalCardType", "MedicalCardID") // 第一级
                .addLevel("CostItemLists", "CostItemList", costItemListsParams, "VisitDateTime", "DeptCode", "DeptName", "DoctorId", "DoctorName", "TotalCost", "VisitId", "Remark") // 第二级
                .addLevel("DetailsItemLists", "DetailsItemList", null, "CostDate", "CostItemId", "CostItemName", "CostItemCount", "FeeNo") // 第三级
                .addLevel(null, null, null, "CostId", "CostName", "DrugSpecifications", "CostNumber", "NumberUnit", "CostPrice") // 第四级
                .addFieldFormatter("SourcePatientId", value -> Integer.parseInt(String.valueOf(value)) + 1)//修改字段值【时间格式化、枚举转换等】。如：每个值的SourcePatientId都进行 + 1
                .addFieldFormatter("CostDate", value -> DateTimeUtils.dateToString((Date) value, DateTimeUtils.DEFAULT_DATE_TIME_FORMAT))//日期格式化
                .build()//结束构建链
                .transform(records);//传入原始数据，进行数据处理

        BSxml bSxml = new BSxml();
        bSxml.setData(result);
        log.info("对象（值为null也输出）{}", bSxml);
        //JacksonUtils配置了值为null，则不序列化
        log.info("XML(值为null，不序列化)：{}", JacksonUtils.XML.writeValueAsString(bSxml));
    }

    /**
     * 不适用模板时使用
     */
    @Test
    public void test2() throws JsonProcessingException {
        // 第一层分组键：VisitOrganization + VisitOrganizationName + PatientType + Name
        Map<String, InspectionTestRecord> recordMap = new LinkedHashMap<>(); // 保持插入顺序
        //1.查询出来的结果，先在SQL中用ORDER BY 排好序，这样代码里只要保证顺序插入即可保证列表有序。如果排序错误就会导致丢失一部分，所以排序要小心。
        List<InspectionTestRecords> sqlResults = inspectionTestRecordsMapper.selectList(new LambdaQueryWrapper<InspectionTestRecords>()
                .orderByAsc(InspectionTestRecords::getVisitOrganization,
                        InspectionTestRecords::getPatientType,
                        InspectionTestRecords::getTestId,
                        InspectionTestRecords::getPlantTestId,
                        InspectionTestRecords::getBioId
                )
        );

        InspectionTestRecord currentRecord = null;//临时存放当前记录
        String previousKey = null;//临时存放当前key到下一条记录用于判断上一条记录和当前记录是否是一样的key
        for (InspectionTestRecords sqlResult : sqlResults) {
            String visitOrganization = sqlResult.getVisitOrganization();
            String visitOrganizationName = sqlResult.getVisitOrganizationName();
            String patientType = sqlResult.getPatientType();
            String name = sqlResult.getName();

            //2.生成当前记录的分组键
            String currentKey = String.join("|", visitOrganization, visitOrganizationName, patientType, name);
            //如果切换到新分组，创建新记录
            if (!currentKey.equals(previousKey)) {
                currentRecord = new InspectionTestRecord(visitOrganization, visitOrganizationName, patientType, name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                recordMap.put(currentKey, currentRecord);
                previousKey = currentKey;
            }

            // TestResults 按顺序添加子对象（依赖数据库排序）
            String testId = sqlResult.getTestId();
            String testName = sqlResult.getTestName();
            if (testId != null) {
                currentRecord.getTestResults().add(new TestResult(testId, testName));
            }

            // PlantResults 按顺序添加子对象（依赖数据库排序）
            String plantTestId = sqlResult.getPlantTestId();
            String plantTestName = sqlResult.getPlantTestName();
            if (plantTestId != null) {
                currentRecord.getPlantResults().add(new PlantResult(plantTestId, plantTestName));
            }

            // 处理 BioResults（包含嵌套的 AntiResults）
            String bioId = sqlResult.getBioId();
            if (bioId == null) {
                continue;
            }

            String bioName = sqlResult.getBioName();
            String antiEaxmMethod = sqlResult.getAntiExamMethod();
            String antiResultUnit = sqlResult.getAntiResultUnit();
            String antiResultBioId = sqlResult.getAntiResultBioId();
            String etestResult = sqlResult.getEtestResult();

            // 获取当前 InspectionTestRecord 的 BioResults 列表
            List<BioResult> bioResults = currentRecord.getBioResults();
            // 判断是否需要创建新的 BioResult，前提是bioId是唯一
            if (bioResults.isEmpty() || !bioResults.getLast().getBioId().equals(bioId)) {
                // 创建新的 AntiResults 和 AntiResult
                AntiResults antiResults = new AntiResults(antiEaxmMethod, antiResultUnit, new ArrayList<>());
                antiResults.getAntiResult().add(
                        new AntiResult(antiResultBioId, etestResult)
                );

                // 创建并添加新的 BioResult
                BioResult newBioResult = new BioResult(bioId, bioName, antiResults);
                bioResults.add(newBioResult);
            } else {
                // 获取最后一个 BioResult，直接添加 AntiResult
                BioResult lastBioResult = bioResults.getLast();
                lastBioResult.getAntiResults().getAntiResult().add(
                        new AntiResult(antiResultBioId, etestResult)
                );
            }
        }
        // 将分组结果存入顶层报告
        List<InspectionTestRecord> inspectionTestRecords = new ArrayList<>(recordMap.values());

        log.info("{}", JacksonUtils.JSON.writeValueAsString(inspectionTestRecords));
    }

}

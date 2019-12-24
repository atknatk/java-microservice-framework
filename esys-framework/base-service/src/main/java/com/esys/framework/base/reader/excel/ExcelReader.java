package com.esys.framework.base.reader.excel;

import com.esys.framework.base.enums.DataTypeEnum;
import com.esys.framework.base.enums.ImportSectionEnum;
import com.esys.framework.base.reader.ImportReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public class ExcelReader implements ImportReader {

    final static SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");


    /**
     * Verilen excel stream'i list olarak doner
     * @param stream Excel Stream
     * @param sectionEnum Section Tipi
     * @param exportType Export Tipi
     * @return List of Excel
     */
    public List read(InputStream stream, ImportSectionEnum sectionEnum, Class exportType){
        try {
            Workbook wb = WorkbookFactory.create(stream);
            Sheet sheet = wb.getSheetAt(0);
            Map<String, List<ExcelValueConfig[]>> excelRowValuesMap = getExcelRowValues(sheet);
            return ExcelPojoMapper.getPojos(excelRowValuesMap.get(sectionEnum.getValue()),exportType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Excell Sheet icerisindeki data Map olarak doner
     * @param sheet Sheet
     * @return Map of Excel
     */
    public static Map<String, List<ExcelValueConfig[]>> getExcelRowValues(final Sheet sheet) {
        Map<String, List<ExcelValueConfig[]>> excelMap = new HashMap<>();
        Map<String, ExcelValueConfig[]> excelSectionHeaders = getExcelHeaderSections();
        int totalRows = sheet.getLastRowNum();
        excelSectionHeaders.forEach((section, excelValueConfigs) -> {
            List<ExcelValueConfig[]> excelValueConfigList = new ArrayList<>();
            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                ExcelValueConfig[] excelValueConfigArr = new ExcelValueConfig[excelValueConfigs.length];
                int k = 0;
                for (ExcelValueConfig ehc : excelValueConfigs) {
                    int cellIndex = ehc.getExcelIndex();
                    String cellType = ehc.getExcelColType();
                    Cell cell = row.getCell(cellIndex);
                    ExcelValueConfig config = new ExcelValueConfig();
                    config.setExcelColType(ehc.getExcelColType());
                    config.setExcelHeader(ehc.getExcelHeader());
                    config.setExcelIndex(ehc.getExcelIndex());
                    config.setPojoAttribute(ehc.getPojoAttribute());
                    if (DataTypeEnum.STRING.getValue().equalsIgnoreCase(cellType)) {
                        config.setExcelValue(cell.getStringCellValue());
                    } else if (DataTypeEnum.DOUBLE.getValue().equalsIgnoreCase(cellType)
                            || DataTypeEnum.INTEGER.getValue().equalsIgnoreCase(cellType)
                            || DataTypeEnum.FLOAT.getValue().equalsIgnoreCase(cellType)) {
                        config.setExcelValue(String.valueOf(cell.getNumericCellValue()));
                    } else if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        config.setExcelValue(String.valueOf(dtf.format(cell.getDateCellValue())));
                    }
                    excelValueConfigArr[k++] = config;
                }
                excelValueConfigList.add(excelValueConfigArr);
            }
            excelMap.put(section, excelValueConfigList);
        });
        return excelMap;
    }

    /**
     * excell'in başlık bilgileri
     * @return Map of Header
     */
    private static Map<String, ExcelValueConfig[]> getExcelHeaderSections() {
        List<Map<String, List<ExcelValueConfig>>> jsonConfigMap = getExcelHeaderConfigSections();
        Map<String, ExcelValueConfig[]> jsonMap = new HashMap<>();
        jsonConfigMap.forEach(jps -> {
            jps.forEach((section, values) -> {
                ExcelValueConfig[] excelValueConfigs = new ExcelValueConfig[values.size()];
                jsonMap.put(section, values.toArray(excelValueConfigs));
            });
        });
        return jsonMap;
    }

    /**
     * Excel'in config bilgisini alir
     * @return liste olarak config döner
     */
    private static List<Map<String, List<ExcelValueConfig>>> getExcelHeaderConfigSections() {
        List<Map<String, List<ExcelValueConfig>>> jsonMap = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonConfig = new String(
                    Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("config/excelConfig.json").toURI())));
            jsonMap = objectMapper.readValue(jsonConfig,
                    new TypeReference<List<HashMap<String, List<ExcelValueConfig>>>>() {
                    });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }

}

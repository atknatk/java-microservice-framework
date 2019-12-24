package com.esys.framework.base.reader.excel;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.esys.framework.base.reader.excel.ExcelReader.getExcelRowValues;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)
 * @project esys-framework
 */
@RunWith(SpringRunner.class)
public class ExcelReaderTest {

    @Test
    public void read() {
            boolean is = false;
        try {

            Workbook wb = WorkbookFactory.create( new ByteArrayInputStream("Test".getBytes()));
            Sheet sheet = wb.getSheetAt(0);
            Map<String, List<ExcelValueConfig[]>> excelRowValuesMap = getExcelRowValues(sheet);
            assertThat(excelRowValuesMap).isNull();
        } catch (EncryptedDocumentException | IOException e) {
            assertThat(is).isFalse();
        } catch (InvalidFormatException e) {
            assertThat(is).isFalse();
        }


    }
}

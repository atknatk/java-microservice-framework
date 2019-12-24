package com.esys.framework.base;

import com.esys.framework.base.reader.excel.ExcelReaderTest;
import com.esys.framework.base.service.impl.CurrencyServiceTest;
import com.esys.framework.base.service.impl.ProductServiceTest;
import com.esys.framework.base.service.impl.ReportServiceTest;
import com.esys.framework.base.service.impl.SignServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CurrencyServiceTest.class,
        ProductServiceTest.class,
        ReportServiceTest.class,
        SignServiceTest.class,
        ExcelReaderTest.class
})
public class BaseTestSuite {

    @BeforeClass
    public static void setUp() {
        System.out.println("setting up");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }

}

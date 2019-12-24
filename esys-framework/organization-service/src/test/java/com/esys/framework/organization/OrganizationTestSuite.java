package com.esys.framework.organization;

import com.esys.framework.organization.service.impl.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MainGroupServiceTest.class,
        GroupServiceTest.class,
        CompanyServiceTest.class,
        BranchOfficeServiceTest.class,
        DepartmentServiceTest.class,
        EmployeeServiceTest.class,
        CustomerServiceTest.class
})
public class OrganizationTestSuite {
    @BeforeClass
    public static void setUp() {
        System.out.println("setting up");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }


}

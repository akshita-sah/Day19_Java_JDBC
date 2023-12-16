import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Emily", 20000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Emily");
        Assert.assertTrue(result);
    }
    @Test
    public void givenEmployeePayrollStartDates_findNumberOfDataInAGivenRange() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.findRowsBetweenRange("2023-11-12");
        Assert.assertEquals(2, employeePayrollData.size());
    }
    @Test
    public void givenEmployeePayrollDB_findAvgSalaryGroupByGender()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double AvgSalaryM = employeePayrollService.getAvgSalary("M");
        Assert.assertEquals(32500.0,AvgSalaryM,0.0);
        double AvgSalaryF = employeePayrollService.getAvgSalary("F");
        Assert.assertEquals(20000.0,AvgSalaryF,0.0);
    }
    @Test
    public void givenEmployeePayrollDB_findSumSalaryGroupByGender()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double AvgSumM = employeePayrollService.getSumSalary("M");
        Assert.assertEquals(65000.0,AvgSumM,0.0);
        double AvgSumF = employeePayrollService.getSumSalary("F");
        Assert.assertEquals(20000.0,AvgSumF,0.0);
    }
}

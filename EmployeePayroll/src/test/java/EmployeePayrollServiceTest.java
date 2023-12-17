import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmployeePayrollServiceTest {
    //UC 2 - To Retrieve details from DB
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }
    //UC 3,4 - To Update details of a user with name Terisa to 20000.
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 20000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
    //UC 5 - To find the details of entries in a particular time range
    @Test
    public void givenEmployeePayrollStartDates_findNumberOfDataInAGivenRange() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.findRowsBetweenRange("2023-11-12");
        Assert.assertEquals(2, employeePayrollData.size());
    }
    //UC 6 - To find avg, sum of entries according to gender.
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
    /*UC 7 - Add a new user to DB, UC 8 -  Add a new user in employee_payroll_table and also in
    payroll_details table, also ensures either all or none operations take place.
     */
    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployeeToPayroll("Mark","M",50000.0, LocalDate.now());
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);
    }
}

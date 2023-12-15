import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws SQLException {
        EmployeePayrollDBService employeePayrollService = new EmployeePayrollDBService();
        List<EmployeePayrollData> employeeDataList = employeePayrollService.readData();
        Assert.assertEquals(3,employeeDataList.size());
    }
}

import java.sql.SQLException;
import java.util.List;

public class EmployeePayrollService {
    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public static void main(String[] args) throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        System.out.println(employeePayrollData);
        employeePayrollService.updateEmployeeSalary("Emily", 20000.00);
        employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        System.out.println(employeePayrollData);
        employeePayrollData = employeePayrollService.findRowsBetweenRange("2023-10-12");
        System.out.println(employeePayrollData);
        double AvgSalaryM = employeePayrollService.getAvgSalary("M");
        double AvgSalaryF = employeePayrollService.getAvgSalary("F");
        System.out.println("The average salary of Males : "+AvgSalaryM+" Average salary of Females : "+AvgSalaryF);
        double SumSalaryM = employeePayrollService.getSumSalary("M");
        double SumSalaryF = employeePayrollService.getSumSalary("F");
        System.out.println("The average salary of Males : "+SumSalaryM+" Average salary of Females : "+SumSalaryF);
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, double salary) throws SQLException {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData != null) employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<EmployeePayrollData> findRowsBetweenRange(String range) throws SQLException {
        List<EmployeePayrollData> employeeRangeList = employeePayrollDBService.findEmployeeDataBetweenRange(range);
        return employeeRangeList;
    }

    public Double getAvgSalary(String genderGroup)
    {
        return employeePayrollDBService.getAvgSalaryByGroup(genderGroup);
    }

    public Double getSumSalary(String genderGroup)
    {
        return employeePayrollDBService.getSumSalaryByGroup(genderGroup);
    }
}
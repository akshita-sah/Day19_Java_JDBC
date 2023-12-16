import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeePayrollDBService {
    private static EmployeePayrollDBService employeePayrollDBService;
    public PreparedStatement employeePayrollDataStatement;
    private EmployeePayrollDBService(){
    }

    public static EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService == null){
            employeePayrollDBService = new EmployeePayrollDBService();
        }
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_service?useSSL=false";
        String userName = "root";
        String password = "akshita";
        Connection connection;
        System.out.println("Connecting to database : " +jdbcURL);
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println("Connection is successful!!!" + connection);
        return connection;
    }

    /**
     * To retrieve data from the database using sql query.
     * @return List<EmployeePayrollData>
     */
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll_table; ";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try{
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e ){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try{
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try{
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll_table WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int updateEmployeeData(String name, double salary) throws SQLException {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingPreparedStatement(String name,double salary) throws SQLException {
        try(Connection connection = this.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("update employee_payroll_table set salary=? where name=?");
            stmt.setDouble(1, salary);//1 specifies the first parameter in the query i.e. name
            stmt.setString(2, name);
            int i = stmt.executeUpdate();
            System.out.println(i + " records updated");
            return i;
        }
        catch (SQLException e ){
            e.printStackTrace();
        }
        return 0;
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll_table set salary = %.2f WHERE name = '%s';", salary, name);
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int noOfUpdates =  statement.executeUpdate(sql);
            return noOfUpdates;
        } catch (SQLException e ){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * To find the list of entries between a certain range and return them.
     * @return List<EmployeePayrollData>
     * @throws SQLException
     */
    public List<EmployeePayrollData> findEmployeeDataBetweenRange(String range) throws SQLException {
        String sql = String.format("SELECT * FROM employee_payroll_table WHERE start BETWEEN CAST('%s' AS DATE) AND DATE(NOW())",range);
        List<EmployeePayrollData> employeeRangeList = new ArrayList<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeeRangeList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeeRangeList;
    }

    /**
     * To find average and sum of salaries after grouping by gender and finding for each gender.
     * @param genderGroup
     * @return
     */
    public Double getAvgSalaryByGroup(String genderGroup)
    {
        String sql = String.format("SELECT AVG(salary) FROM employee_payroll_table WHERE gender = '%s'",genderGroup);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            Double avgSalary = 0.0;
            while (resultSet.next()) {
                avgSalary = resultSet.getDouble("AVG(salary)");
            }
            return avgSalary;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }

    public Double getSumSalaryByGroup(String genderGroup)
    {
        String sql = String.format("SELECT SUM(salary) FROM employee_payroll_table WHERE gender = '%s'",genderGroup);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            Double sumSalary = 0.0;
            while (resultSet.next()) {
                sumSalary = resultSet.getDouble("SUM(salary)");
            }
            return sumSalary;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }
}

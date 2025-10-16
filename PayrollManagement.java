import java.util.ArrayList;
import java.util.Scanner;

interface Payable {
    double calculateNetSalary();
    void generatePayslip();
}

abstract class Employee {
    protected String empId;
    protected String name;
    protected double basicSalary;

    public Employee(String empId, String name, double basicSalary) {
        this.empId = empId;
        this.name = name;
        this.basicSalary = basicSalary;
    }

    public String getEmpId() { return empId; }
    public String getName() { return name; }
    public double getBasicSalary() { return basicSalary; }

    public void setEmpId(String empId) { this.empId = empId; }
    public void setName(String name) { this.name = name; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    abstract double calculateTax();
}

class PermanentEmployee extends Employee implements Payable {
    private double bonus;

    public PermanentEmployee(String empId, String name, double basicSalary, double bonus) {
        super(empId, name, basicSalary);
        this.bonus = bonus;
    }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    @Override
    double calculateTax() {
        return 0.10 * (basicSalary + bonus);
    }

    @Override
    public double calculateNetSalary() {
        return (basicSalary + bonus) - calculateTax();
    }

    @Override
    public void generatePayslip() {
        System.out.println("----- Payslip (Permanent Employee) -----");
        System.out.println("ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Basic Salary: " + basicSalary);
        System.out.println("Bonus: " + bonus);
        System.out.println("Tax: " + calculateTax());
        System.out.println("Net Salary: " + calculateNetSalary());
        System.out.println("----------------------------------------");
    }
}

class ContractEmployee extends Employee implements Payable {
    private int contractDuration;

    public ContractEmployee(String empId, String name, double basicSalary, int contractDuration) {
        super(empId, name, basicSalary);
        this.contractDuration = contractDuration;
    }

    public int getContractDuration() { return contractDuration; }
    public void setContractDuration(int contractDuration) { this.contractDuration = contractDuration; }

    @Override
    double calculateTax() {
        return 0.05 * basicSalary;
    }

    @Override
    public double calculateNetSalary() {
        return basicSalary - calculateTax();
    }

    @Override
    public void generatePayslip() {
        System.out.println("----- Payslip (Contract Employee) -----");
        System.out.println("ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Basic Salary: " + basicSalary);
        System.out.println("Contract Duration: " + contractDuration + " months");
        System.out.println("Tax: " + calculateTax());
        System.out.println("Net Salary: " + calculateNetSalary());
        System.out.println("--------------------------------------");
    }
}

public class PayrollManagementSystem {
    static ArrayList<Employee> employees = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\nWelcome to Employee Payroll Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Highest Net Salary");
            System.out.println("5. Average Salary");
            System.out.println("6. Generate Payslip");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewAllEmployees();
                case 3 -> searchEmployee();
                case 4 -> highestNetSalary();
                case 5 -> averageSalary();
                case 6 -> generatePayslip();
                case 7 -> exitProgram();
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 7);
    }

    static void addEmployee() {
        if (employees.size() >= 5) {
            System.out.println("Maximum limit of 5 employees reached!");
            return;
        }

        System.out.print("Enter Employee Type (Permanent/Contract): ");
        String type = sc.nextLine();

        System.out.print("Enter ID: ");
        String id = sc.nextLine();

        for (Employee e : employees) {
            if (e.getEmpId().equalsIgnoreCase(id)) {
                System.out.println("Employee ID already exists!");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Basic Salary: ");
        double salary = sc.nextDouble();

        if (salary <= 0) {
            System.out.println("Salary must be positive!");
            return;
        }

        if (type.equalsIgnoreCase("Permanent")) {
            System.out.print("Enter Bonus: ");
            double bonus = sc.nextDouble();
            if (bonus < 0) {
                System.out.println("Bonus must be positive!");
                return;
            }
            employees.add(new PermanentEmployee(id, name, salary, bonus));
            System.out.println("Employee added successfully!");

        } else if (type.equalsIgnoreCase("Contract")) {
            System.out.print("Enter Contract Duration (in months): ");
            int duration = sc.nextInt();
            if (duration <= 0) {
                System.out.println("Duration must be positive!");
                return;
            }
            employees.add(new ContractEmployee(id, name, salary, duration));
            System.out.println("Employee added successfully!");

        } else {
            System.out.println("Invalid Employee Type!");
        }
    }

    static void viewAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }

        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                "ID", "Name", "Type", "Basic Salary", "Bonus/Duration", "Tax", "Net Salary");
        for (Employee e : employees) {
            String type = e instanceof PermanentEmployee ? "Permanent" : "Contract";
            double tax = e.calculateTax();
            double net = ((Payable) e).calculateNetSalary();
            String extra = (e instanceof PermanentEmployee)
                    ? String.valueOf(((PermanentEmployee) e).getBonus())
                    : ((ContractEmployee) e).getContractDuration() + " months";

            System.out.printf("%-10s %-15s %-15s %-15.2f %-15s %-15.2f %-15.2f\n",
                    e.getEmpId(), e.getName(), type, e.getBasicSalary(), extra, tax, net);
        }
    }

    static void searchEmployee() {
        System.out.print("Enter Employee ID to search: ");
        String id = sc.nextLine();
        for (Employee e : employees) {
            if (e.getEmpId().equalsIgnoreCase(id)) {
                ((Payable) e).generatePayslip();
                return;
            }
        }
        System.out.println("Employee not found!");
    }

    static void highestNetSalary() {
        if (employees.isEmpty()) {
            System.out.println("No employees to evaluate!");
            return;
        }

        Employee highest = employees.get(0);
        double maxSalary = ((Payable) highest).calculateNetSalary();

        for (Employee e : employees) {
            double net = ((Payable) e).calculateNetSalary();
            if (net > maxSalary) {
                maxSalary = net;
                highest = e;
            }
        }

        System.out.println("Employee with Highest Net Salary:");
        ((Payable) highest).generatePayslip();
    }

    static void averageSalary() {
        if (employees.isEmpty()) {
            System.out.println("No employees to calculate average!");
            return;
        }

        double total = 0;
        for (Employee e : employees) {
            total += ((Payable) e).calculateNetSalary();
        }
        double avg = total / employees.size();
        System.out.println("Average Net Salary: " + avg);
    }

    static void generatePayslip() {
        System.out.print("Enter Employee ID for Payslip: ");
        String id = sc.nextLine();
        for (Employee e : employees) {
            if (e.getEmpId().equalsIgnoreCase(id)) {
                ((Payable) e).generatePayslip();
                return;
            }
        }
        System.out.println("Employee not found!");
    }

    static void exitProgram() {
        System.out.println("Exiting program...");
        System.out.println("Total Employees: " + employees.size());
        if (!employees.isEmpty()) averageSalary();
        System.out.println("Goodbye!");
    }
}

import java.util.*;

class Student {
    String name;
    String email;
    String pass;
    double cgpa;
    List<String> skills;

    public Student(String name, String email, String pass, double cgpa, List<String> skills) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.cgpa = cgpa;
        this.skills = skills;
    }
}

class Company {
    String name;
    String email;
    String pass;
    List<Internship> internships = new ArrayList<>();
    List<Placement> placements = new ArrayList<>();

    public Company(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }
}

class Internship {
    String name;
    int slots;
    double cgpaRequired;
    List<String> skills;
    String companyName;

    public Internship(String name, int slots, double cgpaRequired, List<String> skills, String companyName) {
        this.name = name;
        this.slots = slots;
        this.cgpaRequired = cgpaRequired;
        this.skills = skills;
        this.companyName = companyName;
    }
}

class Placement {
    String name;
    int slots;
    double cgpaRequired;
    List<String> skills;
    String companyName;

    public Placement(String name, int slots, double cgpaRequired, List<String> skills, String companyName) {
        this.name = name;
        this.slots = slots;
        this.cgpaRequired = cgpaRequired;
        this.skills = skills;
        this.companyName = companyName;
    }
}

public class Storage {
    public static List<Student> students = new ArrayList<>();
    public static List<Company> companies = new ArrayList<>();
    public static List<Internship> internships = new ArrayList<>();
    public static List<Placement> placements = new ArrayList<>();

    public static void addStudent(Student s) {
        students.add(s);
    }

    public static void addCompany(Company c) {
        companies.add(c);
    }

    public static void addInternship(Internship i) {
        internships.add(i);
        for (Company c : companies) {
            if (c.name.equals(i.companyName)) {
                c.internships.add(i);
                break;
            }
        }
    }

    public static void addPlacement(Placement p) {
        placements.add(p);
        for (Company c : companies) {
            if (c.name.equals(p.companyName)) {
                c.placements.add(p);
                break;
            }
        }
    }

    public static Student getStudentByEmail(String email) {
        for (Student s : students) {
            if (s.email.equals(email)) return s;
        }
        return null;
    }

    public static Company getCompanyByEmail(String email) {
        for (Company c : companies) {
            if (c.email.equals(email)) return c;
        }
        return null;
    }

    public static Student loginStudent(String email, String pass) {
        for (Student s : students) {
            if (s.email.equals(email) && s.pass.equals(pass)) return s;
        }
        return null;
    }

    public static Company loginCompany(String email, String pass) {
        for (Company c : companies) {
            if (c.email.equals(email) && c.pass.equals(pass)) return c;
        }
        return null;
    }

    public static void clearAll() {
        students.clear();
        companies.clear();
        internships.clear();
        placements.clear();
    }

    public static void printAllData() {
        System.out.println("Students:");
        for (Student s : students) {
            System.out.println(s.name + " | " + s.email + " | CGPA: " + s.cgpa + " | Skills: " + s.skills);
        }

        System.out.println("\nCompanies:");
        for (Company c : companies) {
            System.out.println(c.name + " | " + c.email);
            System.out.println(" Internships:");
            for (Internship i : c.internships) {
                System.out.println("  " + i.name + " | Slots: " + i.slots + " | CGPA Req: " + i.cgpaRequired + " | Skills: " + i.skills);
            }
            System.out.println(" Placements:");
            for (Placement p : c.placements) {
                System.out.println("  " + p.name + " | Slots: " + p.slots + " | CGPA Req: " + p.cgpaRequired + " | Skills: " + p.skills);
            }
        }
    }
}
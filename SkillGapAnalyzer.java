import java.util.*;

class Student {
    String name;
    List<String> skills;

    public Student(String name, List<String> skills) {
        this.name = name;
        this.skills = skills;
    }
}

class Company {
    String name;
    List<String> requiredSkills;

    public Company(String name, List<String> requiredSkills) {
        this.name = name;
        this.requiredSkills = requiredSkills;
    }
}

public class SkillGapAnalyzer {

    List<Student> students = new ArrayList<>();
    List<Company> companies = new ArrayList<>();

    Map<String, List<String>> analyzeSkillGaps(Student student) {
        Map<String, List<String>> gaps = new HashMap<>();
        for (Company c : companies) {
            List<String> missing = new ArrayList<>();
            for (String reqSkill : c.requiredSkills) {
                if (!student.skills.contains(reqSkill)) {
                    missing.add(reqSkill);
                }
            }
            gaps.put(c.name, missing);
        }
        return gaps;
    }

    void printSkillGaps() {
        for (Student s : students) {
            System.out.println("Skill Gap Report for: " + s.name);
            Map<String, List<String>> gaps = analyzeSkillGaps(s);
            for (String company : gaps.keySet()) {
                List<String> missing = gaps.get(company);
                if (missing.size() == 0) {
                    System.out.println("  " + company + ": No gap! Student is fully eligible.");
                } else {
                    System.out.println("  " + company + ": Missing skills -> " + missing);
                }
            }
            System.out.println("----------------------------");
        }
    }

    public static void main(String[] args) {
        SkillGapAnalyzer analyzer = new SkillGapAnalyzer();

        analyzer.students.add(new Student("Rahul", Arrays.asList("Java", "C++")));
        analyzer.students.add(new Student("Aman", Arrays.asList("Python")));
        analyzer.students.add(new Student("Neha", Arrays.asList("Java", "HTML", "CSS")));
        analyzer.students.add(new Student("Sita", Arrays.asList("HTML", "CSS", "Python")));

        analyzer.companies.add(new Company("XYZ", Arrays.asList("Java", "C++", "Python")));
        analyzer.companies.add(new Company("ABC", Arrays.asList("Python", "HTML")));
        analyzer.companies.add(new Company("PQR", Arrays.asList("Java", "CSS")));

        analyzer.printSkillGaps();
    }
}
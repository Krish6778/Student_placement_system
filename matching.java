import java.util.*;

class Student {
    String name;
    double cgpa;
    List<String> skills;
    List<Integer> preferences;
    String placedCompany = null;

    public Student(String name, double cgpa, List<String> skills, List<Integer> preferences) {
        this.name = name;
        this.cgpa = cgpa;
        this.skills = skills;
        this.preferences = preferences;
    }
}

class Company {
    String name;
    List<String> requiredSkills;
    double cgpaRequirement;
    int seats;

    public Company(String name, List<String> requiredSkills, double cgpaRequirement, int seats) {
        this.name = name;
        this.requiredSkills = requiredSkills;
        this.cgpaRequirement = cgpaRequirement;
        this.seats = seats;
    }
}

public class matching {

    List<Student> students = new ArrayList<Student>();
    List<Company> companies = new ArrayList<Company>();

    int skillMatch(Student s, Company c) {
        int count = 0;
        for (String sk : c.requiredSkills) {
            for (String ssk : s.skills) {
                if (sk.equals(ssk)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    void placeStudents() {
        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).cgpa < students.get(j).cgpa) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
            }
        }

        for (int ci = 0; ci < companies.size(); ci++) {
            Company c = companies.get(ci);
            List<Student> eligible = new ArrayList<Student>();
            for (Student s : students) {
                if (s.placedCompany == null && s.cgpa >= c.cgpaRequirement) {
                    eligible.add(s);
                }
            }

            int n = eligible.size();
            int capacity = c.seats;
            if (n == 0 || capacity == 0)
                continue;

            int[][] dp = new int[n + 1][capacity + 1];
            for (int i = 1; i <= n; i++) {
                int score = skillMatch(eligible.get(i - 1), c);
                for (int w = 1; w <= capacity; w++) {
                    if (w >= 1)
                        dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - 1] + score);
                    else
                        dp[i][w] = dp[i - 1][w];
                }
            }

            int w = capacity;
            for (int i = n; i > 0 && w > 0; i--) {
                if (dp[i][w] != dp[i - 1][w]) {
                    Student s = eligible.get(i - 1);
                    s.placedCompany = c.name;
                    c.seats--;
                    w--;
                    if (c.seats == 0)
                        break;
                }
            }
        }

        for (Student s : students) {
            if (s.placedCompany != null)
                continue;
            int bestScore = -1;
            int bestCompanyIndex = -1;

            for (int pi = 0; pi < s.preferences.size(); pi++) {
                int ci = s.preferences.get(pi);
                if (ci >= companies.size())
                    continue;
                Company c = companies.get(ci);
                if (s.cgpa < c.cgpaRequirement || c.seats <= 0)
                    continue;

                int score = skillMatch(s, c);
                if (score > bestScore) {
                    bestScore = score;
                    bestCompanyIndex = ci;
                }
            }

            if (bestCompanyIndex != -1) {
                Company c = companies.get(bestCompanyIndex);
                s.placedCompany = c.name;
                c.seats--;
            }
        }
    }

    void printResults() {
        System.out.println("===== Final Placement =====");
        for (Student s : students) {
            if (s.placedCompany != null)
                System.out.println(s.name + " -> " + s.placedCompany);
            else
                System.out.println(s.name + " -> Not Placed");
        }
        System.out.println("===========================");
    }

    public static void main(String[] args) {
        matching cp = new matching();

        cp.students.add(new Student("Rahul", 8.5, Arrays.asList("Java", "C++"), Arrays.asList(0, 1)));
        cp.students.add(new Student("Aman", 7.0, Arrays.asList("Python"), Arrays.asList(1, 0)));
        cp.students.add(new Student("Neha", 9.0, Arrays.asList("Java", "Python", "HTML"), Arrays.asList(0, 1)));
        cp.students.add(new Student("Sita", 6.5, Arrays.asList("HTML", "CSS"), Arrays.asList(1)));

        cp.companies.add(new Company("XYZ", Arrays.asList("Java"), 7.0, 1));
        cp.companies.add(new Company("ABC", Arrays.asList("Python"), 6.0, 2));

        cp.placeStudents();
        cp.printResults();
    }
}
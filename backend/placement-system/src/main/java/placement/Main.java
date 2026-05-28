package placement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    static Gson gson = new Gson();

    public static void main(String[] args) {

        port(8080);
        options("/*", (request, response) -> {

    String headers =
            request.headers(
                    "Access-Control-Request-Headers"
            );

    if(headers != null){

        response.header(
                "Access-Control-Allow-Headers",
                headers
        );

    }

    String method =
            request.headers(
                    "Access-Control-Request-Method"
            );

    if(method != null){

        response.header(
                "Access-Control-Allow-Methods",
                method
        );

    }

    return "OK";

});

        enableCORS();

        /* ========================= */
        /* HOME */
        /* ========================= */

        get("/", (req,res)->{

            return "Placement Management Backend Running";

        });

        /* ========================= */
        /* STUDENT REGISTER */
        /* ========================= */

        post("/student/register",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String name =
                    data.get("name").toString();

            String email =
                    data.get("email").toString();

            String password =
                    data.get("password").toString();

            if(
                    Storage.emailExists(email)
            ){

                return gson.toJson(
                        Map.of(
                                "message",
                                "Email already exists"
                        )
                );

            }

            if(
                    password.length() < 6
            ){

                return gson.toJson(
                        Map.of(
                                "message",
                                "Password too short"
                        )
                );

            }

            Student student =
                    new Student(

                            UUID.randomUUID().toString(),

                            name,

                            email,

                            password

                    );
                    student.cgpa =
        Double.parseDouble(
                data.get("cgpa").toString()
        );

            Storage.addStudent(student);

            return gson.toJson(
                    Map.of(
                            "message",
                            "Student registered successfully"
                    )
            );

        });

        /* ========================= */
        /* STUDENT LOGIN */
        /* ========================= */

        post("/student/login",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String email =
                    data.get("email").toString();

            String password =
                    data.get("password").toString();

            Student student =
                    Storage.loginStudent(
                            email,
                            password
                    );

            if(student == null){

                return gson.toJson(
                        Map.of(
                                "success",
                                false,
                                "message",
                                "Invalid credentials"
                        )
                );

            }

            return gson.toJson(
                    Map.of(
                            "success",
                            true,
                            "student",
                            student
                    )
            );

        });

        /* ========================= */
        /* COMPANY REGISTER */
        /* ========================= */

        post("/company/register",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String name =
                    data.get("name").toString();

            String email =
                    data.get("email").toString();

            String password =
                    data.get("password").toString();

            if(
                    Storage.emailExists(email)
            ){

                return gson.toJson(
                        Map.of(
                                "message",
                                "Email already exists"
                        )
                );

            }

            Company company =
                    new Company(

                            UUID.randomUUID().toString(),

                            name,

                            email,

                            password

                    );

            Storage.addCompany(company);

            return gson.toJson(
                    Map.of(
                            "message",
                            "Company registered successfully"
                    )
            );

        });

        /* ========================= */
        /* COMPANY LOGIN */
        /* ========================= */

        post("/company/login",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String email =
                    data.get("email").toString();

            String password =
                    data.get("password").toString();

            Company company =
                    Storage.loginCompany(
                            email,
                            password
                    );

            if(company == null){

                return gson.toJson(
                        Map.of(
                                "success",
                                false,
                                "message",
                                "Invalid credentials"
                        )
                );

            }

            return gson.toJson(
                    Map.of(
                            "success",
                            true,
                            "company",
                            company
                    )
            );

        });

        /* ========================= */
        /* ADD JOB */
        /* ========================= */

        post("/jobs/add",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String companyEmail =
                    data.get("companyEmail")
                            .toString();

            Company company =
                    Storage.getCompanyByEmail(
                            companyEmail
                    );

            if(company == null){

                return gson.toJson(
                        Map.of(
                                "message",
                                "Company not found"
                        )
                );

            }

            String title =
                    data.get("title").toString();

            String type =
                    data.get("type").toString();

            double cgpa =
                    Double.parseDouble(
                            data.get("cgpa").toString()
                    );

            int slots =
                    Integer.parseInt(
                            data.get("slots").toString()
                    );

            String description =
                    data.get("description")
                            .toString();

            List<String> skills =
                    (List<String>)
                            data.get("skills");

            Job job =
                    new Job(

                            UUID.randomUUID().toString(),

                            title,

                            type,

                            company.name,

                            company.email,

                            cgpa,

                            slots,

                            skills,

                            description

                    );

            Storage.addJob(job);

            return gson.toJson(
                    Map.of(
                            "message",
                            "Job added successfully"
                    )
            );

        });

        /* ========================= */
        /* GET JOBS */
        /* ========================= */

        get("/jobs",(req,res)->{

            return gson.toJson(
                    Storage.jobs
            );

        });

        /* ========================= */
        /* APPLY JOB */
        /* ========================= */

        post("/apply",(req,res)->{

            Map<String,Object> data =
                    gson.fromJson(
                            req.body(),
                            Map.class
                    );

            String studentEmail =
                    data.get("studentEmail")
                            .toString();

            String jobId =
                    data.get("jobId")
                            .toString();

            Student student =
                    Storage.getStudentByEmail(
                            studentEmail
                    );

            Job job =
                    Storage.getJobById(
                            jobId
                    );

            if(student == null || job == null){

                return gson.toJson(
                        Map.of(
                                "message",
                                "Invalid data"
                        )
                );

            }

            int match =
                    Algorithms.calculateSkillMatch(
                            student,
                            job
                    );

            Application app =
                    new Application(

                            student.email,

                            job.company,

                            job.title,

                            "Applied",

                            match,

                            new Date().toString()

                    );

            Storage.addApplication(app);

            return gson.toJson(
                    Map.of(
                            "message",
                            "Applied successfully"
                    )
            );

        });

        /* ========================= */
        /* RECOMMENDATIONS */
        /* ========================= */

        get("/recommendations/:email",(req,res)->{

            String email =
                    req.params(":email");

            Student student =
                    Storage.getStudentByEmail(
                            email
                    );

            if(student == null){

                return gson.toJson(
                        new ArrayList<>()
                );

            }

            return gson.toJson(

                    Algorithms
                            .generateRecommendations(
                                    student
                            )

            );

        });

        /* ========================= */
        /* APPLICATIONS */
        /* ========================= */

        get("/applications/:email",(req,res)->{

            String email =
                    req.params(":email");

            return gson.toJson(

                    Storage
                            .getApplicationsByStudent(
                                    email
                            )

            );

        });

        /* ========================= */
        /* SKILL GAP */
        /* ========================= */

        get("/skillgap/:email",(req,res)->{

            String email =
                    req.params(":email");

            Student student =
                    Storage.getStudentByEmail(
                            email
                    );

            if(student == null){

                return gson.toJson(
                        new ArrayList<>()
                );

            }

            return gson.toJson(

                    Algorithms
                            .analyzeSkillGap(
                                    student
                            )

            );

        });

        /* ========================= */
        /* COMPANY JOBS */
        /* ========================= */

        get("/company/jobs/:email",(req,res)->{

            String email =
                    req.params(":email");

            return gson.toJson(

                    Storage
                            .getJobsByCompany(
                                    email
                            )

            );

        });

        /* ========================= */
        /* DELETE JOB */
        /* ========================= */

        delete("/jobs/delete/:id",(req,res)->{

            String id =
                    req.params(":id");

            boolean deleted =
                    Storage.deleteJob(id);

            return gson.toJson(
                    Map.of(
                            "message",
                            deleted
                                    ?
                                    "Deleted successfully"
                                    :
                                    "Job not found"
                    )
            );

        });

        /* ========================= */
        /* DEMO DATA */
        /* ========================= */

        loadDemoData();

    }

    /* ========================= */
    /* DEMO DATA */
    /* ========================= */

    static void loadDemoData(){

        Company c1 =
                new Company(
                        UUID.randomUUID().toString(),
                        "Google",
                        "google@gmail.com",
                        "123456"
                );

        Storage.addCompany(c1);

        Job j1 =
                new Job(

                        UUID.randomUUID().toString(),

                        "Frontend Developer",

                        "Placement",

                        "Google",

                        c1.email,

                        7.5,

                        5,

                        Arrays.asList(
                                "HTML",
                                "CSS",
                                "JavaScript",
                                "React"
                        ),

                        "Frontend role with React"

                );

        Storage.addJob(j1);

    }

    /* ========================= */
    /* ENABLE CORS */
    /* ========================= */

    private static void enableCORS(){

        before((request,response)->{

            response.header(
                    "Access-Control-Allow-Origin",
                    "*"
            );

            response.header(
                    "Access-Control-Allow-Methods",
                    "*"
            );

            response.header(
                    "Access-Control-Allow-Headers",
                    "*"
            );

        });

    }

}
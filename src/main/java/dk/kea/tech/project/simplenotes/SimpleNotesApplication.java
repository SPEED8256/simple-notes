package dk.kea.tech.project.simplenotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleNotesApplication {

    public static void main(String[] args) {
        System.out.println("start22222");
        System.out.println("${rds.password}");
        System.out.println("${rds.username}");
        System.out.println("jdbc:mysql://${RDS.HOSTNAME}:${RDS.PORT}/${rds.db.name}");
        SpringApplication.run(SimpleNotesApplication.class, args);
    }

}

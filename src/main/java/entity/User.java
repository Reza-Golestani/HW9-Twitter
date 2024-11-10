package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private long id;
    private String username;
    private String password;
    private String displayName;
    private String email;
    private String bio;
    private LocalDate created;
}

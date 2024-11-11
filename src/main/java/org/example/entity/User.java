package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private long id;
    private String email;
    private String username;
    private String password;
    private String displayName;
    private String bio;
    private LocalDate created;
}

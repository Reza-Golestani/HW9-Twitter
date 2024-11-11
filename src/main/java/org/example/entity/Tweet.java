package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Tweet {
    private long id;
    private String text;
    private Tweet retweetTo;
    private User writer;
    private List<User> likedBy;
    private List<User> dislikedBy;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private List<Tag> tags;
}

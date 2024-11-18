package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Tweet {
    private long id;
    private String text;
    private Tweet retweetTo;
    private User writer;
    private Set<String> tags = new HashSet<>();
    private List<Reaction> reactions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    @Override
    // todo: reactions + retweeted to
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String stringForm =
          writer.getDisplayedName() + ": " + text + "\n";
        if (!tags.isEmpty()) {
            stringForm += "   " + tags + ",";
        }
        stringForm +="   posted at " + createdAt.format(formatter);

        return stringForm;
    }
}





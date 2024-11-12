package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ReactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reaction {
    private long id;
    private Tweet tweet;
    private User user;
    private ReactionType reactionType = ReactionType.NONE;
}

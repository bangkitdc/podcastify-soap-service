package com.podcastify.model;

import java.sql.Timestamp;
import lombok.*;

@Data
@Setter
@NoArgsConstructor
public class LogModel {
    private int id;
    @NonNull private String description;
    @NonNull private String IP;
    @NonNull private String endpoint;
    @NonNull private Timestamp timestamp;
    @NonNull private String fromService;
}

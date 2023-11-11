package com.podcastify.model;

import java.util.Date;

import lombok.*;

@Data
@Setter
@NoArgsConstructor
public class LogModel {
    private int id;
    @NonNull private String description;
    @NonNull private String IP;
    @NonNull private String endpoint;
    @NonNull private Date timestamp;
    @NonNull private String fromService;
}

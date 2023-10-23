package com.podcastify.model;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogModel {
    private int id;
    private String description;
    private String IP;
    private String endpoint;
    private Date timestamp;
}

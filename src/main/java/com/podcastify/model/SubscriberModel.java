package com.podcastify.model;

import lombok.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class SubscriberModel {
    private int subscriberID;
    @NonNull private String subscriberName;
    private int creatorID;
    @NonNull private String creatorName;
    @NonNull private String status;
    @NonNull private Timestamp createdAt;
    @NonNull private Timestamp updatedAt;
}

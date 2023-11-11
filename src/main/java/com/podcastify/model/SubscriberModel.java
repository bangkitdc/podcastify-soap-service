package com.podcastify.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class SubscriberModel {
    private int subscriberID;
    @NonNull private String subscriberName;
    private int creatorID;
    @NonNull private String creatorName;
    @NonNull private String status;
    @NonNull private Date createdAt;
    @NonNull private Date updatedAt;
}

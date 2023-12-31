package com.podcastify.model;

import javax.xml.bind.annotation.*;

import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "statusCode",
        "message",
})

@NoArgsConstructor
@AllArgsConstructor
@Setter
@XmlRootElement(name = "ResponseModel")
public class ResponseModel {
    @XmlElement(required = true)
    private int statusCode;
    @XmlElement(required = true)
    private String message;
}

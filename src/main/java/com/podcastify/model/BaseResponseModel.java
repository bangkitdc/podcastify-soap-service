package com.podcastify.model;

import javax.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Base Response Model
 * Use this if the data is of primitive types.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "statusCode",
        "message",
        "data"
})

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "BaseResponseModel")
public class BaseResponseModel {
    @XmlElement(required = true)
    private int statusCode;
    @XmlElement(required = true)
    private String message;
    private Object data;
}

package org.example.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address implements Serializable {

    @BsonProperty
    private String city;
    @BsonProperty
    private String street;
    @BsonProperty
    private String number;

    @BsonCreator
    public Address(@BsonProperty("city") String city,
                   @BsonProperty("street") String street,
                   @BsonProperty("number") String number){
        this.city = city;
        this.street = street;
        this.number = number;
    }
}


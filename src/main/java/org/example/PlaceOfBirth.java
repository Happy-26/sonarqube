package org.example;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: HAPPY
 * @Project_name: SpEL
 * @Date: 2023/12/22 10:08
 * @Description:
 */
@Data
@Accessors(chain = true)
public class PlaceOfBirth {
    private String city;
    private String country;

    public PlaceOfBirth(String city) {
        this.city = city;
    }

    public PlaceOfBirth(String city, String country) {
        this(city);
        this.country = country;
    }
}

package muraca.country;

import lombok.Data;

import java.io.Serializable;

@Data
public class Country implements Serializable {
    private String name;
    private String flagUrl;
    private String lat;
    private String lon;
}

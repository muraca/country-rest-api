package muraca.country;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CountryRestController {

    private RestCountriesRestService restCountriesRestService;

    @Autowired
    public void setRestCountriesRestService(RestCountriesRestService r) {
        restCountriesRestService = r;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<Country>> getAllCountries() {
        try {
            Set<Country> countries = restCountriesRestService.getAllCountries();
            if (countries != null && !countries.isEmpty()) {
                return ResponseEntity.ok(countries);
            }
        }
        catch (Exception e) {}

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping(value="/country/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable("name") String name) {
        try {
            Country country = restCountriesRestService.getCountryByName(name);
            if (country != null) {
                return ResponseEntity.ok(country);
            }
        }
        catch (Exception e) {}

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value="/border")
    public ResponseEntity<Object> getDoCountriesBorder(@RequestBody Map<String, String> requestBody) {
        String firstCountry = requestBody.get("firstCountry");
        String secondCountry = requestBody.get("secondCountry");

        try {
            List<String> firstCountryNeighbours = restCountriesRestService.getNeighbours(firstCountry);
            String secondCountryId = restCountriesRestService.getCca3Id(secondCountry);

            if (firstCountryNeighbours != null && !firstCountryNeighbours.isEmpty() &&
                    secondCountryId != null && !secondCountryId.isBlank()) {
                Map<String,String> responseBody = new HashMap<>();
                responseBody.put("firstCountry", firstCountry);
                responseBody.put("secondCountry", secondCountry);
                responseBody.put("border", String.valueOf(firstCountryNeighbours.contains(secondCountryId)));
                return ResponseEntity.ok(responseBody);
            }
        }
        catch (Exception e) {}

        return ResponseEntity.badRequest().build();
    }

}

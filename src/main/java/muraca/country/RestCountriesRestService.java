package muraca.country;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RestCountriesRestService {

    private final RestTemplate restTemplate;
    private final GsonJsonParser jsonParser;

    private final String BASE_URL = "https://restcountries.com/v3.1";
    private final String COUNTRY_FIELDS = "?fields=name,latlng,flags";

    public RestCountriesRestService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
        jsonParser = new GsonJsonParser();
    }

    private Country mapToCountry(Map<String,Object> map) {
        if (!map.isEmpty()) try {
            Country country = new Country();

            Object name = map.get("name");
            if (name instanceof String nameStr) {
                country.setName(nameStr);
            }
            else if (name instanceof Map nameMap) {
                String commonName = nameMap.get("common").toString();
                country.setName(commonName);
            }
            else return null;

            Object flags = map.get("flags");
            if (flags instanceof List<?> flagsList) {
                country.setFlagUrl(flagsList.get(0).toString());
            }
            else if (flags instanceof Map flagsMap) {
                String png = flagsMap.get("png").toString();
                country.setFlagUrl(png);
            }

            Object latlng = map.get("latlng");
            if (latlng instanceof List<?> latlngList) {
                country.setLat(latlngList.get(0).toString());
                country.setLon(latlngList.get(1).toString());
            }

            return country;
        } catch (Exception e) {}
        return null;
    }

    public Set<Country> getAllCountries() throws RestClientException {
        String url = BASE_URL + "/all" + COUNTRY_FIELDS;
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Object> responseBody = jsonParser.parseList(response.getBody());
            if (responseBody.isEmpty()) {
                return null;
            }
            Set<Country> countries = new HashSet<>();
            for (Object obj : responseBody) {
                if (obj instanceof Map countryMap) {
                    countries.add(mapToCountry(countryMap));
                }
            }
            return countries;
        }
        return null;
    }

    public Country getCountryByName(String name) throws RestClientException {
        String url = BASE_URL + "/name/" + name + COUNTRY_FIELDS;
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Object> responseBody = jsonParser.parseList(response.getBody());
            if (!responseBody.isEmpty() && responseBody.get(0) instanceof Map countryMap) {
                return mapToCountry(countryMap);
            }
        }
        return null;
    }

    public List<String> getNeighbours(String name) throws RestClientException {
        String url = BASE_URL + "/name/" + name + "?fields=borders";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Object> responseBody = jsonParser.parseList(response.getBody());
            if (!responseBody.isEmpty() && responseBody.get(0) instanceof Map map) {
                Object borders = map.get("borders");
                if (borders instanceof List bordersList) {
                    return bordersList;
                }
            }
        }
        return null;
    }

    public String getCca3Id(String name) throws RestClientException {
        String url = BASE_URL + "/name/" + name + "?fields=cca3";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Object> responseBody = jsonParser.parseList(response.getBody());
            if (!responseBody.isEmpty() && responseBody.get(0) instanceof Map map) {
                Object cca3 = map.get("cca3");
                if (cca3 instanceof String cca3Id) {
                    return cca3Id;
                }
            }
        }
        return null;
    }

}

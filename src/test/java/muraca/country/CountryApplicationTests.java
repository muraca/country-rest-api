package muraca.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class CountryApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void countryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/country/italy"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()")
						.value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("name")
						.value("Italy"))
				.andExpect(MockMvcResultMatchers.jsonPath("flagUrl")
						.value("https://flagcdn.com/w320/it.png"));


		mockMvc.perform(MockMvcRequestBuilders.get("/country/south"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()")
						.value(4))
				.andExpect(MockMvcResultMatchers.jsonPath("name")
						.value("South Africa"))
				.andExpect(MockMvcResultMatchers.jsonPath("flagUrl")
						.value("https://flagcdn.com/w320/za.png"));

		mockMvc.perform(MockMvcRequestBuilders.get("/country/ciao"))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	void borderTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/border")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstCountry\":\"Italy\",\"secondCountry\":\"France\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()")
						.value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("border")
						.value("true"));

		mockMvc.perform(MockMvcRequestBuilders.post("/border")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstCountry\":\"Italy\",\"secondCountry\":\"Germany\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()")
						.value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("border")
						.value("false"));

		mockMvc.perform(MockMvcRequestBuilders.post("/border")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstCountry\":\"Italy\",\"secondCountry\":\"Ciao\"}"))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
}

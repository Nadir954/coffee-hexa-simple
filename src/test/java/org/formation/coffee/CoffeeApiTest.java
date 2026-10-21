package org.formation.coffee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:coffee-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.properties.hibernate.generate_statistics=true"})
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class CoffeeApiTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired EntityManagerFactory emf;

    @Test void listsSeedDataAndReviewsInASingleQuery() throws Exception {
        var statistics = emf.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
        mvc.perform(get("/api/coffees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Granador"))
                .andExpect(jsonPath("$[0].reviews[0].author").value("Ana"))
                .andExpect(jsonPath("$[0].reviews[1].rating").value(4))
                .andExpect(jsonPath("$[1].reviews[0].author").value("Cleo"))
                .andExpect(jsonPath("$[2].reviews").isEmpty());
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }
    @Test void createsThenPatchesACoffee() throws Exception {
        var result = mvc.perform(post("/api/coffees").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Yirgacheffe\",\"price\":25}"))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.reviews").isEmpty()).andReturn();
        String location = result.getResponse().getHeader("Location");
        mvc.perform(patch(location).contentType(MediaType.APPLICATION_JSON).content("{\"price\":12}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Yirgacheffe"))
                .andExpect(jsonPath("$.price").value(12));
        mvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.price").value(12));
    }
    @Test void keepsReviewsWhenUpdatingSeededCoffee() throws Exception {
        mvc.perform(patch("/api/coffees/1").contentType(MediaType.APPLICATION_JSON).content("{\"price\":12}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Granador"))
                .andExpect(jsonPath("$.reviews.length()").value(2));
        assertThat(jdbc.queryForObject("select price from coffee where id = 1", Integer.class)).isEqualTo(12);
        // Ne pas laisser les autres tests dépendre de cette modification.
        jdbc.update("update coffee set price = 10 where id = 1");
    }
    @Test void returnsProblemDetailsForMissingCoffee() throws Exception {
        mvc.perform(get("/api/coffees/999"))
                .andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.coffeeId").value(999))
                .andExpect(jsonPath("$.instance").value("/api/coffees/999"));
    }
    @Test void rejectsBusinessRuleViolations() throws Exception {
        mvc.perform(post("/api/coffees").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Gold\",\"price\":500}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(422));
    }
    @ParameterizedTest @ValueSource(strings = {"{\"name\":\" \",\"price\":10}", "{\"name\":\"A\"}", "{", "{\"name\":\"A\",\"price\":\"abc\"}"})
    void rejectsMalformedRequests(String body) throws Exception {
        mvc.perform(post("/api/coffees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }
    @Test void validatesApiVersions() throws Exception {
        mvc.perform(get("/api/coffees").header("API-Version", "1")).andExpect(status().isOk());
        mvc.perform(get("/api/coffees").header("API-Version", "2")).andExpect(status().isBadRequest());
    }
    @Test void rollsBackAnInvalidUpdate() throws Exception {
        mvc.perform(patch("/api/coffees/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Changed\",\"price\":500}"))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(get("/api/coffees/1")).andExpect(jsonPath("$.name").value("Granador"))
                .andExpect(jsonPath("$.price").value(10));
    }
    @Test void rejectsBlankPatchNamesInTheDomain() throws Exception {
        mvc.perform(patch("/api/coffees/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" \"}"))
                .andExpect(status().isUnprocessableEntity());
    }
}

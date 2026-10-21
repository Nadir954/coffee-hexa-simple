package org.formation.coffee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.env.Environment;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:coffee-without-seed")
@ActiveProfiles("test")
class NonDevProfileTest {
    @Autowired JdbcTemplate jdbc;
    @Autowired Environment environment;
    @Test void createsSchemaWithoutDemoDataOrDevTools() {
        assertThat(jdbc.queryForObject("select count(*) from coffee", Integer.class)).isZero();
        assertThat(jdbc.queryForObject("select count(*) from review", Integer.class)).isZero();
        assertThat(environment.getProperty("springdoc.api-docs.enabled", Boolean.class)).isFalse();
        assertThat(environment.getProperty("springdoc.swagger-ui.enabled", Boolean.class)).isFalse();
        assertThat(environment.getProperty("spring.h2.console.enabled", Boolean.class, false)).isFalse();
    }
}

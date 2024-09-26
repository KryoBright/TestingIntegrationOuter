package com.kafkabroker.kafkabroker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkabroker.kafkabroker.feignclients.DistantWorkTime;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class KafkabrokerApplicationTests {

	@Autowired
	DistantWorkTime distantWorkTime;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@AfterEach
	void teardown() {
		var entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery(
						"DELETE FROM employee WHERE id=?1;"
				)
				.setParameter(1,"id")
				.executeUpdate();
		entityManager.getTransaction().commit();
	}

	@Test
	void test() throws Exception {
		var entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery(
				"INSERT INTO employee(id, employee_name, status,position) VALUES  (?1,?2,?3,?4);"
		)
				.setParameter(1,"id")
				.setParameter(2, "name")
				.setParameter(3, "WORKING")
				.setParameter(4,"TECH")
				.executeUpdate();
		entityManager.getTransaction().commit();
		//Когда
		var actualResponse = distantWorkTime.readEmployeesMap();
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).usingRecursiveComparison()
				.isEqualTo(List.of());
	}

}

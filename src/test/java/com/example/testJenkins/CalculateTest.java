/**
 * 
 */
package com.example.testJenkins;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author arickyu
 *
 */
@SpringBootTest
@DisplayName("測試計算機")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CalculateTest {
	@Autowired
	private MockMvc mockMvc;

	@BeforeAll()
	public void BeforeAll() {
		System.out.println("==================測試開始==================");
	}

	@AfterAll()
	public void AfterAll() {
		System.out.println("==================測試結束==================");
	}

	@BeforeEach()
	public void BeforeEach(TestInfo t) {
		System.out.println("========={" + t.getDisplayName() + "}開始=========");
	}

	@AfterEach()
	public void AfterEach() {
		System.out.println("============================");
		System.out.println("");
	}

	/**
	 * Test method for
	 * {@link com.example.testJenkins.Calculate#method1(java.util.Map)}.
	 */
	@Test
	@DisplayName("測試加法")
	final void testMethod1() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("a", "10");
		params.put("b", "5");
		String jsonParams = new ObjectMapper().writeValueAsString(params);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/method1").contentType(MediaType.APPLICATION_JSON).content(jsonParams))
				.andExpect(status().isOk()).andExpect(content().string("15"));
		System.out.println("pass");
	}

	/**
	 * Test method for
	 * {@link com.example.testJenkins.Calculate#method2(java.util.Map)}.
	 */
	@Test
	@DisplayName("測試減法")
	final void testMethod2() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("a", "10");
		params.put("b", "5");
		String jsonParams = new ObjectMapper().writeValueAsString(params);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/method2").contentType(MediaType.APPLICATION_JSON).content(jsonParams))
				.andExpect(status().isOk()).andExpect(content().string("5"));
		System.out.println("pass");
	}

	/**
	 * Test method for
	 * {@link com.example.testJenkins.Calculate#method3(java.util.Map)}.
	 */
	@Test
	@DisplayName("測試乘法")
	final void testMethod3() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("a", "10");
		params.put("b", "5");
		String jsonParams = new ObjectMapper().writeValueAsString(params);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/method3").contentType(MediaType.APPLICATION_JSON).content(jsonParams))
				.andExpect(status().isOk()).andExpect(content().string("50"));
		System.out.println("pass");
	}

	/**
	 * Test method for
	 * {@link com.example.testJenkins.Calculate#method4(java.util.Map)}.
	 */
	@Test
	@DisplayName("測試除法")
	final void testMethod4() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("a", "10");
		params.put("b", "5");
		String jsonParams = new ObjectMapper().writeValueAsString(params);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/method4").contentType(MediaType.APPLICATION_JSON).content(jsonParams))
				.andExpect(status().isOk()).andExpect(content().string("2"));
		System.out.println("pass");
	}

}

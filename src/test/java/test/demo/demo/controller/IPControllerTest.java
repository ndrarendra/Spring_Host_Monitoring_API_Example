package test.demo.demo.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import test.demo.demo.repository.ATrailRepository;
import test.demo.demo.repository.IPRepository;

@WebMvcTest(IPController.class)
public class IPControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IPRepository ipRepository;

	@MockBean
	private ATrailRepository aTrailRepository;

	@Test
	public void createNew() throws Exception {
		this.mockMvc.perform(post("/api/v1/ip_list").content("abc").contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("<name>"))
			.andExpect(model().attribute("<name>", "<value>"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$['<key>']").value("<value>"));
	}
}

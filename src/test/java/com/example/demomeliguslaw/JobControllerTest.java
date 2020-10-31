package com.example.demomeliguslaw;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demomeliguslaw.dto.LoginRequestDto;
import com.example.demomeliguslaw.entity.Job;
import com.example.demomeliguslaw.repository.JobRepository;
import com.example.demomeliguslaw.service.LoginService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class JobControllerTest {
	private static final SimpleDateFormat simpleDateFromat = new SimpleDateFormat("yyy-MM-dd");
	private static final ObjectMapper om = new ObjectMapper();
	private static String TOKEN;
	@Autowired
	JobRepository jobRepository;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	LoginService loginService;

	@Before
	public void setup() throws Exception{
		jobRepository.deleteAll();
		om.setDateFormat(simpleDateFromat);
		TOKEN = "Bearer " + loginService.getToken(new LoginRequestDto("user1","user")).getToken();
	}

	//	@Test
	//	void contextLoads() {
	//	}

	private Map<String,Job> getTestData(){
		Map<String,Job> data = new HashMap<>();

		Job softwareDeveloper = new Job("Software Developer", 10340000L);
		data.put("softwareDeveloper", softwareDeveloper);
		Job softwareDeveloperManager = new Job("Software Developer Manager", 20000000L);
		data.put("softwareDeveloperManager", softwareDeveloperManager);
		Job qualityAssurance = new Job("Quality Assurance", 10340000L);
		data.put("qualityAssurance", qualityAssurance);
		Job analyst = new Job("Analyst", 10340000L);
		data.put("analyst", analyst);
		return data;
	}

	@org.junit.Test
	public void testJobEndPointWithPOST() throws Exception {
		Job expectedRecord = getTestData().get("softwareDeveloper");
		Job actualRecord = om.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN)
				.content(om.writeValueAsBytes(getTestData().get("softwareDeveloper"))))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn().getResponse().getContentAsString(), Job.class);
		Assert.assertTrue(new ReflectionEquals(expectedRecord,"id").matches(actualRecord));
		assertEquals(true, jobRepository.findById(actualRecord.getId()).isPresent());
	}

	@org.junit.Test
	public void testJobEndPointWithGetList() throws Exception{
		Map<String, Job> data = getTestData();
		List<Job> expectedRecords = new ArrayList<>();
		for(Map.Entry<String, Job> kv : data.entrySet()) {
			expectedRecords.add(om.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", TOKEN)
					.content(om.writeValueAsBytes(kv.getValue())))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isCreated())
					.andReturn().getResponse().getContentAsString(), Job.class));
		}

		Collections.sort(expectedRecords,Comparator.comparing(Job::getId));

		List<Job> actualRecords = om.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(expectedRecords.size())))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString(), new TypeReference<List<Job>>() {
		});

		for(int i=0; i<expectedRecords.size(); i++) {
			Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
		}
	}

	@org.junit.Test
	public void testJobEndpointWithGetById() throws Exception{
		Job actualRecord = om.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN)
				.content(om.writeValueAsBytes(getTestData().get("softwareDeveloper"))))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThan(0)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn().getResponse().getContentAsString(), Job.class);

		Job expectedRecord = om.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/jobs/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString()
				, Job.class);

		Assert.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

		//		mockMvc.perform(MockMvcRequestBuilders.get("/jobs/0")
		//				.contentType(MediaType.APPLICATION_JSON)
		//				.header("Authorization", TOKEN))
		//		.andDo(MockMvcResultHandlers.print())
		//		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@org.junit.Test
	public void testJobEndpointWithGetRoleAsc() throws Exception{
		Map<String, Job> data = getTestData();
		List<Job> expectedRecords = new ArrayList<>();
		for(Map.Entry<String, Job> kv : data.entrySet()) {
			expectedRecords.add(om.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", TOKEN)
					.content(om.writeValueAsBytes(kv.getValue())))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isCreated())
					.andReturn().getResponse().getContentAsString(), Job.class));
		}

		expectedRecords.stream().sorted(Comparator.comparing(Job::getRole));

		List<Job> actualRecords = om.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/jobs?sort=asc&&orderby=role")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(expectedRecords.size())))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString(), new TypeReference<List<Job>>() {
		});

		for(int i=0; i<expectedRecords.size(); i++) {
			Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i),"id").matches(actualRecords.get(i)));
		}
	}

	@org.junit.Test
	public void testJobEndpointWithGetRoleDesc() throws Exception{
		Map<String, Job> data = getTestData();
		List<Job> expectedRecords = new ArrayList<>();
		for(Map.Entry<String, Job> kv : data.entrySet()) {
			expectedRecords.add(om.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/jobs")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", TOKEN)
					.content(om.writeValueAsBytes(kv.getValue())))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isCreated())
					.andReturn().getResponse().getContentAsString(), Job.class));
		}

		expectedRecords.stream().sorted(Comparator.comparing(Job::getRole).reversed());

		List<Job> actualRecords = om.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/jobs?sort=desc&&orderby=role")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(expectedRecords.size())))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString(), new TypeReference<List<Job>>() {
		});

		for(int i=0; i<expectedRecords.size(); i++) {
			Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i),"id").matches(actualRecords.get(i)));
		}
	}

}

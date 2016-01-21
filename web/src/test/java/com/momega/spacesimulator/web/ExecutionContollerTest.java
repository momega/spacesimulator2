package com.momega.spacesimulator.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationState;
import com.momega.spacesimulator.simulation.test.TestDefinition;
import com.momega.spacesimulator.simulation.test.TestSimulation;
import com.momega.spacesimulator.web.config.AppConfig;
import com.momega.spacesimulator.web.config.ControllerConfig;
import com.momega.spacesimulator.web.config.WebAppConfig;
import com.momega.spacesimulator.web.controller.BasicSimulationDto;
import com.momega.spacesimulator.web.controller.FieldType;
import com.momega.spacesimulator.web.controller.FieldValueDto;
import com.momega.spacesimulator.web.controller.SimulationController;
import com.momega.spacesimulator.web.service.DefinitionService;
import com.momega.spacesimulator.web.service.SimulationHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, ControllerConfig.class, WebAppConfig.class })
@WebAppConfiguration
public class ExecutionContollerTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
    private Gson gson;

	private MockMvc mockMvc;
	
	@Mock
	private DefinitionService definitionService;
	
	@Autowired
	@InjectMocks
	private SimulationController simulationController;
	
	@Autowired
	private SimulationHolder simulationHolder;

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        simulationHolder.clearSimulations();
    }
	
	@Test
    public void newSimulation() throws Exception {
        BasicSimulationDto definitionValueDto = new BasicSimulationDto();
        definitionValueDto.setName("Test");
        FieldValueDto fieldValue = new FieldValueDto();
        fieldValue.setName("count");
        fieldValue.setType(FieldType.INT);
        fieldValue.setValue("10");
        definitionValueDto.getFieldValues().add(fieldValue);

        FieldValueDto fieldValue2 = new FieldValueDto();
        fieldValue2.setName("speed");
        fieldValue2.setType(FieldType.DOUBLE);
        fieldValue2.setValue("200.0");
        definitionValueDto.getFieldValues().add(fieldValue2);

        TestDefinition def = new TestDefinition();
        final Simulation<?,?> sim = wac.getBean(TestSimulation.class);
        when(definitionService.findDefinition("Test")).thenReturn(def);
        when(definitionService.createSimulation(TestSimulation.class)).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return sim;
            }
        });

        String data = gson.toJson(definitionValueDto);
        this.mockMvc.perform(post("/api/simulation").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.simulationState").value(SimulationState.PREPARING.toString()))
                .andExpect(jsonPath("$.uuid").value(simulationHolder.getSimulations().get(0).getUuid()));

        String uuid = simulationHolder.getSimulations().get(0).getUuid();

        this.mockMvc.perform(get("/api/simulation/{uuid}", uuid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.fieldValues.*", hasSize(2)))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].type").value("INT"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].value").value("10"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].type").value("DOUBLE"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].value").value("200.0"));

        this.mockMvc.perform(put("/api/execution/{uuid}", uuid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json;charset=UTF-8"))
        		.andExpect(jsonPath("$.name").value("Test"))
        		.andExpect(jsonPath("$.fieldValues.*", hasSize(2)));
        
        Thread.sleep(1000);
        
        this.mockMvc.perform(get("/api/simulation/{uuid}", uuid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.name").value("Test"))
		        .andExpect(jsonPath("$.fieldValues.*", hasSize(2)))
		        .andExpect(jsonPath("$.simulationState").value(SimulationState.RUNNING.toString()))
		        .andExpect(jsonPath("$.totalInputs").value(10))
		        .andExpect(jsonPath("$.completedInputs", greaterThan(0)))
		        .andExpect(jsonPath("$.failedInputs").value(0))
		        .andExpect(jsonPath("$.startedAt", notNullValue()));
        
        Thread.sleep(500);
        
        this.mockMvc.perform(delete("/api/execution/{uuid}", uuid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType("application/json;charset=UTF-8"))
	        .andExpect(jsonPath("$.name").value("Test"))
	        .andExpect(jsonPath("$.simulationState").value(SimulationState.CANCELED.toString()))
	        .andExpect(jsonPath("$.startedAt", notNullValue()));
    }	

}

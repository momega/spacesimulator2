package com.momega.spacesimulator.web;

import com.google.gson.Gson;
import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationFactory;
import com.momega.spacesimulator.simulation.SimulationHolder;
import com.momega.spacesimulator.simulation.SimulationState;
import com.momega.spacesimulator.simulation.test.TestDefinition;
import com.momega.spacesimulator.simulation.test.TestFields;
import com.momega.spacesimulator.simulation.test.TestSimulation;
import com.momega.spacesimulator.web.config.AppConfig;
import com.momega.spacesimulator.web.config.ControllerConfig;
import com.momega.spacesimulator.web.config.WebAppConfig;
import com.momega.spacesimulator.web.controller.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by martin on 12/31/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, ControllerConfig.class, WebAppConfig.class})
@WebAppConfiguration
public class SimulationControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @InjectMocks
    @Autowired
    private SimulationController simulationController;

    @Mock
    private SimulationFactory simulationFactory;

    @Autowired
    private SimulationHolder simulationHolder;

    @Autowired
    private Gson gson;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        simulationHolder.clearSimulations();
    }

    @Test
    public void emptyArray() throws Exception {
        this.mockMvc.perform(get("/simulation/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void oneSimulationArray() throws Exception {
        TestSimulation sim = new TestSimulation();
        TestFields params = new TestFields();
        params.setCount(10);
        params.setSpeed(200.0);
        sim.setFields(params);
        TestDefinition def = new TestDefinition();
        List<Simulation<?,?>> sims = Collections.singletonList(sim);
        simulationHolder.addSimulation(sim);

        when(simulationFactory.findDefinition(sims.get(0).getName())).thenReturn(def);

        this.mockMvc.perform(get("/simulation/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[0].fieldValues.*", hasSize(2)))
                .andExpect(jsonPath("$[0].fieldValues[?(@.name == 'count')].type").value("INT"))
                .andExpect(jsonPath("$[0].fieldValues[?(@.name == 'count')].value").value("10"))
                .andExpect(jsonPath("$[0].fieldValues[?(@.name == 'speed')].type").value("DOUBLE"))
                .andExpect(jsonPath("$[0].fieldValues[?(@.name == 'speed')].value").value("200.0"));
    }

    @Test
    public void deleteSimulation() throws Exception {
        TestSimulation sim = new TestSimulation();
        TestFields fields = new TestFields();
        fields.setCount(10);
        fields.setSpeed(200.0);
        sim.setFields(fields);
        TestDefinition def = new TestDefinition();
        simulationHolder.addSimulation(sim);

        this.mockMvc.perform(delete("/simulation/{uuid}", sim.getUuid()).contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/simulation/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void updateSimulation() throws Exception  {
        TestSimulation sim = new TestSimulation();
        TestFields fields = new TestFields();
        fields.setCount(10);
        fields.setSpeed(200.0);
        sim.setFields(fields);
        TestDefinition def = new TestDefinition();
        simulationHolder.addSimulation(sim);

        SimulationDto simulationDto = new SimulationDto();
        simulationDto.setUuid(sim.getUuid());
        simulationDto.setName(sim.getName());
        FieldValueDto fieldValue = new FieldValueDto();
        fieldValue.setName("count");
        fieldValue.setType(FieldType.INT);
        fieldValue.setValue("25");
        simulationDto.getFieldValues().add(fieldValue);
        FieldValueDto fieldValue2 = new FieldValueDto();
        fieldValue2.setName("speed");
        fieldValue2.setType(FieldType.DOUBLE);
        fieldValue2.setValue("200.0");
        simulationDto.getFieldValues().add(fieldValue2);

        String data = gson.toJson(simulationDto);
        this.mockMvc.perform(put("/simulation/{uuid}", sim.getUuid()).contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.simulationState").value(SimulationState.PREPARING.toString()))
                .andExpect(jsonPath("$.uuid").value(sim.getUuid()))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].type").value("INT"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].value").value("25"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].type").value("DOUBLE"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].value").value("200.0"));

    }

    @Test
    public void newSimulation() throws Exception {
        DefinitionValueDto definitionValueDto = new DefinitionValueDto();
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
        final Simulation<?,?> sim = new TestSimulation();
        when(simulationFactory.findDefinition("Test")).thenReturn(def);
        when(simulationFactory.createSimulation("Test")).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return sim;
            }
        });

        String data = gson.toJson(definitionValueDto);
        this.mockMvc.perform(post("/simulation/list").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.simulationState").value(SimulationState.PREPARING.toString()))
                .andExpect(jsonPath("$.uuid").value(simulationHolder.getSimulations().get(0).getUuid()));

        String uuid = simulationHolder.getSimulations().get(0).getUuid();

        this.mockMvc.perform(get("/simulation/{uuid}", uuid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.fieldValues.*", hasSize(2)))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].type").value("INT"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'count')].value").value("10"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].type").value("DOUBLE"))
                .andExpect(jsonPath("$.fieldValues[?(@.name == 'speed')].value").value("200.0"));

    }

}

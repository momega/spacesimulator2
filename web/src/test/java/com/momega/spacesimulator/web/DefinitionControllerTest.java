package com.momega.spacesimulator.web;

import com.momega.spacesimulator.web.config.AppConfig;
import com.momega.spacesimulator.web.config.ControllerConfig;
import com.momega.spacesimulator.web.config.WebAppConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by martin on 12/27/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, ControllerConfig.class, WebAppConfig.class})
@WebAppConfiguration
public class DefinitionControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getDefinitionList() throws Exception {
        this.mockMvc.perform(get("/definition/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].name").value("Moon Orbit"))
        		.andExpect(jsonPath("$[0].fields[?(@.name == 'startBurnTime')].type").value("TIMESTAMP"))
        		.andExpect(jsonPath("$[0].fields[?(@.name == 'burnTime')].type").value("DOUBLE"));
    }


}

package com.mycompany.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.messaging.app.Application;
import com.messaging.app.Controller;
import com.messaging.app.DatabaseCommunicator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RestControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private Controller controller;
    
    @Autowired
    private DatabaseCommunicator databaseCommunicator;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.databaseCommunicator.init();
        controller.setDatabaseCommunicator(databaseCommunicator);
    }


    @Test
    public void testCreateMessage() throws Exception {
        
        this.mockMvc.perform(post("/message/create/")
        		.param("message", "Message")
        		.param("from", "Natalia")
        		.param("to", "Pawel"))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void testCreateMessageMissingParams() throws Exception {
        
        this.mockMvc.perform(post("/message/create/")
        		.param("message", "Message")
        		.param("from", "Natalia"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testDeleteNotExistingMessage() throws Exception {
        
        this.mockMvc.perform(delete("/messages/delete/1/"))
                .andExpect(status().isNotFound());
    }
}

    

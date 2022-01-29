package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.service.DemoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	DemoService service;

	@Test
	void contextLoads() {
		assertNotNull(service);
	}
	
	@Test
	void sendMessage() {
		assertNotNull(service);
		service.sendMessage();
	}

	@Test
	void deadMessage() {
		assertNotNull(service);
		service.deadMessage();
	}

}

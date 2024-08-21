package com.vision.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import com.vision.api.controller.AuthorController;

@SpringBootTest
public class SpringAuthorRestApiApplicationTests{
	@Autowired
	private AuthorController authorController;
	
	
	@Test
	void contextLoads() {
		Assertions.assertThat(authorController).isNotNull();
	}
	
	@Test
	public void applicationStarts() {
		SpringAuthorRestApiApplication.main(new String[] {});
	 }
}
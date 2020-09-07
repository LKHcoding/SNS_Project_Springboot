package com.cos.instagram.web;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cos.instagram.web.dto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {
	
	@LocalServerPort
	private int port; //8080
	
	private MockMvc mvc; // 컨트롤러에서 들어오는 주소요청을 가로채기
	
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void 회원가입() throws Exception{
		String username = "kakaofriends";
		String password = "1234";
		String email = "kakaofriends@nate.com";
		String name = "카카오프렌즈";

		String url = "http://localhost:"+port+"/auth/join";
		System.out.println("url : "+url);
		JoinReqDto user = JoinReqDto.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
		
		StringBuilder formData = new StringBuilder();
		formData.append("username="+username+"&");
		formData.append("password="+password+"&");
		formData.append("email="+email+"&");
		formData.append("name="+name);
		
		System.out.println("user : "+user);
		mvc.perform(
				post(url)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(formData.toString())
		).andExpect(status().is3xxRedirection());
	}
}






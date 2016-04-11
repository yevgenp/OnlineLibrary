package com.example.ol;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.ol.db.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OnlineLibraryApplication.class)
@WebAppConfiguration
public class OnlineLibraryApplicationTests {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private BookRepository bookRepository;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	public void getHomeControllerAsAnonimous() throws Exception {
		mvc.perform( get("/").with(csrf()) )
			.andExpect(view().name("home"));
	}

	@Test
	public void getAddBookAsUser() throws Exception {
		mvc.perform( get("/addBook").with(csrf())
				.with(user("user").roles("USER")))
			.andExpect(status().isForbidden());
	}
	
	@Test
	public void getAddBookAsAdmin() throws Exception {
		mvc.perform( get("/addBook").with(csrf())
				.with(user("admin").roles("ADMIN")))
			.andExpect(view().name("addBook"));
	}
	
	@Test
	@Transactional
	public void AddAndDeleteBook() throws Exception {
		
		long books = bookRepository.count();
				
		MockMultipartFile file = new MockMultipartFile("file","ABCDxyz".getBytes());

		long bookId = (long) mvc.perform( fileUpload("/addBook").file(file).with(csrf())
								.with(user("admin").roles("ADMIN"))
								.param("title", "aTitle"))
						.andExpect(status().isFound())
						.andExpect(redirectedUrl("/result"))
						.andExpect(flash().attributeExists("bookid"))
						.andExpect(flash().attribute("result",
								"has been added successfully."))
						//.andDo(print())
						.andReturn().getFlashMap().get("bookid");
		
		assertEquals(bookRepository.count(),books+1);
		
		mvc.perform( get("/delete/" + bookId).with(csrf())
				.with(user("admin").roles("ADMIN")))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/result"))
			.andExpect(flash().attribute("result",
				"The book has been deleted."));
		
		assertEquals(bookRepository.count(),books);
	}
}

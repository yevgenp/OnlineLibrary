package com.example.ol.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ol.db.BookRepository;
import com.example.ol.db.UserRepository;
import com.example.ol.domain.Authority;
import com.example.ol.domain.Book;
import com.example.ol.domain.User;

@Controller
public class OLController {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	UserRepository userRepository;

	
	@ModelAttribute("book")
	public Book bookData () {
		return new Book();
	}
	
	
	@ModelAttribute("user")
	public User userData () {
		return new User();
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLogin(){
		return "login";
	}
	
	
	@RequestMapping(value={"/", "/home"}, method=RequestMethod.GET)
	public String home(Model m, RedirectAttributes ra){
		
		m.addAttribute("bookslist", bookRepository.getBooks());
		return "home";
	}
	
	
	@RequestMapping(value={"/", "/home"}, method=RequestMethod.POST)
	public String filter(Book b, Model m){
		
		m.addAttribute("bookslist", 
				bookRepository.getBooks(b.getAuthor(), b.getGenre(), b.getTitle()) );
		return "home";
	}

	
	@RequestMapping(value="/result", method=RequestMethod.GET)
	public String result(Model m){
		return "result";
	}
		
	
	@RequestMapping(value="/addBook", method=RequestMethod.GET)
	public String getAddBookForm(){
		return "addBook";
	}
	
	
	@RequestMapping(value="/addBook", method=RequestMethod.POST)
	public String addBook(
			@Valid Book book, Errors errors, 
			@RequestPart("file") MultipartFile file,
			RedirectAttributes ra){
		
		if (errors.hasErrors() 
				|| book.getTitle().trim().isEmpty() 
				|| file.isEmpty())
			return "addBook";
	
		try {
			book.setFilename(file.getOriginalFilename());
			book.setContent(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		book = bookRepository.save(book);
		ra.addFlashAttribute("result", "has been added successfully.");
		ra.addFlashAttribute("bookid", book.getId());
		return "redirect:/result";
	}
	
	
	@RequestMapping(value="/books/{bookId}", method=RequestMethod.GET)
	public String getBookInfo(
			@PathVariable("bookId") long bookId,
			Model model){
		
		model.addAttribute("book", bookRepository.getOne(bookId));
		return "book";
	}
	
	
	@RequestMapping(value="/download/{bookId}", method=RequestMethod.GET)
	public void downloadBook(
			@PathVariable("bookId") long bookId,
			HttpServletResponse response){
		
		Book b = bookRepository.getOne(bookId);
		try {
			response.setHeader("Content-Disposition", "attachment;filename=\"" +
								b.getFilename()+ "\"");
			OutputStream out = response.getOutputStream();
			//response.setContentType(b.getContentType());
			IOUtils.copy(new ByteArrayInputStream(b.getContent()), out);
			out.flush();
			out.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/delete/{bookId}")
	public String deleteBook(
			@PathVariable("bookId") long bookId,
			RedirectAttributes ra){
		
		Book book = bookRepository.getOne(bookId);
		for (User user : userRepository.findAll()) {
			user.getBooks().remove(book);
		}
		bookRepository.delete(book);
		ra.addFlashAttribute("result", "The book has been deleted.");
		return "redirect:/result";
	}
	
	
	@RequestMapping(value="/edit/{bookId}", method=RequestMethod.GET)
	public String getEditBookForm(
			@PathVariable("bookId") long bookId,
			Model m){
		
		m.addAttribute("book", bookRepository.getOne(bookId));
		return "editBook";
	}
	
	
	@RequestMapping(value="/edit/{bookId}", method=RequestMethod.POST)
	public String editBook(
			@PathVariable("bookId") long bookId,
			@Valid Book book, Errors errors, 
			@RequestPart("file") MultipartFile file, 
			RedirectAttributes ra){
		
		if (errors.hasErrors() 
				|| book.getTitle().trim().isEmpty()) return "editBook";
		
		Book b = new Book(bookRepository.getOne(bookId));
		if(!file.isEmpty()){
			try {
				b.setFilename(file.getOriginalFilename());
				b.setContent(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}		
		};
		b.setAuthor(book.getAuthor());
		b.setGenre(book.getGenre());
		b.setTitle(book.getTitle());
		b.setDescription(book.getDescription());
		bookRepository.save(b);
		ra.addFlashAttribute("result", "The book has been updated.");
		return "redirect:/result";
	}
	
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String getRegisterForm(){
		return "register";
	}
	
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerUser(
			@Valid User user, Errors errors, 
			RedirectAttributes ra){

		if (errors.hasErrors()) return "register";

		if(userRepository.exists(user.getUsername()) ){
			ra.addFlashAttribute("result", "This user already exists.\nChoose another login name.");
		}
		else{
			user.setEnabled(true);
			Authority auth = new Authority("ROLE_USER");
			auth.setUser(user);
			user.setAuthority(auth);
			userRepository.save(user);
			ra.addFlashAttribute("result", "User " + user.getUsername() + " has been registered.");
		}

		return "redirect:/result";
	}

	
	@RequestMapping(value="/users/{username}", method=RequestMethod.GET)
	public String getUserProfile(
			@PathVariable("username") String name,
			Model model){
		
		User user = userRepository.getUserByUsername(name);
		model.addAttribute("userData", user);
		model.addAttribute("bookslist", user.getBooks());
		return "profile";
	}

	
	@RequestMapping(value="/favorites")
	public ResponseEntity<String> handleFavorites(
			@RequestParam(value="action") String action,
			@RequestParam(value="bookid") long bookId,
			Principal principal){
		
			User user;
			Book book;
		
		switch (action.toLowerCase()) {
		
		case "add":
			user = userRepository.getUserByUsername(principal.getName());
			book = bookRepository.getOne(bookId);
			user.getBooks().add(book);
			userRepository.save(user);
			return new ResponseEntity<String>(HttpStatus.CREATED);
			
		case "rem":
			user = userRepository.getUserByUsername(principal.getName());
			book = bookRepository.getOne(bookId);
			user.getBooks().remove(book);
			userRepository.save(user);
			return new ResponseEntity<String>(HttpStatus.OK);
			
		default:
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} 
	}
	
	
	@ExceptionHandler({EntityNotFoundException.class, 
		EmptyResultDataAccessException.class})
	public String ErrorHandler(Exception e){
		return "redirect:/home"; //Temporary solution
	}
}

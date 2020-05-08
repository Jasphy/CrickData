package com.web.sample.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.sample.model.Person;
import com.web.sample.model.Register;
import com.web.sample.model.Role;
import com.web.sample.service.PersonService;

@Controller
@SessionScope
public class MvcController {

	@Autowired
	com.web.sample.model.PersonRepository repository;

	@Autowired
	com.web.sample.model.RegisterRepository regrepository;

	@Autowired
	com.web.sample.model.RoleRepository rolerepos;

	Role role;

	@Autowired
	PersonService personservice;

	public static HashMap<Integer, Person> cachedata = null;
	public static List<Integer> pagelist = null;
	public static List<List<Person>> cachegroups = null;

	@GetMapping("/login") // login page
	public String login() {

		return "login";
	}

	@GetMapping("/homepage1/{id}") // homepage navigation
	public String homepage1(@PathVariable("id") Integer id, Model model) {

		role = rolerepos.findRoleById(id);
		model.addAttribute(role);

		return "homepage";
	}

	@GetMapping("/register") // Registration Form
	public String gotoregisterpage() {

		return "register";
	}

	@PostMapping("/addregister") // After submitting Registration Form
	public String addnewregister(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("email") String email, @RequestParam("gender") String gender,
			@RequestParam("country") String country, @RequestParam("roleid") Integer roleid) {

		Register rs = new Register(username, password, email, gender, country, roleid);
		regrepository.save(rs);

		return "redirect:/login";

	}

	@PostMapping("/homepage") // HomePage
	public String homepage(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model, HttpSession session) {

		String user = username.toString();
		session.setAttribute("username", user);
		session.setMaxInactiveInterval(60);

		Register reg;

		if (regrepository.findRegisterByName(user) == null) {

			return "redirect:/login";

		}

		reg = regrepository.findRegisterByName(user);

		if (username.equals(reg.getName())) {

			if (password.equals(reg.getPassword())) {

				role = rolerepos.findRoleById(reg.getRoleId());
				model.addAttribute("role", role);
				model.addAttribute("jsession", session);
				return "homepage";
			}
		}
		return "redirect:/login";

	}

	@GetMapping(value = "/viewall/{rolename}") // To view all players
	public String personList(@PathVariable("rolename") String rolename, Model model) {

		List<Person> list = new ArrayList<>();
		preparecache(repository.findAll());
		int recordsperpage = 4;
		int count = 0;

		Set<Integer> var = cachedata.keySet();

		pagelist = new ArrayList<>();

		for (Integer key : cachedata.keySet()) {

			if (count < recordsperpage) {
				list.add(cachedata.get(key));

				count++;
			}
		}

		if (var.size() % 4 == 0) {

			int totalpages = var.size() / 4;
			int i = 0;

			while (i < totalpages) {

				pagelist.add(new Integer(i));
				i++;
			}
		}

		else {
			int totalpages = var.size() / 4 + 1;
			int i = 0;

			while (i < totalpages) {

				pagelist.add(new Integer(i));
				i++;
			}

		}

		model.addAttribute(role);
		model.addAttribute("persons", list);
		model.addAttribute("currentpage", 1);
		model.addAttribute("count", 4);
		model.addAttribute("noofpages", pagelist);

		return "person1";
	}

	@GetMapping(value = "/viewallbypage/{count}") // To view all players by pagination
	public String personListbypage(@PathVariable("count") int count, Model model) {

		List<Person> list = new ArrayList<>();

		int recordsperpage = 4;
		// int countnext =count+1;
		int count2 = count;
		//int count3 = 0;

		int every4record = 0;
		
		int start = (recordsperpage*count2)-recordsperpage+1;
		int end = (recordsperpage*count2);
		
		boolean add=false;
		
		
		Set<Integer> tempset=cachedata.keySet();
		
		for(Integer key:tempset) {
			
			if((key.intValue()+1)==start) {
				
				
			add=true;
			
				
			}
			
			
			if(add) {
				
				list.add(cachedata.get(key));
			}
			
			if((key.intValue()+1)==end) {
				
				add=false;
			}
			
			
			
		}

		// every4records increment value

		/*
		 * for(Integer key:cachedata.keySet()) {
		 * 
		 * 
		 * 
		 * if(count2>count && count3<recordsperpage) { list.add(cachedata.get(key));
		 * 
		 * count2++; count3++; } count2++;
		 * 
		 * 
		 * list.add(cachedata.get(key)); count2++;
		 * 
		 * if(count2==recordsperpage) {
		 * 
		 * every4record++; }
		 * 
		 * if(count==every4record) {
		 * 
		 * 
		 * }
		 * 
		 * 
		 * }
		 */

		model.addAttribute(role);
		model.addAttribute("persons", list);
		model.addAttribute("currentpage", 1);
		model.addAttribute("count", count2);
		model.addAttribute("noofpages", pagelist);
		preparecache(repository.findAll());

		return "person1";
	}

	private HashMap<Integer, Person> preparecache(List<Person> list) {

		cachedata = new HashMap<>();
		int i = 0;

		for (Person p : list) {

			cachedata.put(new Integer(i), p);
			i++;
		}

		return cachedata;

	}

	@GetMapping(value = "/person/{name}") // To view one player
	public String personbyname(@PathVariable("name") String name, Model model) {
		model.addAttribute("person", repository.findPesronByName(name));
		model.addAttribute(role);

		Person p = repository.findPesronByName(name);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(p);
			System.out.println(jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "View";
	}

	@GetMapping(value = "/update/{name}") // Edit Form
	public String editform(@PathVariable("name") String name, Model model) {

		if (role.getRoledesc().equals("Admin")) {
			model.addAttribute("person", repository.findPesronByName(name));
			model.addAttribute(role);
			return "Edit";
		}

		else
			return "AccessDenied";

	}

	@PostMapping("/edit") // After submitting Edit form
	public String editformsubmit(@RequestParam("name") String name, @RequestParam("country") String country,
			Model model) {

		Person person1 = repository.findOne(name);
		person1.setCountry(country);
		person1 = repository.save(person1);
		model.addAttribute("person", repository.findPesronByName(name));
		model.addAttribute(role);
		return "afteredit";
	}

	@RequestMapping(value = "/image/{name}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getImage(@PathVariable("name") String name) throws IOException {

		Person person = repository.findPesronByName(name);

		InputStreamResource isr;

		HttpHeaders headers = new HttpHeaders();

		try {

			if (person != null) {
				isr = new InputStreamResource(person.getPhoto().getBinaryStream());
				return new ResponseEntity<>(isr, headers, HttpStatus.OK);
			}
			System.err.println("displayAttachment() :: Error displaying attachment for name: " + name);
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
					"No Attachment found for " + name, "/download"), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.OK);
		}

	}

	@GetMapping("/addnewplayer") // Go to player page
	public String gotonewplayerpage() {

		return "AddPlayer";
	}

	@RequestMapping("/delete/{name}") // delete player
	public String delete(@PathVariable("name") String name, Model model) {

		System.out.println("deleted:" + name);
		model.addAttribute(role);
		int a = repository.deletePesronByName(name);

		System.out.println("deleted:" + name + ": : " + a);

		return "homepage";

	}

	@RequestMapping(value = "/generatereport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE) // GeneratefullReport
	public ResponseEntity<?> generatereport() throws IOException {

		String pdffile = personservice.generatereport();
		File f = new File(pdffile);

		InputStreamResource isr;

		HttpHeaders headers = new HttpHeaders();

		try {
			FileInputStream fis = new FileInputStream(f);

			isr = new InputStreamResource(fis);
			return new ResponseEntity<>(isr, headers, HttpStatus.OK);

		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(headers, HttpStatus.OK);
		}

	}

	@PostMapping("/addplayer") // Add new Player
	public String addnewplayer(@RequestParam("playername") String name,
			@RequestParam("dateofbirth") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dOB,
			@RequestParam("gender") String gender, @RequestParam("mailid") String email,
			@RequestParam("country") String country, @RequestParam("state") String state,
			@RequestParam("image") MultipartFile filename, @RequestParam("mobile") Integer mobileNo,
			@RequestParam("pincode") Integer pincode, Model model) {

		InputStream ins = null;
		Blob blob = null;
		byte[] fileContent = new byte[(int) filename.getSize()];
		try {
			ins = filename.getInputStream();
			ins.read(fileContent);
			try {
				blob = new SerialBlob(fileContent);
			} catch (SerialException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Person person = new Person(name, dOB, gender, email, country, state, blob, mobileNo, pincode);
		repository.save(person);

		// Register rs=new Register(name,password,email,gender,country,roleid);
		// regrepository.save(rs);

		System.out.println(role);
		model.addAttribute("role", role);
		return "homepage";

	}

}

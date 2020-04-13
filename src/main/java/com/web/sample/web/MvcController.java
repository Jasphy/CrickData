package com.web.sample.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

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
	
	@GetMapping("/login") //login page
	public String login(){
		
		return "login";
	}
	@GetMapping("/homepage1/{id}") //homepage navigation
	public String homepage1(@PathVariable("id") Integer id,Model model){
		
		role=rolerepos.findRoleById(id);
		model.addAttribute(role);
		
		return "homepage";
	}
	
	@GetMapping("/register") // Registration Form
	public String gotoregisterpage(){
		
		return "register";
	}
	
	@PostMapping("/addregister") //After submitting Registration Form
	public String addnewregister(@RequestParam("username")String username,@RequestParam("password")String password,
@RequestParam("email")String email,@RequestParam("gender")String gender,@RequestParam("country")String country,
@RequestParam("roleid")Integer roleid){
	
	    
		Register rs=new Register(username,password,email,gender,country,roleid);
	    regrepository.save(rs);
	
		
		return "redirect:/login";
	
	}
	@PostMapping("/homepage") //HomePage
	public String homepage(@RequestParam("username")String username,@RequestParam("password")String password,Model model
			,HttpSession session){
		
		
		String user=username.toString();
		session.setAttribute("username", user);
		session.setMaxInactiveInterval(60);
		
		
		Register reg;
		
		if(regrepository.findRegisterByName(user)==null) {
			
			return "redirect:/login";
			
		}
		 
		reg=regrepository.findRegisterByName(user);
		
		
		if(username.equals(reg.getName())){
			
			
			if(password.equals(reg.getPassword()))
		{
			
				role=rolerepos.findRoleById(reg.getRoleId());
				model.addAttribute("role",role);
				model.addAttribute("jsession", session);
				return "homepage";	
		}
		}
	return "redirect:/login";	
	
		}

	@GetMapping(value="/viewall/{rolename}") // To view all players
	public String personList(@PathVariable("rolename") String rolename,Model model) {
		
		model.addAttribute(role);
		model.addAttribute("persons", repository.findAll());

		return "person1";
	}

	@GetMapping(value="/person/{name}") // To view one player
	public String personbyname(@PathVariable("name") String name,Model model){
		model.addAttribute("person",repository.findPesronByName(name));
		model.addAttribute(role);
		return "View";
	}

	@GetMapping(value="/update/{name}") // Edit Form
	public String editform(@PathVariable("name") String name,Model model) 
	{
		
		
		
		if(role.getRoledesc().equals("admin"))
		{
		model.addAttribute("person", repository.findPesronByName(name));
		model.addAttribute(role);
		return "Edit";
		}

		
		else
			return "AccessDenied";
		
	}

	@PostMapping("/edit") // After submitting Edit form 
	public String editformsubmit(@RequestParam("name")String name,@RequestParam("country")String country,Model model) {


		Person person1 = repository.findOne(name);
        person1.setCountry(country);
	    person1 = repository.save(person1);
		model.addAttribute("person", repository.findPesronByName(name));
		model.addAttribute(role);
		return "afteredit";
	}
	
	@RequestMapping(value="/image/{name}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<?> getImage(@PathVariable("name") String name) throws IOException{

	Person person = repository.findPesronByName(name);
		
	InputStreamResource isr;

	HttpHeaders headers = new HttpHeaders();
    
	try {
		
		if(person!=null){
		isr = new InputStreamResource(person.getPhoto().getBinaryStream());
		 return new ResponseEntity<>(isr,headers,HttpStatus.OK);
		}
		System.err.println("displayAttachment() :: Error displaying attachment for name: " + name);
		 return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "No Attachment found for " + name,"/download"), HttpStatus.INTERNAL_SERVER_ERROR);
	} 
	 
	 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new ResponseEntity<>(headers,HttpStatus.OK);
	}

	}
	
	@GetMapping("/addnewplayer") // Go to player page
	public String gotonewplayerpage(){
		
		return "AddPlayer";
	}
	
	@RequestMapping(value="/generatereport", method = RequestMethod.GET,produces= MediaType.APPLICATION_PDF_VALUE) // GeneratefullReport
	public ResponseEntity<?> generatereport() throws IOException{
		
		String pdffile=personservice.generatereport();
		File f=new File(pdffile);
		
		InputStreamResource isr;

		HttpHeaders headers = new HttpHeaders();
	    
		try {
			FileInputStream fis = new FileInputStream(f);
			
			isr = new InputStreamResource(fis);
			 return new ResponseEntity<>(isr,headers,HttpStatus.OK);
			
			} 
		 
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(headers,HttpStatus.OK);
		}

	}
	
	@PostMapping("/addplayer") // Add new Player
	public String addnewplayer(@RequestParam("playername")String name,@RequestParam("dateofbirth")@DateTimeFormat(pattern = "yyyy-MM-dd")Date dOB, @RequestParam("gender")String gender,@RequestParam("mailid")String email,
			@RequestParam("country")String country,@RequestParam("state") String state, @RequestParam("image")MultipartFile filename,@RequestParam("mobile")Integer mobileNo,
	@RequestParam("pincode")Integer pincode,Model model)
{
		
		
		InputStream ins=null;
		Blob blob=null;
		byte[] fileContent = new byte[(int) filename.getSize()];
		try {
			ins=filename.getInputStream();
			ins.read(fileContent);
			try {
				blob=new SerialBlob(fileContent);
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
		
	    
		Person person=new Person(name,dOB,gender,email,country,state,blob,mobileNo,pincode);
		repository.save(person);
		
		//Register rs=new Register(name,password,email,gender,country,roleid);
	    //regrepository.save(rs);
		model.addAttribute("role",role);
        return "homepage";
	
	}

}

package com.web.sample.web;

//import java.sql.Blob;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Date;
//import java.util.List;
//import java.util.Locale;
import java.util.Locale;

//import javax.persistence.Entity;

//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.web.sample.model.Person;
import com.web.sample.service.PersonService;

@Controller
@RestController
@RequestMapping("/api")
public class PersonController {

	@Autowired
	com.web.sample.model.PersonRepository repository;
	@Autowired
	PersonService personservice;
	
	/*@RequestMapping(value="/get/{name}",method=RequestMethod.GET)
	@ResponseBody
	public Person findAll(@PathVariable String name){
		
		
		Person person=repository.findOne(name);
		return person1;
	    }*/
				  
	

	@RequestMapping(value ="/download/{name}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> downloadAttachment(@PathVariable("name") String name) {

        try {
            Person person = repository.findPesronByName(name);
            if (person != null) {
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.setContentDispositionFormData("attachment; filename=" + person.getName(), "");
                InputStreamResource isr = new InputStreamResource(person.getPhoto().getBinaryStream());
                return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
            }
           System.err.println("downloadAttachment() :: Error downloading attachment for name: " + name);
           return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "No Attachment found for " + name,"/download"), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
        catch (Exception ex) {
        	System.err.println("downloadAttachment() :: Exception while getting attachment by name " + name + " : " + ex.getMessage());
        	return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "No Attachment found for " + name,"/download"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
}	
	
	@RequestMapping("/update")
	 public String update(@RequestParam String name, @RequestParam String country) {
	
	  Person person = repository.findOne(name);
	  
		person.setCountry(country);

	  person = repository.save(person);
	   
	  String result = "<html>";
		
		result += "<div>" +repository.findOne(name).toString() + "</div>";
		result+="<p>Country has been updated successfully...</p>";
		

		return result+ "</html>";
	 }
	
	@RequestMapping("/updatedate")
	 public String update(@RequestParam String name, @RequestParam Date date) {
	
	  Person person = repository.findOne(name);
	  
		person.setDOB(date);

	  person = repository.save(person);
	   
	  String result = "<html>";
		
		result += "<div>" +repository.findOne(name).toString() + "</div>";
		result+="<p>Date of birth has been updated...</p>";
		

		return result+ "</html>";
	 }

	
	@RequestMapping("/delete")
	 public String delete(@RequestParam String name) {
	  repository.delete(name);
	     return "Record with the "+name+" deleted successfully";
	 }
	
			 
	 @RequestMapping("/test1")
	    @ResponseBody
	    public String handleRequest (Locale locale) {
	        return String.format("Request received. Language: %s, Country: %s %n",
	                             locale.getLanguage(), locale.getDisplayCountry());
	    }
	   }
	

	


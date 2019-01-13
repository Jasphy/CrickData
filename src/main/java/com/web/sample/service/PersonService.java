package com.web.sample.service;


import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.web.sample.model.Person;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Service("PersonService")
public class PersonService  {
	
	private final com.web.sample.model.PersonRepository personrepository;
	
	public PersonService(com.web.sample.model.PersonRepository personrepository){
		this. personrepository= personrepository;
		
	}

	/*public List<Person> findAll(){
		
		List<Person> persons=new ArrayList<>();
		
		for(Person person:personrepository.findAll()){
			
			persons.add(person);
		}
		return persons;
	}*/
	
	public List<Person> getPersons() {
		return personrepository.findAll();
	}
	
     public String generatereport(){
		 
		 JasperReport jr=null;
		 JasperPrint jp=null;
			
			try {
				jr=JasperCompileManager.compileReport("Cricket_list.jrxml");
				
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
			try {
				jp=JasperFillManager.fillReport(jr,null,personrepository.datasource().getConnection());
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JRPdfExporter exporter = new JRPdfExporter();
			 
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(
			  new SimpleOutputStreamExporterOutput("cricketer_report.pdf"));
			 
			SimplePdfReportConfiguration reportConfig
			  = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);
			 
			SimplePdfExporterConfiguration exportConfig
			  = new SimplePdfExporterConfiguration();
			exportConfig.setMetadataAuthor("Balaji");
			exportConfig.setEncrypted(true);
			exportConfig.setAllowedPermissionsHint("PRINTING");
			 
			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);
			 
			try {
				exporter.exportReport();
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	 return "cricketer_report.pdf";
	 }
	
}

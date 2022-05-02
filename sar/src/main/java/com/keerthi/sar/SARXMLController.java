package com.keerthi.sar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@RestController
public class SARXMLController {

	@PostMapping("/validateXml")
	public String vlaidateSARXML(@RequestParam("file") MultipartFile file) throws IOException {

		String response = validateXML(file);

		System.out.println(response);
		return response;

	}

	private String validateXML(MultipartFile file) throws IOException {

		System.out.println("validateXML - start - " + new Date());
		InputStream inputStream =  new BufferedInputStream(file.getInputStream());
		Source xmlFile = new StreamSource(inputStream);

		URL schemaFile = new URL("https://www.fincen.gov/sites/default/files/schema/base/EFL_SARXBatchSchema.xsd");
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			System.out.println("validateXML ended - " + new Date());
			return "SAR Batch XML is valid";
		} catch(SAXException e) {
			return "SAR Batch XML is not valid. Reason : " + e;
		} catch(IOException e) {
			return "IOException :" + e.getStackTrace();
		} catch (Exception e) {
			return "Exception :" + e.getStackTrace();
		}
	}

}

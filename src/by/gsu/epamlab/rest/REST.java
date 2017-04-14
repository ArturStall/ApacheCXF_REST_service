package by.gsu.epamlab.rest;

import java.io.StringReader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.json.JSONObject;

import by.gsu.epamlab.bean.Chapter;
import by.gsu.epamlab.bean.Document;

@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_JSON)
public class REST {
	Document doc;
		
	@POST
	public void addDocument(String upload) {
		JAXBContext context;
		Unmarshaller unmarshaller;
		try {
			context = JAXBContext.newInstance(Document.class);
	        unmarshaller = context.createUnmarshaller();
	        doc = (Document) unmarshaller.unmarshal(new StringReader(upload));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	public String getDocument() {
		return new JSONObject(doc).toString();
	}
		
	@PUT
	public String updateDocument(String update) {
		JAXBContext context;
		Unmarshaller unmarshaller;
		try {
			context = JAXBContext.newInstance(Document.class);
	        unmarshaller = context.createUnmarshaller();
	        Chapter updateChapter = (Chapter) unmarshaller.unmarshal(new StringReader(update));
	        Chapter removeChapter = null; 
	        for (Chapter chapter: doc.getChapter()) {
	        	if (chapter.getId() == updateChapter.getId()) {
	        		removeChapter = chapter;
	        	}
	        }
	        doc.getChapter().remove(removeChapter);
	        doc.getChapter().add(updateChapter);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return new JSONObject(doc).toString();
	}
	
	@DELETE
	public Response deleteFirstChapterDocument() {
		if (doc.getChapter().size() == 0) {
			return Response.status(403).type("text/plain").entity("There are no chapters in the document on the service").build();
		}
		doc.getChapter().remove(0);
		return Response.status(200).build();
	}
}
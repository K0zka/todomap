package org.todomap.o29.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.o29.beans.Attachment;


@Path("/attachments")
@Produces("application/json")
public interface AttachmentService {

	@XmlRootElement(name="atchmnt")
	public class ShortAttachment {
		public ShortAttachment() {
		}
		public ShortAttachment(final Attachment attachment) {
			this.id = attachment.getId();
			this.filename = attachment.getFileName();
		}
		private long id;
		private String filename;
		public long getId() {
			return id;
		}
		public String getFilename() {
			return filename;
		}
		public void setId(long id) {
			this.id = id;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
	}

	@POST
	@Path("/add")
	void addAttachment(Attachment attachment, InputStream data) throws IOException;

	@GET
	@Path("/{id}/get")
	@XmlElementWrapper(name="attachments")
	List<Attachment> getAttachments(@PathParam("id") long id);

	@GET
	@Path("/{id}/get.shrt")
	@XmlElementWrapper(name="atchs")
	List<ShortAttachment> getShortAttachments(@PathParam("id") long id);
	
	@GET
	@Path("/{id}")
	Attachment getAttachment(@PathParam("id") long id);
	
	@DELETE
	@Path("delete/{id}")
	void deleteAttachment(@PathParam("id") long id);
	
	public InputStream getData(final Attachment attachment) throws IOException;
	public InputStream getThumbnail(final Attachment attachment) throws IOException;
	public OutputStream writeData(final Attachment attachment) throws IOException;
}

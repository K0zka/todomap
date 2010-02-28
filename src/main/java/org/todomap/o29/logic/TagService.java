package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.todomap.o29.beans.Tag;

import com.sun.xml.txw2.annotation.XmlElement;

@Path("/tags/")
@Produces("application/json")
public interface TagService {
	
	@XmlElement(value="tagc")
	public class TagCloudElem {
		Tag tag;
		public Tag getTag() {
			return tag;
		}
		public void setTag(Tag tag) {
			this.tag = tag;
		}
		public long getWight() {
			return wight;
		}
		public void setWight(long wight) {
			this.wight = wight;
		}
		long wight;
	}
	
	@GET
	@Path("/cloud/{lang}")
	List<TagCloudElem> getTagCloud(@PathParam("lang") String language);

	@GET
	@Path("/list/{lang}")
	List<Tag> listTags(@PathParam("lang") String language);

	@GET
	@Path("/list/{lang}/{pref}")
	List<Tag> listTags(@PathParam("lang") String language, @PathParam("pref") String prefix);

	@POST
	@Path("/add/{lang}/{id}")
	public void addTag(@PathParam("id") long id, @PathParam("lang") String language, String tag);
}

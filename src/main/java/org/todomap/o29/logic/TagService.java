package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.o29.beans.Tag;

@Path("/tags/")
@Produces("application/json")
public interface TagService {
	
	@XmlRootElement(name = "tagc")
	public class TagCloudElem {
		Tag tag = new Tag();
		public Tag getTag() {
			return tag;
		}
		public void setTag(Tag tag) {
			this.tag = tag;
		}
		float weight;
		public float getWeight() {
			return weight;
		}
		public void setWeight(float weight) {
			this.weight = weight;
		}
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
	public long addTag(@PathParam("id") long id, @PathParam("lang") String language, Tag tag);
	
	@GET
	@Path("/tagsof/{id}")
	List<Tag> listTagsOfbean(@PathParam("id") long id);
}

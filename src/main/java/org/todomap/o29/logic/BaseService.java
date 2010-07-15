package org.todomap.o29.logic;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.todomap.o29.beans.BaseBean;

@Path("/base/")
@Produces("application/json")
public interface BaseService {

	@GET
	@Path("/get/{id}")
	BaseBean getById(@PathParam("id") long id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@POST
	// delete would be nice, but it is not supported by 'some browsers'
	@Path("/remove/{id}")
	BaseBean removebyId(@PathParam("id") long id);

	List<Date> listActiveDays(final String countryCode);

	List<BaseBean> list(final String countryCode, final Date day);

	@GET
	@Path("/search/{text}")
	List<BaseBean> search(@PathParam("text") String freeText);
}

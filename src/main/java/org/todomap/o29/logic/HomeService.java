package org.todomap.o29.logic;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.todomap.o29.beans.Coordinate;
import org.todomap.o29.beans.User;


@Path("/home/")
@Produces("application/json")
public interface HomeService {
	
	public class HomeLocation {
		Coordinate loc;
		short zoomLevel;
		public Coordinate getLoc() {
			return loc;
		}
		public void setLoc(Coordinate loc) {
			this.loc = loc;
		}
		public short getZoomLevel() {
			return zoomLevel;
		}
		public void setZoomLevel(short zoomLevel) {
			this.zoomLevel = zoomLevel;
		}
	}

	@Path("/auth")
	@GET
	boolean isAuthenticated();
	
	@Path("/loc")
	@GET
	HomeLocation getHome();

	@Path("/set")
	@POST
	void setHome(HomeLocation loc);

	@Path("/user/get")
	@GET
	User getUser();

	@Path("/user/set")
	@POST
	void setUserData(User user);
}

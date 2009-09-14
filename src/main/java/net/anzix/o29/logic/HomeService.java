package net.anzix.o29.logic;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.anzix.o29.beans.Coordinate;

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
	
	@Path("/loc")
	@GET
	public HomeLocation getHome();

	@Path("/set")
	@PUT
	public void setHome(HomeLocation loc);
	
}

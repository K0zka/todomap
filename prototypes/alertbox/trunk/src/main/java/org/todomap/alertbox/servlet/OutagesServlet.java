package org.todomap.alertbox.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.todomap.alertbox.History;
import org.todomap.alertbox.History.Outage;
import org.todomap.alertbox.Monitor;
import org.todomap.alertbox.Monitorable;

public class OutagesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6091062548698990808L;

	final private long oneWeek = 7l * 24l * 60l * 60l * 1000l;

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		final History history = (History) request.getServletContext()
				.getAttribute("history");
		final Monitor monitor = (Monitor) request.getServletContext()
				.getAttribute("monitor");
		final String id = StringUtils.substringAfterLast(
				request.getRequestURI(), "/");

		for (final Monitorable monitorable : monitor.getMonitorables()) {
			if (id.equals(monitorable.getId())) {

				response.setContentType("image/jpeg");
				final BufferedImage image = new BufferedImage(250, 50,
						BufferedImage.TYPE_INT_RGB);
				final Graphics2D graphics = image.createGraphics();
				graphics.setColor(Color.GREEN);
				graphics.fill3DRect(0, 0, 250, 50, true);

				graphics.setColor(Color.BLACK);
				final long oneWeekAgo = System.currentTimeMillis() - oneWeek;
				for (final Outage outage : history.listOutages(monitorable,
						new Date(oneWeekAgo), new Date())) {
					long start = outage.getStart().getTime();
					long end = outage.getEnd() == null ? System
							.currentTimeMillis() : outage.getEnd()
							.getTime();
					graphics.fillRect((int) (start / (250 * oneWeek)), 0,
							(int) ((end - start) / (250 * oneWeek)), 50);
				}
				ImageIO.write(image, "jpeg", response.getOutputStream());
				break;
			}
		}
	}

}

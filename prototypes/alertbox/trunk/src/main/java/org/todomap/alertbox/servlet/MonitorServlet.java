package org.todomap.alertbox.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.todomap.alertbox.History;
import org.todomap.alertbox.Monitor;
import org.todomap.alertbox.Notifier;

public class MonitorServlet extends HttpServlet {

	private static final File alertboxConfigDir = new File(System
						.getProperty("user.home"), ".alertbox");
	/**
	 * 
	 */
	private static final long serialVersionUID = 4380959961948400306L;
	
	final History history = new History();
	Monitor monitor = null;
	final Timer timer = new Timer();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("statuses", monitor.getStatuses());
		request.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request,
				response);
		;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		Notifier notifier = getNotifier();
		try {
			notifier.start();
		} catch (final Exception e) {
			throw new ServletException(e);
		}
		monitor = new Monitor(notifier, history);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					monitor.updateAllMonitors();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 1000, 60000);
	}

	private Notifier getNotifier() {
		try {
			JAXBContext context = JAXBContext
					.newInstance("org.todomap.alertbox.notifiers");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Notifier) unmarshaller.unmarshal(new File(alertboxConfigDir, "notifiers.xml"));
		} catch (JAXBException e) {
			return null;
		}
	}
}

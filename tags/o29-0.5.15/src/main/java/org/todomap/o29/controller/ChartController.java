package org.todomap.o29.controller;

import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.RatingReport;
import org.todomap.o29.logic.RatingService;

public class ChartController implements Controller {

	public ChartController(RatingService ratingService) {
		super();
		this.ratingService = ratingService;
	}

	final RatingService ratingService;

	@Override
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final int width = Integer.parseInt(request.getParameter("w"));
		final int height = Integer.parseInt(request.getParameter("h"));
		final long beanId = Long.parseLong(request.getParameter("id"));

		final BufferedImage chartImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		final DefaultPieDataset pieDataset = new DefaultPieDataset();
		final RatingReport anonRatingReport = ratingService.getAnonRatingReport(beanId);
		for(final RatingReport.RatingReportItem item : anonRatingReport.getItems()) {
			pieDataset.setValue(String.valueOf(item.getValue()), item.getCount());
		}
		pieDataset.getItemCount();
		final PiePlot plot = new PiePlot(pieDataset);
		final JFreeChart chart = new JFreeChart(plot);
		chart.setBorderVisible(false);
		chart.draw(chartImage.createGraphics(), new Float(0, 0, width, height));

		response.setContentType("image/jpeg");
		ImageIO.write(chartImage, "jpeg", response.getOutputStream());
		return null;
	}

}

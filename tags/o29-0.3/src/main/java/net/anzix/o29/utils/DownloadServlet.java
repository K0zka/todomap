package net.anzix.o29.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import net.anzix.o29.beans.Attachment;

public class DownloadServlet extends SpringServlet {

	private static final long serialVersionUID = 1827555980223764337L;

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		final long id = getId(req);
		final Attachment attachment = attachmentService.getAttachment(id);
		if(req.getRequestURI().indexOf("thumbnail") != -1) {
			findThumbnail(resp, attachment);
		} else {
			findData(resp, attachment);
		}
	}

	void findData(final HttpServletResponse resp,
			final Attachment attachment) throws IOException {
		if (attachment == null || attachment.getData() == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			sendData(resp, attachment.getMime(), attachment.getData());
		}
	}

	void findThumbnail(final HttpServletResponse resp,
			final Attachment attachment) throws IOException {
		if (attachment == null || attachment.getThumbnail() == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			sendData(resp, attachment.getMime(), attachment.getThumbnail());
		}
	}

	long getId(final HttpServletRequest req) {
		final String[] split = req.getPathInfo().split("/");
		return Long.parseLong(split[split.length - 1]);
	}

	void sendData(final HttpServletResponse resp,
			final String mime, final InputStream data) throws IOException {
		resp.setContentType(mime);
		IOUtils.copy(data, resp.getOutputStream());
	}

}

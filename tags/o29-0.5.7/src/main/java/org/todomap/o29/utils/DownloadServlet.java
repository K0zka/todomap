package org.todomap.o29.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.todomap.o29.beans.Attachment;
import org.todomap.o29.beans.BaseBean;


public class DownloadServlet extends SpringServlet {

	private static final long serialVersionUID = 1827555980223764337L;

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		final long id = getId(req);
		final BaseBean bean = baseService.getById(id);
		if(bean instanceof Attachment) {
			final Attachment attachment = (Attachment)bean;
			if(req.getRequestURI().indexOf("thumbnail") != -1) {
				findThumbnail(resp, attachment);
			} else {
				findData(resp, attachment);
			}
		} else {
			
		}
	}

	void findData(final HttpServletResponse resp,
			final Attachment attachment) throws IOException {
		final InputStream data = attachmentService.getData(attachment);
		try {
			if (attachment == null || data == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				sendData(resp, attachment.getMime(), data);
			}
		} finally {
			IOUtils.closeQuietly(data);
		}
	}

	void findThumbnail(final HttpServletResponse resp,
			final Attachment attachment) throws IOException {
		final InputStream thumbnail = attachmentService.getThumbnail(attachment);
		try {
			if (attachment == null || thumbnail == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				sendData(resp, attachment.getMime(), thumbnail);
			}
		} finally {
			IOUtils.closeQuietly(thumbnail);
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

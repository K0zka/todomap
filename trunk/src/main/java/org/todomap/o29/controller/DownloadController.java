package org.todomap.o29.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.Attachment;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.logic.AttachmentService;
import org.todomap.o29.logic.BaseService;

public final class DownloadController implements Controller{

	final BaseService baseService;
	public DownloadController(BaseService baseService,
			AttachmentService attachmentService) {
		super();
		this.baseService = baseService;
		this.attachmentService = attachmentService;
	}

	final AttachmentService attachmentService;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final long id = getId(request);
		final BaseBean bean = baseService.getById(id);
		if(bean instanceof Attachment) {
			final Attachment attachment = (Attachment)bean;
			if(request.getRequestURI().indexOf("thumbnail") != -1) {
				findThumbnail(response, attachment);
			} else {
				findData(response, attachment);
			}
		} else {
			
		}
		return null;
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

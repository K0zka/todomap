package org.todomap.o29.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.Attachment;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.AttachmentService;
import org.todomap.o29.logic.BaseService;
import org.todomap.o29.logic.UserService;


public final class UploadController implements Controller {

	final UserService userService;
	public UploadController(UserService userService,
			AttachmentService attachmentService, BaseService baseService) {
		super();
		this.userService = userService;
		this.attachmentService = attachmentService;
		this.baseService = baseService;
	}

	final AttachmentService attachmentService;
	final BaseService baseService;
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("uploading...");
		
		final User user = userService.getCurrentUser();
		if(user == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		
		final long id = getId(request);
		
		final ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
		try {
			@SuppressWarnings("unchecked")
			final List<FileItem> items = fileUpload.parseRequest(request);
			for(final FileItem item : items) {
				if("file".equals(item.getFieldName())) {
					final Attachment attachment = new Attachment();
					attachment.setCreator(user);
					attachment.setFileName(item.getName());
					attachment.setMime(item.getContentType());
					final OutputStream dataStream = attachmentService.writeData(attachment);
					try {
						IOUtils.copy(item.getInputStream(), dataStream);
					} finally {
						IOUtils.closeQuietly(dataStream);
					}
					
					final BaseBean attachedTo = baseService.getById(id);
					
					attachment.setAttachedTo(attachedTo);
					attachmentService.addAttachment(attachment, item.getInputStream());
				}
			}
			response.getWriter().println("ok");
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
		
		return null;
	}
	
	final long getId(final HttpServletRequest req) {
		String[] split = req.getPathInfo().split("/");
		return Long.parseLong(split[split.length - 1]);
	}


}

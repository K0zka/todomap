package org.todomap.o29.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.todomap.o29.beans.Attachment;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;

public class UploadServlet extends SpringServlet {

	private static final long serialVersionUID = 5126647297488027223L;

	private final static Logger logger = Logger.getLogger(UploadServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("uploading...");
		
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		final User user = userService.getUserByOpenIdUrl(authentication.getName());
		
		final long id = getId(req);
		
		final ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
		try {
			@SuppressWarnings("unchecked")
			final List<FileItem> items = fileUpload.parseRequest(req);
			for(final FileItem item : items) {
				if("file".equals(item.getFieldName())) {
					final Attachment attachment = new Attachment();
					attachment.setCreator(user);
					attachment.setFileName(item.getName());
					attachment.setMime(item.getContentType());
					attachment.setData(item.getInputStream());
					
					final BaseBean attachedTo = baseService.getById(id);
					
					attachment.setAttachedTo(attachedTo);
					attachmentService.addAttachment(attachment);
				}
			}
			resp.getWriter().println("ok");
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
		
		
	}

	final long getId(final HttpServletRequest req) {
		String[] split = req.getPathInfo().split("/");
		return Long.parseLong(split[split.length - 1]);
	}
	
}

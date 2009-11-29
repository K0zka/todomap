package org.todomap.o29.logic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.todomap.o29.beans.Attachment;

public class JpaAttachmentService extends JpaDaoSupport implements
		AttachmentService {

	private static final String ThumbnailPrefix = "thmb-";

	private final static Logger logger = Logger.getLogger(JpaAttachmentService.class);
	
	String fileStorageDir = "attachments";

	List<String> acceptedMime = new ArrayList<String>();
	
	UserService userService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<String> getAcceptedMime() {
		return acceptedMime;
	}

	public void setAcceptedMime(List<String> acceptedMime) {
		this.acceptedMime = acceptedMime;
	}

	@Override
	public void addAttachment(Attachment attachment) throws IOException {
		if(!acceptedMime.contains(attachment.getMime())){
			logger.info("Attachment was ignored:"+attachment.getMime()+" "+attachment.getFileName());
			return;
		}
		attachment.setCreator(userService.getCurrentUser());
		getJpaTemplate().persist(attachment);
		attachment.getId();
		final File dataFile = new File(
				fileStorageDir, String.valueOf(attachment.getId()));
		final FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
		File thumbnailFile = new File(
				fileStorageDir, ThumbnailPrefix.concat(String.valueOf(attachment.getId())));
		try {
			IOUtils.copy(attachment.getData(), fileOutputStream);
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
		}
		//generate thumbnail
		final BufferedImage image = ImageIO.read(dataFile);
		final Image scaledInstance = image.getScaledInstance(100, 80, Image.SCALE_FAST);
		BufferedImage newImage = new BufferedImage(100, 80, BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(scaledInstance, 0, 0, null);
		ImageIO.write(newImage, "jpg", thumbnailFile);
	}

	Attachment loadData(final Attachment attachment) {
		if(attachment != null) {
			try {
				attachment.setData(new FileInputStream(new File(fileStorageDir, String.valueOf(attachment.getId()))));
			} catch (FileNotFoundException e) {
				logger.error("Attachment not found in file store ", e);
			}
			try {
				attachment.setThumbnail(new FileInputStream(new File(fileStorageDir, ThumbnailPrefix.concat(String.valueOf(attachment.getId())))));
			} catch (FileNotFoundException e) {
				logger.error("Attachment thumbnail not found in file store ", e);
			}
		}
		return attachment;
	}
	
	public void init() {
		final File storageDir = new File(fileStorageDir);
		if(!storageDir.exists()) {
			storageDir.mkdirs();
		}
	}

	public String getFileStorageDir() {
		return fileStorageDir;
	}

	public void setFileStorageDir(String fileStorageDir) {
		this.fileStorageDir = fileStorageDir;
	}

	@Override
	public List<Attachment> getAttachments(long id) {
		final HashMap<String, Long> params = new HashMap<String, Long>();
		params.put("id", id);
		@SuppressWarnings("unchecked")
		final List<Attachment> attachments = getJpaTemplate().findByNamedParams("select OBJECT(attachment) from "+Attachment.class.getName() + " attachment where attachedTo.id = :id", params);
		for(final Attachment attachment : attachments) {
			loadData(attachment);
		}
		return attachments;
	}

	@Override
	public List<ShortAttachment> getShortAttachments(final long id) {
		ArrayList<ShortAttachment> ret = new ArrayList<ShortAttachment>();
		for(Attachment attachment : getAttachments(id)) {
			ret.add(new ShortAttachment(attachment));
		}
		return ret;
	}

	@Override
	public Attachment getAttachment(long id) {
		return loadData(getJpaTemplate().find(Attachment.class, id));
	}

	@Override
	public void deleteAttachment(long id) {
		final Attachment attachment = getJpaTemplate().find(Attachment.class, id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(attachment.getCreator().getOpenIdUrl().equals(authentication.getName())) {
			//remove from db
			getJpaTemplate().remove(attachment);
			
		}
	}

}

package org.todomap.o29.logic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.Attachment;
import org.todomap.o29.beans.User;

public class JpaAttachmentService extends JpaDaoSupport implements
		AttachmentService {

	private static final String ThumbnailPrefix = "thmb-";

	private final static Logger logger = Logger
			.getLogger(JpaAttachmentService.class);

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
	public Attachment addAttachment(Attachment attachment, InputStream data)
			throws IOException {
		if (!acceptedMime.contains(attachment.getMime())) {
			logger.info("Attachment was ignored:" + attachment.getMime() + " "
					+ attachment.getFileName());
			return null;
		}
		attachment.setCreator(userService.getCurrentUser());
		getJpaTemplate().persist(attachment);
		attachment.getId();
		final File dataFile = getFile(attachment);
		final FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
		File thumbnailFile = getThumbnailFile(attachment);
		try {
			fileOutputStream.flush();
			IOUtils.copy(data, fileOutputStream);
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
		}
		// generate thumbnail
		final BufferedImage image = ImageIO.read(getFile(attachment));
		final Image scaledInstance = image.getScaledInstance(100, 80,
				Image.SCALE_FAST);
		BufferedImage newImage = new BufferedImage(100, 80,
				BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(scaledInstance, 0, 0, null);
		ImageIO.write(newImage, "jpg", thumbnailFile);
		return attachment;
	}

	public void init() {
		final File storageDir = new File(fileStorageDir);
		if (!storageDir.exists()) {
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
		final List<Attachment> attachments = getJpaTemplate()
				.findByNamedParams(
						"select OBJECT(attachment) from "
								+ Attachment.class.getName()
								+ " attachment where attachedTo.id = :id",
						params);
		return attachments;
	}

	@Override
	public List<ShortAttachment> getShortAttachments(final long id) {
		ArrayList<ShortAttachment> ret = new ArrayList<ShortAttachment>();
		for (Attachment attachment : getAttachments(id)) {
			ret.add(new ShortAttachment(attachment));
		}
		return ret;
	}

	@Override
	public Attachment getAttachment(long id) {
		return getJpaTemplate().find(Attachment.class, id);
	}

	@Override
	public Attachment deleteAttachment(long id) {
		final User currentUser = userService.getCurrentUser();
		final Attachment attachment = getJpaTemplate().find(Attachment.class,
				id);
		if (currentUser != null
				&& attachment != null
				&& currentUser.getOpenIdUrl().equals(
						attachment.getCreator().getOpenIdUrl())) {
			final File data = getFile(attachment);
			final File thumbnailData = getThumbnailFile(attachment);
			getJpaTemplate().remove(attachment);
			data.delete();
			thumbnailData.delete();
		}
		return null;
	}

	@Override
	public InputStream getData(final Attachment attachment) throws IOException {
		return new FileInputStream(getFile(attachment));
	}

	File getFile(final Attachment attachment) {
		return new File(fileStorageDir, String.valueOf(attachment.getId()));
	}

	@Override
	public InputStream getThumbnail(final Attachment attachment)
			throws IOException {
		return new FileInputStream(getThumbnailFile(attachment));
	}

	private File getThumbnailFile(final Attachment attachment) {
		return new File(fileStorageDir, ThumbnailPrefix
				.concat(String.valueOf(attachment.getId())));
	}

	@Override
	public OutputStream writeData(final Attachment attachment)
			throws IOException {
		return new FileOutputStream(getFile(attachment));
	}

}

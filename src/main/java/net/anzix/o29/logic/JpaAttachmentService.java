package net.anzix.o29.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.anzix.o29.beans.Attachment;

import org.apache.commons.io.IOUtils;
import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaAttachmentService extends JpaDaoSupport implements
		AttachmentService {

	String fileStorageDir = "attachments";

	@Override
	public void addAttachment(Attachment attachment) throws IOException {
		getJpaTemplate().persist(attachment);
		attachment.getId();
		final FileOutputStream fileOutputStream = new FileOutputStream(new File(
				fileStorageDir, String.valueOf(attachment.getId())));
		try {
			IOUtils.copy(attachment.getData(), fileOutputStream);
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
		}
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

}

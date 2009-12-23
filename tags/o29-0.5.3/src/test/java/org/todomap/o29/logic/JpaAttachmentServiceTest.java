package org.todomap.o29.logic;

import java.io.IOException;

import org.junit.Test;
import org.todomap.o29.beans.Attachment;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/o29/logic/JpaAttachmentServiceTestCtx.xml")
public class JpaAttachmentServiceTest extends UnitilsJUnit4 {
	@SpringBean("attachmentService")
	AttachmentService attachmentService;
	
	@Test
	public void testGetAttachments() {
		attachmentService.getAttachments(1);
	}
	
	@Test
	public void testAddAttachment() throws IOException {
		final Attachment attachment = new Attachment();
		attachment.setMime("image/gif");
		attachment.setFileName("duke.gif");
		attachment.setAttachedTo(attachment);
		attachmentService.addAttachment(attachment, Thread.currentThread().getContextClassLoader().getResourceAsStream("duke.gif"));
	}
}

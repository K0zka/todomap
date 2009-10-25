package net.anzix.o29.logic;

import java.io.IOException;

import net.anzix.o29.beans.Attachment;

public interface AttachmentService {
	void addAttachment(Attachment attachment) throws IOException;
}

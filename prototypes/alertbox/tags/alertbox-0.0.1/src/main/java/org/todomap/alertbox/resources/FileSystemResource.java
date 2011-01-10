package org.todomap.alertbox.resources;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fs")
public class FileSystemResource extends BaseMonitorable {

	String path;
	long neededFreeSpaceWarn = 0l;
	long neededFreeSpaceError = 0l;

	@Override
	public String getName() {
		return path;
	}

	@Override
	public StatusDescription check() throws Exception {
		final File file = new File(path);
		long freespace = file.getFreeSpace() / (1024 * 1024);
		return new StatusDescription(
				neededFreeSpaceWarn > freespace ? (neededFreeSpaceError > freespace ? Status.Fail
						: Status.Warning)
						: Status.Ok, "Free space: " + freespace + " MB");
	}

	@XmlAttribute(name = "path")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlAttribute(name = "freespace-warn")
	public long getNeededFreeSpaceWarn() {
		return neededFreeSpaceWarn;
	}

	public void setNeededFreeSpaceWarn(long neededFreeSpaceWarn) {
		this.neededFreeSpaceWarn = neededFreeSpaceWarn;
	}

	@XmlAttribute(name = "freespace-error")
	public long getNeededFreeSpaceError() {
		return neededFreeSpaceError;
	}

	public void setNeededFreeSpaceError(long neededFreeSpaceError) {
		this.neededFreeSpaceError = neededFreeSpaceError;
	}

	@Override
	public final String getTypeId() {
		return "fs";
	}

}

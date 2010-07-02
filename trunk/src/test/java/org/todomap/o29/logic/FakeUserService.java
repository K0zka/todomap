package org.todomap.o29.logic;

import java.util.List;

import org.todomap.o29.beans.Link;
import org.todomap.o29.beans.User;

public class FakeUserService implements UserService {

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByOpenIdUrl(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public long addUserLink(Link link) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Link> listUserLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long removeLink(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}

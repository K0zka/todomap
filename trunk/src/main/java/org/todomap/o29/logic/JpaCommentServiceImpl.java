package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Comment;

public class JpaCommentServiceImpl extends JpaDaoSupport implements CommentService {

	TranslatorService translatorService;
	UserService userService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void addComment(long id, String text) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		final Comment comment = new Comment();
		comment.setText(text);
		comment.setCreated(new Date());
		translatorService.updateLanguage(comment);
		comment.setBean(bean);
		comment.setCreator(userService.getCurrentUser());

		getJpaTemplate().persist(comment);
	}

	@Override
	public List<Comment> getComments(long id) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		if(bean == null) {
			return new ArrayList<Comment>();
		} else {
			return bean.getComments();
		}
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

}

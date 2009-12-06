package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Comment;
import org.todomap.o29.utils.HtmlUtil;

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
	public Comment addComment(long id, final Comment comment) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		comment.setText(HtmlUtil.cleanup(comment.getText()));
		comment.setCreated(new Date());
		translatorService.updateLanguage(comment);
		comment.setCreator(userService.getCurrentUser());
		comment.setBean(bean);

		getJpaTemplate().persist(comment);
		return comment;
	}

	@Override
	public List<Comment> getComments(final long id) {
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

package org.todomap.o29.logic.facade;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.o29.beans.Rating;
import org.todomap.o29.beans.RatingSummary;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.logic.BookmarkService;
import org.todomap.o29.logic.RatingService;
import org.todomap.o29.logic.TodoService;

@Path("/todofacade/")
@Produces("application/json")
public class TodoFacade {

	TodoService todoService;
	RatingService ratingService;
	BookmarkService bookmarkService;

	@XmlRootElement(name = "todo-rel")
	public static class TodoUserRelationship {
		Todo todo;
		Rating rating;
		RatingSummary ratingSummary;
		public RatingSummary getRatingSummary() {
			return ratingSummary;
		}

		public void setRatingSummary(RatingSummary ratingSummary) {
			this.ratingSummary = ratingSummary;
		}

		boolean isBookmarked;

		public Todo getTodo() {
			return todo;
		}

		public void setTodo(Todo todo) {
			this.todo = todo;
		}

		public Rating getRating() {
			return rating;
		}

		public void setRating(Rating rating) {
			this.rating = rating;
		}

		public boolean isBookmarked() {
			return isBookmarked;
		}

		public void setBookmarked(boolean isBookmarked) {
			this.isBookmarked = isBookmarked;
		}
	}

	@GET
	@Path("/get/{id}")
	public TodoUserRelationship getTodo(@PathParam("id") final long id) {
		final TodoUserRelationship todoUserRel = new TodoUserRelationship();
		final Todo todo = todoService.getShortTodoById(id);
		todoUserRel.setTodo(todo);
		todoUserRel.setBookmarked(bookmarkService.isBookmarked(id));
		todoUserRel.setRating(ratingService.getRating(id));
		todoUserRel.setRatingSummary(ratingService.getRatingSummary(id));
		return todoUserRel;
	}

	public TodoService getTodoService() {
		return todoService;
	}

	public void setTodoService(TodoService todoService) {
		this.todoService = todoService;
	}

	public RatingService getRatingService() {
		return ratingService;
	}

	public void setRatingService(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	public BookmarkService getBookmarkService() {
		return bookmarkService;
	}

	public void setBookmarkService(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

}

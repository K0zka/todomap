package net.anzix.o29.logic;

import java.util.ArrayList;
import java.util.List;

import net.anzix.o29.beans.Coordinate;
import net.anzix.o29.beans.Todo;

import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaTodoServiceImpl extends JpaDaoSupport implements TodoService {
	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getAllTodos() {
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getTodos(double nex, double ney, double swx, double swy) {
		// TODO: Still not correct implementation, but works until I get a GIS
		// up and running.
		return getJpaTemplate()
				.find(
						"select a from "
								+ Todo.class.getName()
								+ " a "
								+ "where a.location.longitude between ? and ? and a.location.latitude between ? and ?",
						(double) nex, (double) swx, (double) ney, (double) swy);
	}

	@Override
	public void addTodo(final Todo todo) {
		getJpaTemplate().persist(todo);
	}

	@Override
	public Todo getById(long id) {
		return getJpaTemplate().find(Todo.class, id);
	}

	@Override
	public List<TodoSummary> getTodoSums(double nex, double ney, double swx,
			double swy) {
		final ArrayList<TodoSummary> ret = new ArrayList<TodoSummary>();
		for (final Object row : getJpaTemplate()
				.find(
						"select a.id, a.shortDescr, a.location from "
								+ Todo.class.getName()
								+ " a "
								+ "where a.location.longitude between ? and ? and a.location.latitude between ? and ?",
						(double) ney, (double) swy, (double) nex, (double) swx)) {
			final TodoSummary todoSummary = new TodoSummary();
			todoSummary.setId((Long) ((Object[]) row)[0]);
			todoSummary.setShortDescr((String) ((Object[]) row)[1]);
			todoSummary.setLocation((Coordinate) ((Object[]) row)[2]);
			ret.add(todoSummary);
		}
		return ret;
	}

}

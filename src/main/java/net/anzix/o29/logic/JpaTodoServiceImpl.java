package net.anzix.o29.logic;

import java.util.List;

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
		//TODO: Still not correct implementation, but works until I get a GIS up and running.
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

}

package net.anzix.o29.logic;

import java.util.List;

import net.anzix.o29.beans.Todo;
import net.anzix.o29.beans.TodoCollection;

import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaTodoServiceImpl extends JpaDaoSupport implements TodoService {
	@SuppressWarnings("unchecked")
	@Override
	public TodoCollection getAllTodos() {
		return new TodoCollection(getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getTodos(long nwx, long nwy, long sex, long sey) {
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a where a.location.longitude between ? and ? and a.location.latitude between ? and ?", (double)nwx,
				(double)nwy, (double)sex, (double)sey);
	}

	@Override
	public void addTodo(final Todo todo) {
		getJpaTemplate().persist(todo);
	}

}

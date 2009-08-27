package net.anzix.o29.beans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="todos")
public class TodoCollection {

	public TodoCollection() {
		super();
	}

	public TodoCollection(Collection<Todo> todos) {
		super();
		this.todos = todos;
	}

	private Collection<Todo> todos;

	@XmlElement(name="todo")
	public Collection<Todo> getTodos() {
		return todos;
	}

	public void setTodos(Collection<Todo> todos) {
		this.todos = todos;
	}
}

package org.todomap.o29.logic;

import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.todomap.geocoder.Address;
import org.todomap.o29.beans.Coordinate;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.logic.JpaTodoServiceImpl;
import org.todomap.o29.logic.TodoService.TodoGroup;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/o29/logic/JpaTodoServiceImplTestCtx.xml")
@DataSet()
public class JpaTodoServiceImplTest extends UnitilsJUnit4 {
	
	@SpringBean("todos")
	JpaTodoServiceImpl todoService;
	
	@SpringBean("transactionManager")
	PlatformTransactionManager transactionManager;
	
	@Test
	public void testGetAllTodos() {
		todoService.getAllTodos();
	}

	@Test
	public void testGetByLocation() {
		List<Todo> todos = todoService.getByLocation("HU", "Budapest", "Budapest");
		Assert.assertNotNull(todos);
	}
	
	@Test
	public void testAddTodo() {
		new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
			
			@Override
			public Object doInTransaction(TransactionStatus status) {
				Todo todo = new Todo();
				todo.setDescription("test test test test");
				todo.setShortDescr("test");
				Coordinate location = new Coordinate();
				location.setLatitude(123);
				location.setLongitude(123);
				todo.setLocation(location);
				todoService.addTodo(todo);

				Todo todo2 = new Todo();
				todo2.setDescription("more test");
				todo2.setShortDescr("lorem ipsum");
				Coordinate location2 = new Coordinate();
				location2.setLatitude(121);
				location2.setLongitude(121);
				todo2.setLocation(location2);
				todoService.addTodo(todo2);

				
				return null;
			}
		});
	}
	
	@Test
	public void testGetTodoGroupsFromArea() {
		new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
			
			@Override
			public Object doInTransaction(TransactionStatus status) {
				check(todoService.getTodoGroupsFromArea("country",0, 0, 100, 100));
				check(todoService.getTodoGroupsFromArea("state",0, 0, 100, 100));
				return null;
			}
			
			public void check(List<TodoGroup> groups) {
				Assert.assertNotNull(groups);
				for(TodoGroup group : groups) {
					Assert.assertNotNull(group.getAddress());
					Assert.assertTrue(group.getNrOfIssues() > 0);
				}
			}
			
		});
		
	}

	@Test
	public void testGetTodoGroup() {
		new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
			
			@Override
			public Object doInTransaction(TransactionStatus status) {
				final Address address = new Address();
				address.setCountry("HU");
				address.setState("Budapest");
				address.setTown("Budapest");
				((JpaTodoServiceImpl)todoService).getGroupByAddress(address);
				address.setTown(null);
				((JpaTodoServiceImpl)todoService).getGroupByAddress(address);
				address.setState(null);
				((JpaTodoServiceImpl)todoService).getGroupByAddress(address);
				return null;
			}
		});
		
	}

}

package net.anzix.o29.logic;

import net.anzix.o29.beans.Coordinate;
import net.anzix.o29.beans.Todo;

import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("net/anzix/o29/logic/JpaTodoServiceImplTestCtx.xml")
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
	
}

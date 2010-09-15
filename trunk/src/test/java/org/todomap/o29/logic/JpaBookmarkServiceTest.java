package org.todomap.o29.logic;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("org/todomap/o29/logic/JpaBookmarkServiceTestCtx.xml")
public class JpaBookmarkServiceTest extends UnitilsJUnit4 {
	@SpringBeanByName
	BookmarkService bookmarkService;
	
	@Test
	public void testGetNumberOfListeners() {
		bookmarkService.getNumberOfListeners(0);
	}
	
}

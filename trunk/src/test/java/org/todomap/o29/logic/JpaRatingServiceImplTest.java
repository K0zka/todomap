package org.todomap.o29.logic;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/o29/logic/JpaRatingServiceImplTestCtx.xml")
public class JpaRatingServiceImplTest extends UnitilsJUnit4 {
	
	@SpringBean("ratingService")
	RatingService ratingService;
	
	@Test
	public void testGetRatingSummary() {
		ratingService.getRatingSummary(0);
	}

}

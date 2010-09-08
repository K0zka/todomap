package org.todomap.o29.logic;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.geocoder.Address;
import org.todomap.o29.logic.StatisticsService.Area;
import org.todomap.o29.logic.StatisticsService.Totals;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("org/todomap/o29/logic/JpaStatisticsServiceTestCtx.xml")
public class JpaStatisticsServiceTest extends UnitilsJUnit4 {

	@SpringBeanByName
	JpaStatisticsService statisticsService;

	@Test
	public void testGetTotals() {
		Totals totals = statisticsService.getTotals();
		Assert.assertNotNull(totals);
	}

	@Test
	public void testGetHeatMap_state() {
		Address address = new Address();
		address.setCountry("HU");
		List<Area> heatMap = statisticsService.getHeatMap(address);
		Assert.assertNotNull(heatMap);
	}

	@Test
	public void testGetHeatMap_town() {
		Address address = new Address();
		address.setCountry("HU");
		address.setState("Baranya");
		List<Area> heatMap = statisticsService.getHeatMap(address);
		Assert.assertNotNull(heatMap);
	}

	@Test
	public void testGetHeatMap_country() {
		Address address = new Address();
		List<Area> heatMap = statisticsService.getHeatMap(address);
		Assert.assertNotNull(heatMap);
	}

}

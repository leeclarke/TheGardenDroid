package model;

import util.BaseUnitTest;
import org.junit.*;
import java.util.*;

import play.test.*;
import models.*;

public class TableReportTest extends BaseUnitTest {
	
	@Test
	public void testAddHeadersAddCells() {
		TableReport tblRpt = new TableReport();
		tblRpt.setColumnTitles("Date Created","Type","Value");
		
		assertNotNull(tblRpt.getColumnTitles());
		assertEquals(3, tblRpt.getColumnTitles().length);
		
		boolean contains = false;
		for (String val : tblRpt.getColumnTitles()) {
			if(val.equalsIgnoreCase("Type"))
				contains = true;
		}
		
		assertTrue(contains);
		
		tblRpt.addRow("2011-01-26 05:10:00.0","Plant Yield","1.75");
		tblRpt.addRow("2011-01-28 06:40:00.0","Plant Yield","2.5");
		tblRpt.addRow("2011-01-30 06:40:00.0","Plant Yield","3.0");
		
		assertTrue(tblRpt.getRows().size()>0);
		assertTrue(tblRpt.getRows().size()==3);
		
		String[] row = tblRpt.getRows().get(0);
		assertTrue(row.length == 3);
		assertEquals("Plant Yield", row[1]);
		
	}
	
	@Test
	public void testToTable() {
		TableReport tblRpt = new TableReport();
		tblRpt.setColumnTitles("Date Created","Type","Value");
		tblRpt.addRow("2011-01-26 05:10:00","Plant Yield","1.75");
		tblRpt.addRow("2011-01-28 06:40:00","Plant Yield","2.5");
		
		String tableStr = tblRpt.toTable();
		assertNotNull(tableStr);
		
		System.out.println("### TO TABLE=="+tableStr);
		
		assertTrue(tableStr.contains("<table"));
		assertTrue(tableStr.contains("</table>"));
		assertTrue(tableStr.contains("Date Created"));
		assertTrue(tableStr.contains("1.75"));

	}
	
}

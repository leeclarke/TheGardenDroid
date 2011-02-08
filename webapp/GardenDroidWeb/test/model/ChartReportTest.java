package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import models.ChartReport;
import models.ChartReport.Point;

import org.junit.Test;

import util.BaseUnitTest;

/**
 * @author leeclarke
 * Expected output patter:   {data:[{...},{...}],options:{}}
 */
public class ChartReportTest  extends BaseUnitTest {

	private final String dataSingle = "{label:\"Foo\", data:[[10.0,1.0],[17.0,-14.0],[30.0,5.0]]}"; 
	
	private final String dataOnlyOut = "{data:[{label:\"Foo\", data:[[10.0,1.0],[17.0,-14.0],[30.0,5.0]]}, " +
			"{label:\"Bar\", data:[[11.0,13.0],[19.0,11.0],[30.0,-7.0]]}],options:{grid:{color:\"#B8C569\"}}}"; 
	
	@Test
	public void testToChart() {
		ChartReport chart = new ChartReport();
		chart.label1 = "Foo";
		chart.label2 = "Bar";
		ArrayList<Point> data1 = new ArrayList<ChartReport.Point>();
		data1.add(chart.new Point(10.0, 1.0));
		data1.add(chart.new Point(17.0, -14.0));
		data1.add(chart.new Point(30.0, 5.0));
		chart.dataSet1 = data1;
		
		ArrayList<Point> data2 = new ArrayList<ChartReport.Point>();
		data2.add(chart.new Point(11.0, 13.0));
		data2.add(chart.new Point(19.0, 11.0));
		data2.add(chart.new Point(30.0, -7.0));
		chart.dataSet2 = data2;
		
		String resp = chart.toChart();
		
		assertNotNull(resp);
		assertEquals(dataOnlyOut, resp);
		
	}

	@Test
	public void testToString() {
		ChartReport chart = new ChartReport();
		chart.label1 = "Foo";
		chart.label2 = "Bar";
		ArrayList<Point> data1 = new ArrayList<ChartReport.Point>();
		data1.add(chart.new Point(new Double(10.0), new Double(1.0)));
		data1.add(chart.new Point(new Double(17.0), new Double(-14.0)));
		data1.add(chart.new Point(new Double(30.0), new Double(5.0)));
		chart.dataSet1 = data1;
		
		ArrayList<Point> data2 = new ArrayList<ChartReport.Point>();
		data2.add(chart.new Point(new Double(11.0), new Double(13.0)));
		data2.add(chart.new Point(new Double(19.0), new Double(11.0)));
		data2.add(chart.new Point(new Double(30.0), new Double(-7.0)));
		chart.dataSet2 = data2;
		
		String resp = chart.toString();
		
		assertNotNull(resp);
		assertEquals(dataOnlyOut, resp);
	}

	@Test
	public void testWriteSingleDataSet(){
		
		String label = "Foo";
		ChartReport chart = new ChartReport();
		ArrayList<Point> data1 = new ArrayList<ChartReport.Point>();
		
		data1.add(chart.new Point(new Double(10.0), new Double(1.0)));
		data1.add(chart.new Point(new Double(17.0), new Double(-14.0)));
		data1.add(chart.new Point(new Double(30.0), new Double(5.0)));
		
		String resp = chart.writeSingleDataSet(label, data1);
		assertNotNull(resp);
		assertEquals(dataSingle, resp);
	}
	
	@Test
	public void testPointToString() {
		ChartReport chart = new ChartReport(); 
		Point pt = chart.new Point(new Double(10.0), new Double(1.0));
		assertEquals(new Double(10.0), pt.xValue);
		assertEquals(new Double(1.0), pt.yValue);
		
		assertEquals("[10.0,1.0]", pt.toString());
	}
	
	
	@Test
	public void testWriteOptions_Default() {
		ChartReport chart = new ChartReport(); 
		
		assertNotNull(chart.writeOptions());
		assertEquals(",options:{grid:{color:\"#B8C569\"}}", chart.writeOptions());
		
	}
	
	@Test
	public void testWriteOptions_Mitiple(){
		String expectedOpts = ",options:{xaxis:{timeformat:\"%m/%d %h:%M\",mode:\"time\"},grid:{color:\"#B8C569\",clickable:true}}";
		ChartReport chart = new ChartReport(); 
		HashMap<String, Object> grid = new HashMap<String, Object>(); 
		grid.put("color", "#B8C569");
		grid.put("clickable", new Boolean(true));
		
		HashMap<String, String> xaxis = new HashMap<String, String>(); 
		xaxis.put("mode", "time");
		xaxis.put("timeformat", "%m/%d %h:%M");
		
		
		chart.options.put("grid", grid);
		chart.options.put("xaxis", xaxis);

		assertNotNull(chart.writeOptions());
		assertEquals(expectedOpts, chart.writeOptions());
		
	}
}

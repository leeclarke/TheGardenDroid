package models;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartReport {

	private ArrayList<ArrayList<Point>> dataSet = new ArrayList<ArrayList<Point>>();
	/**
	 * options should contain <String, Object|HashMap<String,String>> but thats not possible..so its enforced by setters.
	 */
	private HashMap<String, Object> options = new HashMap<String, Object>();
	//TODO: need to look into how necessary doing the above really is.. might want to just ass params for major settings.
	
	/**
	 * Default Constructor ensures that there are 2 dataSets which are the max amount jPlot seems to support at this time.
	 */
	public ChartReport(){
		this.dataSet.add(new ArrayList<Point>());
		this.dataSet.add(new ArrayList<Point>());
	}
	
	
	/**
	 * Generates the output HTML for displaying a chart. Same as calling toString().
	 * @return
	 */
	public String toChart() {
		StringBuilder sb = new StringBuilder("{");
		//TODO: build output.
		//TODO: Write Test!
		
		//data write out
		sb.append("\"data\":[");
		if(dataSet.get(1).size() == 0) {
			boolean isFirst = true;
			for (Point point : dataSet.get(0)) {
				if(!isFirst)
					sb.append(",");
				sb.append(point.toString());
				isFirst = false;
			}
		} else { //Multi-line
			
		}
		sb.append("]");
		//end data
		
		//Options
		if(!this.options.isEmpty())
		{
			sb.append(",\"options\":{");
			
			
			sb.append("}");
		}
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return this.toChart();
	}
	
	/**
	 * Contains the data for a single point on the Chart.
	 *
	 */
	public class Point{
		
		Double xValue;
		Double yValue;
		
		public Point(Double xValue, Double yValue){
			this.xValue = xValue;
			this.yValue = yValue;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("[");
			sb.append(this.xValue).append(",").append(this.yValue).append("]");
			return sb.toString();
		}
	}
}



/*
 * var d1 = {"data":[[1296733200000,55.4],[1296728400000,51.8],[1296723600000,42.8],[1296721200000,37.4],[1296717600000,30.2],[1296714000000,30.2],[1296702000000,35.6],[1296697200000,32.0]],"options":{xaxis: { mode: "time", timeformat: "%m/%d %h:%M"}, grid:{clickable:true}}};
 * */

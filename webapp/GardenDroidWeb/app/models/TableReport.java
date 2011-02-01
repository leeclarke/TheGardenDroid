package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for generating a Report Table result. There is a lot of HTML to generate to build a report and this will
 * allow for a much simpler and easier to read script.
 * 
 * @author leeclarke
 */
public class TableReport {

	private static final String TABLE_HEAD = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" class=\"display\" id=\"reportTable\"> <thead>";
	private static final String TABLE_END = "</tbody></table>";
	
	private String title;
	private String[] colHeads;
	private List<String[]> rows = new ArrayList<String[]>();
	
	public void setColumnTitles(String ... th) {
		colHeads = th;
	}
	
	public String[] getColumnTitles() {
		return colHeads;
	}
	
	public void addRow(String ... cells) {
		this.rows.add(cells);
	}

	public void addRow(Object ... cells) {
		
		String[] vals = new String[cells.length];
		for (int i = 0; i < cells.length; i++) {
			vals[i] = cells[i].toString();
		}
		this.rows.add(vals);
	}
	
	public List<String[]> getRows() {
		return rows;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Builds the Report Table from the data arrays provided.
	 * @return
	 */
	public String toTable() {
		StringBuilder sb = new StringBuilder();
		if(this.title != null)
			sb.append(this.title);
		sb.append(TABLE_HEAD);
		if(this.colHeads != null)
		{
			sb.append("<tr>");
			for(String th: this.colHeads) {
				sb.append("<th>").append(th).append("</th>");
			}
			sb.append("</tr>");
		}
		sb.append("</thead>");
		//Build Body
		for(String[] rowData: rows) {
			sb.append("<tr>");
			for(String cell:rowData){
				sb.append("<td>").append(cell).append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append(TABLE_END);
		return sb.toString();
	}
	
	
}

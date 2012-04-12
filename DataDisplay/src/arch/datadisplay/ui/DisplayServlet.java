package arch.datadisplay.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arch.datadisplay.retrieval.DataAggregatorFactory;
import arch.datadisplay.retrieval.IDataAggregator;
import arch.datasourceinterface.IDataItem;
import arch.datasourceinterface.IDataSource;
import arch.datasourceinterface.IDataSourceService;

public class DisplayServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 590808281763644925L;
    private static final String ANALYZED_DATA = "Analyzed Data";
    private static final String ORIGINAL_DATA = "Original Data";
    
    private static IDataSourceService dataSourceService = null;
    
    protected void setDataSourceService(IDataSourceService ds) {
    	dataSourceService = ds;
    }

    protected void unsetDataSourceService(IDataSourceService dataSourceService) {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	StringBuilder buffer = new StringBuilder(200);
        PrintWriter writer = resp.getWriter();

        renderHeader(buffer);

        List<IDataSource> list = dataSourceService.getDataSources();

        TableObject analyzedTable = new TableObject(req.getParameter("type"), DisplayServlet.ANALYZED_DATA, buffer);
        analyzedTable.addDataSources(list);
        renderTable(analyzedTable);

        TableObject originalTable = new TableObject(DataAggregatorFactory.IDENTITY, ORIGINAL_DATA, buffer);
        originalTable.addDataSources(list);
        renderTable(originalTable);

        renderFooter(buffer);
        writer.write(buffer.toString());
    }

    private void renderFooter(StringBuilder buffer) {
        buffer.append("</body></html>");
    }

    private void renderTable(TableObject tableObject) {
        renderTableHeader(tableObject.getTitle(), tableObject.getBuffer());

        IDataAggregator aggregator = DataAggregatorFactory.getInstance(tableObject.getType());
        aggregator.addDataSource(tableObject.getDataSources());

        List<IDataItem> itemList = aggregator.get();

        for (int rowIdx = 0; rowIdx < itemList.size(); rowIdx++) {
            renderTableRow(tableObject.getBuffer(), itemList, rowIdx);
        }

        renderTableFooter(tableObject.getBuffer());
    }

    private void renderTableFooter(StringBuilder buffer) {
        buffer.append("</table>");
    }

    private void renderTableRow(StringBuilder buffer, List<IDataItem> list1, int i) {
        buffer.append("<tr><th scope=\"row\" class=\"spec\">" + list1.get(i).getCompany() + "</th><td>" + list1.get(i).getArea()
                + "</td><td>" + list1.get(i).getRevenue() + "</td></tr>");
    }

    private void renderTableHeader(String title, StringBuilder buffer) {
        buffer.append("<br><div class=title>" + title + "</div><br>");
        buffer.append("<table cellspacing=\"0\"><tr><th width=\"300px\" scope=\"col\" class=\"nobg\">Company</th><th width=\"200px\" scope=\"col\">Area</th><th width=\"200px\" scope=\"col\">Revenue</th></tr>");
    }

    private void renderHeader(StringBuilder buffer) {
    	buffer.append("<html><head>");
    	buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://localhost:1234/styles.css\"/>");
    	buffer.append("</head><body>");
    	buffer.append("<img src=annual-revenues.jpg/><br>");
    }
}

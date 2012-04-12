package arch.datasourceinterface;

import java.util.ArrayList;

public class DataSourceServiceImpl implements IDataSourceService {
	private ArrayList<IDataSource> dataSourceList = new ArrayList<IDataSource>();
	
	@Override
	public void addDataSource(IDataSource dataSource) {
		dataSourceList.add(dataSource);
	}
	
	@Override
	public ArrayList<IDataSource> getDataSources() {
		return dataSourceList;
	}
}

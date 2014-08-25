package cn.edu.cust.demo.util;

import javax.sql.DataSource;

public interface ResourceConstants {
	
	public final static DataSource DS = DataSourceFactoryImpl.getDataSource();

}

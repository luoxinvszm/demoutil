package cn.edu.cust.demo.util;

import javax.sql.DataSource;

import cn.edu.cust.util.db.DataSourceFactory;

/**
 * 每个项目都需要一个这样的类（如果使用JDBC开发的话）
 * 该类的写法比较固定，可以参考本类的写法
 * @author wj
 *
 */
public class DataSourceFactoryImpl extends DataSourceFactory {
	
	private static final String JNDI = "jdbc/demo";
	
	private static DataSourceFactoryImpl factory = new DataSourceFactoryImpl();
	
	private DataSourceFactoryImpl(){
		jndi = JNDI;
	}

	public static DataSource getDataSource() {
		return factory.getJndiDataSource();
	}

}

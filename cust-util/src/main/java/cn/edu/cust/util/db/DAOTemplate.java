package cn.edu.cust.util.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public abstract class DAOTemplate<T> {
	
	//protected abstract T newObj();
	//protected JdbcTemplate jt = new JdbcTemplate();
	
	protected abstract T wrapResult(ResultSet rs) throws SQLException;
	
	protected abstract void setIdBeforeInsert(JdbcTemplate jt, T t);
	
	protected abstract Object[] getInsertParamValues(T t);
	
	protected abstract Object[] getUpdateParamValues(T t);
	
	//protected abstract void setParams4Load(PreparedStatement ps, T t) throws SQLException;
	//protected abstract void setParams4Delete(PreparedStatement ps, I t) throws SQLException;
	
	protected String insertSQL;
	protected String updateSQL;
	protected String loadSQL;
	protected String deleteSQL;
	protected String getAllSQL;
	
	public void insert(JdbcTemplate jt, T t) {
		setIdBeforeInsert(jt, t);
		jt.update(insertSQL, getInsertParamValues(t));
	}
	
	public void update(JdbcTemplate jt, T t) {
		jt.update(updateSQL, getUpdateParamValues(t));
	}
	
	
	public ArrayList<T> getAll(JdbcTemplate jt) {
		final ArrayList<T> result = new ArrayList<T>();
		jt.query(getAllSQL, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				// TODO Auto-generated method stub
				while(rs.next())
					result.add(wrapResult(rs));
				return null;
			}

		});
		return result;
	}
	
	public T load(JdbcTemplate jt, Object i) {
		return load(jt, new Object[]{i});
	}
	
	
	public T load(JdbcTemplate jt, Object[] i) {
		final Wrapper w = new Wrapper();
		jt.query(loadSQL, i, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				// TODO Auto-generated method stub
				if(rs.next())
					w.t = wrapResult(rs);
				return null;
			}

		});
		return w.t;
	}
	
	public void delete(JdbcTemplate jt, Object i){
		delete(jt, new Object[]{i});
	}
	
	public void delete(JdbcTemplate jt, Object[] i) {
		jt.update(deleteSQL, i);
	}
	
	
	private class Wrapper{
		private T t;
	}

}

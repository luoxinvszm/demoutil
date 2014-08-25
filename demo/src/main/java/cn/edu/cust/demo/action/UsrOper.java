package cn.edu.cust.demo.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.edu.cust.demo.domain.Account;
import cn.edu.cust.demo.domain.AccountDAO;
import cn.edu.cust.demo.domain.OperationDAO;
import cn.edu.cust.demo.util.ResourceConstants;
import cn.edu.cust.util.action.CommonAction;
import cn.edu.cust.util.db.Worker;

public class UsrOper extends CommonAction implements ResourceConstants {
	
	private final static Logger LOG = Logger.getLogger(UsrOper.class);
	private static AccountDAO accountDao = new AccountDAO();
	private static OperationDAO operationDao = new OperationDAO();
	
	/**
	 * 必须得写初始化方法
	 * 初始化父类的log
	 * 并调用父类的init方法
	 */
	public UsrOper(){
		log = LOG;
		init(DS, jt, tt);
	}
	
	private Account acc;
	
	private List<?> logs;
	
	public Account getAcc() {
		return acc;
	}

	public List<?> getLogs() {
		return logs;
	}

	/**
	 * 显示欢迎信息，主要作用就是查询用户信息，然后存入属性acc
	 * @return
	 */
	public String welcome(){
		HttpServletRequest req = ServletActionContext.getRequest();
		//acc = new Account();
		final String accId = req.getRemoteUser();
		
		/**
		 * 不必写数据库打开连接、关闭连接的操作了
		 * 如果需要带事务，用Spring的TransactionCallback
		 */
		return work(new Worker(){

			public void doWork() {
				// TODO Auto-generated method stub
				acc = accountDao.load(jt, accId);
			}
			
		});
	}
	
	/**
	 * 显示用户的操作记录
	 * @return
	 */
	public String logs(){
		HttpServletRequest req = ServletActionContext.getRequest();
		final String accId = req.getRemoteUser();
		return work(new Worker(){

			public void doWork() {
				// TODO Auto-generated method stub
				acc = accountDao.load(jt, accId);
				//这里使用了匿名内部类
				//主要是为了提供一个通用的查询方法，当然这里还没有考虑到分页。
				//分页功能我还得继续完善
				logs = operationDao.search(jt, "c_src_id = ? order by c_date desc limit 50", new Object[]{acc.getId()});
			}
			
		});
	}

}

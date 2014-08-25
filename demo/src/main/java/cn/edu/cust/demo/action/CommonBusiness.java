package cn.edu.cust.demo.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import cn.edu.cust.demo.domain.Account;
import cn.edu.cust.demo.domain.AccountDAO;
import cn.edu.cust.demo.domain.Operation;
import cn.edu.cust.demo.domain.OperationDAO;
import cn.edu.cust.demo.util.ResourceConstants;
import cn.edu.cust.util.action.BusinessException;
import cn.edu.cust.util.action.CommonAction;

public class CommonBusiness extends CommonAction implements ResourceConstants {
	
	private final static Logger LOG = Logger.getLogger(CommonBusiness.class);
	private static AccountDAO accountDao = new AccountDAO();
	private static OperationDAO operationDao = new OperationDAO();
	
	/**
	 * 必须得写初始化方法
	 * 初始化方法要初始化两个属性:log  ds
	 */
	public CommonBusiness(){
		log = LOG;
		init(DS, jt, tt);
	}
	
	private float money;
	
	private String desAcc;
	
	private static final int SAVE = 1;
	private static final int TAKE = 2;
	private static final int TRAN = 3;
	
	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getDesAcc() {
		return desAcc;
	}

	public void setDesAcc(String desAcc) {
		this.desAcc = desAcc;
	}

	/**
	 * 取款业务
	 * 需要注意的是action中这写业务方法的声明
	 * 返回类型是String，参数列表为空
	 * @return
	 */
	public String take(){
		//nexturl = "take_form.action";
		successmsg = "取款成功！";
		HttpServletRequest req = ServletActionContext.getRequest();
		final String accId = req.getRemoteUser();
		
		return work(new TransactionCallback<Object>(){

			/**
			 * 在该方法中不需要处理数据库事务操作了
			 * 另外该方法没有返回值，只能通过抛出异常来转到错误页面
			 */
			public Object doInTransaction(TransactionStatus arg0) {
				// TODO Auto-generated method stub
				Account acc = accountDao.load(jt, accId);
				float bal = acc.getBalance();
				//System.out.println(money);
				if(bal < money){
					throw new BusinessException("余额不足！");
				}
				//插入日志
				Operation opt = new Operation();
				opt.setSrc(acc);
				opt.setDes(acc);
				opt.setType(TAKE);
				opt.setMoney(money);
				operationDao.insert(jt, opt);
				//更新账户余额
				acc.setBalance(bal - money);
				accountDao.update(jt, acc);
				return null;
			}
			
		});
	}
	
	/**
	 * 存款业务
	 * @return
	 */
	public String save(){
		//nexturl = "save_form.action";
		successmsg = "存款成功！";
		HttpServletRequest req = ServletActionContext.getRequest();
		final String accId = req.getRemoteUser();
		return work(new TransactionCallback<Object>(){

			public Object doInTransaction(TransactionStatus arg0) {
				// TODO Auto-generated method stub
				Account acc = accountDao.load(jt, accId);
				//插入日志
				Operation opt = new Operation();
				opt.setSrc(acc);
				opt.setDes(acc);
				opt.setType(SAVE);
				opt.setMoney(money);
				operationDao.insert(jt, opt);
				//更新账户余额
				float bal = acc.getBalance();
				acc.setBalance(bal + money);
				accountDao.update(jt, acc);
				return null;
			}
			
		});
	}
	
	/**
	 * 转帐业务
	 * @return
	 */
	public String transfer(){
		//nexturl = "tran_form.action";
		successmsg="转账成功";
		HttpServletRequest req = ServletActionContext.getRequest();
		final String accId = req.getRemoteUser();
		return work(new TransactionCallback<Object>(){

			public Object doInTransaction(TransactionStatus ts) {
				// TODO Auto-generated method stub
				Account acc = accountDao.load(jt, accId);
				if(acc.getId().equals(desAcc)){
					throw new BusinessException("目标账户不能是本账户！");
				}
				float bal = acc.getBalance();
				if(bal < money){
					throw new BusinessException("余额不足！");
				}
				acc.setBalance(bal - money);
				Account des = accountDao.load(jt, desAcc);
				if(des == null){
					throw new BusinessException("对方账户不存在！");
				}
				//插入日志
				Operation opt = new Operation();
				opt.setSrc(acc);
				opt.setDes(des);
				opt.setType(TRAN);
				opt.setMoney(money);
				operationDao.insert(jt, opt);
				//更新账户余额
				bal = des.getBalance();
				des.setBalance(bal + money);
				accountDao.update(jt, acc);
				accountDao.update(jt, des);
				return null;
			}
			
		});
	}

}

package cn.edu.cust.demo.domain;

import java.util.Date;

public class Operation {
	
	private long id;
	
	private Account src;
	
	private Account des;
	
	private float money;
	
	private Date date;
	
	private int type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getSrc() {
		return src;
	}

	public void setSrc(Account src) {
		this.src = src;
	}

	public Account getDes() {
		return des;
	}

	public void setDes(Account des) {
		this.des = des;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

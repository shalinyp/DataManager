package com.dm.service.models;


/**
* <h1>CustOrder</h1>
* 
* Model class to hold Customer and Order objects.
*
* @author  Shalini Prasannakumar
* @version 1.0
* @since   2020-02-11 
*/
public class CustOrder {
	
	private Customer cust;
	private Order order;
	public Customer getCust() {
		return cust;
	}
	public void setCust(Customer cust) {
		this.cust = cust;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}

}

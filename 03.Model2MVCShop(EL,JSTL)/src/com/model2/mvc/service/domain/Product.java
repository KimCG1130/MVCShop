package com.model2.mvc.service.domain;

import java.sql.Date;


public class Product {
	
	
	private int prodNo;
	private String prodName;
	private String prodDetail;//설명
	private int price;
	private String manuDate;//manufacturing제조 날짜
	private String fileName;
	private Date regDate;
	//private String proTranCode;
	
	public Product(){
	}
	
	//public String getProTranCode() {
		//return proTranCode;
	//}
	//public void setProTranCode(String proTranCode) {
		//this.proTranCode = proTranCode;
	//}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getManuDate() {
		return manuDate;
	}
	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getProdDetail() {
		return prodDetail;
	}
	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public int getProdNo() {
		return prodNo;
	}
	public void setProdNo(int prodNo) {
		this.prodNo = prodNo;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	// Override
	public String toString() {
		
		/*return "ProductVO : [prodNo]" + prodNo
				+ "[prodDetail]" +prodDetail+ "[price]" + price + "[manuDate]" + manuDate
				+ "[prodName]" + prodName + "[fileName]" + fileName;*/
		return "ProductVO : [fileName]" + fileName
				+ "[manuDate]" + manuDate+ "[price]" + price + "[prodDetail]" + prodDetail
				+ "[prodName]" + prodName + "[prodNo]" + prodNo;
	}	
}
package com.example.testJenkins.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Entity
@Accessors(chain = true)
public class StockList {
	 
	private String code;
	private String name;
	private String category;
	private String industry;
	private String CFICode;

	public StockList(String code, String name, String category, String industry,String cfiCode) {
		this.code = code;
		this.name = name;
		this.category = category;
		this.industry = industry;
		this.CFICode = cfiCode;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getIndustry() {
		return industry;
	}

	public String getCFICode() {
		return CFICode;
	}

	public void setCFICode(String cFICode) {
		CFICode = cFICode;
	}

	public String toString() {
		return code + "\t" + name + "\t" + category + "\t" + industry;
	}
}

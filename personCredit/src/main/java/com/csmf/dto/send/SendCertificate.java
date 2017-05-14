package com.csmf.dto.send;

import java.util.Date;

/** 
 * 项目名称：personCredit
 * 包名：com.csmf.dto.send 
 * 文件名称： SendCertificate.java
 * 类的描述：简历的证书模块发送给区块链的POJO    
 * 创建时间：2017年5月2日/下午2:08:02
 */  
public class SendCertificate {
	
	/**
	 * 获证时间
	 */
	private String certificateTime;
	/**
	 * 证书名称
	 */
	private String name;
	/**
	 * 证书类型
	 */
	private String certificateType;
	
	/**
	 * 唯一标识
	 */
	private String id;
	
	public String getCertificateTime() {
		return certificateTime;
	}
	public void setCertificateTime(String certificateTime) {
		this.certificateTime = certificateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}

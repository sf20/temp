package openDemo.entity.sync.xingdou;

import javax.xml.bind.annotation.XmlElement;

public class XingdouOuInfoModel {
	private String ID;
	private String parentID;
	private String ouName;
	private String status;

	@XmlElement(name = "DEPTID")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@XmlElement(name = "PARENTID")
	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	@XmlElement(name = "DEPTNAME")
	public String getOuName() {
		return ouName;
	}

	public void setOuName(String ouName) {
		this.ouName = ouName;
	}

	@XmlElement(name = "DISABLE")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
package openDemo.entity.sync.xingdou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class XingdouResEmpData {

	private List<XingdouUserInfoModel> list;

	@XmlElement(name = "Table")
	public List<XingdouUserInfoModel> getList() {
		return list;
	}

	public void setList(List<XingdouUserInfoModel> list) {
		this.list = list;
	}

}

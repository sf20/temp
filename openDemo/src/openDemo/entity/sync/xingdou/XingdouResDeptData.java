package openDemo.entity.sync.xingdou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class XingdouResDeptData {

	private List<XingdouOuInfoModel> list;

	@XmlElement(name = "Table")
	public List<XingdouOuInfoModel> getList() {
		return list;
	}

	public void setList(List<XingdouOuInfoModel> list) {
		this.list = list;
	}

}

package openDemo.entity.sync.xingdou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class XingdouResData<T> {

	private List<T> list;

	@XmlElement(name = "Table")
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}

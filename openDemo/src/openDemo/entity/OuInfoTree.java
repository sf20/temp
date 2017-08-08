package openDemo.entity;

import java.util.ArrayList;
import java.util.List;

public class OuInfoTree {
	private OuInfoModel parentNode;
	private List<OuInfoTree> childrenNodes = new ArrayList<OuInfoTree>();

	public OuInfoTree(OuInfoModel parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * 递归添加子节点
	 * 
	 * @param ouInfoTree
	 * @param childOrg
	 */
	public void addChildren(OuInfoTree ouInfoTree, OuInfoModel childOrg) {
		OuInfoModel parent = ouInfoTree.getParentNode();
		List<OuInfoTree> childrenList = ouInfoTree.getChildrenNodes();

		if (childOrg.getParentID().equals(parent.getID())) {
			childrenList.add(new OuInfoTree(childOrg));
		} else {
			for (OuInfoTree temp : childrenList) {
				addChildren(temp, childOrg);
			}
		}
	}

	/**
	 * 将树形结构里的所有节点以集合形式返回
	 * 
	 * @param orgTree
	 * @return
	 */
	public List<OuInfoModel> treeToList(OuInfoTree orgTree) {

		return findAllOrgs(new ArrayList<OuInfoModel>(), orgTree);
	}

	/**
	 * 递归查找子节点
	 * 
	 * @param orgList
	 * @param orgTree
	 * @return
	 */
	private List<OuInfoModel> findAllOrgs(List<OuInfoModel> orgList, OuInfoTree orgTree) {
		orgList.add(orgTree.getParentNode());

		for (OuInfoTree org : orgTree.getChildrenNodes()) {
			// orgList.add(org.getParentNode());

			findAllOrgs(orgList, org);
		}

		return orgList;
	}

	public OuInfoModel getParentNode() {
		return parentNode;
	}

	public void setParentNode(OuInfoModel parentNode) {
		this.parentNode = parentNode;
	}

	public List<OuInfoTree> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<OuInfoTree> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
}

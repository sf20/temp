package openDemo.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoModel;
import openDemo.entity.OuInfoTree;
import openDemo.entity.sync.OpOuInfoModel;
import openDemo.entity.sync.OpReqJsonModle;
import openDemo.service.sync.OppleSyncService;

public class OppleSyncServiceTest {

	public static void main(String[] args) throws Exception {
		// opSyncServiceTest();

		postGetJsonTest();
	}

	static void postGetJsonTest() throws Exception {
		String query = "QueryOrgInfo";// QueryEmpInfo
		String mode = "1";
		String jsonString = OppleSyncService.getJsonPost(query, mode);
		System.out.println(jsonString);

		// printOpOuInfoModel(jsonString);
	}

	static void opSyncServiceTest() {
		Date startDate = new Date();
		System.out.println("同步中......");

		OppleSyncService opSyncService = new OppleSyncService();
		try {
			opSyncService.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	static void printOpOuInfoModel(String jsonString) throws IOException, ReflectiveOperationException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(new SimpleDateFormat("yyyyMMdd"));

		OpReqJsonModle<OpOuInfoModel> modle = new OpReqJsonModle<>();
		modle = mapper.readValue(jsonString, new TypeReference<OpReqJsonModle<OpOuInfoModel>>() {
		});

		List<OuInfoModel> newList = copyCreateEntityList(modle.getEsbResData().get("SapMiddleOrg"), OuInfoModel.class);

		removeExpiredOrgs(newList, "1");
		System.out.println(newList.size());

		OuInfoTree rootOrgTree = new OuInfoTree(getRootOrg(newList));
		for (OuInfoModel org : newList) {
			// 根组织没有父节点
			if (org.getParentID() != null) {
				// TODO to fix
				rootOrgTree.addChildren(rootOrgTree, org);
			}
		}

		List<OuInfoModel> treeList = rootOrgTree.treeToList(rootOrgTree);
		System.out.println(treeList.size());
		// for (OuInfoModel model : treeList) {
		// System.out.println(model.getID() + "==" + model.getOuName() + "==" +
		// model.getParentID());
		// }
	}

	private static long calcMinutesBetween(Date d1, Date d2) {
		return Math.abs((d2.getTime() - d1.getTime())) / 1000;
	}

	private static <E, T> List<T> copyCreateEntityList(List<E> fromList, Class<T> toListClassType)
			throws ReflectiveOperationException {
		List<T> entityList = null;

		int listSize = fromList.size();
		if (fromList != null && listSize > 0) {
			entityList = new ArrayList<T>(listSize);

			for (int i = 0; i < listSize; i++) {
				T instance = toListClassType.newInstance();
				BeanUtils.copyProperties(instance, fromList.get(i));
				entityList.add(instance);
			}
		}

		return entityList;
	}

	private static void removeExpiredOrgs(List<OuInfoModel> list, String mode) {
		for (Iterator<OuInfoModel> iterator = list.iterator(); iterator.hasNext();) {
			OuInfoModel org = iterator.next();
			// 仅全量模式下执行
			if ("1".equals(mode)) {
				if (isOrgExpired(org)) {
					iterator.remove();
					// logger.info("删除了过期组织：" + org.getOuName());
				}
			}

			// 除了编号为 00000001 之外的所有无parentcode的部门都不同步
			if (org.getParentID() == null && Integer.parseInt(org.getID()) != 1) {
				iterator.remove();
			}
		}
	}

	private static boolean isOrgExpired(OuInfoModel org) {
		Date endDate = org.getEndDate();
		if (endDate == null) {
			return true;
		}

		return endDate.compareTo(new Date()) < 0;
	}

	private static OuInfoModel getRootOrg(List<OuInfoModel> newList) {
		OuInfoModel rootOrg = null;
		for (OuInfoModel org : newList) {
			if ("1".equals(org.getID())) {
				rootOrg = org;
				break;
			}
		}
		return rootOrg;
	}
}

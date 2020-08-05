/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceRepair;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_repair.dao.ZbDeviceRepairDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 维修单Service
 * @author xuejh
 * @version 2020-05-07
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceRepairService extends CrudService<ZbDeviceRepairDao, ZbDeviceRepair> {

	@Autowired
	private ZbDeviceRepairDao repairDao;

	@Autowired
	private ZbDeviceAccountsService accountsService;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceRepair
	 * @return
	 */
	@Override
	public ZbDeviceRepair get(ZbDeviceRepair zbDeviceRepair) {
		return super.get(zbDeviceRepair);
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceRepair 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceRepair> findPage(ZbDeviceRepair zbDeviceRepair) {
		if (StringUtils.isBlank(zbDeviceRepair.getSearchInner())) {
			// 默认查询院内维修单
			zbDeviceRepair.setSearchInner(ZbDeviceRepair.REPAIR_TYPE_INNER);
		}

		List<ZbDeviceRepair> repairList = repairDao.findPage(zbDeviceRepair);
		if (CollectionUtils.isNotEmpty(repairList)) {
			for (ZbDeviceRepair deviceRepair : repairList) {
				User createUser = UserUtils.get(deviceRepair.getCreateBy());
				if (createUser != null) {
					String createDeptName = getCreateDeptName(createUser);
					if (StringUtils.isNotBlank(createDeptName)) {
						deviceRepair.setCreateDeptName(createDeptName);
					}
				}
			}
			return new Page<>(zbDeviceRepair.getPageNo(), zbDeviceRepair.getPageSize(), repairDao.findCount(zbDeviceRepair), repairList);
		}

		return new Page<>();
	}

	/**
	 * 生成维修单接口
	 * @author xuejh
	 * @create 2020/5/7 15:39
	 * @param zbDeviceRepair
	 * @return void
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceRepair zbDeviceRepair) throws Exception {
		if (zbDeviceRepair == null) {
			throw new BusinessException("生成维修单参数为空");
		}

		ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceRepair.getDeviceAccountsId());
		if (deviceAccounts == null) {
			throw new BusinessException("根据台账Id["+ zbDeviceRepair.getDeviceAccountsId() +"]查询台账未找到");
		}

		// 生成不重复的单据编号
		if (zbDeviceRepair.getIsNewRecord()) {
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceRepairBillCode(new Date(), zbDeviceRepair.getRepairType());
				ZbDeviceRepair deviceRepair = new ZbDeviceRepair();
				deviceRepair.setBillCode(billCode);

				ZbDeviceRepair existModel = super.get(deviceRepair);
				if (existModel == null) {
					zbDeviceRepair.setBillCode(billCode);
					break;
				}
			}
		}

		if (zbDeviceRepair.getRepairType().equals(ZbDeviceRepair.REPAIR_TYPE_INNER)) {
			// 院内维修单据、台账状态变更
			if (StringUtils.isBlank(zbDeviceRepair.getState())) {
				// 维修申请单审核自动生成维修单
				zbDeviceRepair.setState(ZbDeviceRepair.STATE_HANDOVER);
			} else if (zbDeviceRepair.getState().equals(ZbDeviceRepair.STATE_HANDOVER)) {
				// 交接完毕修改状态为待诊断
				zbDeviceRepair.setState(ZbDeviceRepair.STATE_REPAIR_RESULT);
			} else if (zbDeviceRepair.getState().equals(ZbDeviceRepair.STATE_REPAIR_RESULT)) {
				// 待诊断点击诊断
				if (StringUtils.isBlank(zbDeviceRepair.getRepairCode())) {
					throw new BusinessException("请选择维修人员");
				}

				if (zbDeviceRepair.getRepairResultType().equals(ZbDeviceRepair.REPAIR_RESULT_NORMAL) || zbDeviceRepair.getRepairResultType().equals(ZbDeviceRepair.REPAIR_RESULT_PUR)) {
					// 正常维修/采购器材，单据状态为维修中
					zbDeviceRepair.setState(ZbDeviceRepair.STATE_REPAIRING);
				} else {
					// 报废，单据状态为 维修完成
					zbDeviceRepair.setState(ZbDeviceRepair.STATE_REPAIR_OVER);
					// 修改台账状态为正常
					deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
					accountsService.update(deviceAccounts);
				}
			} else if (zbDeviceRepair.getState().equals(ZbDeviceRepair.STATE_REPAIRING)) {
				// 维修中点击完成，修改单据状态为维修完成，台账状态为正常
				zbDeviceRepair.setState(ZbDeviceRepair.STATE_REPAIR_OVER);
				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
				accountsService.update(deviceAccounts);
			}
		} else {
			if (StringUtils.isBlank(zbDeviceRepair.getState())) {
				zbDeviceRepair.setState(ZbDeviceRepair.STATE_OUTER_CONFIRM);
			} else {
				// 外送维修，单据维修完成，台账正常 ？？？
				zbDeviceRepair.setState(ZbDeviceRepair.STATE_REPAIR_OVER);
				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
				accountsService.update(deviceAccounts);
			}
		}

		super.save(zbDeviceRepair);

		String bizType = "zbDeviceRepair_handover_file";
		if (zbDeviceRepair.getRepairType().equals(ZbDeviceRepair.REPAIR_TYPE_OUTER)) {
			bizType = "zbDeviceRepair_outer_file";
		}

		// 保存附件
		FileUploadUtils.saveFileUpload(zbDeviceRepair.getId(), bizType);
	}
	
	/**
	 * 更新状态
	 * @param zbDeviceRepair
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceRepair zbDeviceRepair) {
		super.updateStatus(zbDeviceRepair);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceRepair
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceRepair zbDeviceRepair) {
		super.delete(zbDeviceRepair);


	}

	/**
	 * 根据用户获取用户归属单位信息
	 * @author xuejh
	 * @create 2020/5/7 14:37
	 * @param user
	 * @return java.lang.String
	 */
	private String getCreateDeptName(User user) {
		Employee employee = EmpUtils.get(user);
		if (employee != null) {
			Office office = employee.getOffice();
			Company company = employee.getCompany();
			if (office != null && company != null) {
				return company.getCompanyName() + "-" + office.getOfficeName();
			}
		}

		return null;
	}

	/**
	 * 获取页面初始化参数
	 * @author xuejh
	 * @create 2020/5/7 17:22
	 * @param zbDeviceRepair
	 * @return com.rd.modules.device.entity.ZbDeviceRepair
	 */
	public ZbDeviceRepair getPageShowData(ZbDeviceRepair zbDeviceRepair) {
		// 根据Id查询维修单详情
		zbDeviceRepair = repairDao.getRepairBillDetailById(zbDeviceRepair);

		// 获取责任人
		User createUser = UserUtils.get(zbDeviceRepair.getCreateBy());
		if (createUser != null) {
			zbDeviceRepair.setCreateByName(createUser.getUserName());
			String createDeptName = getCreateDeptName(createUser);
			if (StringUtils.isNotBlank(createDeptName)) {
				zbDeviceRepair.setCreateDeptName(createDeptName);
			}
		}

		if (StringUtils.isNotBlank(zbDeviceRepair.getRepairCode())) {
			User repairUser = UserUtils.get(zbDeviceRepair.getRepairCode());
			if (repairUser != null) {
				zbDeviceRepair.setRepairName(repairUser.getUserName());
			}
		}

		return zbDeviceRepair;
	}
}
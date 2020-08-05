/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_move.service;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceScrapMove;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_scrap_move.dao.ZbDeviceScrapMoveDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 报废处置移交单Service
 * @author xuejh
 * @version 2020-06-01
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceScrapMoveService extends CrudService<ZbDeviceScrapMoveDao, ZbDeviceScrapMove> {

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceScrapMoveDao scrapMoveDao;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceScrapMove
	 * @return
	 */
	@Override
	public ZbDeviceScrapMove get(ZbDeviceScrapMove zbDeviceScrapMove) {
		return super.get(zbDeviceScrapMove);
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceScrapMove 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceScrapMove> findPage(ZbDeviceScrapMove zbDeviceScrapMove) {
		List<ZbDeviceScrapMove> scrapMoveList = scrapMoveDao.findPage(zbDeviceScrapMove);
		if (CollectionUtils.isNotEmpty(scrapMoveList)) {
			return new Page<>(zbDeviceScrapMove.getPageNo(), zbDeviceScrapMove.getPageSize(), scrapMoveDao.findCount(zbDeviceScrapMove), scrapMoveList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceScrapMove
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceScrapMove zbDeviceScrapMove) throws Exception {
		if (zbDeviceScrapMove == null) {
			throw new BusinessException("生成报废移交单对象不能为空");
		}

		// 生成不重复的单据编号
		while (true) {
			String billCode = GenerateBillCodeUtils.getDeviceScrapMoveBillCode(new Date());
			ZbDeviceScrapMove scrapMove = new ZbDeviceScrapMove();
			scrapMove.setBillCode(billCode);

			ZbDeviceScrapMove existModel = super.get(scrapMove);
			if (existModel == null) {
				zbDeviceScrapMove.setBillCode(billCode);
				break;
			}
		}

		// 状态为待确认
		zbDeviceScrapMove.setState(ZbDeviceScrapMove.STATE_WAIT_CONFIRM);

		// 修改台账状态
		ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceScrapMove.getDeviceAccountsId());
		if (deviceAccounts == null) {
			throw new BusinessException("台账Id["+ zbDeviceScrapMove.getDeviceAccountsId() +"]信息未找到");
		}

		deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_WAIT_SCRAP);
		accountsService.update(deviceAccounts);

		// 保存处置移交单
		super.save(zbDeviceScrapMove);
	}
	
	/**
	 * 更新状态
	 * @param zbDeviceScrapMove
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceScrapMove zbDeviceScrapMove) {
		super.updateStatus(zbDeviceScrapMove);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceScrapMove
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceScrapMove zbDeviceScrapMove) {
		super.delete(zbDeviceScrapMove);
	}

	/**
	 * 处置移交单确认
	 * @author xuejh
	 * @create 2020/6/2 17:52
	 * @param zbDeviceScrapMove
	 * @return void
	 */
	@Transactional(readOnly=false)
	public void confirm(ZbDeviceScrapMove zbDeviceScrapMove) throws Exception {
		if (!zbDeviceScrapMove.getState().equals(ZbDeviceScrapMove.STATE_WAIT_CONFIRM)) {
			throw new BusinessException("状态必须是待确认");
		}

		// 修改状态确认完成
		zbDeviceScrapMove.setState(ZbDeviceScrapMove.STATE_OVER_CONFIRM);

		// 修改台账状态为已报废
		ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceScrapMove.getDeviceAccountsId());
		if (deviceAccounts == null) {
			throw new BusinessException("台账Id["+ zbDeviceScrapMove.getDeviceAccountsId() +"]信息未找到");
		}

		deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_SCRAP);
		deviceAccounts.setStatus(DataEntity.STATUS_DELETE);
		accountsService.update(deviceAccounts);

		super.update(zbDeviceScrapMove);
	}

	/**
	 * 获取完成页面初始化数据
	 * @author xuejh
	 * @create 2020/6/2 18:00
	 * @param zbDeviceScrapMove
	 * @return com.rd.modules.device.entity.ZbDeviceScrapMove
	 */
	public ZbDeviceScrapMove getPageShowData(ZbDeviceScrapMove zbDeviceScrapMove) {
		ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceScrapMove.getDeviceAccountsId());
		if (deviceAccounts != null) {
			zbDeviceScrapMove.setAccountsCode(deviceAccounts.getAccountsCode());
			zbDeviceScrapMove.setDeviceName(deviceAccounts.getDeviceName());
			zbDeviceScrapMove.setSpec(deviceAccounts.getSpec());
			zbDeviceScrapMove.setUnitType(deviceAccounts.getUnitType());

			User ownerUser = UserUtils.get(deviceAccounts.getOwnerCode());
			if (ownerUser != null) {
				zbDeviceScrapMove.setOwnerName(ownerUser.getUserName());
			}

			String deptName = DeviceUtils.getCreateDeptName(ownerUser);
			if (StringUtils.isNotBlank(deptName)) {
				zbDeviceScrapMove.setOwnerOfficeName(deptName);
			}
		}

		return zbDeviceScrapMove;
	}
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_plan.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device.entity.ZbDeviceScrapMove;
import com.rd.modules.device_scrap_move.service.ZbDeviceScrapMoveService;
import com.rd.modules.device_scrap_plan.dao.ZbDeviceScrapPlanDao;
import com.rd.modules.device_scrap_plan.dao.ZbDeviceScrapPlanItemDao;
import com.rd.modules.device.entity.ZbDeviceScrapPlan;
import com.rd.modules.device.entity.ZbDeviceScrapPlanItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.rd.modules.device.common.DeviceUtils.getCreateDeptName;

/**
 * 设备报废计划Service
 * @author xuejh
 * @version 2020-06-01
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceScrapPlanService extends CrudService<ZbDeviceScrapPlanDao, ZbDeviceScrapPlan> {

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceScrapPlanDao deviceScrapPlanDao;
	
	@Autowired
	private ZbDeviceScrapPlanItemDao zbDeviceScrapPlanItemDao;

	/**
	 * 引入报废移交服务
	 */
	@Autowired
	private ZbDeviceScrapMoveService scrapMoveService;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceScrapPlan
	 * @return
	 */
	@Override
	public ZbDeviceScrapPlan get(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		ZbDeviceScrapPlan entity = super.get(zbDeviceScrapPlan);
		if (entity != null){
			ZbDeviceScrapPlanItem zbDeviceScrapPlanItem = new ZbDeviceScrapPlanItem(entity);
			zbDeviceScrapPlanItem.setStatus(ZbDeviceScrapPlanItem.STATUS_NORMAL);
			entity.setZbDeviceScrapPlanItemList(zbDeviceScrapPlanItemDao.findList(zbDeviceScrapPlanItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceScrapPlan 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceScrapPlan> findPage(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		List<ZbDeviceScrapPlan> deviceScrapPlanList = deviceScrapPlanDao.findPage(zbDeviceScrapPlan);
		if (CollectionUtils.isNotEmpty(deviceScrapPlanList)) {
			return new Page<>(zbDeviceScrapPlan.getPageNo(), zbDeviceScrapPlan.getPageSize(), deviceScrapPlanDao.findCount(zbDeviceScrapPlan), deviceScrapPlanList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存/修改报废计划
	 * @author xuejh
	 * @create 2020/6/1 15:59
	 * @param zbDeviceScrapPlan
	 * @return void
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceScrapPlan zbDeviceScrapPlan) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isBlank(zbDeviceScrapPlan.getSubmitType())) {
			// 未审核
			zbDeviceScrapPlan.setState(ZbDeviceScrapPlan.AUDIT_STATE_N);
		} else {
			// 待提交
			zbDeviceScrapPlan.setState(ZbDeviceScrapPlan.AUDIT_STATE_WAIT);
		}

		// 校验单据明细
		checkBillItems(zbDeviceScrapPlan);

		super.save(zbDeviceScrapPlan);

		// 保存 ZbDeviceScrapPlan子表
		for (ZbDeviceScrapPlanItem zbDeviceScrapPlanItem : zbDeviceScrapPlan.getZbDeviceScrapPlanItemList()){
			if (!ZbDeviceScrapPlanItem.STATUS_DELETE.equals(zbDeviceScrapPlanItem.getStatus())){
				zbDeviceScrapPlanItem.setScrapPlanId(zbDeviceScrapPlan);
				if (zbDeviceScrapPlanItem.getIsNewRecord()){
					zbDeviceScrapPlanItemDao.insert(zbDeviceScrapPlanItem);
				}else{
					zbDeviceScrapPlanItemDao.update(zbDeviceScrapPlanItem);
				}
			}else{
				zbDeviceScrapPlanItemDao.delete(zbDeviceScrapPlanItem);
			}
		}
	}

	/**
	 * 新增/修改单据信息校验
	 * @author xuejh
	 * @create 2020/6/1 15:55
	 * @param scrapPlan
	 * @return void
	 */
	private void checkBillItems(ZbDeviceScrapPlan scrapPlan) throws Exception {
		// 获取单据明细
		List<ZbDeviceScrapPlanItem> itemList = scrapPlan.getZbDeviceScrapPlanItemList();
		if (CollectionUtils.isEmpty(itemList)) {
			throw new BusinessException("报废计划明细为空");
		}

		// 单据明细校验
		for (ZbDeviceScrapPlanItem item : itemList) {
			if (StringUtils.isBlank(item.getDeviceAccountsId())) {
				throw new BusinessException("设备台账Id不能为空");
			}

			ZbDeviceAccounts zbDeviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (zbDeviceAccounts == null) {
				throw new BusinessException("根据设备台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceScrapPlan
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		super.updateStatus(zbDeviceScrapPlan);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceScrapPlan
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		super.delete(zbDeviceScrapPlan);
		ZbDeviceScrapPlanItem zbDeviceScrapPlanItem = new ZbDeviceScrapPlanItem();
		zbDeviceScrapPlanItem.setScrapPlanId(zbDeviceScrapPlan);
		zbDeviceScrapPlanItemDao.deleteByEntity(zbDeviceScrapPlanItem);
	}

	/**
	 * 获取新增/修改页面初始化数据
	 * @author xuejh
	 * @create 2020/6/1 15:28
	 * @param zbDeviceScrapPlan
	 * @return com.rd.modules.device.entity.ZbDeviceScrapPlan
	 */
	public ZbDeviceScrapPlan getPageShowData(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		// 新增单据获取页面初始化数据
		if (zbDeviceScrapPlan.getIsNewRecord()) {
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceScrapPlanBillCode(new Date());
				ZbDeviceScrapPlan scrapPlan = new ZbDeviceScrapPlan();
				scrapPlan.setBillCode(billCode);

				ZbDeviceScrapPlan existModel = super.get(scrapPlan);
				if (existModel == null) {
					zbDeviceScrapPlan.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();

			if (loginUser != null) {
				// 初始化申请人信息
				zbDeviceScrapPlan.setCreateBy(loginUser.getUserCode());
				zbDeviceScrapPlan.setCreateByName(loginUser.getUserName());
			}

			String createDeptName = getCreateDeptName(loginUser);
			if (StringUtils.isNotBlank(createDeptName)) {
				zbDeviceScrapPlan.setDeptName(createDeptName);
			}

			// 移交单位编码
			Office office = EmpUtils.getOffice();
			if (office != null) {
				zbDeviceScrapPlan.setDeptCode(office.getOfficeCode());
			}

			zbDeviceScrapPlan.setCreateDate(new Date());
		} else {
			// 获取报废计划明细
			List<ZbDeviceScrapPlanItem> scrapPlanItems = zbDeviceScrapPlanItemDao.findListById(zbDeviceScrapPlan);
			if (CollectionUtils.isNotEmpty(scrapPlanItems)) {
				zbDeviceScrapPlan.setZbDeviceScrapPlanItemList(scrapPlanItems);
			}

			// 初始化申请人信息
			User createUser = UserUtils.get(zbDeviceScrapPlan.getCreateBy());
			if (createUser != null) {
				zbDeviceScrapPlan.setCreateByName(createUser.getUserName());
			}

			String createDeptName = getCreateDeptName(createUser);
			if (StringUtils.isNotBlank(createDeptName)) {
				zbDeviceScrapPlan.setDeptName(createDeptName);
			}
		}

		return zbDeviceScrapPlan;
	}

	/**
	 * 审核设备报废计划
	 * @author xuejh
	 * @create 2020/6/1 16:13
	 * @param scrapPlan
	 * @return void
	 */
	public void audit(ZbDeviceScrapPlan scrapPlan) throws Exception {
		if (!scrapPlan.getState().equals(ZbDeviceScrapPlan.AUDIT_STATE_N)) {
			throw new BusinessException("单据必须是未审核");
		}

		checkBillItems(scrapPlan);

		// 修改单据状态为已审核
		scrapPlan.setState(ZbDeviceScrapPlan.AUDIT_STATE_Y);

		deviceScrapPlanDao.update(scrapPlan);

		// 循环明细生成报废处置移交单
		List<ZbDeviceScrapPlanItem> items = scrapPlan.getZbDeviceScrapPlanItemList();
		for (ZbDeviceScrapPlanItem item : items) {
			// 自动生成报废处置移交单
			ZbDeviceScrapMove scrapMove = new ZbDeviceScrapMove();
			scrapMove.setDeviceAccountsId(item.getDeviceAccountsId());

			scrapMoveService.saveModel(scrapMove);
		}

	}
}
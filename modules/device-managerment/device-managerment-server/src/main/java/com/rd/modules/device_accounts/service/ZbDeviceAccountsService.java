/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_accounts.service;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.device.api.DeviceAccountsServiceApi;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDevice;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceAccountsItem;
import com.rd.modules.device.entity.ZbDevicePriceRecord;
import com.rd.modules.device_accounts.dao.ZbDeviceAccountsDao;
import com.rd.modules.device_accounts.dao.ZbDeviceAccountsItemDao;
import com.rd.modules.device_info.service.ZbDeviceService;
import com.rd.modules.device_move.dao.ZbDeviceMoveRecordDao;
import com.rd.modules.device_price_record.service.ZbDevicePriceRecordService;
import com.rd.modules.device_repair_record.dao.ZbDeviceRepairRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备登记Service
 * @author xuejh
 * @version 2020-04-15
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceAccountsService extends CrudService<ZbDeviceAccountsDao, ZbDeviceAccounts> implements DeviceAccountsServiceApi {

	@Autowired
	private ZbDeviceService deviceService;

	@Autowired
	private ZbDevicePriceRecordService priceRecordService;

	@Autowired
	private ZbDeviceAccountsDao deviceAccountsDao;

	@Autowired
	private ZbDeviceAccountsItemDao accountsItemDao;

	@Autowired
	private ZbDeviceMoveRecordDao moveRecordDao;

	@Autowired
	private ZbDeviceRepairRecordDao repairRecordDao;

	/**
	 * 获取单条数据
	 * @param zbDeviceAccounts
	 * @return
	 */
	@Override
	public ZbDeviceAccounts get(ZbDeviceAccounts zbDeviceAccounts) {
		return super.get(zbDeviceAccounts);
	}
	
	/**
	 * 根据时间筛选分页数据
	 * @author xuejh
	 * @create 2020/4/15 17:08
	 * @param zbDeviceAccounts
	 * @return com.jeesite.common.entity.Page<com.rd.modules.device_accounts.entity.ZbDeviceAccounts>
	 */
	@Override
	public Page<ZbDeviceAccounts> findPage(ZbDeviceAccounts zbDeviceAccounts) {
		List<ZbDeviceAccounts> accountsList = deviceAccountsDao.findPage(zbDeviceAccounts);

		if (CollectionUtils.isNotEmpty(accountsList)) {
			for (ZbDeviceAccounts accounts : accountsList) {
				User ownerUser = UserUtils.get(accounts.getOwnerCode());
				if (ownerUser != null) {
					String createDeptName = DeviceUtils.getCreateDeptName(ownerUser);
					accounts.setOwnerOfficeName(createDeptName);
				}
			}

			return new Page<ZbDeviceAccounts>(zbDeviceAccounts.getPageNo(), zbDeviceAccounts.getPageSize(), deviceAccountsDao.findCount(zbDeviceAccounts), accountsList);
		}

		return new Page<>();
	}

	/**
	 * 根据拥有者类型，获取对应拥有者名称
	 * @author xuejh
	 * @create 2020/4/23 15:52
	 * @param accounts
	 * @return void
	 */
	private void getOwnerName(ZbDeviceAccounts accounts) {
		if (accounts.getOwnerType().equals(ZbDeviceAccounts.OWNER_TYPE_EMP)) {
			// 个人所有，获取用户名称
			User ownerUser = UserUtils.get(accounts.getOwnerCode());
			if (ownerUser != null) {
				accounts.setOwnerName(ownerUser.getUserName());
			}
		} else {
			// 部门所有，获取部门名称
			Office office = EmpUtils.getOffice(accounts.getOwnerCode());
			if (office != null) {
				accounts.setOwnerName(office.getOfficeName());
			}
		}
	}

	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceAccounts
	 * @param optType  1：页面新增/修改操作；2：采购新增
	 */
	@Transactional(readOnly=false)
	public void saveAccounts(ZbDeviceAccounts zbDeviceAccounts, int optType, ZbDevicePriceRecord priceRecord) throws Exception {
		// 如果为采购初始化台账
		if (optType == 2) {
			// 生成设备编码
			String deviceCode = getDeviceCode();
			zbDeviceAccounts.setAccountsCode(deviceCode);

			// 校验价格记录
			checkPriceRecord(priceRecord);
		}

		// 校验台账
		checkAccounts(zbDeviceAccounts);

		if (zbDeviceAccounts.getIsNewRecord() || optType == 2) {
			// 设置台账状态 默认为正常
			zbDeviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);

			if (zbDeviceAccounts.getIsNewRecord()) {
				// 页面新增，初始化设备表
				ZbDevice device = new ZbDevice();
				device.setDeviceName(zbDeviceAccounts.getDeviceName());
				device.setUnitType(zbDeviceAccounts.getUnitType());
				device.setBrand(zbDeviceAccounts.getBrand());
				device.setSpec(zbDeviceAccounts.getSpec());

				deviceService.saveDevice(device);
			}

			// 采购则初始化设备价格
			if (optType == 2) {
				priceRecordService.save(priceRecord);
			}
		}

		// 保存台账
		super.save(zbDeviceAccounts);

		// 保存台账附件明细
		for (ZbDeviceAccountsItem item : zbDeviceAccounts.getItemList()){
			if (!ZbDeviceAccountsItem.STATUS_DELETE.equals(item.getStatus())){
				item.setDeviceAccountsId(zbDeviceAccounts);
				if (item.getIsNewRecord()){
					accountsItemDao.insert(item);
				}else{
					accountsItemDao.update(item);
				}
			}else{
				accountsItemDao.delete(item);
			}
		}

//		// 保存上传图片
//		FileUploadUtils.saveFileUpload(zbDeviceAccounts.getId(), "zbDevice_image");
//		// 保存上传附件
//		FileUploadUtils.saveFileUpload(zbDeviceAccounts.getId(), "zbDevice_file");
	}

	/**
	 * 校验台账数据
	 * @author xuejh
	 * @create 2020/5/13 14:44
	 * @param zbDeviceAccounts
	 * @return void
	 */
	private void checkAccounts(ZbDeviceAccounts zbDeviceAccounts) throws BusinessException {
		if (StringUtils.isBlank(zbDeviceAccounts.getAccountsCode())) {
			throw new BusinessException("设备编号不能为空");
		}

		if (StringUtils.isBlank(zbDeviceAccounts.getDeviceName())) {
			throw new BusinessException("设备名称不能为空");
		}

		if (StringUtils.isBlank(zbDeviceAccounts.getUnitType())) {
			throw new BusinessException("设备型号不能为空");
		}

		if (StringUtils.isBlank(zbDeviceAccounts.getSpec())) {
			throw new BusinessException("设备规格不能为空");
		}

		if (StringUtils.isBlank(zbDeviceAccounts.getOwnerOfficeCode())) {
			throw new BusinessException("归属单位不能为空");
		}

		if (StringUtils.isBlank(zbDeviceAccounts.getOwnerCode())) {
			throw new BusinessException("责任人不能为空");
		}

		if (zbDeviceAccounts.getProduceDate() == null) {
			throw new BusinessException("购入日期不能为空");
		}
	}

	/**
	 * 校验采购价格记录
	 * @author xuejh
	 * @create 2020/5/13 14:42
	 * @param priceRecord
	 * @return void
	 */
	private void checkPriceRecord(ZbDevicePriceRecord priceRecord) throws BusinessException {
		if (priceRecord == null) {
			throw new BusinessException("采购初始化台账,采购价格记录对象为空");
		}

		if (StringUtils.isBlank(priceRecord.getDeviceId())) {
			throw new BusinessException("设备Id不能为空");
		}

		if (StringUtils.isBlank(priceRecord.getBatchNumber())) {
			throw new BusinessException("采购批次号不能为空");
		}

		if (StringUtils.isBlank(priceRecord.getSupplierId())) {
			throw new BusinessException("采购供应商Id不能为空");
		}

		if (priceRecord.getPrice().compareTo(BigDecimal.ZERO) != 1) {
			throw new BusinessException("采购单价不能小于等于0");
		}

		if (priceRecord.getNum() == 0) {
			throw new BusinessException("采购数量不能为0");
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceAccounts
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceAccounts zbDeviceAccounts) {
		super.updateStatus(zbDeviceAccounts);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceAccounts
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceAccounts zbDeviceAccounts) {
		zbDeviceAccounts.setStatus(DataEntity.STATUS_DELETE);
		super.updateStatus(zbDeviceAccounts);
	}

	/**
	 * 获取新增页面初始化数据
	 * @author xuejh
	 * @create 2020/4/22 15:00
	 * @param zbDeviceAccounts
	 * @return com.rd.modules.device_accounts.entity.ZbDeviceAccounts
	 */
	public ZbDeviceAccounts getPageShowData(ZbDeviceAccounts zbDeviceAccounts) throws Exception {
		// 新增台账获取页面初始化数据
		if (zbDeviceAccounts.getIsNewRecord()) {
			// 生成设备编码
			String deviceCode = getDeviceCode();
			zbDeviceAccounts.setAccountsCode(deviceCode);
		} else {
			User ownerUser = UserUtils.get(zbDeviceAccounts.getOwnerCode());
			if (ownerUser != null) {
				zbDeviceAccounts.setOwnerName(ownerUser.getUserName());
			}

			Office office = EmpUtils.getOffice(zbDeviceAccounts.getOwnerOfficeCode());
			if (office != null) {
				zbDeviceAccounts.setOwnerOfficeName(office.getOfficeName());
			}

			// 查询明细
			List<ZbDeviceAccountsItem> itemList = accountsItemDao.findItemsById(zbDeviceAccounts);
			if (CollectionUtils.isNotEmpty(itemList)) {
				zbDeviceAccounts.setItemList(itemList);
			}
			// 查看、修改页面
			// 给拥有者名称设值
//			getOwnerName(zbDeviceAccounts);

//			// 查询设备变更记录
//			List<ZbDeviceMoveRecord> moveRecordList = moveRecordDao.findListByAccountsId(zbDeviceAccounts);
//			if (CollectionUtils.isNotEmpty(moveRecordList)) {
//				zbDeviceAccounts.setMoveRecordList(moveRecordList);
//			}
//
//			// 查询设备维修记录
//			ZbDeviceRepairRecord searchRepair = new ZbDeviceRepairRecord();
//			searchRepair.setDeviceAccountsId(zbDeviceAccounts.getId());
//			List<ZbDeviceRepairRecord> repairRecordList = repairRecordDao.findPage(searchRepair);
//			if (CollectionUtils.isNotEmpty(repairRecordList)) {
//				zbDeviceAccounts.setRepairRecordList(repairRecordList);
//			}
		}

		return zbDeviceAccounts;
	}

	/**
	 * 生成台账编码
	 * @author xuejh
	 * @create 2020/5/13 18:09
	 * @return void
	 */
	private String getDeviceCode() {
		// 生成设备编码 8位数,从一递增;
		ZbDeviceAccounts deviceAccounts = deviceAccountsDao.getLastDeviceSortCode();
		if (deviceAccounts == null) {
			// 从一开始编号
			String firstCode = String.format("%08d", 1);
			return firstCode;
		}

		// 累加1
		String accountsCode = deviceAccounts.getAccountsCode();
		int nextCode = Integer.valueOf(accountsCode) + 1;
		String newCode = String.format("%08d", nextCode);
		return newCode;
	}
}
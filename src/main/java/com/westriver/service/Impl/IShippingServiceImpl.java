package com.westriver.service.Impl;

import com.westriver.common.ServerResponse;
import com.westriver.dao.ShippingMapper;
import com.westriver.service.IShippingService;
import com.westriver.pojo.Shipping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zack on 2018/11/1.
 */
@Service
public class IShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse<String> addAddress(Shipping shipping) { //
        if (StringUtils.isEmpty(shipping.getReceiverName()) || StringUtils.isEmpty(shipping.getReceiverPhone()) || StringUtils.isEmpty(shipping.getReceiverAddress())) {
            return ServerResponse.createByErrorMessage("资料不全");
        }

        int addCount = shippingMapper.addAddress(shipping);
        if (addCount > 0) {
            return ServerResponse.createBySuccessMessage("添加成功");
        }else {
            return ServerResponse.createByErrorMessage("添加失败");
        }

    }

    public ServerResponse<String> updateAddress(Shipping shipping) {
        if (shipping.getId() == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        int updateCount = shippingMapper.selectAddressById(shipping.getId());
        if (updateCount > 0) {
            shippingMapper.updateAddress(shipping);
            return ServerResponse.createBySuccessMessage("更新成功");
        }else {
            return ServerResponse.createByErrorMessage("没有该地址");
        }
    }
}

package com.image.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.image.imagecommon.model.entity.User;
import com.image.project.annotation.AuthCheck;
import com.image.project.common.*;
import com.image.project.constant.CommonConstant;
import com.image.project.exception.BusinessException;
import com.image.project.model.dto.order.OrderInfoQueryRequest;
import com.image.project.model.dto.order.OrderInfoUpdateRequest;
import com.image.project.model.entity.OrderInfo;
import com.image.project.service.OrderInfoService;
import com.image.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/31
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {
    @Resource
    OrderInfoService orderInfoService;

    @Resource
    UserService userService;

    /**
     * 删除
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return true or false
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrderInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = orderInfoService.getById(id);
        if (oldOrderInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldOrderInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = orderInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 批量删除
     *
     * @param deleteBatchRequest ids
     * @param request            请求
     * @return true or false
     */
    @PostMapping("/deleteBatch")
    public BaseResponse<Boolean> deleteBatchOrderInfo(@RequestBody DeleteBatchRequest deleteBatchRequest, HttpServletRequest request) {
        if (deleteBatchRequest == null || deleteBatchRequest.getIds().length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        Long[] ids = deleteBatchRequest.getIds();
        // 判断是否存在
        for (Long id : ids) {
            OrderInfo orderInfo = orderInfoService.getById(id);
            if (orderInfo == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }
        }
        // 仅管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = orderInfoService.removeBatchByIds(Arrays.asList(ids));
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateOrderInfo(@RequestBody OrderInfoUpdateRequest orderInfoUpdateRequest,
                                                 HttpServletRequest request) {
        if (orderInfoUpdateRequest == null || orderInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoUpdateRequest, orderInfo);
        // 参数校验
        orderInfoService.validOrderInfo(orderInfo, false);
        User user = userService.getLoginUser(request);
        long id = orderInfoUpdateRequest.getId();
        // 判断是否存在
        OrderInfo oldOrderInfo = orderInfoService.getById(id);
        if (oldOrderInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldOrderInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = orderInfoService.updateById(orderInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据Id获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<OrderInfo> getOrderInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo orderInfo = orderInfoService.getById(id);
        return ResultUtils.success(orderInfo);
    }

    /**
     * 获取用户订单
     *
     * @param idRequest
     * @return
     */
    @GetMapping("/listByUserId")
    public BaseResponse<List<OrderInfo>> listOrderInfoByUserId(IdRequest idRequest) {
        OrderInfo orderInfo = new OrderInfo();
        if (idRequest != null) {
            orderInfo.setUserId(idRequest.getId());
        }
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>(orderInfo);
        List<OrderInfo> interfaceInfoList = orderInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 获取列表
     *
     * @param orderInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<OrderInfo>> listOrderInfo(OrderInfoQueryRequest orderInfoQueryRequest) {
        OrderInfo orderInfo = new OrderInfo();
        if (orderInfoQueryRequest != null) {
            BeanUtils.copyProperties(orderInfoQueryRequest, orderInfo);
        }
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>(orderInfo);
        List<OrderInfo> interfaceInfoList = orderInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param orderInfoQueryRequest 订单查询失败
     * @param request               请求参数
     * @return ture or false
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<OrderInfo>> listOrderInfoByPage(OrderInfoQueryRequest orderInfoQueryRequest, HttpServletRequest request) {
        if (orderInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderInfo orderInfoQuery = new OrderInfo();
        BeanUtils.copyProperties(orderInfoQueryRequest, orderInfoQuery);
        long current = orderInfoQueryRequest.getCurrent();
        long size = orderInfoQueryRequest.getPageSize();
        String sortField = orderInfoQueryRequest.getSortField();
        String sortOrder = orderInfoQueryRequest.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>(orderInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<OrderInfo> orderInfoPage = orderInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(orderInfoPage);
    }
}

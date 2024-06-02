package com.image.project.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.image.imagecommon.model.entity.InterfaceInfo;
import com.image.imagecommon.model.entity.UserInterfaceInfo;
import com.image.project.annotation.AuthCheck;
import com.image.project.common.BaseResponse;
import com.image.project.common.ErrorCode;
import com.image.project.common.ResultUtils;
import com.image.project.constant.UserConstant;
import com.image.project.exception.BusinessException;
import com.image.project.model.vo.InterfaceInfoVO;
import com.image.project.service.InterfaceInfoService;
import com.image.project.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析数据
 *
 * @author JXY
 * @version 1.0
 * @since 2024/5/28
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceIdObjMap = userInterfaceInfoList
                .stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceIdObjMap.keySet());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        if (CollectionUtil.isEmpty(interfaceInfoList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtil.copyProperties(interfaceInfo, interfaceInfoVO);
            Integer totalNum = interfaceIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).toList();
        return ResultUtils.success(interfaceInfoVOList);
    }
}

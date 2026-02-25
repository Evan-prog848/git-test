package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.WantedDTO;
import com.textbook.entity.Wanted;
import com.textbook.vo.WantedVO;

/**
 * 求购服务接口
 */
public interface WantedService extends IService<Wanted> {

    /**
     * 发布求购
     */
    Long publish(WantedDTO dto);

    /**
     * 更新求购
     */
    void update(WantedDTO dto);

    /**
     * 分页查询求购列表
     */
    Page<WantedVO> pageList(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 我的求购列表
     */
    Page<WantedVO> myWanted(Integer pageNum, Integer pageSize);

    /**
     * 关闭求购
     */
    void closeWanted(Long id);

    /**
     * 删除求购
     */
    void deleteWanted(Long id);
}

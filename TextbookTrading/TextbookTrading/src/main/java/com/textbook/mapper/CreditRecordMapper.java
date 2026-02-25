package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.CreditRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 信用记录Mapper
 */
@Mapper
public interface CreditRecordMapper extends BaseMapper<CreditRecord> {

    /**
     * 计算用户信用分总变化
     */
    @Select("SELECT COALESCE(SUM(score_change), 0) FROM credit_record WHERE user_id = #{userId} AND status = 1")
    Integer sumScoreChangeByUserId(@Param("userId") Long userId);
}

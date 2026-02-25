package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.Textbook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 教材Mapper接口
 */
@Mapper
public interface TextbookMapper extends BaseMapper<Textbook> {

    @Select("SELECT c.name as name, COUNT(t.id) as value " +
            "FROM textbook t " +
            "LEFT JOIN category c ON t.category_id = c.id " +
            "GROUP BY c.name")
    List<Map<String, Object>> selectCategoryStats();

    /**
     * 分页查询教材列表（只返回状态正常的用户发布的教材）
     *
     * @param page 分页对象
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param minCondition 最低新旧程度
     * @param orderBy 排序方式
     * @return 教材分页列表
     */
    @Select("<script>" +
            "SELECT t.* FROM textbook t " +
            "INNER JOIN user u ON t.user_id = u.id " +
            "WHERE t.status = 1 " +
            "AND t.audit_status = 1 " +
            "AND u.status = 1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (t.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR t.author LIKE CONCAT('%', #{keyword}, '%') " +
            "OR t.course LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='categoryId != null'>" +
            "AND t.category_id = #{categoryId} " +
            "</if>" +
            "<if test='campusId != null'>" +
            "AND t.campus_id = #{campusId} " +
            "</if>" +
            "<if test='courseId != null'>" +
            "AND t.course_id = #{courseId} " +
            "</if>" +
            "<if test='courseName != null and courseName != \"\"'>" +
            "AND t.course LIKE CONCAT('%', #{courseName}, '%') " +
            "</if>" +
            "<if test='minPrice != null'>" +
            "AND t.price &gt;= #{minPrice} " +
            "</if>" +
            "<if test='maxPrice != null'>" +
            "AND t.price &lt;= #{maxPrice} " +
            "</if>" +
            "<if test='minCondition != null'>" +
            "AND t.condition_level &gt;= #{minCondition} " +
            "</if>" +
            "<choose>" +
            "<when test='orderBy == \"price_asc\"'>" +
            "ORDER BY t.price ASC " +
            "</when>" +
            "<when test='orderBy == \"price_desc\"'>" +
            "ORDER BY t.price DESC " +
            "</when>" +
            "<when test='orderBy == \"views\"'>" +
            "ORDER BY t.view_count DESC " +
            "</when>" +
            "<otherwise>" +
            "ORDER BY t.create_time DESC " +
            "</otherwise>" +
            "</choose>" +
            "</script>")
    IPage<Textbook> selectTextbookPageWithUserStatus(
            Page<Textbook> page,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("campusId") Long campusId,
            @Param("courseId") Long courseId,
            @Param("courseName") String courseName,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minCondition") Integer minCondition,
            @Param("orderBy") String orderBy
    );
}

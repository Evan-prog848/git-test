package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.AuditDTO;
import com.textbook.entity.Textbook;
import com.textbook.service.TextbookService;
import com.textbook.vo.TextbookVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台教材管理控制器
 */
@Api(tags = "后台-教材管理模块")
@RestController
@RequestMapping("/api/admin/textbook")
public class AdminTextbookController {

    @Autowired
    private TextbookService textbookService;

    @ApiOperation("教材列表")
    @GetMapping("/list")
    public Result<Page<TextbookVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer auditStatus) {
        Page<TextbookVO> page = textbookService.adminPageList(pageNum, pageSize, title, categoryId, status, auditStatus);
        return Result.success(page);
    }

    @ApiOperation("审核教材")
    @PostMapping("/audit")
    public Result<Void> audit(@Valid @RequestBody AuditDTO dto) {
        textbookService.audit(dto.getId(), dto.getAuditStatus(), dto.getAuditRemark());
        return Result.success();
    }

    @ApiOperation("上架/下架教材")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        textbookService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("删除教材")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        textbookService.deleteTextbook(id);
        return Result.success();
    }
}

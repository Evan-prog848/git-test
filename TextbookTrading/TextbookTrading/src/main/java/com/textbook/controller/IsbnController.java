package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.service.IsbnService;
import com.textbook.vo.BookInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ISBN查询控制器
 */
@Api(tags = "ISBN查询模块")
@RestController
@RequestMapping("/api/isbn")
public class IsbnController {

    @Autowired
    private IsbnService isbnService;

    private final RestTemplate restTemplate = new RestTemplate();

    @ApiOperation("根据ISBN查询书籍信息")
    @GetMapping("/query")
    public Result<BookInfoVO> queryByIsbn(@RequestParam String isbn) {
        BookInfoVO bookInfo = isbnService.queryByIsbn(isbn);
        return Result.success(bookInfo);
    }

    @ApiOperation("代理获取图片（绕过防盗链）")
    @GetMapping("/proxy-image")
    public void proxyImage(@RequestParam String url, HttpServletResponse response) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            headers.set("Referer", "https://book.douban.com/");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> imageResponse = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

            if (imageResponse.getStatusCode() == HttpStatus.OK && imageResponse.getBody() != null) {
                response.setContentType("image/jpeg");
                response.setHeader("Cache-Control", "max-age=86400");
                OutputStream os = response.getOutputStream();
                os.write(imageResponse.getBody());
                os.flush();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

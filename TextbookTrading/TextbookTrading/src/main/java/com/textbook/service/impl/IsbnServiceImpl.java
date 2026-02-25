package com.textbook.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbook.service.IsbnService;
import com.textbook.vo.BookInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ISBN查询服务实现类
 * 优先使用国内API（豆瓣、中国国家图书馆等）
 */
@Service
public class IsbnServiceImpl implements IsbnService {

    private static final Logger log = LoggerFactory.getLogger(IsbnServiceImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public IsbnServiceImpl() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    // 豆瓣图书详情页API（通过ISBN直接访问）
    private static final String DOUBAN_ISBN_URL = "https://book.douban.com/isbn/%s/";
    // 备用：Open Library API
    private static final String OPEN_LIBRARY_API = "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";

    @Override
    public BookInfoVO queryByIsbn(String isbn) {
        BookInfoVO result = new BookInfoVO();
        result.setIsbn(isbn);

        String cleanIsbn = isbn.replaceAll("[^0-9X]", "");
        if (cleanIsbn.length() != 10 && cleanIsbn.length() != 13) {
            result.setSuccess(false);
            result.setErrorMessage("ISBN格式不正确，应为10位或13位");
            return result;
        }

        // 尝试从豆瓣ISBN页面获取
        try {
            BookInfoVO doubanResult = queryFromDoubanIsbn(cleanIsbn);
            if (doubanResult != null && doubanResult.getSuccess()) {
                return doubanResult;
            }
        } catch (Exception e) {
            log.warn("豆瓣ISBN查询失败: {}", e.getMessage());
        }

        // 备用：Open Library
        try {
            BookInfoVO openLibraryResult = queryFromOpenLibrary(cleanIsbn);
            if (openLibraryResult != null && openLibraryResult.getSuccess()) {
                return openLibraryResult;
            }
        } catch (Exception e) {
            log.warn("Open Library API查询失败: {}", e.getMessage());
        }

        result.setSuccess(false);
        result.setErrorMessage("未找到该ISBN对应的书籍信息，请手动输入");
        return result;
    }

    /**
     * 从豆瓣ISBN页面获取图书信息
     */
    private BookInfoVO queryFromDoubanIsbn(String isbn) {
        try {
            String url = String.format(DOUBAN_ISBN_URL, isbn);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            headers.set("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String html = response.getBody();
            
            if (html == null || html.isEmpty()) {
                return null;
            }

            BookInfoVO vo = new BookInfoVO();
            vo.setIsbn(isbn);
            vo.setSuccess(true);

            // 提取标题 - <span property="v:itemreviewed">书名</span>
            Pattern titlePattern = Pattern.compile("property=\"v:itemreviewed\">([^<]+)</span>");
            Matcher titleMatcher = titlePattern.matcher(html);
            if (titleMatcher.find()) {
                vo.setTitle(titleMatcher.group(1).trim());
            }

            // 提取封面图片
            Pattern coverPattern = Pattern.compile("src=\"(https://img[^\"]+doubanio\\.com/view/subject/[^\"]+)\"");
            Matcher coverMatcher = coverPattern.matcher(html);
            if (coverMatcher.find()) {
                vo.setCover(coverMatcher.group(1));
            }

            // 提取作者 - <span class="pl">作者</span>...<a...>作者名</a>
            Pattern authorPattern = Pattern.compile("<span class=\"pl\">\\s*作者[^<]*</span>[^<]*<a[^>]*>([^<]+)</a>");
            Matcher authorMatcher = authorPattern.matcher(html);
            if (authorMatcher.find()) {
                vo.setAuthor(authorMatcher.group(1).trim());
            }

            // 提取出版社
            Pattern publisherPattern = Pattern.compile("<span class=\"pl\">出版社:</span>\\s*<a[^>]*>([^<]+)</a>");
            Matcher publisherMatcher = publisherPattern.matcher(html);
            if (publisherMatcher.find()) {
                vo.setPublisher(publisherMatcher.group(1).trim());
            } else {
                // 备用模式：出版社可能不是链接
                Pattern publisherPattern2 = Pattern.compile("<span class=\"pl\">出版社:</span>\\s*([^<]+)<br");
                Matcher publisherMatcher2 = publisherPattern2.matcher(html);
                if (publisherMatcher2.find()) {
                    vo.setPublisher(publisherMatcher2.group(1).trim());
                }
            }

            // 提取出版年份
            Pattern datePattern = Pattern.compile("<span class=\"pl\">出版年:</span>\\s*([^<]+)<br");
            Matcher dateMatcher = datePattern.matcher(html);
            if (dateMatcher.find()) {
                vo.setPublishDate(dateMatcher.group(1).trim());
            }

            // 如果没有获取到标题，说明解析失败
            if (vo.getTitle() == null || vo.getTitle().isEmpty()) {
                return null;
            }

            return vo;
        } catch (Exception e) {
            log.error("解析豆瓣ISBN页面失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Open Library查询图书信息（备用）
     */
    private BookInfoVO queryFromOpenLibrary(String isbn) {
        try {
            String url = String.format(OPEN_LIBRARY_API, isbn);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getBody() == null || response.getBody().equals("{}")) {
                return null;
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode bookNode = root.get("ISBN:" + isbn);

            if (bookNode == null) {
                return null;
            }

            BookInfoVO vo = new BookInfoVO();
            vo.setIsbn(isbn);
            vo.setSuccess(true);

            if (bookNode.has("title")) {
                vo.setTitle(bookNode.get("title").asText());
            }

            if (bookNode.has("authors") && bookNode.get("authors").isArray()) {
                StringBuilder authors = new StringBuilder();
                for (JsonNode author : bookNode.get("authors")) {
                    if (authors.length() > 0) authors.append(", ");
                    authors.append(author.get("name").asText());
                }
                vo.setAuthor(authors.toString());
            }

            if (bookNode.has("publishers") && bookNode.get("publishers").isArray()) {
                JsonNode firstPublisher = bookNode.get("publishers").get(0);
                if (firstPublisher != null && firstPublisher.has("name")) {
                    vo.setPublisher(firstPublisher.get("name").asText());
                }
            }

            if (bookNode.has("cover") && bookNode.get("cover").has("large")) {
                vo.setCover(bookNode.get("cover").get("large").asText());
            } else if (bookNode.has("cover") && bookNode.get("cover").has("medium")) {
                vo.setCover(bookNode.get("cover").get("medium").asText());
            }

            if (bookNode.has("publish_date")) {
                vo.setPublishDate(bookNode.get("publish_date").asText());
            }

            return vo;
        } catch (Exception e) {
            log.error("解析Open Library响应失败", e);
            return null;
        }
    }
}

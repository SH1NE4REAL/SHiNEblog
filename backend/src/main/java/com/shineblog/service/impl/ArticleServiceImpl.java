package com.shineblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shineblog.entity.Article;
import com.shineblog.mapper.ArticleMapper;
import com.shineblog.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}


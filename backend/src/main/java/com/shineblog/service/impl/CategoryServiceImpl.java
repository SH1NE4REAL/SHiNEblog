package com.shineblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shineblog.entity.Category;
import com.shineblog.mapper.CategoryMapper;
import com.shineblog.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}


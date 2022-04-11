package net.xdclass.forum.service.impl;

import net.xdclass.forum.dao.CategoryDao;
import net.xdclass.forum.domain.Category;
import net.xdclass.forum.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    @Override
    public List<Category> list() {
        return categoryDao.list();
    }
}

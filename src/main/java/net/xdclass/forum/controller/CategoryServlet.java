package net.xdclass.forum.controller;

import net.xdclass.forum.domain.Category;
import net.xdclass.forum.service.CategoryService;
import net.xdclass.forum.service.impl.CategoryServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 *
 * Description: 因为继承了BaseServlet 会先执行BaseServlet相关操作
 */
@WebServlet(name="categoryServlet",urlPatterns = {"/category"})
public class CategoryServlet extends BaseServlet{
    private CategoryService categoryService = new CategoryServiceImpl();

    /**
     * 返回全部分类
     * @param request
     * @param response
     */
    public void list(HttpServletRequest request, HttpServletResponse response){
        List<Category> list = categoryService.list();
        // 暂时打印一下
        System.out.println(list.toString());
    }
}

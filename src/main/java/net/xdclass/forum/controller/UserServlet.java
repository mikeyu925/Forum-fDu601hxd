package net.xdclass.forum.controller;

import net.xdclass.forum.domain.User;
import net.xdclass.forum.service.UserService;
import net.xdclass.forum.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name="userServlet",urlPatterns = {"/user"})
public class UserServlet extends BaseServlet{

    private UserService userService = new UserServiceImpl();

    /**
     * 用户登入
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String phone = request.getParameter("phone");
        String pwd = request.getParameter("pwd");

        User user = userService.login(phone,pwd);
        if (user != null){
            request.getSession().setAttribute("loginUser",user);
            // 跳转至首页
            response.sendRedirect("/topic?method=list&c_id=1");
        }else{
            request.setAttribute("msg","用户名或密码不正确");
            request.getRequestDispatcher("/user/login.jsp").forward(request,response);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        // 页面跳转 至 登入界面
        response.sendRedirect("/topic?method=list&c_id=1");
    }

    /**
     * http://localhost:8080/user?method=register
     * 用户注册
     * @param request
     * @param response
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用户提交的是表单
        User user = new User();
        // 封装表单提交的数据 以 key:value映射到user对象
        Map<String,String[]> map =  request.getParameterMap();

        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 在Service进行处理User

        if (userService.register(user) > 0) {
            //注册成功，跳转至登录页面 TODO
            request.getRequestDispatcher("/user/login.jsp").forward(request,response);
        }else{
            //注册失败，跳转至注册页面 TODO
            request.getRequestDispatcher("/user/register.jsp").forward(request,response);
        }
    }

}

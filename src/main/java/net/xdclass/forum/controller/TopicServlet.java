package net.xdclass.forum.controller;

import net.xdclass.forum.domain.Reply;
import net.xdclass.forum.domain.Topic;
import net.xdclass.forum.domain.User;
import net.xdclass.forum.dto.PageDTO;
import net.xdclass.forum.service.CategoryService;
import net.xdclass.forum.service.TopicService;
import net.xdclass.forum.service.impl.CategoryServiceImpl;
import net.xdclass.forum.service.impl.TopicServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="topicServlet",urlPatterns = {"/topic"})
public class TopicServlet extends BaseServlet{
    /**
     * 默认分页大小
     */
    private static final int pageSize = 5;

    private TopicService topicService = new TopicServiceImpl();

    private CategoryService categoryService = new CategoryServiceImpl();

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cId = Integer.parseInt(request.getParameter("c_id"));
        // 默认第一页
        int page = 1;
        String currnetPage = request.getParameter("page");
        if (currnetPage != null && currnetPage.length() > 0){
            page = Integer.parseInt(currnetPage);
        }

        PageDTO<Topic> pageDTO = topicService.listTopicPageByCid(cId,page,pageSize);
//        System.out.println(pageDTO.toString());

        request.getSession().setAttribute("categoryList",categoryService.list());
        request.setAttribute("topicPage",pageDTO);
        //页面跳转
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }

    /**
     * http://localhost:8080/topic?method=findDetailById&topic_id=1&page=2
     * 查看主题的全部回复
     * @param request
     * @param response
     */
    public void findDetailById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取topicid
        int topicid = Integer.parseInt(request.getParameter("topic_id"));
        // 默认第一页
        int page = 1;
        String currnetPage = request.getParameter("page");
        if (currnetPage != null && currnetPage.length() > 0){
            page = Integer.parseInt(currnetPage);
        }

        // 处理浏览量 如果同一个session内只算一次
        String sessionReadKey = "is_read"+topicid;
        Boolean isRead = (Boolean) request.getSession().getAttribute(sessionReadKey);
        if (isRead == null){
            request.getSession().setAttribute(sessionReadKey,true);
            // 新增一个pv
            topicService.addOnePV(topicid);
        }

        Topic topic = topicService.findById(topicid);
        PageDTO<Reply> replyPageDTO = topicService.findReplyPageByTopicId(topicid,page,pageSize);

//        System.out.println(replyPageDTO.toString());

        request.setAttribute("topic",topic);
        request.setAttribute("replyPage",replyPageDTO);

        request.getRequestDispatcher("/topic_detail.jsp").forward(request,response);
    }

    /**
     * 添加主题
     * @param request
     * @param response
     */
    public void addTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser == null){
            request.setAttribute("msg","请先登录!");
            // 页面跳转至登录界面
            response.sendRedirect("/user/login.jsp");
            return ;
        }
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int cId = Integer.parseInt(request.getParameter("c_id"));

        int rows = topicService.addTopic(loginUser,title,content,cId);
        if (rows == 1){
            // 发布成功
            response.sendRedirect("/topic?method=list&c_id="+cId);
        }
    }

    public void replyByTopicId(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser == null){
            request.setAttribute("msg","请先登录!");
            // 页面跳转至登录界面
            response.sendRedirect("/user/login.jsp");
            return ;
        }
        int topicId = Integer.parseInt(request.getParameter("topic_id"));
        String content = request.getParameter("content");

        int rows = 0;
        rows = topicService.replyByTopicId(loginUser,topicId,content);
        if (rows == 1){
            // 回复主题成功
            response.sendRedirect("/topic?method=findDetailById&topic_id="+topicId+"&page=1");
        }
    }
}

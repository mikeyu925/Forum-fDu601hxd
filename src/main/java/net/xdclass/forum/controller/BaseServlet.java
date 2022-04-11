package net.xdclass.forum.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name="BaseServlet")
public class BaseServlet extends HttpServlet {

    /**
     * 子类的Servlet被访问，会调用service，子类里面没有重写，就会调用父类的service
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // 获取请求方法
        String method = req.getParameter("method");
        // 通过不同的方法映射到不同的Servlet
        if (method != null){
            // 利用到了反射：通过方法->找到反射的类->然后再调用
            try {
                // 获得当前被访问对象的字节码对象，和谢家吗对象里面指定的方法
                Method targetMethod =  this.getClass().getMethod(method,HttpServletRequest.class,HttpServletResponse.class);
                // 执行对应的方法
                targetMethod.invoke(this,req,resp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}

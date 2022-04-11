package net.xdclass.forum.service.impl;

import net.xdclass.forum.dao.UserDao;
import net.xdclass.forum.domain.User;
import net.xdclass.forum.service.UserService;
import net.xdclass.forum.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDao();

    @Override
    public User login(String phone, String pwd) {
        String md5pwd = CommonUtil.MD5(pwd);

        User user = userDao.findByPhoneAndPwd(phone,md5pwd);
        return user;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public int register(User user) {
        user.setRole(1);
        user.setCreateTime(new Date());
        user.setImg(getRandomImg());
        user.setPwd(CommonUtil.MD5(user.getPwd()));  // 对密码进行MD5加密
        int i = 0;
        try {
            i = userDao.save(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 放在CDN上的随机头像
     */
    private static final String [] headImg = {
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/12.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/11.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/13.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/14.jpeg",
            "https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/xdclass_pro/default/head_img/15.jpeg"
    };

    private String getRandomImg(){
        int size =  headImg.length;
        Random random = new Random();
        int index = random.nextInt(size);
        return headImg[index];
    }
}

package net.xdclass.forum.service.impl;

import net.xdclass.forum.dao.CategoryDao;
import net.xdclass.forum.dao.ReplyDao;
import net.xdclass.forum.dao.TopicDao;
import net.xdclass.forum.domain.Category;
import net.xdclass.forum.domain.Reply;
import net.xdclass.forum.domain.Topic;
import net.xdclass.forum.domain.User;
import net.xdclass.forum.dto.PageDTO;
import net.xdclass.forum.service.TopicService;

import java.util.Date;
import java.util.List;

public class TopicServiceImpl implements TopicService {
    private TopicDao topicDao = new TopicDao();
    private ReplyDao replyDao = new ReplyDao();
    private CategoryDao categoryDao = new CategoryDao();
    @Override
    public PageDTO<Topic> listTopicPageByCid(int cId, int page, int pageSize) {
        int totalRecodNum = topicDao.countTotalTopicByCid(cId);

        int from = (page - 1) * pageSize;
        // 分页查询
        List<Topic> list = topicDao.findListByCid(cId,from,pageSize);

        PageDTO<Topic> pageDTO = new PageDTO<>(page,pageSize,totalRecodNum);
        pageDTO.setList(list);

        return pageDTO;
    }
    /**
     *
     * @param topicid
     * @return
     */
    @Override
    public Topic findById(int topicid) {
        return topicDao.findById(topicid);
    }

    /**
     * 查找指定topic_id的第几页的回复
     * @param topicid
     * @param page
     * @param pageSize
     * @return
     */
    public PageDTO<Reply> findReplyPageByTopicId(int topicid, int page, int pageSize){
        // 查询总的回复数
        int totalReplyNumber = replyDao.countTotalReplyByCid(topicid);
        int from = (page - 1) * pageSize;
        // 分页查询
        List<Reply> list = replyDao.findListByTopicId(topicid,from,pageSize);

        PageDTO<Reply> replyPageDTO = new PageDTO<>(page,pageSize,totalReplyNumber);
        replyPageDTO.setList(list);

        return replyPageDTO;
    }

    /**
     * 添加主题实现
     * @param loginUser
     * @param title
     * @param content
     * @param cId
     * @return
     */
    @Override
    public int addTopic(User loginUser, String title, String content, int cId) {
        Category category = categoryDao.findById(cId);
        if (category == null){
            return 0;
        }

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setCreateTime(new Date());
        topic.setUpdateTime(new Date());
        topic.setPv(1);
        topic.setDelete(0);
        topic.setUserId(loginUser.getId());
        topic.setUsername(loginUser.getUsername());
        topic.setUserImg(loginUser.getImg());
        topic.setcId(cId);
        topic.setHot(0);

        int rows = 0;
        try {
            rows = topicDao.save(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public int replyByTopicId(User loginUser, int topicId, String content) {
        // 找到属于哪一层
        int floor = topicDao.findLatestFloorByTopicId(topicId);

        Reply reply = new Reply();
        reply.setContent(content);
        reply.setCreateTime(new Date());
        reply.setUpdateTime(new Date());
        reply.setFloor(floor+1);   // 楼层+1
        reply.setTopicId(topicId);

        reply.setUserId(loginUser.getId());
        reply.setUsername(loginUser.getUsername());
        reply.setUserImg(loginUser.getImg());
        reply.setDelete(0);

        int rows = replyDao.save(reply);

        return rows;
    }

    @Override
    public void addOnePV(int topicid) {
        Topic topic = topicDao.findById(topicid);
        int newPv = topic.getPv()+1;
        topicDao.updatePV(topicid,newPv,topic.getPv());

    }
}

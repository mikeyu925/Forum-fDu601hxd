package net.xdclass.forum.dao;

import net.xdclass.forum.domain.Topic;
import net.xdclass.forum.util.DataSourceUtil;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class TopicDao {

    private QueryRunner queryRunner = new QueryRunner(DataSourceUtil.getDataSource());

    //开启驼峰映射
    private BeanProcessor beanProcessor = new GenerousBeanProcessor();
    private RowProcessor processor = new BasicRowProcessor(beanProcessor);

    /**
     * 根据cId查询总行数
     * @param cId
     * @return
     */
    public int countTotalTopicByCid(int cId){
        // sql 中的关键字delete需要作为字段需要加上``
        String sql = "select count(*) from topic where c_id=? and `delete`=0";
        Long count = null;
        try{
            count = (Long) queryRunner.query(sql,new ScalarHandler<>(),cId);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return count.intValue();
    }

    /**
     * 分页查询
     * @param cId
     * @param from
     * @param pageSize
     * @return
     */
    public List<Topic> findListByCid(int cId,int from,int pageSize){
        String sql = "select * from topic where c_id=? and `delete`=0 order by update_time desc limit ?,?";
        List<Topic> list = null;
        try {
            list = queryRunner.query(sql, new BeanListHandler<>(Topic.class,processor),cId,from,pageSize);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return list;
    }

    /**
     *
     */
    public Topic findById(int topicid){
        String sql = "select * from topic where id=?";
        Topic topic = null;
        try {
            topic = queryRunner.query(sql,new BeanHandler<>(Topic.class,processor),topicid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return topic;
    }

    /**
     * 插入一条主题
     * @param topic
     * @return
     * @throws Exception
     */
    public int save(Topic topic) throws Exception{
        String sql = "insert into topic(c_id,title,content,pv,user_id,username,user_img,create_time,update_time,hot,`delete`) values(?,?,?,?,?,?,?,?,?,?,?)";

        Object [] params = {
                topic.getcId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getPv(),
                topic.getUserId(),
                topic.getUsername(),
                topic.getUserImg(),
                topic.getCreateTime(),
                topic.getUpdateTime(),
                topic.getHot(),
                topic.getDelete()
        };
        int i = 0;
        try{
            i= queryRunner.update(sql,params);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception();
        }
        return i;
    }

    /**
     * 通过topic_id查找最新回复的楼层
     * @param topicId
     * @return
     */
    public int findLatestFloorByTopicId(int topicId) {
        int defaultfloor = 0;
        String sql = "select floor from reply where topic_id=? order by floor desc limit 1";
        Integer floor = null;
        try {
            floor = (Integer)queryRunner.query(sql,new ScalarHandler<>(),topicId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (floor == null){
            return defaultfloor;
        }
        return floor;
    }

    /**
     * 更新浏览量
     * @param topicid
     * @param newPv
     * @param oldPv
     * @return
     */
    public int updatePV(int topicid, int newPv, int oldPv) {
        String sql = "update topic set pv=? where pv=? and id=?";
        int rows = 0;
        try {
           rows =  queryRunner.update(sql,newPv,oldPv,topicid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rows;
    }
}

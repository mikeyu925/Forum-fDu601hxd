package net.xdclass.forum.dao;

import net.xdclass.forum.domain.Reply;
import net.xdclass.forum.domain.Topic;
import net.xdclass.forum.util.DataSourceUtil;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class ReplyDao {
    private QueryRunner queryRunner = new QueryRunner(DataSourceUtil.getDataSource());

    //开启驼峰映射
    private BeanProcessor beanProcessor = new GenerousBeanProcessor();
    private RowProcessor processor = new BasicRowProcessor(beanProcessor);

    /**
     * 根据topic_id查询回复总记录数
     * @param topicid
     * @return
     */
    public int countTotalReplyByCid(int topicid){
        String sql = "select count(*) from reply where topic_id=?";
        Long count = null;
        try{
            count = (Long) queryRunner.query(sql,new ScalarHandler<>(),topicid);
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return count.intValue();
    }

    /**
     * 根据topic_id查找回复
     * @param topicid
     * @param from
     * @param pageSize
     * @return
     */
    public List<Reply> findListByTopicId(int topicid, int from, int pageSize) {
        // 此处查询结果按照 回复更新时间降序排序
        String sql = "select * from reply where topic_id=? order by update_time asc limit ?,?";
        List<Reply> list = null;
        try {
            list = queryRunner.query(sql, new BeanListHandler<>(Reply.class,processor),topicid,from,pageSize);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return list;
    }

    /**
     * 回复reply一个topic
     * @param reply
     * @return
     */
    public int save(Reply reply) {
        String sql = "insert into reply (topic_id,floor,content,user_id,username,user_img,create_time,update_time,`delete`) values (?,?,?,?,?,?,?,?,?)";
        Object [] params = {
                reply.getTopicId(),
                reply.getFloor(),
                reply.getContent(),
                reply.getUserId(),
                reply.getUsername(),
                reply.getUserImg(),
                reply.getCreateTime(),
                reply.getUpdateTime(),
                reply.getDelete()
        };
        int rows = 0;
        try{
            rows = queryRunner.update(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rows;
    }
}

package com.rongji.dfish.framework.hibernate5.plugin.lob.dao.impl;

import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.hibernate5.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * lob的hibernate实现dao层
 *
 * @author lamontYu
 * @date 2019-12-05
 * @since 5.0
 */
public class LobDao4Hibernate extends FrameworkDao4Hibernate<PubLob, String> implements LobDao {
//    @Override
//    public int saveLobData(PubLob pubLob){
//        Connection con = null;
//        PreparedStatement ps = null;
//        DataSource ds = (DataSource) SystemContext.getInstance().get("dataSource");
//        try {
//            con = ds.getConnection();
//            String sql = "INSERT INTO animal(name,age,picture) VALUES(?,?,?)";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "TheCat");
//            ps.setInt(2, 8);
//            InputStream in = new FileInputStream("J:/test1/TomCat.png");//生成被插入文件的节点流
//            //设置Blob
//            ps.setBlob(3, in);
//
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            if(ps != null){
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(con != null){
//                try {
//                    con.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    @Override
    public int updateLobData(String lobId, byte[] lobData, Date operTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.lobData=?,t.operTime=? WHERE t.lobId=?", new Object[]{lobData, operTime, lobId});
    }

    @Override
    public int archive(String lobId, String archiveFlag, Date archiveTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.archiveFlag=?,t.archiveTime=? WHERE t.lobId=?", new Object[]{archiveFlag, archiveTime, lobId});
    }

}

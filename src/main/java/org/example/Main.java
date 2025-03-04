package org.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;


/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class Main {
    private static final String ZK_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {
        // 1. 连接ZooKeeper服务端
        zk = new ZooKeeper(ZK_ADDRESS, SESSION_TIMEOUT, event -> {
            if (event.getType()== Watcher.Event.EventType.NodeDataChanged){
                System.out.println("配置已更新，重新加载配置");
            }
        });
        System.out.println("连接状态：" + zk.getState());

        // 2. 创建持久节点（需检查节点是否存在）
        String nodePath = "/demo_node/shi";
        // 检查节点是否存在
        if (zk.exists(nodePath, false) == null) {
            // 创建节点
            zk.create(nodePath, "shitforyou".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("节点创建成功");
        }

        // 3. 读取节点数据
        byte[] data = zk.getData(nodePath, true, null);
        System.out.println("节点数据：" + new String(data));

        // 4. 关闭连接
        zk.close();
    }
}
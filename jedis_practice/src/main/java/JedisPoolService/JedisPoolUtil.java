package JedisPoolService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil
{
    private static JedisPool jedisPool;

    //先用饿汉单例模式简单实现，回头再看其他的
    private static JedisPoolUtil istance = new JedisPoolUtil();

    public static JedisPoolUtil getInstance(){
        return istance;
    }

    public JedisPool getJedisPool(){
        if(jedisPool == null){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(200);
            config.setMaxIdle(50);
            config.setMaxWaitMillis(1000 * 100);
            config.setTestOnBorrow(false);
            jedisPool = new JedisPool(config, "127.0.0.1",6379);
        }
        return jedisPool;
    }



}

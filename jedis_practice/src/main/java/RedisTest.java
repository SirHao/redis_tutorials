//import JedisPoolService.JedisPoolUtil;
import JedisPoolService.JedisPoolUtil;
import Serializer.Club;
import Serializer.ProtostuffSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class RedisTest
{
    private static Jedis getJedis(){
        JedisPoolUtil jedisPoolUtil=JedisPoolUtil.getInstance();
        JedisPool pool=jedisPoolUtil.getJedisPool();
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jedis;

    }

    //序列化测试
    private static void RunSerialize(){
        ProtostuffSerializer protostuffSerializer=new ProtostuffSerializer();
        Jedis jedis=new Jedis("127.0.0.1",6379);

        String key="club:1";
        Club club=new Club(1,"MILAN","AC",3);

        byte[] clubByte = protostuffSerializer.serialize(club);
        jedis.set(key.getBytes(),clubByte);

        byte[] resultByte=jedis.get(key.getBytes());
        Club resultClub=protostuffSerializer.deserialize(resultByte);

        System.out.println(resultClub.getClubName());
    }

    //jedis连接池
    private static void RunJedisPool(){
        JedisPoolUtil jedisPoolUtil=JedisPoolUtil.getInstance();
        JedisPool pool = jedisPoolUtil.getJedisPool();

        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set("kawayi","hahaha");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(jedis!=null){
                System.out.println(jedis.get("kawayi"));
                jedis.close();
            }
        }
    }

    //pipeline测试
    private static void RunPipeline(){
        Jedis jedis=null;

        jedis=getJedis();
        Pipeline pipeline=jedis.pipelined();
        //*****无返回结果执行
        List<String> delKeys= new ArrayList<String>();
        delKeys.add("hello");
        delKeys.add("kawayi");

        for(String key:delKeys){
            pipeline.del(key);
        }
        //真正执行的地方
        try{
            pipeline.sync();
        }catch (Exception e){
            e.printStackTrace();
        }

        //*****有返回结果
        pipeline.incr("counter");
        pipeline.incrBy("counter",10);
        List<Object> resultList=new ArrayList<Object>();
        try {
            resultList=pipeline.syncAndReturnAll();
        }catch (Exception e){
            e.printStackTrace();
        }

        for(Object result:resultList){
            System.out.println(result);
        }

    }

    //LUA脚本
    private static void RunLua(){
        Jedis jedis=getJedis();
        //方法一
        String key="hello";
        jedis.set("hello","Robin");
        String scrpit="return redis.call('get',KEYS[1])";
        Object result=jedis.eval(scrpit,1,key);
        System.out.println(result);

        //方法二
        String scriptSha=jedis.scriptLoad(scrpit);
        Object result2=jedis.evalsha(scriptSha,1,key);
        System.out.println(result2);
    }

    public static void main(String[] args) {
        RunSerialize();
        RunJedisPool();
        RunPipeline();
        RunLua();
    }

}

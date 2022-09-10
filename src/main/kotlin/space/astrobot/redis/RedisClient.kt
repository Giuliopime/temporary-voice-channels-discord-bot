package space.astrobot.redis

import redis.clients.jedis.JedisPooled
import space.astrobot.Env

object RedisClient {
    private lateinit var client: JedisPooled

    fun connect() {
        client = JedisPooled(Env.Redis.host, Env.Redis.port)
    }

    fun getClient() = client
}

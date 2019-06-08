package dao

import redis.clients.jedis.Jedis
import model.User

class UserDAO {
    val jedis = Jedis("redis")

    fun findAll(): Any {
        val users = mutableListOf<User>()
        val mutUsers: MutableSet<String> = jedis.keys("*")
        for (mutUser in mutUsers) {
            val k = mutUser
            val v = jedis.get(k).toInt()
            users.add(User(name = k, downloads = v))
        }
        return users
    }

    fun findByName(name: String): User {
        val downloads = jedis.get(name).toInt()
        val user = User(name = name, downloads = downloads)
        return user
    }

}
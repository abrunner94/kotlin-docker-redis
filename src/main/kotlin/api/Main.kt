package api

import dao.UserDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import redis.clients.jedis.Jedis
import java.lang.Exception

fun main(args: Array<String>) {
    val userDAO = UserDAO()

    val app = Javalin.create().apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("Not found") }
    }.start(7000)

    app.routes {
        get("/users") { ctx ->
            ctx.json(userDAO.findAll())
        }
        get("/user/:name") { ctx ->
            ctx.json(userDAO.findByName(ctx.pathParam("name")))
        }
    }

}
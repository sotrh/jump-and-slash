package sotrh.libgdx.jumpandslash

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import sotrh.libgdx.jumpandslash.box2d.*

class JumpAndSlashGame : ApplicationAdapter() {

    lateinit var world: World
    lateinit var camera: OrthographicCamera
    lateinit var debugRenderer: Box2DDebugRenderer

    var accumulator = 0f
    val scaledWidth: Float get() { return Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat() / PIXELS_PER_METER * Gdx.graphics.width }
    val scaledHeight: Float get() { return Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat() / PIXELS_PER_METER * Gdx.graphics.height }

    override fun create() {
        world = World(Vector2(0f, -10f), true)

        debugRenderer = Box2DDebugRenderer()

        camera = OrthographicCamera()

        val body = world.createBody {
            it.type = BodyDef.BodyType.DynamicBody
            it.position.set(scaledWidth / 2f, scaledHeight / 2f)
        }

        val fixture = body.createBoxFixture(0.5f, 0.5f)

        val ground = world.createBody {
            it.position.set(scaledWidth / 2, 0.1f)
        }.apply {
            createBoxFixture(scaledWidth, 0.2f) { }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        val aspectRatio = width / height.toFloat()
        val scale = aspectRatio / PIXELS_PER_METER
        camera.setToOrtho(false, width * scale, height * scale)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        debugRenderer.render(world, camera.combined)

        doPhysicsStep(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        world.dispose()
        debugRenderer.dispose()
    }

    private fun doPhysicsStep(deltaTime: Float) {
        val frameTime = Math.min(deltaTime, 0.25f)
        accumulator += frameTime

        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION)
            accumulator -= TIME_STEP
        }
    }
}

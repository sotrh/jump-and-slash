package sotrh.libgdx.jumpandslash.box2d

import com.badlogic.gdx.physics.box2d.*

/**
 * Created by benjamin on 3/4/17
 */

fun World.createBody(block: (BodyDef)->Unit): Body {
    val bodyDef = BodyDef()
    block(bodyDef)
    return createBody(bodyDef)
}

fun Body.createCircleFixture(radius: Float, block: (FixtureDef)->Unit): Fixture {
    val circle = CircleShape()
    circle.radius = radius

    val fixtureDef = FixtureDef()
    fixtureDef.shape = circle
    block(fixtureDef)

    val fixture = createFixture(fixtureDef)

    circle.dispose()

    return fixture
}

fun Body.createBoxFixture(width: Float, height: Float, block: ((FixtureDef) -> Unit)? = null): Fixture {
    val box = PolygonShape()
    box.setAsBox(width, height)

    val fixtureDef = FixtureDef()
    fixtureDef.shape = box
    block?.invoke(fixtureDef)

    val fixture = createFixture(fixtureDef)

    box.dispose()

    return fixture
}
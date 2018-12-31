package main.java.tools;

import main.java.creature.Creature;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.CLASS)
public @interface AtomicOperation {
    GameLogger.AtomicOptType type() default GameLogger.AtomicOptType.MOVE;
    Class<? extends Creature> operatorType() default Creature.class;
}

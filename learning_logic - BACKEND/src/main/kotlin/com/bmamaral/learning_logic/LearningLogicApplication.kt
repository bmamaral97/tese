package com.bmamaral.learning_logic

import com.bmamaral.learning_logic.model.UserPasswordDTO
import com.bmamaral.learning_logic.services.app.ExercisesService
import com.bmamaral.learning_logic.services.app.ScenariosService
import com.bmamaral.learning_logic.services.app.UsersService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class LearningLogicApplication {
    @Bean
    fun init(
        users: UsersService,
        scenarios: ScenariosService,
        exercises: ExercisesService
    ) = CommandLineRunner {
        // ADD DEFAULT USERS
        if (!users.getUserOptional("admin").isPresent)
            users.createUser(UserPasswordDTO("admin", "password"), "ADMIN")
        if (!users.getUserOptional("instructor").isPresent)
            users.createUser(UserPasswordDTO("instructor", "password"), "INSTRUCTOR")
        // ADD DEFAULT SCENARIOS
        if (!scenarios.getScenarioOptional("SC1").isPresent)
            scenarios.addScenarioCode(
                "SC1/R,S,B,C,U/It is raining., The sun is shining., A rainbow can be seen, It is cloudy., Max is carrying an umbrella.",
                "instructor"
            )
        // ADD EXERCISE TO DEFAULT SCENARIO
        if (!exercises.getExerciseOptional(4L).isPresent)
            exercises.addExerciseByCode("PL1", "SC1/Max carries an umbrella if and only if it rains./R&U", "instructor")
        if (!exercises.getExerciseOptional(5L).isPresent)
            exercises.addExerciseByCode("PL2", "(~(B&C))|(A<=>~B)/0", "instructor")
        if (!exercises.getExerciseOptional(6L).isPresent)
            exercises.addExerciseByCode("PL3", "(R=>S)=>(~B&S)/DNF/1", "instructor")

    }
}

fun main(args: Array<String>) {
    runApplication<LearningLogicApplication>(*args)
}
package com.bmamaral.learning_logic.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "USERS")
open class UserDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) open val id: Long,
    open val username: String,
    open var password: String,
    @ElementCollection(fetch = FetchType.EAGER) open val authorities: Set<String>
)

@Entity
data class StudentDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val username: String,
    override var password: String,
    @ElementCollection override val authorities: Set<String>,
    @OneToMany val completed: MutableSet<ExerciseDAO>
) : UserDAO(id, username, password, authorities) {
    constructor(data: UserPasswordDTO) : this(0, data.username, data.password, setOf("STUDENT"), mutableSetOf())
    constructor(data: UserPasswordDTO, authorities: Collection<GrantedAuthority>) : this(
        0,
        data.username,
        data.password,
        buildSet { authorities.map { add(it.authority) } },
        mutableSetOf()
    )

    fun completeExercise(exercise: ExerciseDAO) {
        this.completed.add(exercise)
    }
}

@Entity
data class InstructorDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val username: String,
    override var password: String,
    @ElementCollection override val authorities: Set<String>,
    @OneToMany val createdExercises: MutableList<ExerciseDAO>,
    @OneToMany val createdScenarios: MutableList<ScenarioDAO>
) : UserDAO(id, username, password, authorities) {
    constructor(data: UserPasswordDTO) : this(
        0, data.username, data.password, setOf("INSTRUCTOR"), mutableListOf(), mutableListOf()
    )

    fun addCreatedExercise(exercise: ExerciseDAO) {
        this.createdExercises.add(exercise)
    }

    fun addCreatedScenario(scenario: ScenarioDAO) {
        this.createdScenarios.add(scenario)
    }
}

@Entity
data class AdminDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val username: String,
    override var password: String,
    @ElementCollection override val authorities: Set<String>
) : UserDAO(id, username, password, authorities) {
    constructor(data: UserPasswordDTO) : this(0, data.username, data.password, setOf("ADMIN"))
}

data class UserSecurityDAO(@Id val username: String = "", var password: String = "")

@Entity
@Table(name = "SCENARIOS")
data class ScenarioDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    val name: String,
    @ManyToOne val creator: UserDAO,
    @ElementCollection val map: Map<String, String>, //key: psymbol, value: phrase
    @OneToMany(
        cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "scenario", orphanRemoval = true
    ) var exercises: MutableList<PL1DAO>
) {
    constructor(name: String, creator: UserDAO, map: Map<String, String>) : this(
        0, name, creator, map, mutableListOf()
    )

    fun updateExercises(newList: MutableList<PL1DAO>) {
        this.exercises = newList
    }

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, name=$name, creator=${creator.username}, map=$map, exercises=${exercises.map { it.id }})"
    }
}

@Entity
@Table(name = "EXERCISES")
open class ExerciseDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) open val id: Long,
    open val type: String,
    @ManyToOne open val creator: UserDAO,
    open val formula: String,
)

@Entity
data class PL1DAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val type: String,
    @ManyToOne override val creator: UserDAO,
    override val formula: String,
    @ManyToOne(fetch = FetchType.LAZY) @JsonIgnoreProperties(value = ["exercises"]) @JoinColumn(name = "scenario_id") val scenario: ScenarioDAO,
    val phrase: String
) : ExerciseDAO(id, type, creator, formula) {
    constructor(creator: UserDAO, scenario: ScenarioDAO, formula: String, phrase: String) : this(
        0, "PL1", creator, formula, scenario, phrase
    )
}

@Entity
data class PL2DAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val type: String,
    @ManyToOne override val creator: UserDAO,
    override val formula: String,
    val complexity: Int
) : ExerciseDAO(id, type, creator, formula) {
    constructor(creator: UserDAO, formula: String, complexity: Int) : this(
        0, "PL2", creator, formula, complexity
    )
}

@Entity
data class PL3DAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) override val id: Long,
    override val type: String,
    @ManyToOne override val creator: UserDAO,
    override val formula: String,
    val normalForm: String,
    val complexity: Int,
    val endFormula: String,
) : ExerciseDAO(id, type, creator, formula) {
    constructor(creator: UserDAO, formula: String, normalForm: String, endFormula: String) : this(
        0, "PL3", creator, formula, normalForm, 0, endFormula
    )

    constructor(creator: UserDAO, formula: String, normalForm: String) : this(
        0, "PL3", creator, formula, normalForm, 1, ""
    )

    @Override
    override fun toString(): String {
        return if (complexity == 0) {
            this::class.simpleName + "(id=$id, type=$type, creator=${creator.username}, formula=$formula, normalForm=$normalForm, complexity=0, endFormula=$endFormula)"
        } else {
            this::class.simpleName + "(id=$id, type=$type, creator=${creator.username}, formula=$formula, normalForm=$normalForm, complexity=1)"
        }
    }
}

/*
@Entity TODO(pl4 implementation)
data class PL4DAO(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long)
*/

@Entity
data class SubmissionDAO(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    @OneToOne val user: UserDAO,
    @OneToOne val exercise: ExerciseDAO,
    val grade: Int,
    val submittedAt: Date,
)
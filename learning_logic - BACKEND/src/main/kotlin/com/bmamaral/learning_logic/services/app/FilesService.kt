package com.bmamaral.learning_logic.services.app

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.file.Paths

@Service
class FilesService(
    private val scenariosService: ScenariosService,
    private val exercisesService: ExercisesService,
) {
    fun receiveFile(file: MultipartFile, instructor: String) {
        try {
            val path = Paths.get("src/main/resources/data/data.txt").toAbsolutePath().toString()
            val fileTemp = File(path)
            file.transferTo(fileTemp)
            loadInfo(fileTemp, instructor)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Error("Error loading file...")
        }
    }

    fun loadInfo(file: File, instructor: String) {
        loadInfoFromFileToMap(file).forEach { entry ->
            when (entry.key) {
                "SCENARIO" -> {
                    for (code in entry.value) scenariosService.addScenarioCode(code, instructor)
                }
                "PL1" -> {
                    for (code in entry.value) exercisesService.addExerciseByCode("PL1", code, instructor)
                }
                "PL2" -> {
                    for (code in entry.value) exercisesService.addExerciseByCode("PL2", code, instructor)
                }
                "PL3" -> {
                    for (code in entry.value) exercisesService.addExerciseByCode("PL3", code, instructor)
                }
                /* "PL4" -> {
                    for (code in entry.value) exercisesService.addExerciseByCode("PL4", code, instructor)
                } */
            }
        }
    }

    private fun loadInfoFromFileToMap(file: File): Map<String, MutableList<String>> {
        val mutableMap: MutableMap<String, MutableList<String>> = mutableMapOf()
        try {
            BufferedReader(FileReader(file.path)).use { br ->
                var type = ""
                br.lines().use { lines ->
                    for (it in lines) {
                        if (it.isEmpty()) continue
                        if (it.first() == '=') {
                            type = it.drop(1)
                            mutableMap[type] = mutableListOf()
                        } else {
                            if (isTypeValid(type)) {
                                mutableMap[type]!!.add(it)
                            } else throw PreconditionFailedException("Invalid type for information loading!: $type")
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableMap.toMap()
    }

    private fun isTypeValid(type: String): Boolean {
        val validTypes = listOf("SCENARIO", "PL1", "PL2", "PL3", "PL4")
        var isValid = true

        if (!validTypes.contains(type)) isValid = false

        return isValid
    }

}
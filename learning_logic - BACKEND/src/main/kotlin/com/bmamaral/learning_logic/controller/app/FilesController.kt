package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.controller.app.api.FilesAPI
import com.bmamaral.learning_logic.services.app.FilesService
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FilesController(val files: FilesService) : FilesAPI {

    override fun loadInformation(
        @RequestPart("file") file: MultipartFile, @RequestPart("instructor") instructor: String
    ) = handle4xx { files.receiveFile(file, instructor) }

}
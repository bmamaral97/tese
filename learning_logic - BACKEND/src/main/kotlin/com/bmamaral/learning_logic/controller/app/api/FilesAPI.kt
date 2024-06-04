package com.bmamaral.learning_logic.controller.app.api

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Api(
    value = "LEARNING LOGIC - File Upload Controller",
    description = "OPERATIONS RELATED TO FILE UPLOADING TO THE SYSTEM"
)

@RequestMapping("api/files")
interface FilesAPI {

    @Operation(summary = "Upload text file and load information")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201", description = "Uploaded file information", content = [Content()]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        )]
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun loadInformation(@RequestPart("file") file: MultipartFile, @RequestPart("instructor") instructor: String)

}
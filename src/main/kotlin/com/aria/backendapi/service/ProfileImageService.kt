package com.aria.backendapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ProfileImageService {

    @Value("\${upload.directory:uploads/profile_pictures}")
    private lateinit var uploadDir: String

    fun saveProfileImage(file: MultipartFile): String {
        val folder = Paths.get(uploadDir)
        if (!Files.exists(folder)) Files.createDirectories(folder)

        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        val path = folder.resolve(fileName)

        file.transferTo(path.toFile())
        return fileName
    }

    fun loadImage(filename: String): Resource {
        val path: Path = Paths.get(uploadDir).resolve(filename)
        return try {
            UrlResource(path.toUri()).apply {
                if (!exists() || !isReadable) throw RuntimeException("No se puede leer el archivo.")
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException("Error al cargar la imagen", e)
        }
    }
}
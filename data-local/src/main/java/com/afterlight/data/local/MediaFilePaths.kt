package com.afterlight.data.local

import java.io.File

object MediaFilePaths {
    fun encryptedFile(filesDir: File, partyId: String, mediaId: String): File {
        return File(filesDir, "parties/$partyId/$mediaId.enc")
    }
}

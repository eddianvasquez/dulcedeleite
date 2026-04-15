package edu.ucne.dulcedeleite.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.dulcedeleite.data.remote.dto.UploadResponseDto
import edu.ucne.dulcedeleite.domain.repository.ProductoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import edu.ucne.dulcedeleite.utils.toMultipartBodyPart
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val repository: ProductoRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(uriString: String): Resource<UploadResponseDto> {
        return try {
            val uri = Uri.parse(uriString)
            val multipartBodyPart = uri.toMultipartBodyPart(context)
            if (multipartBodyPart != null) {
                repository.uploadImage(multipartBodyPart)
            } else {
                Resource.Error("No se pudo procesar la imagen seleccionada.")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al procesar la imagen.")
        }
    }
}

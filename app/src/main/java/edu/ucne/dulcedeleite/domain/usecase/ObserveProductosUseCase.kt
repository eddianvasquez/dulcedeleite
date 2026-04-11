package edu.ucne.dulcedeleite.domain.usecase

import edu.ucne.dulcedeleite.domain.repository.ProductoRepository
import javax.inject.Inject

class ObserveProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke() = repository.observeProductos()
}
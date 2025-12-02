package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class Pedido(
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuario")
    val usuario: Int,

    @SerializedName("total")
    val total: Double,

    @SerializedName("fecha_pedido")
    val fechaPedido: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("metodo_pago")
    val metodoPago: String? = null,

    @SerializedName("detalles")
    val detalles: List<DetallePedido>? = null
) {
    val totalFormateado: String
        get() = "S/ %.2f".format(total)

    val estadoTexto: String
        get() = when (estado) {
            "pendiente" -> "Pendiente"
            "en_proceso" -> "En Proceso"
            "enviado" -> "Enviado"
            "entregado" -> "Entregado"
            "cancelado" -> "Cancelado"
            else -> estado
        }
}

data class DetallePedido(
    @SerializedName("id")
    val id: Int,

    @SerializedName("pedido")
    val pedido: Int,

    @SerializedName("producto")
    val producto: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    @SerializedName("producto_nombre")
    val productoNombre: String? = null,

    @SerializedName("producto_imagen")
    val productoImagen: String? = null
) {
    val subtotal: Double
        get() = cantidad * precioUnitario

    val subtotalFormateado: String
        get() = "S/ %.2f".format(subtotal)
}

data class CrearPedidoRequest(
    @SerializedName("usuario")
    val usuario: Int,

    @SerializedName("total")
    val total: Double,

    @SerializedName("metodo_pago")
    val metodoPago: String,

    @SerializedName("detalles")
    val detalles: List<DetallePedidoRequest>
)

data class DetallePedidoRequest(
    @SerializedName("producto")
    val producto: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double
)

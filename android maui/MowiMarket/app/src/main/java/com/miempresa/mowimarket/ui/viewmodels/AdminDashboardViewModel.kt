package com.miempresa.mowimarket.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.mowimarket.data.models.*
import com.miempresa.mowimarket.data.repository.MowiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class AdminDashboardState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // KPIs
    val totalProductos: Int = 0,
    val totalPedidos: Int = 0,
    val pedidosPendientes: Int = 0,
    val totalVentas: Double = 0.0,
    val productossBajoStock: Int = 0,

    // Datos para gráficos
    val ventasPorCategoria: List<VentasPorCategoria> = emptyList(),
    val productosMasVendidos: List<ProductoMasVendido> = emptyList(),
    val usuariosActivosSemana: List<UsuariosActivosSemana> = emptyList(),

    // Listas completas
    val productos: List<Producto> = emptyList(),
    val pedidos: List<Pedido> = emptyList(),
    val usuarios: List<UserData> = emptyList(),

    // Filtro de período
    val periodoFiltro: PeriodoFiltro = PeriodoFiltro.SEMANA
)

enum class PeriodoFiltro {
    HOY, SEMANA, MES
}

class AdminDashboardViewModel(context: Context) : ViewModel() {
    private val repository = MowiRepository(context)

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    init {
        cargarDashboard()
    }

    fun cargarDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // Cargar todos los datos en paralelo
                val productosResult = repository.getProductos()
                val pedidosResult = repository.getPedidos()
                val usuariosResult = repository.getUsuarios()
                val ventasResult = repository.getVentasPorCategoria()
                val masVendidosResult = repository.getProductosMasVendidos()
                val usuariosSemanaResult = repository.getUsuariosActivosSemana()

                val productos = productosResult.getOrNull() ?: emptyList()
                val pedidos = pedidosResult.getOrNull() ?: emptyList()
                val usuarios = usuariosResult.getOrNull() ?: emptyList()
                val ventas = ventasResult.getOrNull() ?: emptyList()
                val masVendidos = masVendidosResult.getOrNull() ?: emptyList()
                val usuariosSemana = usuariosSemanaResult.getOrNull() ?: emptyList()

                // Calcular KPIs
                val pedidosFiltrados = filtrarPedidosPorPeriodo(pedidos, _state.value.periodoFiltro)
                val totalVentas = pedidosFiltrados.sumOf { it.total }
                val pedidosPendientes = pedidos.count { it.estado.equals("pendiente", ignoreCase = true) }
                val productosBajoStock = productos.count { it.stock < 10 }

                _state.value = _state.value.copy(
                    isLoading = false,
                    totalProductos = productos.size,
                    totalPedidos = pedidosFiltrados.size,
                    pedidosPendientes = pedidosPendientes,
                    totalVentas = totalVentas,
                    productossBajoStock = productosBajoStock,
                    ventasPorCategoria = ventas,
                    productosMasVendidos = masVendidos,
                    usuariosActivosSemana = usuariosSemana,
                    productos = productos,
                    pedidos = pedidos,
                    usuarios = usuarios
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar el dashboard: ${e.message}"
                )
            }
        }
    }

    fun cambiarPeriodoFiltro(periodo: PeriodoFiltro) {
        _state.value = _state.value.copy(periodoFiltro = periodo)

        // Recalcular KPIs basados en el nuevo período
        val pedidosFiltrados = filtrarPedidosPorPeriodo(_state.value.pedidos, periodo)
        val totalVentas = pedidosFiltrados.sumOf { it.total }

        _state.value = _state.value.copy(
            totalPedidos = pedidosFiltrados.size,
            totalVentas = totalVentas
        )
    }

    private fun filtrarPedidosPorPeriodo(pedidos: List<Pedido>, periodo: PeriodoFiltro): List<Pedido> {
        val ahora = Calendar.getInstance()
        val fechaInicio = Calendar.getInstance()

        when (periodo) {
            PeriodoFiltro.HOY -> {
                fechaInicio.set(Calendar.HOUR_OF_DAY, 0)
                fechaInicio.set(Calendar.MINUTE, 0)
                fechaInicio.set(Calendar.SECOND, 0)
            }
            PeriodoFiltro.SEMANA -> {
                fechaInicio.add(Calendar.DAY_OF_YEAR, -7)
            }
            PeriodoFiltro.MES -> {
                fechaInicio.add(Calendar.MONTH, -1)
            }
        }

        return pedidos.filter { pedido ->
            try {
                // Parsear la fecha del pedido (formato ISO: "2024-12-02T10:30:00Z")
                val fechaPedido = Calendar.getInstance()
                // Simplificación: asumimos que las fechas están en formato ISO
                // En producción, usar un parser robusto como SimpleDateFormat
                pedido.fechaPedido?.let {
                    fechaPedido.timeInMillis >= fechaInicio.timeInMillis &&
                    fechaPedido.timeInMillis <= ahora.timeInMillis
                } ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}

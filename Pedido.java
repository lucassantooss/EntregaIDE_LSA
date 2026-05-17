import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Pedido {
    private int identificador;
    private Date fechaPedido;
    private EstadoPedido estado;
    private double total;
    private Cliente cliente;
    private Repartidor repartidor;
    private Set<Producto> productos; 

    public Pedido(int identificador, Cliente cliente) {
        this.identificador = identificador;
        this.fechaPedido = new Date();
        this.estado = EstadoPedido.PENDIENTE;
        this.cliente = cliente;
        this.repartidor = null; 
        this.productos = new HashSet<>();
        this.total = 0.0;
        
        cliente.agregarPedido(this); 
    }

    public boolean addProducto(Producto producto) {
        if (this.productos.add(producto)) {
            calcularTotal();
            return true;
        }
        System.out.println("No se pueden añadir productos duplicados.");
        return false;
    }

    private void calcularTotal() {
        this.total = 0.0;
        for (Producto p : productos) {
            this.total += p.getPrecio();
        }
    }

    public boolean asignarRepartidor(Repartidor repartidor) {
        if (this.productos.isEmpty()) {
            System.out.println("No es posible asignar un repartidor si no hay productos.");
            return false;
        }
        
        if (repartidor.getEstado() != EstadoRepartidor.DISPONIBLE) {
            System.out.println("El repartidor no está disponible.");
            return false;
        }

        this.repartidor = repartidor;
        this.estado = EstadoPedido.EN_REPARTO; 
        this.repartidor.setEstado(EstadoRepartidor.OCUPADO); 
        return true;
    }

    
    public boolean entregarPedido() {
        if (this.estado != EstadoPedido.EN_REPARTO) {
            System.out.println("No es posible entregar un pedido si no está en reparto.");
            return false;
        }

        this.estado = EstadoPedido.ENTREGADO; 
        if (this.repartidor != null) {
            this.repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
        }
        return true;
    }

    // Getters
    public int getIdentificador() { return identificador; }
    public Date getFechaPedido() { return fechaPedido; }
    public EstadoPedido getEstado() { return estado; }
    public double getTotal() { return total; }
    public Cliente getCliente() { return cliente; }
    public Repartidor getRepartidor() { return repartidor; }
    public Set<Producto> getProductos() { return productos; }
}
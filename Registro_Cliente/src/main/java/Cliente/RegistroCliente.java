package Cliente;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
//funcionalidad a la regla de negocio
public class RegistroCliente {
    private final Map<String,Cliente> clientesPorDNI = new HashMap<>();

    public void registrarCliente(Cliente cliente){
        if(clientesPorDNI.containsKey(cliente.dni())){
            throw new IllegalArgumentException("Ya existe un cliente registrado con DNI" + cliente.dni());
        }
        clientesPorDNI.put(cliente.dni(), cliente);
    }

    public Optional<Cliente> buscarPorDNI(String dni){
        return Optional.ofNullable(clientesPorDNI.get(dni));
    }

    public int cantidad(){
        return clientesPorDNI.size();
    }

}

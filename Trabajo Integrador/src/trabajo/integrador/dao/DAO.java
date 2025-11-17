package trabajo.integrador.dao;

import java.util.List;

public interface DAO<T, K> {
    void crear(T entidad);            // insertar
    void actualizar(T entidad);       // update
    void eliminar(K id);              // delete
    T leerPorId(K id);                // select one
    List<T> leerTodos();              // select all
}

package trabajo.integrador;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import trabajo.integrador.dao.MysqlCodigoBarrasDAO;
import trabajo.integrador.dao.MysqlProductoDAO;
import trabajo.integrador.dao.MysqlRolDAO;
import trabajo.integrador.dao.MysqlUsuarioDAO;
import trabajo.integrador.dao.tipoCodigoBarras;
import trabajo.integrador.entities.CodigoBarras;
import trabajo.integrador.entities.Producto;
import trabajo.integrador.entities.Rol;
import trabajo.integrador.entities.Usuario;

public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final MysqlProductoDAO productoDAO;
    private final MysqlCodigoBarrasDAO codigoBarrasDAO;
    private final MysqlUsuarioDAO usuarioDAO;
    private final MysqlRolDAO rolDAO;

    public AppMenu(Connection conn, MysqlProductoDAO productoDAO, MysqlCodigoBarrasDAO codigoBarrasDAO, MysqlUsuarioDAO usuarioDAO, MysqlRolDAO rolDAO) {
        this.productoDAO = productoDAO;
        this.codigoBarrasDAO = codigoBarrasDAO;
        this.usuarioDAO = usuarioDAO;
        this.rolDAO = rolDAO;
    }

    public void start() {
        int option = -1;
        do {
            System.out.println("\n===== MENU PRINCIPAL CRUD =====");
            System.out.println("1) Productos");
            System.out.println("2) Códigos de Barras");
            System.out.println("3) Usuarios");
            System.out.println("4) Roles");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            option = readInt();

            switch (option) {
                case 1: menuProducto(); break;
                case 2: menuCodigoBarras(); break;
                case 3: menuUsuario(); break;
                case 4: menuRol(); break;
                case 0: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción inválida");
            }

        } while (option != 0);
    }

    private void menuProducto() {
        int op;
        do {
            System.out.println("\n--- Productos ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Buscar por ID");
            System.out.println("4) Actualizar");
            System.out.println("5) Eliminar");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = readInt();

            switch (op) {
                case 1: crearProducto(); break;
                case 2: listarProductos(); break;
                case 3: buscarProductoPorId(); break;
                case 4: actualizarProducto(); break;
                case 5: eliminarProducto(); break;
                case 0: break;
                default: System.out.println("Opción inválida");
            }

        } while (op != 0);
    }

    private void crearProducto() {

        String nombre = readValidatedString("Nombre");
        String marca = readValidatedString("Marca");
        String categoria = readValidatedString("Categoria");
        double precio = readValidatedDouble("Precio", true);
        double peso = readValidatedDouble("Peso (gramos)", true);

        Producto p = new Producto(0, nombre, precio);
        p.setMarca(marca);
        p.setCategoria(categoria);
        p.setPeso(peso);

        productoDAO.crear(p);
        int productoId = p.getId();

        tipoCodigoBarras tipo = readValidatedEnum("Tipo (EAN13/EAN8/UPC)", tipoCodigoBarras.class);
        String valor = readValidatedNumericString("Valor (numérico)");
        Date fecha = readValidatedDate("Fecha asignación");

        System.out.print("Observaciones: ");
        String obs = readLine();

        CodigoBarras c = new CodigoBarras(productoId, tipo, valor, fecha, obs);
        codigoBarrasDAO.crear(c);
    }

    private void listarProductos() {
        List<Producto> lista = productoDAO.leerTodos();
        for (Producto p : lista) {
            System.out.println(
                p.getId() + " | " + p.getNombre()
                + " | $" + p.getPrecio()
                + " | Marca: " + (p.getMarca() != null ? p.getMarca() : "-")
                + " | Categoria: " + (p.getCategoria() != null ? p.getCategoria() : "-")
                + " | Peso(g): " + p.getPeso()
                + " | FechaCreacion: " + (p.getFechaCreacion() != null ? p.getFechaCreacion() : "-")
                + " | CodigoID: " + (p.getCodigoId() != 0 ? p.getCodigoId() : "Código no asignado")
            );
        }
    }

    private void buscarProductoPorId() {
        System.out.print("ID: ");
        int id = readInt();
        Producto p = productoDAO.leerPorId(id);
        if (p != null) System.out.println(p);
        else System.out.println("No encontrado");
    }

    private void actualizarProducto() {

        System.out.print("ID a actualizar: ");
        int id = readInt();
        Producto p = productoDAO.leerPorId(id);

        if (p == null) {
            System.out.println("No existe el producto");
            return;
        }

        p.setNombre(readValidatedString("Nuevo nombre (actual: " + p.getNombre() + ")"));
        p.setMarca(readValidatedString("Nueva marca (actual: " + p.getMarca() + ")"));
        p.setCategoria(readValidatedString("Nueva categoría (actual: " + p.getCategoria() + ")"));
        p.setPrecio(readValidatedDouble("Nuevo precio", true));
        p.setPeso(readValidatedDouble("Nuevo peso (g)", true));

        productoDAO.actualizar(p);
    }

    private void eliminarProducto() {
        System.out.print("ID a eliminar: ");
        int id = readInt();
        productoDAO.eliminar(id);
    }

    private void menuCodigoBarras() {
        int op;
        do {
            System.out.println("\n--- Códigos de Barras ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Buscar por ID");
            System.out.println("4) Actualizar");
            System.out.println("5) Eliminar");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = readInt();

            switch (op) {
                case 1: crearCodigoBarras(); break;
                case 2: listarCodigos(); break;
                case 3: buscarCodigoPorId(); break;
                case 4: actualizarCodigo(); break;
                case 5: eliminarCodigo(); break;
            }

        } while (op != 0);
    }

    private void crearCodigoBarras() {

        int productoId = (int) readValidatedDouble("Producto ID", true);
        tipoCodigoBarras tipo = readValidatedEnum("Tipo (EAN13/EAN8/UPC)", tipoCodigoBarras.class);
        String valor = readValidatedNumericString("Valor (numérico)");
        Date fecha = readValidatedDate("Fecha asignación");

        System.out.print("Observaciones: ");
        String obs = readLine();

        CodigoBarras c = new CodigoBarras(productoId, tipo, valor, fecha, obs);
        codigoBarrasDAO.crear(c);
    }

    private void listarCodigos() {
        List<CodigoBarras> lista = codigoBarrasDAO.leerTodos();
        for (CodigoBarras c : lista) {
            System.out.println(
                c.getId() + " | " + c.getTipo() + " | " + c.getValor()
                + " | ProductoID: " + c.getProductoId()
                + " | FechaAsignacion: " + (c.getFechaAsignacion() != null ? c.getFechaAsignacion() : "-")
            );
        }
    }

    private void buscarCodigoPorId() {
        System.out.print("ID: ");
        int id = readInt();
        CodigoBarras c = codigoBarrasDAO.leerPorId(id);
        if (c != null) System.out.println(c);
        else System.out.println("No encontrado");
    }

    private void actualizarCodigo() {

        System.out.print("ID a actualizar: ");
        int id = readInt();
        CodigoBarras existente = codigoBarrasDAO.leerPorId(id);

        if (existente == null) {
            System.out.println("No existe el código de barras");
            return;
        }

        int productoId = (int) readValidatedDouble("Nuevo Producto ID", true);
        tipoCodigoBarras tipo = readValidatedEnum("Nuevo tipo", tipoCodigoBarras.class);
        String valor = readValidatedNumericString("Nuevo valor numérico");
        Date fecha = readValidatedDate("Nueva fecha asignación");

        System.out.print("Observaciones: ");
        String obs = readLine();

        CodigoBarras c = new CodigoBarras(existente.getId(), productoId, tipo.toString(), valor, fecha, obs);
        codigoBarrasDAO.actualizar(c);
    }

    private void eliminarCodigo() {
        System.out.print("ID a eliminar: ");
        int id = readInt();
        codigoBarrasDAO.eliminar(id);
    }

    private void menuUsuario() {
        int op;
        do {
            System.out.println("\n--- Usuarios ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Buscar por ID");
            System.out.println("4) Actualizar");
            System.out.println("5) Eliminar");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = readInt();

            switch (op) {
                case 1: crearUsuario(); break;
                case 2: listarUsuarios(); break;
                case 3: buscarUsuarioPorId(); break;
                case 4: actualizarUsuario(); break;
                case 5: eliminarUsuario(); break;
            }

        } while (op != 0);
    }

    private void crearUsuario() {
        System.out.print("Username: ");
        String username = readLine();
        System.out.print("Email: ");
        String email = readLine();
        System.out.print("Password: ");
        String password = readLine();
        System.out.print("Rol ID: ");
        int rolId = readInt();

        Usuario u = new Usuario(username, email, "", rolId);
        u.cambiarPassword(password);
        usuarioDAO.crear(u);
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioDAO.leerTodos();
        for (Usuario u : lista) {
            System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getEmail() + " | Rol:" + u.getRolId());
        }
    }

    private void buscarUsuarioPorId() {
        System.out.print("ID: ");
        int id = readInt();
        Usuario u = usuarioDAO.leerPorId(id);
        if (u != null) System.out.println(u);
        else System.out.println("No encontrado");
    }

    private void actualizarUsuario() {

        System.out.print("ID a actualizar: ");
        int id = readInt();
        Usuario u = usuarioDAO.leerPorId(id);

        if (u == null) {
            System.out.println("No existe el usuario");
            return;
        }

        System.out.print("Nuevo username: ");
        String username = readLine();
        System.out.print("Nuevo email: ");
        String email = readLine();
        System.out.print("Nuevo rolId: ");
        int rolId = readInt();

        System.out.print("Cambiar password? (s/n): ");
        String cambiar = readLine();

        if ("s".equalsIgnoreCase(cambiar)) {
            System.out.print("Nueva password: ");
            u.cambiarPassword(readLine());
        }

        Usuario actualizado = new Usuario(
                u.getId(), u.isEliminado(),
                username, email,
                u.getPasswordHash(),
                rolId, u.getActivo(), u.getFechaRegistro()
        );

        usuarioDAO.actualizar(actualizado);
    }

    private void eliminarUsuario() {
        System.out.print("ID a eliminar: ");
        int id = readInt();
        usuarioDAO.eliminar(id);
    }

    private void menuRol() {
        int op;
        do {
            System.out.println("\n--- Roles ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Buscar por ID");
            System.out.println("4) Actualizar");
            System.out.println("5) Eliminar");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = readInt();

            switch (op) {
                case 1: crearRol(); break;
                case 2: listarRoles(); break;
                case 3: buscarRolPorId(); break;
                case 4: actualizarRol(); break;
                case 5: eliminarRol(); break;
            }

        } while (op != 0);
    }

    private void crearRol() {
        String nombre = readValidatedString("Nombre");
        System.out.print("Descripción: ");
        String desc = readLine();
        rolDAO.crear(new Rol(nombre, desc));
    }

    private void listarRoles() {
        List<Rol> lista = rolDAO.leerTodos();
        for (Rol r : lista) {
            System.out.println(r.getId() + " | " + r.getNombre() + " | " + r.getDescripcion());
        }
    }

    private void buscarRolPorId() {
        System.out.print("ID: ");
        int id = readInt();
        Rol r = rolDAO.leerPorId(id);
        if (r != null) System.out.println(r);
        else System.out.println("No encontrado");
    }

    private void actualizarRol() {
        System.out.print("ID a actualizar: ");
        int id = readInt();
        Rol r = rolDAO.leerPorId(id);

        if (r == null) {
            System.out.println("No existe el rol");
            return;
        }

        r.setNombre(readValidatedString("Nuevo nombre"));
        System.out.print("Nueva descripción: ");
        String desc = readLine();

        rolDAO.actualizar(new Rol(id, r.getNombre(), desc));
    }

    private void eliminarRol() {
        System.out.print("ID a eliminar: ");
        int id = readInt();
        rolDAO.eliminar(id);
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Ingrese un número entero válido: ");
            }
        }
    }

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private double readValidatedDouble(String label, boolean positiveOnly) {
        while (true) {
            System.out.print(label + ": ");
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (!positiveOnly || value > 0) return value;
                System.err.println("Error: el valor debe ser mayor a cero.");
            } catch (Exception e) {
                System.err.println("Error: ingrese un número válido.");
            }
        }
    }

    private String readValidatedString(String label) {
        while (true) {
            System.out.print(label + ": ");
            String v = scanner.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.err.println("Error: " + label + " no puede estar vacío.");
        }
    }

    private <E extends Enum<E>> E readValidatedEnum(String label, Class<E> enumClass) {
        while (true) {
            System.out.print(label + ": ");
            try {
                return Enum.valueOf(enumClass, scanner.nextLine().trim().toUpperCase());
            } catch (Exception e) {
                System.err.println("Opción inválida. Opciones válidas:");
                for (E c : enumClass.getEnumConstants()) {
                    System.out.println(" - " + c.name());
                }
            }
        }
    }

    private String readValidatedNumericString(String label) {
        while (true) {
            System.out.print(label + ": ");
            String v = scanner.nextLine().trim();
            if (v.matches("\\d+")) return v;
            System.err.println("Error: solo números.");
        }
    }

    private Date readValidatedDate(String label) {
        while (true) {
            System.out.print(label + " (YYYY-MM-DD): ");
            try {
                return Date.valueOf(scanner.nextLine().trim());
            } catch (Exception e) {
                System.err.println("Fecha inválida.");
            }
        }
    }
}

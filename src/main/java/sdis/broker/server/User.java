package sdis.broker.server;

public class User {
    private final String name;
    private final String password;
    private final Status status;

    public User(String name, String password, Status status) {
        if (name == null || password == null || status == null)
            throw new IllegalArgumentException("Nombre, contraseña y status no puede ser null");

        this.name = name;
        this.password = password;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }
}

package org.example.repository;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Repository {

    private String id;
    private ArrayList<Person> people;
    private ArrayList<Car> cars;
    private ArrayList<Dog> dogs;
    private ArrayList<CarTransaction> carTransactions;

    // Configuración de H2 Embebido (Genera un archivo local llamado 'proyecto_db.mv.db')
    //private static final String URL = "jdbc:h2:./proyecto_db;DB_CLOSE_DELAY=-1";
    private static final String URL = "jdbc:h2:~/proyecto_db;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public Repository(){
        this.id = UUID.randomUUID().toString();
        this.people = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.dogs = new ArrayList<>();
        this.carTransactions = new ArrayList<>();

        // 1. Inicializamos las tablas si no existen
        initDatabase();
        // 2. Cargamos los datos guardados en disco hacia nuestras colecciones de memoria
        loadDataFromDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // =========================================================================
    // INICIALIZACIÓN DE TABLAS (SQL DDL)
    // =========================================================================
    private void initDatabase() {
            // Escapamos "year" usando comillas dobles "" para que H2 lo trate como un nombre de columna válido
            String createCarTable = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "id VARCHAR(255) PRIMARY KEY, " +
                    "make VARCHAR(255), " +
                    "model VARCHAR(255), " +
                    "\"year\" INT);"; // <-- Añadidas comillas escapadas a \"year\"

            String createDogTable = "CREATE TABLE IF NOT EXISTS DOG (" +
                    "id VARCHAR(255) PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "breed VARCHAR(255), " +
                    "age INT);";

            String createPersonTable = "CREATE TABLE IF NOT EXISTS PERSON (" +
                    "id VARCHAR(255) PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "age INT, " +
                    "car_id VARCHAR(255), " +
                    "FOREIGN KEY (car_id) REFERENCES CAR(id) ON DELETE SET NULL);";

            String createTxTable = "CREATE TABLE IF NOT EXISTS TRANSACTION (" +
                    "id VARCHAR(255) PRIMARY KEY, " +
                    "buyer_id VARCHAR(255), " +
                    "seller_id VARCHAR(255), " +
                    "car_id VARCHAR(255), " +
                    "description VARCHAR(255), " +
                    "date TIMESTAMP);";

            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                // Ejecutamos cada tabla de forma individual en el orden relacional correcto
                stmt.execute(createCarTable);
                stmt.execute(createDogTable);
                stmt.execute(createPersonTable);
                stmt.execute(createTxTable);

                System.out.println("✅ [H2 DATABASE] All tables (CAR, DOG, PERSON, TRANSACTION) have been verified and are READY.");
            } catch (SQLException e) {
                System.err.println("❌ [H2 DATABASE] Fatal error creating tables: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /*
        // Definimos el script en el orden estricto de dependencias relacionales
        String sqlScript =
                "CREATE TABLE IF NOT EXISTS CAR (" +
                        "  id VARCHAR(255) PRIMARY KEY, " +
                        "  make VARCHAR(255), " +
                        "  model VARCHAR(255), " +
                        "  year INT" +
                        "); " +

                        "CREATE TABLE IF NOT EXISTS DOG (" +
                        "  id VARCHAR(255) PRIMARY KEY, " +
                        "  name VARCHAR(255), " +
                        "  breed VARCHAR(255), " +
                        "  age INT" +
                        "); " +

                        "CREATE TABLE IF NOT EXISTS PERSON (" +
                        "  id VARCHAR(255) PRIMARY KEY, " +
                        "  name VARCHAR(255), " +
                        "  age INT, " +
                        "  car_id VARCHAR(255), " +
                        "  FOREIGN KEY (car_id) REFERENCES CAR(id) ON DELETE SET NULL" +
                        "); " +

                        "CREATE TABLE IF NOT EXISTS TRANSACTION (" +
                        "  id VARCHAR(255) PRIMARY KEY, " +
                        "  buyer_id VARCHAR(255), " +
                        "  seller_id VARCHAR(255), " +
                        "  car_id VARCHAR(255), " +
                        "  description VARCHAR(255), " +
                        "  date TIMESTAMP" +
                        ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Ejecutamos todo el bloque estructurado de una sola vez
            stmt.execute(sqlScript);
            System.out.println("✅ [H2 DATABASE] All tables (CAR, DOG, PERSON, TRANSACTION) have been verified and are READY.");
        } catch (SQLException e) {
            System.err.println("❌ [H2 DATABASE] Fatal error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
    /*
    private void initDatabase() {
            // 1. CAR y DOG se deben crear primero porque son independientes
            String createCarTable = "CREATE TABLE IF NOT EXISTS CAR (id VARCHAR(255) PRIMARY KEY, make VARCHAR(255), model VARCHAR(255), year INT);";
            String createDogTable = "CREATE TABLE IF NOT EXISTS DOG (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), breed VARCHAR(255), age INT);";

            // 2. PERSON se crea después porque DEPENDE de CAR (debido al FOREIGN KEY)
            String createPersonTable = "CREATE TABLE IF NOT EXISTS PERSON (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), age INT, car_id VARCHAR(255), FOREIGN KEY (car_id) REFERENCES CAR(id) ON DELETE SET NULL);";

            // 3. TRANSACTION se crea al final porque depende de las anteriores
            String createTxTable = "CREATE TABLE IF NOT EXISTS TRANSACTION (id VARCHAR(255) PRIMARY KEY, buyer_id VARCHAR(255), seller_id VARCHAR(255), car_id VARCHAR(255), description VARCHAR(255), date TIMESTAMP);";

            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                // Ejecutamos en el orden correcto de dependencias
                stmt.execute(createCarTable);
                stmt.execute(createDogTable);
                stmt.execute(createPersonTable); // Ahora CAR ya existe, por lo que esta línea NO fallará
                stmt.execute(createTxTable);
                System.out.println("H2 Database tables created/verified successfully.");
            } catch (SQLException e) {
                System.err.println("Database init error: " + e.getMessage());
            }
        }

    */
    // =========================================================================
    // HIDRATACIÓN DE MEMORIA DESDE DISCO (Cargar datos al iniciar)
    // =========================================================================
    private void loadDataFromDatabase() {
        try (Connection conn = getConnection()) {
            // A. Cargar Coches
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM CAR")) {
                while (rs.next()) {
                    Car c = new Car(rs.getString("make"), rs.getString("model"), rs.getInt("year"));
                    c.setId(rs.getString("id")); // Asegúrate de tener setId en tu modelo, o pásalo por constructor
                    cars.add(c);
                }
            }
            // B. Cargar Perros
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM DOG")) {
                while (rs.next()) {
                    Dog d = new Dog(rs.getString("name"), rs.getString("breed"), rs.getInt("age"));
                    d.setId(rs.getString("id"));
                    dogs.add(d);
                }
            }
            // C. Cargar Personas (unidas a sus coches correspondientes en memoria)
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM PERSON")) {
                while (rs.next()) {
                    Person p = new Person(rs.getString("name"), rs.getInt("age"));
                    p.setId(rs.getString("id"));
                    String carId = rs.getString("car_id");
                    if (carId != null) {
                        p.setCar(getCarById(carId)); // Enlazamos la referencia de memoria existente
                    }
                    people.add(p);
                }
            }
            // D. Cargar Transacciones
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("SELECT * FROM TRANSACTION")) {
                while (rs.next()) {
                    Person buyer = getPersonById(rs.getString("buyer_id"));
                    Person seller = getPersonById(rs.getString("seller_id"));
                    Car car = getCarById(rs.getString("car_id"));

                    // Usamos rs.getString("description") de la DB para rellenar tu parámetro 'contract'
                    CarTransaction tx = new CarTransaction(buyer, seller, rs.getTimestamp("date"), car, rs.getString("description"));

                    // ¡Importante! Le asignamos el ID persistido en la base de datos para no generar uno nuevo aleatorio
                    tx.setId(rs.getString("id"));

                    carTransactions.add(tx);
                }
            }
            System.out.println(">>> Persistent memory successfully rehydrated from H2 Database!");
        } catch (SQLException e) {
            System.err.println("Rehydration error: " + e.getMessage());
        }
    }

    // =========================================================================
    // MÉTODOS CRUD CON AUTO-SINCRONIZACIÓN EN BASE DE DATOS
    // =========================================================================

    // ===== Person =====
    public void addPerson(Person person) {
        people.add(person);
        String sql = "MERGE INTO PERSON KEY(id) VALUES (?, ?, ?, ?)"; // MERGE inserta o actualiza automáticamente
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, person.getId());
            pstmt.setString(2, person.getName());
            pstmt.setInt(3, person.getAge());
            pstmt.setString(4, person.getCar() != null ? person.getCar().getId() : null);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Save error (Person): " + e.getMessage()); }
    }

    public Person getPersonById(String id) {
        for (Person p : people) { if (p.getId().equals(id)) return p; }
        return null;
    }

    public ArrayList<Person> getAllPeople() {
        // Cada vez que se consulta la lista, actualizamos los estados de las personas en la DB por si cambió su coche asignado
        for (Person p : people) {
            String sql = "UPDATE PERSON SET car_id = ? WHERE id = ?";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, p.getCar() != null ? p.getCar().getId() : null);
                pstmt.setString(2, p.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) { /* Sincronización silenciosa */ }
        }
        return people;
    }

    public boolean removePerson(String id) {
        String sql = "DELETE FROM PERSON WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Delete error (Person): " + e.getMessage()); }
        return people.removeIf(p -> p.getId().equals(id));
    }

    // ===== Car =====
    public void addCar(Car car) {
        cars.add(car);
        String sql = "MERGE INTO CAR KEY(id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, car.getId());
            pstmt.setString(2, car.getMake());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Save error (Car): " + e.getMessage()); }
    }

    public Car getCarById(String id) {
        for (Car c : cars) { if (c.getId().equals(id)) return c; }
        return null;
    }

    public ArrayList<Car> getAllCars() {
        return cars;
    }

    public boolean removeCar(String id) {
        String sql = "DELETE FROM CAR WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Delete error (Car): " + e.getMessage()); }
        return cars.removeIf(c -> c.getId().equals(id));
    }

    // ===== Dog =====
    public void addDog(Dog dog) {
        dogs.add(dog);
        String sql = "MERGE INTO DOG KEY(id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dog.getId());
            pstmt.setString(2, dog.getName());
            pstmt.setString(3, dog.getBreed());
            pstmt.setInt(4, dog.getAge());
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Save error (Dog): " + e.getMessage()); }
    }

    public Dog getDogById(String id) {
        for (Dog d : dogs) { if (d.getId().equals(id)) return d; }
        return null;
    }

    public ArrayList<Dog> getAllDogs() {
        return dogs;
    }

    public boolean removeDog(String id) {
        String sql = "DELETE FROM DOG WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Delete error (Dog): " + e.getMessage()); }
        return dogs.removeIf(d -> d.getId().equals(id));
    }
    // ===== CarTransaction =====
    public void addCarTransaction(CarTransaction carTransaction) {
        carTransactions.add(carTransaction);
        String sql = "INSERT INTO TRANSACTION (id, buyer_id, seller_id, car_id, description, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carTransaction.getId());
            pstmt.setString(2, carTransaction.getBuyer() != null ? carTransaction.getBuyer().getId() : null);
            pstmt.setString(3, carTransaction.getSeller() != null ? carTransaction.getSeller().getId() : null);
            pstmt.setString(4, carTransaction.getCar() != null ? carTransaction.getCar().getId() : null);
            pstmt.setString(5, carTransaction.getDescription());
            pstmt.setTimestamp(6, new Timestamp(carTransaction.getDate().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Save error (Transaction): " + e.getMessage()); }
    }
    public CarTransaction getCarTransactionById(String id) {
        for (CarTransaction ct : carTransactions) { if (ct.getId().equals(id)) return ct; }
        return null;
    }
    public ArrayList<CarTransaction> getAllCarTransactions() {
        return this.carTransactions;
    }
    public boolean removeCarTransaction(String id) {
        String sql = "DELETE FROM TRANSACTION WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("SQL Delete error (Transaction): " + e.getMessage()); }
        return carTransactions.removeIf(ct -> ct.getId().equals(id));
    }
}

/*package org.example.repository;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;

import java.util.ArrayList;
import java.util.UUID;

public class Repository {

    private String id;
    private ArrayList<Person> people;
    private ArrayList<Car> cars;
    private ArrayList<Dog> dogs;
    private ArrayList<CarTransaction> carTransactions;

    public Repository(){
        this.id = UUID.randomUUID().toString();
        this.people = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.dogs = new ArrayList<>();
        this.carTransactions = new ArrayList<>();
    }

    // ===== Person =====
    public void addPerson(Person person) {
        people.add(person);
    }

    public Person getPersonById(String id) {
        for (Person p : people) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Person> getAllPeople() {
        return people;
    }

    public boolean removePerson(String id) {
        return people.removeIf(p -> p.getId().equals(id));
    }

    // ===== Car =====
    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCarById(String id) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Car> getAllCars() {
        return cars;
    }

    public boolean removeCar(String id) {
        return cars.removeIf(c -> c.getId().equals(id));
    }

    // ===== Dog =====
    public void addDog(Dog dog) {
        dogs.add(dog);
    }

    public Dog getDogById(String id) {
        for (Dog d : dogs) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Dog> getAllDogs() {
        return dogs;
    }

    public boolean removeDog(String id) {
        return dogs.removeIf(d -> d.getId().equals(id));
    }

    // ===== CarTransaction =====
    public void addCarTransaction(CarTransaction carTransaction) {
        carTransactions.add(carTransaction);
    }

    public CarTransaction getCarTransactionById(String id) {
        for (CarTransaction ct : carTransactions) {
            if (ct.getId().equals(id)) {
                return ct;
            }
        }
        return null;
    }

    public ArrayList<CarTransaction> getAllCarTransactions() {
        return carTransactions;
    }

    public boolean removeCarTransaction(String id) {
        return carTransactions.removeIf(ct -> ct.getId().equals(id));
    }
}
*/
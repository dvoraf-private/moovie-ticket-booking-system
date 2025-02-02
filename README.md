# Movie Ticket Booking System

This is a Movie Ticket Booking System built using **Spring Boot**, **Java**, **Maven**, and **MySQL**. The system allows users to register, log in, browse movies, and book tickets. The APIs are secured with **JWT** authentication.

## Prerequisites

Before running the application, you need the following tools installed on your machine:

### 1. **Java** 
- Ensure that Java is installed on your machine.
- Check if Java is installed by running:

  ```bash
  java -version
  ```
  
- If Java is not installed, download and install it from [here]().


### 2 **Maven** (for building and running the application)
- Check if Maven is installed by running:

  ```bash
    mvn -version
  ```
- If Maven is not installed, you can download it from [here](https://maven.apache.org/download.cgi).


### 3 **MySQL** (for database setup)
- Ensure that MySQL is installed and running.
- Verify the installation by running:
  ```bash
   mysql --version
  ```
- If MySQL is not installed, you can download it from [here]().


### 4 **Docker** (for running the integration tests using Testcontainers)
- Ensure that Docker is installed on your machine.
- Check if Docker is installed by running:

  ```bash
    docker -version
  ```
- If Docker is not installed, you can download it from [here](https://docs.docker.com/desktop/setup/install/mac-install/).


## Steps to Run the Application
### 1. Clone the Repository
   Clone the project repository to your local machine:

```
git clone git@github.com:dvoraf-private/moovie-ticket-booking-system.git
cd moovie-ticket-booking-system
```

### 2. Set Up the Database
   **Create a MySQL Database** for the application:

```
CREATE DATABASE moviedb;
```
**Configure the Database** in application.properties:

```
spring.datasource.url=jdbc:mysql://localhost:3306/moviedb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 3. Build the Application
   Use Maven to clean and install the application:

```
mvn clean install
```

### 4. Run the Application
   Run the application with the following command:

```
mvn spring-boot:run
```

Your application will be available at http://localhost:8080.

### 5. Running Tests
   To run the tests, use the following Maven command:
```
mvn test
```

## API Documentation
The API documentation is available via **Swagger UI** once the application is running. Access it by visiting:

[Swagger UI](http://localhost:8088/swagger-ui/index.html)

## API Usage:
### 1. Register a New User:

- Use the `/auth/register` API to register a new user.
- Provide the required details (email, password, full name, and role).

Example Request Body for Registration:
```
{
"email": "user@example.com",
"password": "password123",
"fullName": "John Doe",
"role": "ADMIN"
}
```

### 2. Login with an Existing User:

You can log in with an existing user for testing purposes. Use the following credentials:

- **Email**: `admin@gmail.com`
- **Password**: `admin123`

Example Request Body for Login:

```
{
"email": "admin@gmail.com",
"password": "admin123"
}
```

The login will return a **JWT token** in the response.

### 3. Authorization in Swagger UI:

- After logging in, you will receive a **JWT token** in the response body.

- Copy the token from the response and click the **Authorize** button at the top-right of the Swagger UI page.

- Paste the token in the `Value` field.

- After authorization, you can interact with the API as an authenticated user.

## Database Configuration
The database connection details are set in the `application.properties` file. You can configure the connection details to match your environment (e.g., username, password, and database URL).

Example MySQL connection settings:

```
spring.datasource.url=jdbc:mysql://localhost:3306/moviedb
spring.datasource.username=root
spring.datasource.password=yourpassword
```

Ensure that the MySQL database is running and accessible from the application. If you encounter connection issues, check if the database is up and verify the username and password in the `application.properties` file.

## Additional Information
- **JPA/Hibernate** is used for database operations. The schema is automatically created or updated based on the entity classes.
- **JWT Authentication**: All API requests (except for registration and login) require a valid JWT token.
- **Error Handling**: The application includes basic error handling and returns appropriate HTTP status codes for different scenarios.

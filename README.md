# Getting Started

### Requirements
- Java 17
- MySQL 8.0

### Installation
1. Clone the repo
   ```sh
   git clone https://github.com/Joluvc01/backend_polleria.git
   
2. Open the project in your favorite IDE
   
3. Navigate to the application properties
   ```sh
   src/main/resources/application.properties

4. Configure application properties
   ```sh
   spring.datasource.url=jdbc:mysql://localhost:{your_mysql_port}/db_polleria?createDatabaseIfNotExist=true
   spring.datasource.username={your_mysql_username}
   spring.datasource.password={your_mysql_password}
   
5. Run the application
   
6. Navigate to Swagger
   ```sh
   http://localhost:8088/swagger-ui.html

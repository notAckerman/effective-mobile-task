# Effective Mobile - Task Management System

**Effective Mobile** is a Java-based Task Management System designed to help users manage tasks effectively. This system supports creating, editing, viewing, and deleting tasks, with full support for task assignment, commenting, and user authentication.

## Features

- **Authentication & Authorization**: Secure access using JWT tokens.
- **Task Management**: 
  - Create, edit, view, and delete tasks.
  - Manage task attributes like title, description, status (pending, in progress, completed, cancelled, failed), and priority (high, medium, low).
- **Task Assignment**: Assign tasks to users and update their statuses.
- **Task Comments**: Add and manage comments on tasks.
- **User-Specific Tasks**: View tasks authored by you or assigned to you.
- **API Documentation**: Detailed documentation using OpenAPI and Swagger.
- **Error Handling**: Consistent and clear error messages.

## Technology Stack

- **Java**: Programming language used.
- **Spring Framework**: For building the application.
- **MySQL**: Database management system.
- **Docker**: Containerization platform.

## Getting Started

You can run the application either locally or using Docker. Ensure Docker Desktop is installed on your machine for both methods.

### Running Locally

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/effective-mobile.git
   bash```
2. **Open the Project**
   Open the project directory using IntelliJ IDEA or any other IDE of your choice.
3. **Start the Database**
   Open a terminal in the project directory or use the terminal inside your IDE and run:
   ```bash
   docker-compose up -d
   bash```
4. **Run the Application**
   Once the database is up and running, start the application locally through your IDE.
5. **Access the Documentation**
   Visit http://localhost:8888/swagger-ui/index.html to view the API documentation.

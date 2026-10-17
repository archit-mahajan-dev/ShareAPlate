# ShareAPlate — Food Donation Management System

A Java desktop application designed to manage food donation listings, helping organize available food, reservations, and collection status.

## Features

- Add and manage food donation listings
- View donations in a structured dashboard
- Search and filter donations
- Track donation status
- Store donation records using MySQL
- Desktop interface built with Java Swing

## Tech Stack

- **Language:** Java
- **User Interface:** Java Swing
- **Database Connectivity:** JDBC
- **Database:** MySQL
- **Build Tool:** Maven

## Project Structure

```text
ShareAPlate/
├── database/
│   └── schema.sql
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           └── db.properties.example
├── pom.xml
├── README.md
└── .gitignore
```

## Prerequisites

- Java Development Kit (JDK) 17 or compatible
- Apache Maven
- MySQL Server

## Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/archit-mahajan-dev/ShareAPlate.git
   cd ShareAPlate
   ```

2. Create the database using the SQL script in `database/schema.sql`.

3. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`.

4. Enter your local MySQL database name, username, and password in `db.properties`. Never commit real credentials.

5. Build the project:

   ```bash
   mvn clean package
   ```

6. Run the application using your IDE or the configured Maven Java entry point.

## Screenshots

Screenshots of the running application will be added here.

## Project Information

- **Project:** ShareAPlate
- **Project Lead:** Archit Mahajan
- **Purpose:** Learning Java application development, database integration, and desktop UI design.

## Future Improvements

- User authentication and role-based access
- Donation expiry notifications
- Reports and analytics
- Online deployment and a web-based interface

## License

This project is available for educational and learning purposes.
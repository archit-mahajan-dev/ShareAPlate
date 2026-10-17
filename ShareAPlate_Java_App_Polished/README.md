# ShareAPlate — Food Donation Management System

**Project Lead:** Archit Mahajan

A polished Java desktop application prototype with a dashboard layout for managing community food donation listings. It uses Java Swing for the user interface, JDBC for database access, and MySQL for persistent storage.

## Features
- Create, view, search, update, and delete donation listings (CRUD).
- Track status: Available, Reserved, Collected.
- Filter by status and food category.
- Dashboard counters for listings, available donations, collected donations, and total portions.
- Sidebar navigation and a refined green community-focused visual design.
- Prepared SQL statements, basic validation, and error messages.
- MySQL persistence shared across app sessions (unlike the earlier browser-only demo).

## Technology
- Java 17+
- Java Swing
- JDBC
- MySQL 8+
- Maven

## Setup

### 1. Install prerequisites
Install JDK 17 or newer, Maven, and MySQL Server. Start MySQL.

### 2. Create the database
Open MySQL Workbench or a MySQL terminal and run the script in `database/schema.sql`.

### 3. Configure database credentials
Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`, then edit the values:
- `db.url`
- `db.username`
- `db.password`

`db.properties` is ignored by Git. Never commit real database passwords.

### 4. Run the app
From the project directory:

```bash
mvn clean compile exec:java
```

In IntelliJ IDEA or Eclipse, import the folder as a Maven project, make sure JDK 17 is selected, and run `in.shareaplate.Main`.

## Project structure

```text
src/main/java/in/shareaplate/
  Main.java
  db/Database.java
  model/Donation.java
  dao/DonationDAO.java
  ui/MainFrame.java
src/main/resources/
  db.properties.example
database/schema.sql
```

## Demo workflow
1. Add a donation using the form.
2. Select a row to edit or change its status.
3. Search by food name, donor, or location.
4. Use the status/category filters.
5. Delete a test listing when needed.

## Important limitations and responsible use
This is a student project prototype, not a production food redistribution service. It does not include login/roles, online hosting, live notifications, verified food safety, or pickup scheduling. The database runs on the configured MySQL server; a locally installed database is not automatically accessible to other people. Use fictional sample contact details in demonstrations. Do not enter private phone numbers or home addresses into a public demo.

For real community use, add authentication and authorization, input and audit logging, privacy protections, safe food-handling procedures, allergen and expiry checks, and a pilot with a community partner.

## Community purpose
ShareAPlate explores how software can help community groups coordinate surplus-food listings and reduce avoidable food waste. Claim community impact only after genuine testing with intended users and documenting actual feedback.

# MuhaiminTech Spring Boot Boilerplate

A full-featured **Spring Boot 3.5.6** boilerplate with **Java 25**, **MySQL 8**, and a clean **RBAC + Access Profile separation** for enterprise-ready web applications.

This boilerplate is designed to accelerate backend development with ready-to-use authentication, authorization, and modular user management features.

---

## Table of Contents

1. [Technology Stack](#technology-stack)
2. [Project Overview](#project-overview)
3. [Authentication & Authorization](#authentication--authorization)
   - [RBAC (Role-Based Access Control)](#rbac-role-based-access-control)
   - [Access Profiles](#access-profiles)
4. [Database](#database)
5. [How to Run](#how-to-run)
6. [Why This Boilerplate is Better](#why-this-boilerplate-is-better)

---

## Technology Stack

| Layer        | Technology / Version                                |
| ------------ | --------------------------------------------------- |
| Language     | **Java 25**                                         |
| Framework    | **Spring Boot 3.5.6**                               |
| Security     | Spring Security with custom **PermissionEvaluator** |
| Database     | MySQL 8.0                                           |
| ORM          | Spring Data JPA                                     |
| Build Tool   | Maven                                               |
| REST Testing | Postman                                             |

---

## Project Overview

This boilerplate provides:

- **User Management**: Users, Roles, Permissions, Access Profiles
- **RBAC**: Fine-grained role-based authorization
- **Access Profiles**: Direct access/permission assignment to users
- **Method-level security** using `@PreAuthorize` with dynamic checks
- **Admin/Super Admin bypass** for full access
- **REST APIs** for user, role, permission, and access profile management

The system is modular and can be extended for **multi-tenant**, **microservices**, or **enterprise applications**.

---

## Authentication & Authorization

This boilerplate uses a **hybrid authorization system** with **RBAC + Access Profiles**.

### RBAC (Role-Based Access Control)

- **Roles** are collections of **permissions**.
- Users can have one or more roles.
- Permissions are strings like `USER_CREATE`, `USER_UPDATE`, etc.
- **Usage in code:**

```java
@PreAuthorize("hasPermission('RBAC','USER_CREATE')")
@GetMapping("/users")
public ResponseEntity<List<User>> getUsers() { ... }
```

- **Behavior:**
  - Checks if the user has a role containing the required permission.
  - Admins (`ADMIN` or `SUPER_ADMIN`) automatically bypass RBAC.

---

### Access Profiles

- **Access Profiles** are direct user-to-module assignments.
- Useful for **temporary or granular access** without creating roles.
- **Usage in code:**

```java
@PreAuthorize("hasPermission('ACCESS','MANAGE_INVENTORY')")
@GetMapping("/inventory")
public ResponseEntity<List<Inventory>> getInventory() { ... }
```

- **Behavior:**
  - Checks if the user has been assigned this access explicitly.
  - Can coexist with RBAC: some endpoints can check either RBAC or Access Profile depending on the type.

---

## Database

- **MySQL 8.0**
- Schema includes:

```
users
roles
permissions
access_profiles
user_roles
role_permissions
user_access_profiles
flyway_schema_history
```

- Relationships:
  - `User <-> Role <-> Permission`
  - `User <-> AccessProfile`
- **Example:**
  - `User "Alice"` → Roles: `Manager` → Permissions: `USER_CREATE, USER_UPDATE`
  - `User "Bob"` → Access Profile: `USER_MANAGEMENT`

---

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/boilerplate.git
cd boilerplate
```

2. Configure database in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/boilerplate_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

3. Build and run:

```bash
mvn clean install
mvn spring-boot:run
```

4. Access API endpoints via Postman.

---

## Why This Boilerplate is Better

| Feature             | Previous Versions               | This Boilerplate                                                                                       |
| ------------------- | ------------------------------- | ------------------------------------------------------------------------------------------------------ |
| **Spring Boot**     | 2.x → outdated dependencies     | 3.5.6 → latest LTS with security and performance improvements                                          |
| **Java**            | 17 or 19                        | 25 → newest features, improved performance, records, pattern matching                                  |
| **Security**        | Simple role checks              | Custom `PermissionEvaluator` with **RBAC + Access Profiles**, Admin bypass, fine-grained authorization |
| **Method Security** | Limited or outdated annotations | `@PreAuthorize` with dynamic permission checks                                                         |
| **Database**        | MySQL 5.x                       | MySQL 8 → modern features and JSON support                                                             |
| **Extensibility**   | Hard to extend                  | Modular structure: roles, permissions, access profiles, users                                          |
| **Maintainability** | Older dependency versions       | Uses latest Spring 6, clean dependency management, modern patterns                                     |

**Summary:**  
This boilerplate is **more secure, flexible, and future-proof**. It separates **RBAC roles** and **direct Access Profiles** so your authorization is granular and scalable. Compatible with modern enterprise architectures and can be extended for **microservices, multi-tenant systems, and API-first development**.

---

## Notes

- Ensure **eager loading** or proper fetching of roles/permissions in `User` entity to avoid lazy initialization errors.
- Admins and Super Admins automatically bypass all RBAC/access checks.
- For new endpoints, simply use `@PreAuthorize("hasPermission('<TYPE>','<PERMISSION>')")`.

---

## Postman Collection

```
-  Boilerplate.postman_collection.json
```

- Collection includes all CRUD operations for users, roles, permissions, and access profiles.

---

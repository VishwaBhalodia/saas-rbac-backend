# Multi-Tenant SaaS Backend with RBAC

A backend where multiple organizations share the same system, but each org's data is completely isolated — like separate Slack workspaces. Built to demonstrate secure multi-tenancy, JWT auth, and role-based access control.

**Live demo:** https://saas-rbac-backend-1.onrender.com
*(Free-tier hosting — first request may take up to 50 seconds if idle.)*

## The Core Idea

Every organization's Projects and Tasks are invisible to every other organization — even with the exact ID of another org's data, the API returns a 404, not the data. This is enforced at the database query level, not the UI, so it can't be bypassed by calling the API directly.

Two roles exist per org:
- **Admin** — manages members, can delete any project/task
- **Member** — can create and view data, but can only modify tasks they created or are assigned to

## Tech Stack

Java 21 · Spring Boot 4 · Spring Security + JWT · Spring Data JPA/Hibernate · MySQL · Docker · Deployed on Render (app) + Aiven (database)

## How It Works

Every request passes through a custom `JwtAuthFilter`, which validates the JWT and stores the user's `orgId`, `role`, and `email` in a request-scoped `TenantContext`. Every database query is scoped using `TenantContext.getOrgId()`, so isolation is structural — not dependent on the client behaving correctly.

Organization → Users (ADMIN/MEMBER) → Projects → Tasks (createdBy, assignedTo)

## Running Locally

Requires Java 21, Maven, MySQL. Copy `application.properties.example` → `application.properties`, fill in your DB credentials, then:

./mvnw spring-boot:run

## Key Endpoints

`POST /auth/register` · `POST /auth/login` · `POST /projects` · `GET /projects` · `POST /tasks` · `PATCH /tasks/{id}/status` · `DELETE /tasks/{id}`

## Future Improvements

- Automated tests (JUnit + Mockito)
- Flyway migrations instead of Hibernate auto-DDL
- Refresh tokens

## Author

Vishwa Bhalodia — [GitHub](https://github.com/VishwaBhalodia)
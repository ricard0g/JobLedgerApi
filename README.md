# JobLedger

A personal **Job Application Tracker** REST API built with pure Java 21 - no frameworks.
Track applications, interview rounds, and status history through a clean JSON API.

## Tech Stack

| Technology | Role |
|---|---|
| Java 21 | Language |
| `com.sun.net.httpserver.HttpServer` | HTTP server (no framework) |
| JDBC + H2 (file-based) | Database |
| Gson | JSON serialization |
| JJWT | JWT auth (access + refresh tokens) |
| BCrypt | Password hashing |
| Maven | Build tool (fat JAR) |
| Docker + Render | Containerization & deployment |

## API Endpoints

### Auth - Public
| Method | Path | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register & auto-login → `{accessToken, refreshToken}` |
| `POST` | `/api/auth/login` | Login → `{accessToken, refreshToken}` |
| `POST` | `/api/auth/refresh` | Renew access token |
| `POST` | `/api/auth/logout` | Invalidate refresh token |

### Applications - Protected (`Authorization: Bearer <token>`)
| Method | Path | Description |
|---|---|---|
| `GET` | `/api/applications` | List all (filterable by `status`, `isRemote`, `company`) |
| `POST` | `/api/applications` | Create application (auto-fetches company logo) |
| `GET/PUT/DELETE` | `/api/applications/{id}` | Get, update, or delete |
| `GET` | `/api/applications/{id}/timeline` | Status change history |
| `GET/POST` | `/api/applications/{id}/interviews` | List or add interview rounds |
| `GET` | `/api/stats` | Totals, response rate, breakdown by status |

## Database Schema

Four tables: `users`, `applications`, `status_history`, `interviews`.
Status values: `APPLIED` · `INTERVIEW` · `OFFER` · `REJECTED`
Interview outcomes: `PASSED` · `FAILED` · `PENDING`

## Design Notes

- **No framework by design** — built to deeply understand `HttpServer` and JDBC before moving to Spring Boot.
- **JWT strategy** — short-lived access tokens (15 min) + long-lived refresh tokens (7 days) stored in H2 for true invalidation on logout.
- **Status history is automatic** — any `PUT` that changes `status` triggers a `status_history` insert in the service layer.
- **Single H2 file** — appropriate for a single-instance portfolio project. Swap the JDBC URL to PostgreSQL to scale without touching any other layer.
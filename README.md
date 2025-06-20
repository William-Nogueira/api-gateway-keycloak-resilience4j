## Spring API Gateway

This is an **API Gateway** project built using **Spring Cloud Gateway**. It includes features such as:

* Circuit Breaker with Resilience4j
* Load Balancing with Spring Cloud LoadBalancer
* Authentication with Keycloak (OAuth2 JWT)
* Rate Limiting with Redis
* Global Logging Filter with Trace ID and JWT Username
* Fallback Routes for Graceful Degradation

---

### 🧭 Routes

| Route     | Features                                                 |
| --------- | -------------------------------------------------------- |
| /products | Load Balancer + RateLimiter + Circuit Breaker + Fallback |
| /payment  | Circuit Breaker + Fallback (simulates server error 503)  |
| /cart     | Circuit Breaker + Fallback (simulates timeout error)     |

All routes require a valid Bearer token from Keycloak.

---

### 🚀 Getting Started

This project comes with a complete Docker setup including:

* PostgreSQL (Keycloak DB)
* Keycloak (OIDC Provider with preloaded realm and user)
* Redis (Rate Limiter)
* API Gateway Project

To start the environment:

```
docker-compose up --build
```

Once the services are running, you can obtain an access token to interact with the protected routes.

Click the button below to open the Postman Collection and test all routes quickly:

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://god.gw.postman.com/run-collection/31489818-5a19472b-7aa2-4fdc-9bda-b1057b5db8dc?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D31489818-5a19472b-7aa2-4fdc-9bda-b1057b5db8dc%26entityType%3Dcollection%26workspaceId%3Dfcf5b8c7-169e-4da2-9c27-c17136943146)

This collection includes:

* Token request
* Authenticated calls to /products, /payment, and /cart
* Preconfigured headers

---

### 🔐 Manual Authentication (No Postman)

A demo user is already preconfigured in Keycloak for convenience and testing:

* **Username:** `demouser`
* **Password:** `demopassword`

Use the following request to get a token from Keycloak:

```bash
curl -X POST http://localhost:8080/realms/gateway-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=gateway-client" \
  -d "username=demouser" \
  -d "password=demopassword"
```

You’ll get a JSON response like:

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
}
```

Use the access token in the Authorization header as a Bearer token:

```bash
curl http://localhost:8081/products \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

Replace YOUR_ACCESS_TOKEN with the actual token string from the previous step.

---

Built with ❤️ by [William Nogueira](https://www.linkedin.com/in/william-nogueira-dev)


# 🍔 Foodie App - Microservices Project

A full-stack food delivery application built using a **Microservices architecture** with Spring Boot, Angular, PostgreSQL, and Docker. Inspired by platforms like Swiggy and Zomato.

---

## 🛠 Tech Stack

**Backend:**
- Java 21 + Spring Boot
- Spring Data JPA
- Spring Security & JWT (Authentication)
- PostgreSQL
- Gradle
- Eureka Server (Service Registry)
- API Gateway (Spring Cloud Gateway)
- Docker (Optional for deployment)

**Frontend:**
- Angular
- REST API integration

---

## 🧩 Microservices

| Service            | Port | Description                        |  

| `user`           | 8081 | Manages customer and admin registration/login |

| `restaurant`     | 8082 | Handles restaurants and menus    |

| `order`          | 8083 | Places and tracks customer orders |

| `payment`        | 8084 | Handles payment processing       |

| `Mail`           | 8085 | Send Email to Customer(Notification) |

| `api-gateway`    | 8080 | Single entry point for all services |

| `eureka-server`  | 8761 | Service discovery registry       |


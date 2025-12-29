# Architecture Technique du Projet

## Vue d'ensemble
Ce projet implémente une architecture microservices distribuée basée sur les patterns **Event Sourcing** et **CQRS** (Command Query Responsibility Segregation) pour la gestion de conférences et keynotes.

## Composants Principaux

### 1. Services Fonctionnels

#### Keynote-Service
- **Partie Command**: Gestion des commandes (créer, modifier, supprimer keynotes)
- **Partie Query**: Lecture et consultation des keynotes
- Base de données: MySQL (write-side) + projection (read-side)

#### Conference-Service
- **Partie Command**: Gestion des conférences et reviews
- **Partie Query**: Consultation des conférences et analytics
- Base de données: MySQL (write-side) + projection (read-side)

#### Analytics-Service
- Traitement en temps réel avec Kafka Streams
- Calcul du nombre et total des reviews sur fenêtre de 5 secondes
- Agrégation de données pour dashboards

### 2. Services Techniques

#### Gateway-Service
- Point d'entrée unique pour tous les microservices
- Routage des requêtes
- Load balancing
- Port: 8888

#### Discovery-Service (Eureka)
- Enregistrement et découverte des services
- Health checks
- Port: 8761

#### Config-Service
- Centralisation de la configuration
- Configuration externalisée
- Port: 8888 (intégré avec Gateway)

#### Auth-Service
- Authentification OAuth2/JWT
- Intégration Keycloak ou Spring Security
- Gestion des tokens
- Port: 9999

### 3. Infrastructure

#### Axon Server
- Event Store centralisé
- Message routing
- Event sourcing infrastructure
- Port: 8024 (gRPC), 8124 (HTTP)

#### Kafka Broker
- Bus de messages événementiels
- Topics: keynote-events, conference-events, review-events
- Zookeeper pour la coordination
- Ports: 9092 (Kafka), 2181 (Zookeeper)

#### MySQL Databases
- keynote_db: Base de données pour Keynote-Service
- conference_db: Base de données pour Conference-Service
- Port: 3306

#### Keycloak
- Serveur d'authentification OAuth2/OIDC
- Gestion des utilisateurs et rôles
- Port: 8080

### 4. Frontend

#### Web Application (React)
- Interface utilisateur moderne
- Gestion des keynotes
- Gestion des conférences et reviews
- Dashboards analytics en temps réel
- Port: 3000

## Architecture Technique

```
                                    ┌─────────────────┐
                                    │   Frontend      │
                                    │   (React)       │
                                    └────────┬────────┘
                                             │
                                    ┌────────▼────────┐
                                    │  Gateway        │
                                    │  Service        │
                                    └────────┬────────┘
                                             │
                        ┌────────────────────┼────────────────────┐
                        │                    │                    │
                ┌───────▼────────┐  ┌───────▼────────┐  ┌───────▼────────┐
                │   Keynote      │  │  Conference    │  │   Analytics    │
                │   Service      │  │   Service      │  │   Service      │
                └───────┬────────┘  └───────┬────────┘  └───────┬────────┘
                        │                   │                    │
        ┌───────────────┼───────────────────┼────────────────────┘
        │               │                   │
        │       ┌───────▼────────┐  ┌───────▼────────┐
        │       │  MySQL         │  │  MySQL         │
        │       │  keynote_db    │  │  conference_db │
        │       └────────────────┘  └────────────────┘
        │
        │       ┌─────────────────────────────────────┐
        └───────►   Axon Server (Event Store)        │
        │       └─────────────────────────────────────┘
        │
        │       ┌─────────────────────────────────────┐
        └───────►   Kafka Broker                      │
                └─────────────────────────────────────┘

                ┌─────────────────────────────────────┐
                │   Eureka Discovery Service          │
                └─────────────────────────────────────┘

                ┌─────────────────────────────────────┐
                │   Keycloak Auth Server              │
                └─────────────────────────────────────┘
```

## Flux CQRS et Event Sourcing

### Flux de Commande (Write Side)
1. L'utilisateur envoie une commande via le frontend
2. Le Gateway route la commande vers le service approprié
3. Le service valide et crée un événement
4. L'événement est stocké dans Axon Server (Event Store)
5. L'événement est publié sur Kafka
6. Les projections sont mises à jour (Query Side)

### Flux de Query (Read Side)
1. L'utilisateur demande des données via le frontend
2. Le Gateway route la requête vers le service approprié
3. Le service query lit depuis la base de données de projection
4. Les données sont retournées au client

## Technologies Utilisées

- **Backend**: Spring Boot, Spring Cloud
- **Event Sourcing**: Axon Framework, Axon Server
- **Messaging**: Apache Kafka
- **Database**: MySQL
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Security**: Spring Security, OAuth2, JWT, Keycloak
- **Frontend**: React
- **Containerization**: Docker, Docker Compose
- **Build**: Maven

## Ports des Services

| Service | Port |
|---------|------|
| Gateway Service | 8888 |
| Eureka Discovery | 8761 |
| Keynote Command Service | 8081 |
| Keynote Query Service | 8082 |
| Conference Command Service | 8083 |
| Conference Query Service | 8084 |
| Analytics Service | 8085 |
| Auth Service | 9999 |
| Axon Server (HTTP) | 8124 |
| Axon Server (gRPC) | 8024 |
| Kafka | 9092 |
| Zookeeper | 2181 |
| MySQL | 3306 |
| Keycloak | 8080 |
| Frontend | 3000 |

# Conference Management System - Project Summary

## 📋 Réponses aux Questions du Projet

### 1. Architecture Technique ✅
**Fichier:** `ARCHITECTURE.md`

Architecture complète décrivant:
- 9 microservices (Discovery, Gateway, Auth, Keynote Command/Query, Conference Command/Query, Analytics)
- Infrastructure (Axon Server, Kafka, MySQL, Keycloak)
- Patterns CQRS et Event Sourcing
- Diagrammes d'architecture

### 2. Diagramme de Classes ✅
**Fichier:** `CLASS_DIAGRAM.md`

Diagramme de classes détaillé incluant:
- Aggregates (KeynoteAggregate, ConferenceAggregate)
- Commands (Create, Update, Delete)
- Events (Created, Updated, Deleted, ReviewAdded)
- Queries et DTOs
- Relations entre les classes

### 3. Serveur AXON ✅
**Configuration:** `docker-compose.yml`

Axon Server configuré et prêt à déployer:
```yaml
axon-server:
  image: axoniq/axonserver:latest
  ports:
    - "8024:8024"  # gRPC
    - "8124:8124"  # HTTP
```

### 4. Micro-service Keynote-Service ✅
**Fichiers:**
- `keynote-service-command/` - Service de commande avec Aggregate
- `keynote-service-query/` - Service de requête avec projections
- Implémentation complète CQRS avec:
  - Commands: Create, Update, Delete
  - Events: KeynoteCreated, KeynoteUpdated, KeynoteDeleted
  - Queries: GetAll, GetById

### 5. Micro-service Conference-Service ✅
**Fichiers:**
- `conference-service-command/` - Service de commande
- `conference-service-query/` - Service de requête
- Fonctionnalités:
  - Gestion des conférences (ACADEMIQUE, COMMERCIALE)
  - Système de reviews (1-5 étoiles)
  - Calcul automatique du score

### 6. Services Techniques ✅
**Implémentés:**

**Gateway Service** (`gateway-service/`)
- Spring Cloud Gateway
- Routing vers tous les microservices
- Port: 8888

**Eureka Discovery Service** (`discovery-service/`)
- Netflix Eureka Server
- Service discovery et health checks
- Port: 8761

**Config Service**
- Intégré dans chaque service via `application.yml`
- Configuration externalisée

### 7. Service d'Analytics avec Kafka Streams ✅
**Fichier:** `analytics-service/`

Real-time analytics avec:
- Kafka Streams pour traitement temps réel
- Fenêtres temporelles de 5 secondes
- Métriques calculées:
  - Nombre total de reviews
  - Somme des notes
  - Moyenne des notes
- Endpoint: `/analytics/reviews/realtime`

### 8. Sécurité OAuth2/JWT ✅
**Fichier:** `auth-service/`

Système de sécurité complet:
- Spring Security avec JWT
- Support OAuth2 avec Keycloak
- Endpoints d'authentification:
  - POST `/auth/login` - Connexion
  - POST `/auth/register` - Inscription
- Token JWT avec expiration de 24h
- Base de données utilisateurs avec rôles

**Keycloak** configuré dans `docker-compose.yml`:
```yaml
keycloak:
  image: quay.io/keycloak/keycloak:22.0
  ports:
    - "8080:8080"
```

### 9. Application Frontend React ✅
**Fichier:** `frontend/`

Application React complète avec:
- **Composants:**
  - KeynoteList - Gestion des keynotes
  - ConferenceList - Gestion des conférences
  - AnalyticsDashboard - Visualisation analytics temps réel
- **Features:**
  - CRUD complet pour keynotes
  - CRUD complet pour conférences
  - Ajout de reviews
  - Analytics en temps réel (refresh automatique toutes les 5s)
  - Navigation avec React Router
  - Design responsive et moderne
- **Technologies:**
  - React 18
  - React Router DOM
  - Axios pour API calls
  - CSS moderne

### 10. Docker Compose ✅
**Fichier:** `docker-compose.yml`

Déploiement complet avec:
- **Infrastructure:**
  - MySQL (avec init scripts)
  - Axon Server
  - Kafka + Zookeeper
  - Keycloak
- **Microservices:**
  - Discovery Service
  - Gateway Service
  - Auth Service
  - Keynote Command/Query Services
  - Conference Command/Query Services
  - Analytics Service
- **Frontend:**
  - Application React avec Nginx

**Scripts de déploiement:**
- `deploy.sh` - Déploiement automatique avec Docker Compose
- `start-local.sh` - Démarrage local pour développement
- `stop-local.sh` - Arrêt des services locaux

## 🚀 Démarrage Rapide

### Option 1: Docker (Recommandé)
```bash
chmod +x deploy.sh
./deploy.sh
```

### Option 2: Local
```bash
chmod +x start-local.sh
./start-local.sh
```

## 📊 Architecture Implémentée

```
Frontend (React) → Gateway → [Microservices] → Axon Server (Event Store)
                                    ↓
                                  Kafka
                                    ↓
                              Analytics Service
```

## 🎯 Fonctionnalités Principales

### Event Sourcing
- Tous les événements stockés dans Axon Server
- Reconstruction possible de l'état depuis les événements
- Audit trail complet

### CQRS
- Séparation stricte Command/Query
- Modèles d'écriture et de lecture indépendants
- Scalabilité optimisée

### Real-time Analytics
- Traitement en continu avec Kafka Streams
- Fenêtres temporelles de 5 secondes
- Agrégations en temps réel

### Sécurité
- JWT avec Spring Security
- Support OAuth2 via Keycloak
- Authentification centralisée

## 📂 Structure du Projet

```
conference-management-system/
├── common-api/                    # API partagée
├── keynote-service-command/       # Keynote - Commandes
├── keynote-service-query/         # Keynote - Requêtes
├── conference-service-command/    # Conference - Commandes
├── conference-service-query/      # Conference - Requêtes
├── analytics-service/             # Analytics Kafka Streams
├── gateway-service/               # API Gateway
├── discovery-service/             # Eureka Discovery
├── auth-service/                  # Authentification JWT
├── frontend/                      # Application React
├── scripts/                       # Scripts SQL
├── docker-compose.yml             # Déploiement Docker
├── deploy.sh                      # Script de déploiement
├── ARCHITECTURE.md                # Architecture technique
├── CLASS_DIAGRAM.md               # Diagramme de classes
├── IMPLEMENTATION_GUIDE.md        # Guide d'implémentation
├── TESTING_GUIDE.md               # Guide de tests
├── QUICK_START.md                 # Démarrage rapide
└── README.md                      # Documentation principale
```

## 🧪 Tests

Voir `TESTING_GUIDE.md` pour:
- Tests unitaires
- Tests d'intégration
- Tests de performance
- Tests de sécurité
- Tests frontend

## 📚 Documentation

| Fichier | Description |
|---------|-------------|
| `README.md` | Documentation principale et overview |
| `ARCHITECTURE.md` | Architecture technique détaillée |
| `CLASS_DIAGRAM.md` | Diagramme de classes complet |
| `IMPLEMENTATION_GUIDE.md` | Guide d'implémentation pour développeurs |
| `TESTING_GUIDE.md` | Guide complet de tests |
| `QUICK_START.md` | Guide de démarrage rapide |
| `PROJECT_SUMMARY.md` | Ce fichier - Résumé du projet |

## 🔗 URLs des Services

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:3000 | Interface utilisateur |
| Gateway | http://localhost:8888 | Point d'entrée API |
| Eureka | http://localhost:8761 | Service discovery |
| Axon Server | http://localhost:8124 | Event store dashboard |
| Keycloak | http://localhost:8080 | Auth server |

## 🛠️ Technologies Utilisées

- **Backend:** Spring Boot 3.1.5, Spring Cloud 2022.0.4
- **Event Sourcing:** Axon Framework 4.8.0
- **Messaging:** Apache Kafka 3.5.1
- **Database:** MySQL 8.0
- **Service Discovery:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **Security:** Spring Security, OAuth2, JWT, Keycloak
- **Frontend:** React 18, React Router, Axios
- **Containerization:** Docker, Docker Compose
- **Build:** Maven 3.8+
- **Java:** JDK 17

## ✅ Checklist de Réalisation

- [x] Architecture technique établie
- [x] Diagramme de classes créé
- [x] Axon Server déployé
- [x] Keynote-Service développé (Command & Query)
- [x] Conference-Service développé (Command & Query)
- [x] Services techniques implémentés (Gateway, Eureka, Config)
- [x] Analytics service avec Kafka Streams (fenêtre 5s)
- [x] Sécurité OAuth2/JWT avec Keycloak
- [x] Application Frontend React complète
- [x] Docker-compose pour déploiement
- [x] Documentation complète
- [x] Scripts de déploiement

## 🎓 Patterns et Concepts Appliqués

### Event Sourcing
- Event Store avec Axon Server
- Reconstruction de l'état depuis les événements
- Historique complet des changements

### CQRS (Command Query Responsibility Segregation)
- Séparation command/query
- Modèles d'écriture et lecture distincts
- Scalabilité indépendante

### Domain-Driven Design
- Aggregates (Keynote, Conference)
- Commands, Events, Queries
- Domain models

### Microservices Architecture
- Services indépendants
- Communication asynchrone
- Service discovery
- API Gateway pattern

### Event-Driven Architecture
- Communication via événements
- Kafka pour messaging asynchrone
- Event handlers pour projections

## 🔄 Flux de Données

### Création d'un Keynote
1. Frontend → POST `/keynotes/commands`
2. Gateway → Keynote Command Service
3. CreateKeynoteCommand → KeynoteAggregate
4. KeynoteCreatedEvent → Axon Server (Event Store)
5. Event → Kafka → Keynote Query Service
6. Event Handler → Update projection (MySQL)

### Consultation des Keynotes
1. Frontend → GET `/keynotes/queries`
2. Gateway → Keynote Query Service
3. Query Handler → Read from MySQL
4. Response → Frontend

## 🚀 Déploiement en Production

### Prérequis
- Serveur avec Docker & Docker Compose
- 8 GB RAM minimum
- Ports disponibles: 3000, 8080-8085, 8124, 8761, 8888, 9092, 9999

### Steps
```bash
git clone [repository-url]
cd test3
chmod +x deploy.sh
./deploy.sh
```

## 👥 Équipe et Contact

Projet réalisé pour le cours d'Architecture Microservices.

## 📄 Licence

Projet à des fins éducatives.

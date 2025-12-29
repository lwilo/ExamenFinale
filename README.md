# Conference Management System

Système distribué de gestion de conférences et keynotes basé sur les patterns **Event Sourcing** et **CQRS** avec architecture microservices.

## 📋 Table des Matières

- [Architecture](#architecture)
- [Technologies](#technologies)
- [Structure du Projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Installation et Déploiement](#installation-et-déploiement)
- [Services](#services)
- [API Endpoints](#api-endpoints)
- [Documentation](#documentation)

## 🏗️ Architecture

Le système est composé de 9 microservices organisés selon une architecture event-driven avec CQRS et Event Sourcing :

### Services Fonctionnels
- **Keynote-Service** : Gestion des keynotes (command & query)
- **Conference-Service** : Gestion des conférences et reviews (command & query)
- **Analytics-Service** : Analytics temps réel avec Kafka Streams

### Services Techniques
- **Gateway-Service** : API Gateway (Spring Cloud Gateway)
- **Discovery-Service** : Service Discovery (Eureka)
- **Auth-Service** : Authentification OAuth2/JWT

### Infrastructure
- **Axon Server** : Event Store et Message Routing
- **Kafka** : Bus de messages événementiels
- **MySQL** : Bases de données persistantes
- **Keycloak** : Serveur d'authentification

Voir [ARCHITECTURE.md](./ARCHITECTURE.md) pour plus de détails.

## 🛠️ Technologies

- **Backend** : Spring Boot 3.1.5, Spring Cloud 2022.0.4
- **Event Sourcing** : Axon Framework 4.8.0
- **Messaging** : Apache Kafka 3.5.1
- **Database** : MySQL 8.0
- **Service Discovery** : Netflix Eureka
- **API Gateway** : Spring Cloud Gateway
- **Security** : Spring Security, OAuth2, JWT, Keycloak
- **Frontend** : React 18
- **Containerization** : Docker, Docker Compose
- **Build Tool** : Maven 3.8+
- **Java** : JDK 17

## 📁 Structure du Projet

```
conference-management-system/
├── common-api/                    # API commune (DTOs, Events, Commands)
├── keynote-service-command/       # Service de commande Keynote
├── keynote-service-query/         # Service de requête Keynote
├── conference-service-command/    # Service de commande Conference
├── conference-service-query/      # Service de requête Conference
├── analytics-service/             # Service d'analytics temps réel
├── gateway-service/               # API Gateway
├── discovery-service/             # Eureka Discovery
├── auth-service/                  # Service d'authentification
├── frontend/                      # Application React
├── docker-compose.yml             # Configuration Docker
├── ARCHITECTURE.md                # Documentation architecture
├── CLASS_DIAGRAM.md               # Diagramme de classes
└── README.md                      # Ce fichier
```

## 📦 Prérequis

- **Java JDK 17** ou supérieur
- **Maven 3.8+**
- **Docker** et **Docker Compose**
- **Node.js 18+** et **npm** (pour le frontend)
- Au moins **8 GB de RAM** disponible pour Docker

## 🚀 Installation et Déploiement

### Méthode 1 : Déploiement avec Docker Compose (Recommandé)

1. **Cloner le repository**
```bash
git clone https://github.com/lwilo/test3.git
cd test3
```

2. **Démarrer tous les services**
```bash
docker-compose up -d
```

3. **Vérifier que tous les services sont démarrés**
```bash
docker-compose ps
```

4. **Accéder aux services**
- Frontend : http://localhost:3000
- Gateway : http://localhost:8888
- Eureka Dashboard : http://localhost:8761
- Axon Server : http://localhost:8124
- Keycloak : http://localhost:8080

### Méthode 2 : Déploiement Manuel

1. **Démarrer l'infrastructure**
```bash
# Démarrer Axon Server
docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver

# Démarrer MySQL
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0

# Démarrer Kafka et Zookeeper
docker-compose up -d kafka zookeeper

# Démarrer Keycloak
docker run -d --name keycloak -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0 start-dev
```

2. **Compiler et démarrer les microservices**
```bash
# Compiler tous les services
mvn clean install

# Démarrer Discovery Service
cd discovery-service && mvn spring-boot:run &

# Attendre 30 secondes puis démarrer les autres services
cd ../gateway-service && mvn spring-boot:run &
cd ../auth-service && mvn spring-boot:run &
cd ../keynote-service-command && mvn spring-boot:run &
cd ../keynote-service-query && mvn spring-boot:run &
cd ../conference-service-command && mvn spring-boot:run &
cd ../conference-service-query && mvn spring-boot:run &
cd ../analytics-service && mvn spring-boot:run &
```

3. **Démarrer le Frontend**
```bash
cd frontend
npm install
npm start
```

## 🎯 Services

### Ports des Services

| Service | Port | Description |
|---------|------|-------------|
| Frontend | 3000 | Interface utilisateur React |
| Gateway Service | 8888 | Point d'entrée API |
| Discovery Service | 8761 | Eureka Server |
| Keynote Command | 8081 | Commandes Keynote |
| Keynote Query | 8082 | Requêtes Keynote |
| Conference Command | 8083 | Commandes Conference |
| Conference Query | 8084 | Requêtes Conference |
| Analytics Service | 8085 | Analytics temps réel |
| Auth Service | 9999 | Authentification |
| Axon Server HTTP | 8124 | Interface Axon |
| Axon Server gRPC | 8024 | Communication Axon |
| Kafka | 9092 | Message Broker |
| Zookeeper | 2181 | Coordination Kafka |
| MySQL | 3306 | Base de données |
| Keycloak | 8080 | Auth Server |

## 🔌 API Endpoints

### Via Gateway (http://localhost:8888)

#### Keynote Endpoints
```bash
# Créer un keynote
POST /keynotes/commands
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.com",
  "fonction": "Tech Lead"
}

# Lister tous les keynotes
GET /keynotes/queries

# Obtenir un keynote par ID
GET /keynotes/queries/{id}

# Modifier un keynote
PUT /keynotes/commands/{id}

# Supprimer un keynote
DELETE /keynotes/commands/{id}
```

#### Conference Endpoints
```bash
# Créer une conférence
POST /conferences/commands
{
  "titre": "Spring Framework Advanced",
  "type": "ACADEMIQUE",
  "date": "2024-06-15",
  "duree": 120,
  "nombreInscrits": 0
}

# Lister toutes les conférences
GET /conferences/queries

# Obtenir une conférence par ID
GET /conferences/queries/{id}

# Ajouter un review
POST /conferences/commands/{conferenceId}/reviews
{
  "texte": "Excellente présentation!",
  "note": 5
}

# Obtenir les reviews d'une conférence
GET /conferences/queries/{conferenceId}/reviews
```

#### Analytics Endpoints
```bash
# Obtenir les analytics en temps réel
GET /analytics/reviews/realtime
```

#### Auth Endpoints
```bash
# Login
POST /auth/login
{
  "username": "admin",
  "password": "admin123"
}

# Register
POST /auth/register
{
  "username": "user1",
  "password": "password123",
  "email": "user1@example.com"
}
```

## 📚 Documentation

- [Architecture Technique](./ARCHITECTURE.md) - Architecture détaillée du système
- [Diagramme de Classes](./CLASS_DIAGRAM.md) - Modèle de domaine et classes
- [Axon Server Documentation](https://docs.axoniq.io/reference-guide/) - Documentation Axon Framework
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud) - Documentation Spring Cloud

## 🔐 Sécurité

Le système utilise OAuth2 avec JWT pour la sécurité :

1. **Authentification** : Login via `/auth/login` pour obtenir un JWT token
2. **Authorization** : Inclure le token dans le header `Authorization: Bearer {token}`
3. **Keycloak** : Configuration optionnelle pour OAuth2/OIDC centralisé

## 📊 Monitoring

### Eureka Dashboard
Accédez à http://localhost:8761 pour voir tous les services enregistrés.

### Axon Server Dashboard
Accédez à http://localhost:8124 pour :
- Visualiser les événements
- Monitorer les commandes
- Voir les queries

## 🧪 Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter les tests d'un service spécifique
cd keynote-service-command
mvn test
```

## 🐛 Troubleshooting

### Les services ne démarrent pas
1. Vérifier que Docker est en cours d'exécution
2. Vérifier les ports disponibles (8080, 8761, 8888, etc.)
3. Vérifier les logs : `docker-compose logs [service-name]`

### Connexion à MySQL échoue
1. Attendre que MySQL soit complètement démarré
2. Vérifier les credentials dans `application.yml`
3. Créer manuellement les bases de données si nécessaire

### Axon Server non accessible
1. Vérifier que le conteneur Axon Server est démarré
2. Vérifier les ports 8024 et 8124
3. Consulter les logs : `docker logs axonserver`

## 👥 Contributeurs

- Projet réalisé pour le cours d'Architecture Microservices

## 📄 Licence

Ce projet est à des fins éducatives.
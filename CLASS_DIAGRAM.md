# Diagramme de Classes Global

## Vue d'ensemble
Ce document présente le diagramme de classes global de l'application, organisé par microservice selon les patterns CQRS et Event Sourcing.

## Keynote-Service

### Domain Model (Aggregate)

```
┌─────────────────────────────────────┐
│         KeynoteAggregate            │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
├─────────────────────────────────────┤
│ + KeynoteAggregate()                │
│ + handle(CreateKeynoteCommand)      │
│ + handle(UpdateKeynoteCommand)      │
│ + handle(DeleteKeynoteCommand)      │
│ + on(KeynoteCreatedEvent)           │
│ + on(KeynoteUpdatedEvent)           │
│ + on(KeynoteDeletedEvent)           │
└─────────────────────────────────────┘
```

### Commands

```
┌─────────────────────────────────────┐
│      CreateKeynoteCommand           │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      UpdateKeynoteCommand           │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      DeleteKeynoteCommand           │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
└─────────────────────────────────────┘
```

### Events

```
┌─────────────────────────────────────┐
│      KeynoteCreatedEvent            │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      KeynoteUpdatedEvent            │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      KeynoteDeletedEvent            │
├─────────────────────────────────────┤
│ - keynoteId: String                 │
└─────────────────────────────────────┘
```

### Query Side (Projection)

```
┌─────────────────────────────────────┐
│         KeynoteEntity               │
├─────────────────────────────────────┤
│ - id: String (PK)                   │
│ - nom: String                       │
│ - prenom: String                    │
│ - email: String                     │
│ - fonction: String                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      KeynoteEventHandler            │
├─────────────────────────────────────┤
│ + on(KeynoteCreatedEvent)           │
│ + on(KeynoteUpdatedEvent)           │
│ + on(KeynoteDeletedEvent)           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      KeynoteQueryHandler            │
├─────────────────────────────────────┤
│ + handle(GetAllKeynotesQuery)       │
│ + handle(GetKeynoteByIdQuery)       │
└─────────────────────────────────────┘
```

## Conference-Service

### Domain Model (Aggregate)

```
┌─────────────────────────────────────┐
│       ConferenceAggregate           │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - titre: String                     │
│ - type: ConferenceType              │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
│ - score: Double                     │
│ - reviews: List<Review>             │
├─────────────────────────────────────┤
│ + ConferenceAggregate()             │
│ + handle(CreateConferenceCommand)   │
│ + handle(UpdateConferenceCommand)   │
│ + handle(AddReviewCommand)          │
│ + on(ConferenceCreatedEvent)        │
│ + on(ConferenceUpdatedEvent)        │
│ + on(ReviewAddedEvent)              │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│       ConferenceType (Enum)         │
├─────────────────────────────────────┤
│ ACADEMIQUE                          │
│ COMMERCIALE                         │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            Review                   │
├─────────────────────────────────────┤
│ - reviewId: String                  │
│ - date: LocalDateTime               │
│ - texte: String                     │
│ - note: Integer (1-5)               │
│ - conferenceId: String              │
└─────────────────────────────────────┘
```

### Commands

```
┌─────────────────────────────────────┐
│    CreateConferenceCommand          │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - titre: String                     │
│ - type: ConferenceType              │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│    UpdateConferenceCommand          │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - titre: String                     │
│ - type: ConferenceType              │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│       AddReviewCommand              │
├─────────────────────────────────────┤
│ - reviewId: String                  │
│ - conferenceId: String              │
│ - texte: String                     │
│ - note: Integer                     │
└─────────────────────────────────────┘
```

### Events

```
┌─────────────────────────────────────┐
│    ConferenceCreatedEvent           │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - titre: String                     │
│ - type: ConferenceType              │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
│ - score: Double                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│    ConferenceUpdatedEvent           │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - titre: String                     │
│ - type: ConferenceType              │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│       ReviewAddedEvent              │
├─────────────────────────────────────┤
│ - reviewId: String                  │
│ - conferenceId: String              │
│ - date: LocalDateTime               │
│ - texte: String                     │
│ - note: Integer                     │
└─────────────────────────────────────┘
```

### Query Side (Projection)

```
┌─────────────────────────────────────┐
│       ConferenceEntity              │
├─────────────────────────────────────┤
│ - id: String (PK)                   │
│ - titre: String                     │
│ - type: String                      │
│ - date: LocalDate                   │
│ - duree: Integer                    │
│ - nombreInscrits: Integer           │
│ - score: Double                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│         ReviewEntity                │
├─────────────────────────────────────┤
│ - id: String (PK)                   │
│ - conferenceId: String (FK)         │
│ - date: LocalDateTime               │
│ - texte: String                     │
│ - note: Integer                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│    ConferenceEventHandler           │
├─────────────────────────────────────┤
│ + on(ConferenceCreatedEvent)        │
│ + on(ConferenceUpdatedEvent)        │
│ + on(ReviewAddedEvent)              │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│    ConferenceQueryHandler           │
├─────────────────────────────────────┤
│ + handle(GetAllConferencesQuery)    │
│ + handle(GetConferenceByIdQuery)    │
│ + handle(GetReviewsByConferenceQuery)│
└─────────────────────────────────────┘
```

## Analytics-Service

### Kafka Streams Models

```
┌─────────────────────────────────────┐
│      ReviewAnalytics                │
├─────────────────────────────────────┤
│ - conferenceId: String              │
│ - windowStart: Long                 │
│ - windowEnd: Long                   │
│ - totalReviews: Long                │
│ - sumOfNotes: Long                  │
│ - averageNote: Double               │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│    ReviewStreamProcessor            │
├─────────────────────────────────────┤
│ + processReviewStream()             │
│ + aggregateByWindow()               │
└─────────────────────────────────────┘
```

## Services Techniques

### Gateway Service

```
┌─────────────────────────────────────┐
│       GatewayConfig                 │
├─────────────────────────────────────┤
│ + configureRoutes()                 │
│ + addFilters()                      │
└─────────────────────────────────────┘
```

### Discovery Service (Eureka)

```
┌─────────────────────────────────────┐
│     DiscoveryServiceConfig          │
├─────────────────────────────────────┤
│ + enableEurekaServer()              │
└─────────────────────────────────────┘
```

### Auth Service

```
┌─────────────────────────────────────┐
│           User                      │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - username: String                  │
│ - password: String                  │
│ - roles: Set<Role>                  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│           Role                      │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - name: String                      │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│       JwtTokenProvider              │
├─────────────────────────────────────┤
│ + generateToken(Authentication)     │
│ + validateToken(String)             │
│ + getUsernameFromToken(String)      │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      SecurityConfig                 │
├─────────────────────────────────────┤
│ + configureHttpSecurity()           │
│ + configureAuthManager()            │
└─────────────────────────────────────┘
```

## Relations entre les Classes

```
KeynoteAggregate --creates--> KeynoteCreatedEvent
KeynoteAggregate --updates--> KeynoteUpdatedEvent
KeynoteAggregate --deletes--> KeynoteDeletedEvent

KeynoteEventHandler --listens--> KeynoteCreatedEvent
KeynoteEventHandler --listens--> KeynoteUpdatedEvent
KeynoteEventHandler --listens--> KeynoteDeletedEvent

KeynoteEventHandler --persists--> KeynoteEntity

ConferenceAggregate --creates--> ConferenceCreatedEvent
ConferenceAggregate --updates--> ConferenceUpdatedEvent
ConferenceAggregate --adds--> ReviewAddedEvent

ConferenceAggregate --contains--> Review

ConferenceEventHandler --listens--> ConferenceCreatedEvent
ConferenceEventHandler --listens--> ConferenceUpdatedEvent
ConferenceEventHandler --listens--> ReviewAddedEvent

ConferenceEventHandler --persists--> ConferenceEntity
ConferenceEventHandler --persists--> ReviewEntity

ReviewStreamProcessor --consumes--> ReviewAddedEvent
ReviewStreamProcessor --produces--> ReviewAnalytics
```

## Pattern CQRS Illustration

```
         Commands                          Events                        Queries
            │                                │                              │
            │                                │                              │
    ┌───────▼────────┐              ┌───────▼────────┐           ┌────────▼────────┐
    │   Aggregate    │──publishes──►│  Event Store   │──projects─►│   Read Model    │
    │  (Write Side)  │              │  (Axon Server) │           │  (Query Side)   │
    └────────────────┘              └────────────────┘           └─────────────────┘
            │                                │
            │                                │
            └────────────────┬───────────────┘
                             │
                      ┌──────▼──────┐
                      │    Kafka    │
                      │   Topics    │
                      └─────────────┘
```

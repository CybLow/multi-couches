# Changelog

Le format suit [Keep a Changelog 1.1.0](https://keepachangelog.com/en/1.1.0/) ;
la numérotation suit [Semantic Versioning 2.0.0](https://semver.org/).

## [Unreleased]

## [0.9.0] — 2026-04-23

### Added
- **Couche DAO** (PR #21)
  - `GenericDao<T, ID>` abstrait — CRUD commun (`persist`, `find`, `findAll`, `merge`, `remove`)
  - `AddressDao`, `AnimalDao`, `ProductDao` — DAOs triviaux
  - `PetStoreDao` — avec `findAllAnimalsByPetStore(Long)` (JPQL polymorphe, couvre la consigne du sujet)
- **Couche Service** (PR #23)
  - `SeedService` — insère ≥ 3 enregistrements par table via EntityManager
  - `PetStoreService` — expose le use case `findAnimalsOfStore(Long)` pour le Main

## [0.5.0] — 2026-04-23

### Added
- **Hiérarchie `Animal`** (PR #12) — classe abstraite `@Entity` avec `@Inheritance(strategy=JOINED)` (stratégie imposée par le sujet)
- **`Fish`** (PR #14) — sous-classe avec `livingEnv: FishLivEnv` (`@Enumerated(STRING)`)
- **`Cat`** (PR #14) — sous-classe avec `chipId: String` (`unique=true`)
- **`Product`** (PR #16) — id, code (unique), label, type (`@Enumerated(STRING)`), price
- **`PetStore`** (PR #18) — entité pivot avec les 3 relations :
  - `@ManyToOne(unique=true)` vers `Address` (simule 1:1 avec les annotations autorisées)
  - `@OneToMany(mappedBy="petStore", cascade=ALL, orphanRemoval=true)` vers `Animal`
  - `@ManyToMany(mappedBy="petStores")` vers `Product` (owner = `Product`)
- Helpers bidirectionnels (`addAnimal`, `removeAnimal`, `addPetStore`)
- Diagramme ASCII des relations dans `docs/ARCHITECTURE.md`

### Changed
- `Animal` : ajout du champ `petStore` (`@ManyToOne`) pour la relation bidirectionnelle
- `Product` : ajout du champ `petStores` (`@ManyToMany`, owner)
- `persistence.xml` : toutes les 6 entités désormais listées

## [0.3.0] — 2026-04-23

### Added
- Enum `fr.esaip.petstore.entity.enums.ProdType` (`FOOD`, `ACCESSORY`, `CLEANING`)
- Enum `fr.esaip.petstore.entity.enums.FishLivEnv` (`FRESH_WATER`, `SEA_WATER`)
- Entité `fr.esaip.petstore.entity.Address` (id, number, street, zipCode, city) avec `@Column(length=...)` explicites
- `Address` enregistrée dans `persistence.xml`

## [0.2.0] — 2026-04-23

### Added
- Scaffold Maven (`pom.xml`, arbre de packages `fr.esaip.petstore.*`)
- Templates GitHub (`.github/ISSUE_TEMPLATE/*`, `PULL_REQUEST_TEMPLATE.md`, `CODEOWNERS`)
- `CONTRIBUTING.md` décrivant le GitHub Flow, les Conventional Commits et SemVer
- Documentation (`docs/ARCHITECTURE.md`, `docs/DATABASE.md`, `docs/INSTALLATION.md`)
- Script d'init DB (`init-db/01-petstore.sql`)
- `src/main/resources/META-INF/persistence.xml` — persistence-unit `petstore-pu`, Hibernate `hbm2ddl=create`
- `fr.esaip.petstore.config.EntityManagerFactoryProvider` — EMF singleton thread-safe + shutdown hook

## [0.1.0] — 2026-04-23

### Added
- Commit initial — `README.md`, `LICENSE` (MIT), `.gitignore`
- Sujet du TP (`tp-eval-pet-store.pdf`) à la racine
- Config IntelliJ partagée (`.idea/misc.xml`, `modules.xml`, `vcs.xml`)

[Unreleased]: https://github.com/CybLow/multi-couches/compare/v0.9.0...HEAD
[0.9.0]: https://github.com/CybLow/multi-couches/compare/v0.5.0...v0.9.0
[0.5.0]: https://github.com/CybLow/multi-couches/compare/v0.3.0...v0.5.0
[0.3.0]: https://github.com/CybLow/multi-couches/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/CybLow/multi-couches/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/CybLow/multi-couches/releases/tag/v0.1.0

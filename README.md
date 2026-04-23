# multi-couches — TP Eval Pet Store

[![Release](https://img.shields.io/github/v/release/CybLow/multi-couches)](../../releases/latest)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java 17](https://img.shields.io/badge/java-17-red.svg)](https://adoptium.net)
[![Hibernate 6.4](https://img.shields.io/badge/hibernate-6.4.4-59666C.svg)](https://hibernate.org)

> **ESAIP IRA3 · Java / ORM / JPA / Hibernate · DevOps 1 · 2025-2026**
>
> Projet **multi-couches** JPA modélisant une animalerie, dans le cadre des modules
> *ORM / JPA / Hibernate* et *DevOps 1* encadrés par [Séga Sylla](mailto:sega.sylla.biz@gmail.com).

---

## Auteurs

| Prénom NOM | Compte GitHub |
|---|---|
| **Lois MARTIN** | [@CybLow](https://github.com/CybLow) |
| **Maksim DUDARENKA** | [@dimitriLeChasseur](https://github.com/dimitriLeChasseur) |

---

## Contexte du TP

Le sujet complet est joint à la racine : [`tp-eval-pet-store.pdf`](tp-eval-pet-store.pdf).

**En résumé** — modéliser une animalerie en JPA avec :

- Un projet **multi-couches** bien isolé (packages `entity` / `dao` / `service` / `config`)
- Une base de données `petstore` dont le schéma est contrôlé par l'application (`hbm2ddl.auto=create`)
- Les 3 relations imposées : `@OneToMany`, `@ManyToMany`, `@ManyToOne`
- La stratégie d'héritage **JOINED** pour `Animal` → `Fish` / `Cat`
- Au moins **3 enregistrements par table** via `EntityManager`
- Une **requête JPQL** extrayant tous les animaux d'une animalerie donnée
- Un historique Git **propre** avec Pull Requests reviewées (GitHub Flow, SemVer)

---

## Architecture multi-couches

```
┌────────────────────────────────────────────────────┐
│                      Main                          │  point d'entrée
├────────────────────────────────────────────────────┤
│                  service/                          │  logique métier
│          PetStoreService, SeedService              │   (orchestration)
├────────────────────────────────────────────────────┤
│                    dao/                            │  accès aux données
│   GenericDao, {Address,Animal,PetStore,Product}Dao │   (EntityManager)
├────────────────────────────────────────────────────┤
│                   entity/                          │  modèle JPA
│     Address, Animal, Cat, Fish, PetStore,          │   (annotations)
│     Product + enums/                               │
├────────────────────────────────────────────────────┤
│                   config/                          │  infrastructure
│        EntityManagerFactoryProvider                │   (persistence.xml)
└────────────────────────────────────────────────────┘
```

Détails : [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

## Modèle de données

```
                                 Address
                                   ^ 1
                                   |  @ManyToOne(unique=true) — 1:1 simulé
                                   |
                             1  PetStore  N <────────── @ManyToMany ──────── Product
                                   |                                           |
                                   |  @OneToMany(mappedBy="petStore")          |
                                   |  cascade=ALL, orphanRemoval=true          |
                                   v N                                         |
                               Animal  (JOINED)                                |
                                ^   ^                                          |
                                |   |                                          |
                               Fish Cat                                        |
                                                                 type = ProdType
                                                                 (FOOD / ACCESSORY /
                                                                  CLEANING)
```

- **6 entités** : `Address`, `PetStore`, `Product`, `Animal` (abstract), `Fish`, `Cat`
- **2 enums** : `ProdType` (FOOD/ACCESSORY/CLEANING), `FishLivEnv` (FRESH_WATER/SEA_WATER)

---

## Démarrage rapide

**Prérequis** — Java 17+, Maven 3.9+, Podman (ou Docker) avec un conteneur
MariaDB local nommé `jpa-mariadb` et exposé sur le port 3306. Détails :
[`docs/INSTALLATION.md`](docs/INSTALLATION.md).

```bash
# 1. Cloner
git clone git@github.com:CybLow/multi-couches.git
cd multi-couches

# 2. Créer la DB petstore (le schéma lui-même sera créé par Hibernate)
podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql

# 3. Lancer l'application
mvn clean compile exec:java
```

**Sortie attendue** :

```
=== TP Eval Pet Store — démarrage ===

--- 1. Insertion des données de démonstration ---
[logs Hibernate CREATE TABLE × 7, INSERT × 16]
[OK] Seed terminé, transaction commitée.

--- 2. Vérification du nombre d'enregistrements ---
  • 3 Addresses
  • 3 PetStores
  • 4 Animals
  • 3 Products

--- 3. Animaux de la 1re animalerie (requête JPQL imposée) ---
Animalerie ciblée : PetStore#1 'Animalis Bastille' (manager=Alice MARTIN)
  - Cat#1 (chip=250269606123456, noir)
  - Fish#2 (FRESH_WATER, bleu)
[OK] Requête exécutée : 2 animal(s) trouvé(s).

=== TP Eval Pet Store — terminé ===
```

## Vérification en base

```bash
podman exec jpa-mariadb mariadb -ujpa_user -pjpa_pass petstore -e "SHOW TABLES;"
podman exec jpa-mariadb mariadb -ujpa_user -pjpa_pass petstore \
    -e "SELECT a.id, a.couleur, f.living_env, c.chip_id
        FROM animal a
        LEFT JOIN fish f ON f.id = a.id
        LEFT JOIN cat  c ON c.id = a.id;"
```

---

## Structure du projet

```
multi-couches/
├── .github/                ISSUE/PR templates + CODEOWNERS
├── docs/                   ARCHITECTURE, DATABASE, INSTALLATION
├── init-db/
│   └── 01-petstore.sql     Création de la DB + grants jpa_user
├── src/main/
│   ├── java/fr/esaip/petstore/
│   │   ├── Main.java
│   │   ├── config/         EntityManagerFactoryProvider
│   │   ├── entity/         Address, Animal, Cat, Fish, PetStore, Product
│   │   │   └── enums/      FishLivEnv, ProdType
│   │   ├── dao/            GenericDao + 4 DAOs
│   │   └── service/        SeedService, PetStoreService
│   └── resources/META-INF/
│       └── persistence.xml
├── pom.xml
├── README.md               ce fichier
├── CONTRIBUTING.md         workflow DevOps + conventions
├── CHANGELOG.md            Keep a Changelog
├── LICENSE                 MIT
└── tp-eval-pet-store.pdf   sujet original
```

---

## Workflow DevOps

Le projet suit un **GitHub Flow strict** à 2 mains :

- **Issues** → créées depuis les templates `.github/ISSUE_TEMPLATE/`, labellisées, assignées, rattachées au milestone `v1.0.0`
- **Branches** → nommées `<type>/<N>-<slug>` (`feat/5-animal-entity`, `chore/1-project-scaffold`…)
- **Commits** → [Conventional Commits 1.0](https://www.conventionalcommits.org/) (`feat(entity): add Product`)
- **Pull Requests** → template rempli, review obligatoire par le binôme ; plusieurs PRs ont passé par un cycle « request changes → fix → approve » pour refléter une collaboration réelle
- **Releases** → [SemVer 2.0](https://semver.org/), taguées sur `main` avec release notes : `v0.1.0` → `v0.2.0` → `v0.3.0` → `v0.5.0` → `v0.9.0` → **`v1.0.0`**
- **`main` protégée** → pas de push direct, 1 review + statuts verts requis, historique linéaire

Détails : [`CONTRIBUTING.md`](CONTRIBUTING.md).

### Statistiques du projet

- **13 issues** fermées (réparties entre les 2 comptes)
- **18 Pull Requests** mergées (features + releases + fix)
- **60+ commits** en Conventional Commits
- **6 releases** taguées avec release notes

---

## Versions & Releases

| Version | Date | Contenu principal |
|---|---|---|
| [`v1.0.0`](../../releases/tag/v1.0.0) | 2026-04-23 | Release finale — Main + docs complètes |
| [`v0.9.0`](../../releases/tag/v0.9.0) | 2026-04-23 | Couches DAO + Service |
| [`v0.5.0`](../../releases/tag/v0.5.0) | 2026-04-23 | Modèle JPA complet (6 entités + 3 relations + JOINED) |
| [`v0.3.0`](../../releases/tag/v0.3.0) | 2026-04-23 | Enums + Address |
| [`v0.2.0`](../../releases/tag/v0.2.0) | 2026-04-23 | Scaffold Maven + persistence config |
| [`v0.1.0`](../../releases/tag/v0.1.0) | 2026-04-23 | Initial commit |

Historique détaillé : [`CHANGELOG.md`](CHANGELOG.md).

---

## Documentation complémentaire

- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — détail multi-couches + relations JPA + choix de design
- [`docs/DATABASE.md`](docs/DATABASE.md) — schéma DB, commandes podman, troubleshooting
- [`docs/INSTALLATION.md`](docs/INSTALLATION.md) — install OS-par-OS + IntelliJ
- [`CONTRIBUTING.md`](CONTRIBUTING.md) — conventions Git + workflow DevOps

---

## Licence

[MIT](LICENSE) — © 2026 Lois MARTIN, Maksim DUDARENKA.

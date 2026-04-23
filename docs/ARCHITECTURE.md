# Architecture

> Document vivant — complété au fil des PRs qui ajoutent les couches.

## 🧱 Principe multi-couches

Le projet respecte une architecture **multi-couches** classique : chaque couche
a une responsabilité unique et ne dépend que des couches inférieures.

```
┌────────────────────────────────────────────────────┐
│                      Main                          │  ← point d'entrée
├────────────────────────────────────────────────────┤
│                  service/                          │  ← logique métier
│          PetStoreService, SeedService              │     (orchestration)
├────────────────────────────────────────────────────┤
│                    dao/                            │  ← accès aux données
│   GenericDao, {Address,Animal,PetStore,Product}Dao │     (EntityManager)
├────────────────────────────────────────────────────┤
│                   entity/                          │  ← modèle JPA
│     Address, Animal, Cat, Fish, PetStore,          │     (annotations)
│     Product + enums/                               │
├────────────────────────────────────────────────────┤
│                   config/                          │  ← infrastructure
│        EntityManagerFactoryProvider                │     (persistence.xml)
└────────────────────────────────────────────────────┘
```

## 📊 Diagramme UML des entités

Voir le PDF du sujet à la racine : [`tp-eval-pet-store.pdf`](../tp-eval-pet-store.pdf).

### Résumé texte

- **Product** — `id`, `code`, `label`, `type: ProdType`, `price`
- **PetStore** — `id`, `name`, `managerName`
- **Address** — `id`, `number`, `street`, `zipCode`, `city`
- **Animal** *(abstract)* — `id`, `birth`, `couleur`
    - **Fish** *(extends Animal)* — `livingEnv: FishLivEnv`
    - **Cat** *(extends Animal)* — `chipId`

### Enums

- **ProdType** : `FOOD`, `ACCESSORY`, `CLEANING`
- **FishLivEnv** : `FRESH_WATER`, `SEA_WATER`

## 🔗 Relations JPA

| Relation UML | Annotation choisie | Propriétaire | Détail |
|---|---|---|---|
| Product N — N PetStore | `@ManyToMany` | `Product` | Table de jointure `product_petstore (product_id, petstore_id)`. Côté `PetStore` : `@ManyToMany(mappedBy="petStores")` |
| PetStore 1 — 1 Address | `@ManyToOne` + `unique=true` | `PetStore` | Le sujet impose de n'utiliser que `@OneToMany`, `@ManyToMany`, `@ManyToOne`. Un `@ManyToOne` avec `unique=true` sur la FK simule parfaitement un 1:1 côté DB. |
| PetStore 1 — N Animal | `@OneToMany(mappedBy="petStore")` côté `PetStore` + `@ManyToOne` côté `Animal` | `Animal` | `cascade=ALL, orphanRemoval=true` sur le OneToMany |

### Diagramme des relations (texte)

```
                             @ManyToOne (unique)
            PetStore ───────────────────────────────►  Address
               │                                        (petstore.address_id)
               │
               │ @OneToMany (mappedBy="petStore")
               │ @ManyToOne côté Animal
               ▼
             Animal ◄──────┬────── Fish  (JOINED)
               ▲           └────── Cat   (JOINED)
               │
               │ @ManyToMany(mappedBy="petStores")
               │
           Product ────────────────────► (table product_petstore)
                          @ManyToMany
                          @JoinTable
```

## 🌳 Héritage

**Stratégie imposée par le sujet** : `@Inheritance(strategy = InheritanceType.JOINED)`.

- `Animal` est `@Entity` + `abstract` (pas `@MappedSuperclass`, sinon JOINED ne fonctionne pas)
- `Fish` et `Cat` ont leurs propres tables, reliées à `animal.id` via PK

Avantages : normalisation DB, pas de colonnes NULL inutiles.

## 🎛 Choix de design documentés

- **`@Enumerated(EnumType.STRING)`** pour `Product.type` et `Fish.livingEnv` : résiste au réordonnancement des enums (contrairement à `ORDINAL`).
- **Propriétaire `@ManyToMany` = `Product`** : choix arbitraire, on aurait pu prendre `PetStore`. Les deux fonctionnent.
- **`@ManyToOne` + `unique=true`** pour `PetStore → Address` plutôt que `@OneToOne` : respecte la contrainte du sujet et documente explicitement l'unicité côté DB.
- **`java.util.Date` + `@Temporal(DATE)`** pour `Animal.birth` : conforme au style TP classique (pas `LocalDate`).
- **`hibernate.hbm2ddl.auto = create`** : l'application prend la main sur la création du schéma (consigne explicite du sujet).

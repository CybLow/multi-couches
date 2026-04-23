# Base de données — `petstore`

> Document vivant — complété au fur et à mesure que les entités sont créées.

## 🎯 Vue d'ensemble

- **SGBD** : MariaDB 10.x+ (ou MySQL 8+)
- **Nom de la DB** : `petstore`
- **Utilisateur applicatif** : `jpa_user` / `jpa_pass`
- **Contrôle du schéma** : l'application via `hibernate.hbm2ddl.auto=create` — Hibernate `DROP` puis `CREATE` toutes les tables au démarrage.

## 🛠 Setup du container MariaDB (Podman)

Le pattern de conteneur suit la convention du cours : un conteneur unique
`jpa-mariadb` partagé entre TPs, avec un volume `init-db/` qui charge les
scripts SQL au premier démarrage.

```bash
# Création initiale (une seule fois)
podman run -d \
  --name jpa-mariadb \
  -e MARIADB_ROOT_PASSWORD=root \
  -p 3306:3306 \
  -v $(pwd)/init-db:/docker-entrypoint-initdb.d:ro \
  mariadb:10.11

# Rejouer manuellement le script init après le 1er démarrage
podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql
```

## 📜 Script d'init — `init-db/01-petstore.sql`

Crée la base `petstore` et accorde les privilèges à `jpa_user`. Toutes les
tables elles-mêmes sont créées ensuite par Hibernate au démarrage.

## 🗄 Schéma généré

*Document mis à jour quand les entités sont en place.*

Tables attendues :

| Table | Entité | Remarques |
|---|---|---|
| `address` | `Address` | — |
| `animal` | `Animal` (JOINED) | Contient `id`, `birth`, `couleur`, FK `petstore_id` |
| `fish` | `Fish` | `id` (= PK de `animal`), `living_env` |
| `cat` | `Cat` | `id` (= PK de `animal`), `chip_id` |
| `pet_store` | `PetStore` | FK `address_id` (unique) |
| `product` | `Product` | — |
| `product_petstore` | Jointure `@ManyToMany` | `(product_id, petstore_id)` |

## 🔍 Commandes utiles

```bash
# Inspecter les tables créées par Hibernate
podman exec jpa-mariadb mariadb -ujpa_user -pjpa_pass petstore -e "SHOW TABLES;"

# Voir le DDL d'une table
podman exec jpa-mariadb mariadb -ujpa_user -pjpa_pass petstore -e "SHOW CREATE TABLE animal\G"

# Compter les enregistrements
podman exec jpa-mariadb mariadb -ujpa_user -pjpa_pass petstore -e "SELECT COUNT(*) FROM animal;"

# Dump complet pour backup
podman exec jpa-mariadb mariadb-dump -uroot -proot petstore > /tmp/petstore-backup.sql

# Reset total (efface tout)
podman exec jpa-mariadb mariadb -uroot -proot -e "DROP DATABASE IF EXISTS petstore;"
podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql
```

## 🚨 Troubleshooting

| Symptôme | Cause probable | Solution |
|---|---|---|
| `Communications link failure` | Conteneur pas démarré | `podman start jpa-mariadb` |
| `Access denied for 'jpa_user'@'localhost'` | Grants pas appliqués | Rejouer `01-petstore.sql` en root |
| `Unknown database 'petstore'` | Script init pas exécuté | `podman exec -i jpa-mariadb mariadb -uroot -proot < init-db/01-petstore.sql` |
| Port 3306 occupé | MySQL/MariaDB local déjà en route | Arrêter le service local (`sudo systemctl stop mariadb`) |

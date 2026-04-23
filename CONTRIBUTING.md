# Guide de contribution

> ⚙️ Ce document décrit le **processus DevOps** du projet `multi-couches`. Il
> vaut pour les 2 membres du binôme et toute personne externe qui forkerait le
> repo.

## 📋 Sommaire

1. [Modèle de branches : GitHub Flow](#-modèle-de-branches--github-flow)
2. [Conventional Commits](#-conventional-commits)
3. [Nommage des branches](#-nommage-des-branches)
4. [Workflow complet](#-workflow-complet)
5. [Pull Requests & reviews](#-pull-requests--reviews)
6. [Versioning sémantique](#-versioning-sémantique)

---

## 🌿 Modèle de branches : GitHub Flow

Le projet suit le **[GitHub Flow](https://docs.github.com/get-started/using-github/github-flow)** :
une seule branche permanente `main` — toujours intégrable — et des branches de
feature courtes qui sont mergées rapidement via Pull Request.

| Branche | Nature | Durée de vie |
|---|---|---|
| `main` | Intégrable, protégée (pas de push direct, PR + 1 review obligatoires) | Permanente |
| `feat/*`, `fix/*`, `docs/*`, `chore/*`, `refactor/*` | Branches de feature | Éphémères (≤ 1 journée, supprimées après merge) |

Pas de branche `develop`, pas de `release/*` : inutile pour un projet court à
équipe réduite.

---

## ✍️ Conventional Commits

Tous les messages de commit doivent respecter
[**Conventional Commits 1.0.0**](https://www.conventionalcommits.org/).

Format :

```
<type>(<scope>): <description en impératif>

<corps optionnel expliquant le pourquoi, pas le quoi>

<footer optionnel — Closes #N, BREAKING CHANGE, etc.>
```

### Types acceptés

| Type | Quand l'utiliser | Exemple |
|---|---|---|
| `feat` | Nouvelle fonctionnalité | `feat(entity): add Address entity` |
| `fix` | Correction de bug | `fix(dao): handle empty result list in findAll` |
| `docs` | Documentation pure | `docs(readme): add quick start section` |
| `chore` | Tooling, configuration, dépendances | `chore(maven): bump hibernate to 6.4.5` |
| `refactor` | Restructuration sans changement de comportement | `refactor(service): extract EntityManager factory` |
| `test` | Tests unitaires / d'intégration | `test(entity): add Animal inheritance test` |
| `style` | Formatage, indentation, points-virgules | `style: apply IntelliJ code format` |
| `perf` | Amélioration de performance | `perf(dao): add batch insert` |
| `build` | Build system (pom.xml, plugins) | `build(maven): add exec-maven-plugin` |
| `ci` | Intégration continue | `ci: add GitHub Actions workflow` |

### Scopes utilisés sur ce projet

`maven`, `github`, `config`, `entity`, `dao`, `service`, `main`, `readme`, `setup`, `db`.

### Exemples

```
feat(entity): add abstract Animal with JOINED inheritance

Configure @Inheritance(strategy = JOINED) pour permettre à Fish et Cat
d'avoir leurs propres tables reliées par PK. Respecte la consigne du sujet.

Closes #5
```

```
refactor(entity): apply review feedback — rename managerName column

Suite à la review de @dimitriLeChasseur, on utilise @Column(name="manager_name")
pour rester cohérent avec le snake_case de toutes les autres colonnes.
```

---

## 🌱 Nommage des branches

Format : `<type>/<numéro-issue>-<slug-kebab-case>`

- `<type>` : même liste que pour les commits (`feat`, `fix`, `docs`, `chore`, `refactor`…)
- `<numéro-issue>` : le numéro de l'issue GitHub traitée
- `<slug>` : description courte en kebab-case

Exemples :

```
feat/4-address-entity
feat/5-animal-entity
docs/12-readme-final
chore/1-project-scaffold
```

---

## 🔄 Workflow complet

1. **Créer l'issue** via un [template](./.github/ISSUE_TEMPLATE/) — title en Conventional Commits, labels `type: …` + `area: …`, milestone `v1.0.0 — Final Release`, s'assigner.
2. **Créer la branche** depuis `main` à jour :
   ```bash
   git switch main && git pull
   git switch -c feat/4-address-entity
   ```
3. **Coder par petits commits atomiques** (un commit = une idée). Ne pas craindre d'avoir plusieurs commits : ils seront squashés ou mergés selon la stratégie de la PR.
4. **Pusher la branche** :
   ```bash
   git push -u origin feat/4-address-entity
   ```
5. **Ouvrir la PR** via `gh pr create` — le template s'applique automatiquement. Mentionner `Closes #4` pour fermer l'issue au merge.
6. **Demander la review** au binôme (automatique via CODEOWNERS + `--reviewer`).
7. **Review** par le binôme : commentaires, suggestions, éventuellement `Request changes` → pousser des commits de fix → re-review → `Approve`.
8. **Merge** par l'auteur une fois approuvée :
   - `gh pr merge <N> --squash --delete-branch` pour les petites PRs
   - `gh pr merge <N> --merge --delete-branch` pour les grosses PRs structurantes (préserve l'historique atomique)
9. **Sync local** : `git switch main && git pull --prune`.

---

## 🔍 Pull Requests & reviews

### Stratégie de merge

| Type de PR | Stratégie | Pourquoi |
|---|---|---|
| Setup / petits changements / doc | `--squash` | Historique `main` lisible, 1 commit = 1 PR |
| Grosse feature structurante (entité complète, couche DAO, Main) | `--merge` | Préserve les commits atomiques pour l'audit / review a posteriori |

### Review

- **Review obligatoire** : 1 approbation requise par la branch protection.
- **CODEOWNERS** s'applique : tout changement est automatiquement soumis au binôme.
- **Review constructive** : pas de « LGTM » générique, indiquer ce qui a été vérifié (mapping JPA, nom de colonne, cascade, …).
- Si des changements sont demandés : l'auteur pousse des commits `refactor: apply review feedback — <précision>` sur la même branche.

---

## 🏷 Versioning sémantique

Le projet suit [**SemVer 2.0.0**](https://semver.org/) : `MAJOR.MINOR.PATCH`.

- `MAJOR` — changement non rétrocompatible
- `MINOR` — nouvelle fonctionnalité rétrocompatible
- `PATCH` — bug fix rétrocompatible

À chaque jalon significatif, on tague depuis `main` :

```bash
git tag -a vX.Y.Z -m "Release vX.Y.Z"
git push origin vX.Y.Z
gh release create vX.Y.Z --generate-notes --title "vX.Y.Z — <titre descriptif>"
```

Et on met à jour `CHANGELOG.md` (format [Keep a Changelog 1.1.0](https://keepachangelog.com/)).

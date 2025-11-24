# ICoop — Jeu 2D Coopératif

ICoop est un jeu coopératif en Java (**PlayEngine – EPFL**) où deux joueurs incarnent des entités élémentaires **Feu 🔥 / Eau 💧**.  
Le jeu repose sur l’exploration, l’utilisation d’objets et la gestion d’interactions élémentaires jusqu’à un boss final.

---

## 🎮 Concept

- Deux joueurs simultanés avec capacités distinctes.
- Interactions basées sur les affinités élémentaires (dégâts, murs, ennemis).
- Objets collectables avec usages (ex. bombe explosive).
- Boss final avec attaques à distance et vulnérabilité conditionnelle.

---

## 🧠 Principes appliqués

- **Double dispatch** via `ICoopInteractionVisitor`.
- **Polymorphisme** sur les entités (projectiles, ennemis, murs).
- **Behavior par cellule** (`ICoopBehavior`) : distingue `canWalk` / `canFly`.
- **Gestion d’état** (inventaire, immunité, élément actif).

---

## 🗂️ Structure

actor/ # Player, Ennemi, Boss, Projectile, Mur...
handler/ # Visitor
ICoopBehavior.java
ICoop.java # main

## 🚀 Extensions réalisées

- Boss final complet
- Extension de la map et nouvelles zones jouables
- Interactions avancées murs/éléments
- Projectiles traversant les zones non walkable (`canFly`)

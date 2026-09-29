# PROJECT GENESIS

An **original-superhero** Minecraft mod built on the [Palladium](https://www.curseforge.com/minecraft/mc-mods/threetag-palladium) powers framework.

> No Marvel, DC, or The Boys content. Every hero, serum, suit, and power in this mod is an original creation of the Genesis Dynamics universe.

---

## Concept

In the world of PROJECT GENESIS, the shadowy corporation **GENESIS DYNAMICS** created an experimental compound called the **GENESIS SERUM**. After the catastrophe known as **THE GENESIS EVENT**, its research scattered across the world.

You don't play as a pre-made hero. You find a serum, survive the transformation, and **build your own super-human**.

| Principle | Meaning |
|-----------|---------|
| **Power = body** | Powers live in the character, not the gear. |
| **Suit = equipment** | Suits only control, stabilise, or improve powers. |
| **Progression** | Unlock abilities across 4 stages (Manifestation → Adaptation → Mastery → Ascension). |
| **Stability** | A physiological limit. Overuse powers and you become unstable — never dead. |
| **Energy** | Short-term resource spent on active abilities. |

---

## The Four Serums

| Serum | Theme | Key Abilities |
|-------|-------|---------------|
| **Genesis Atlas** | Super-strength & durability | Enhanced Strength, Enhanced Durability, Power Jump, Ground Impact, **Titan State** |
| **Genesis Volt** | Speed & electricity | Enhanced Speed, Electric Discharge, Electric Dash, Chain Lightning, **Overcharge** |
| **Genesis Helios** | Flight & solar energy | Flight, Energy Blast, Heat Burst, **Solar Overdrive** |
| **Genesis Vector** | Telekinesis & force fields | Telekinetic Push, Telekinetic Pull, Force Field, **Vector Burst** |

---

## Requirements

| Component | Version |
|-----------|---------|
| Minecraft | **1.20.1** |
| Java | **17** |
| Loader | Forge **47.2.20+** or Fabric Loader **0.15.11+** |
| Palladium | **4.5.9+1.20.1** |
| Gradle | 8.x (via wrapper) |

---

## Building

```bash
./gradlew build
```

Compiled jars land in:
- `forge/build/libs/projectgenesis-0.1.0-forge.jar`
- `fabric/build/libs/projectgenesis-0.1.0-fabric.jar`

### Development client

```bash
# Forge
./gradlew :forge:runClient

# Fabric
./gradlew :fabric:runClient
```

---

## Testing Commands

All commands require operator permission level 2.

```
/genesis serum <atlas|volt|helios|vector> [player]
/genesis reset [player]
/genesis energy <0-1000>
/genesis stability <0-100>
/genesis unlock <1-4>
```

---

## Controls

| Action | Default Key |
|--------|-------------|
| Genesis Ability 1 | `V` |
| Genesis Ability 2 | `B` |
| Genesis Ability 3 | `N` |
| Genesis Ability 4 | `M` |
| Genesis Ultimate | `G` |
| Genesis Ability Wheel | `H` |

All keybinds are rebindable in the vanilla Controls screen.

---

## Project Structure

```
projectgenesis/
├── common/               # Loader-agnostic code (99 % of the mod)
│   └── src/main/java/com/genesisdynamics/projectgenesis/
│       ├── power/ability/    # Custom Palladium abilities
│       ├── condition/        # Custom Palladium conditions
│       ├── player/           # Energy, Stability, tick handler
│       ├── progression/      # Serum type + power stage persistence
│       ├── suit/             # Suit items & modifiers
│       ├── client/           # HUD, keybinds, client cache
│       ├── command/          # /genesis debug commands
│       └── registry/         # Items, suits, creative tab
├── forge/                # Forge entry point & client hooks
└── fabric/               # Fabric entry point & client hooks
```

Powers are **data-driven**: adding a new serum only requires a new JSON file under `common/src/main/resources/data/projectgenesis/palladium/powers/` plus (optionally) new ability classes.

---

## License

MIT

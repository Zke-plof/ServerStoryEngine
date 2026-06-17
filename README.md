<div align="center">

```
███████╗███████╗███████╗    ███████╗████████╗ ██████╗ ██████╗ ██╗   ██╗
██╔════╝██╔════╝██╔════╝    ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚██╗ ██╔╝
███████╗█████╗  ███████╗    ███████╗   ██║   ███████║██████╔╝ ╚████╔╝
╚════██║██╔══╝  ╚════██║    ╚════██║   ██║   ██╔══██║██╔══██╗  ╚██╔╝
███████║███████╗███████║    ███████║   ██║   ██║  ██║██║  ██║   ██║
╚══════╝╚══════╝╚══════╝    ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝

```

**Your server's memory. Written in stone.**

![Paper](https://img.shields.io/badge/Paper-26.1.2+-black?style=flat-square&labelColor=gold)
![License](https://img.shields.io/badge/license-MIT-black?style=flat-square&labelColor=gray)
![Version](https://img.shields.io/github/v/release/Zke-plof/ServerStoryEngine?style=flat-square&labelColor=crimson)

</div>

---

> *"That guy stole the dragon egg."*
> *"Our kingdom fought a war for 3 weeks."*
> *"Someone became rich from selling potatoes."*
>
> Minecraft forgets most of it. This plugin doesn't.

---

## The idea

Most plugins give you commands, GUIs, features.

This one gives you **stories**.

Every SMP has a history -- wars, heroes, betrayals, empires rising and falling. But the game doesn't remember any of it. Server Story Engine listens to what players do and writes it all down as narrative.

Not logs. Not timestamps. **History.**

```
[Chronicle] The Age of Dragons ended when Valdris struck down the beast
            and claimed victory over the End.
```

No two servers will ever have the same story.

---

## What's inside

```
📦 Plugin
 ├── 📖 Chronicle        -- Every event becomes a story entry
 ├── 🌍 Ages             -- Server shifts between eras based on activity
 ├── 📚 Books            -- Auto-generated written history books
 ├── 🏛️ Landmarks        -- Monuments placed at key locations
 ├── 🗣️ NPC Memory       -- Villagers remember and talk about events
 └── 💬 Discord          -- Live history feed to your Discord channel
```

<details>
<summary><b>📖 Chronicle</b> -- the living record</summary>

Every kill, every build, every milestone gets turned into a narrative entry. Browse it in chat or through a chest GUI.

```
/chronicle        -- read the history
/chronicle gui    -- browse it visually
/chronicle 3      -- jump to page 3
```
</details>

<details>
<summary><b>🌍 Ages</b> -- your server evolves</summary>

The plugin watches your server and shifts between eras based on what's happening:

| Age | Trigger | Feel |
|-----|---------|------|
| **Settlement** | Low population | The land is quiet |
| **Expansion** | Lots of building | Structures rise everywhere |
| **Conflict** | PvP spiking | War breaks out |
| **Prosperity** | Economy booming | Riches everywhere |

Players feel like they're living through history.
</details>

<details>
<summary><b>📚 Books</b> -- collectible lore</summary>

Auto-generated written books you can hold, trade, and stash in libraries.

```
/book The Fall of New Haven
```

A player could find a book describing a war that happened months before they joined.
</details>

<details>
<summary><b>🏛️ Landmarks</b> -- history you can touch</summary>

Gold blocks appear at dragon kill sites, battlefields, great builds. Walk up and right-click to read what happened there.

> *Here 24 warriors fought during the Crimson War.*
</details>

<details>
<summary><b>🗣️ NPC Memory</b> -- villagers that remember</summary>

Right-click a villager. They might tell you about old wars, fallen heroes, or legends.

> *"Have you heard? Valdris defeated the dragon years ago."*
</details>

<details>
<summary><b>💬 Discord</b> -- history goes online</summary>

Every event posts to a Discord channel with colored embeds. Red for kills, gold for money, magenta for dragons.

```
/discord token <bot-token>
/discord channel <channel-id>
/discord enable
```
</details>

---

## Setup

```
1. drop the jar in plugins/
2. restart the server
3. done
```

**Requires:** Paper 26.1.2+, Java 21+

---

## Commands

### Players

| Command | What it does |
|---------|-------------|
| `/chronicle [page]` | Read server history |
| `/chronicle gui` | Open the history browser |
| `/age` | See the current server age |
| `/book [title]` | Get a history book |
| `/landmark` | Find nearby monuments |

### Admins

| Command | What it does |
|---------|-------------|
| `/chronicle-admin add <title> <text>` | Write a custom entry |
| `/chronicle-admin delete <id>` | Remove an entry |
| `/chronicle-admin stats` | History statistics |
| `/chronicle-admin age <type>` | Force an age |
| `/chronicle-admin book <title>` | Generate a book |
| `/chronicle-admin reload` | Reload config |
| `/discord ...` | Configure Discord |

---

## What it looks like

```
[Chronicle] The Age of Dragons ended when Valdris struck down the beast
            and claimed victory over the End.

[Chronicle] Blood was spilled when DarkKnight claimed victory over
            ShadowMage in a fierce duel.

[Chronicle] The Crimson War has begun! The Iron Legion and The Shadow
            Clan clash in a devastating conflict.

[Chronicle] A merchant empire rises. PotatoLord amassed a fortune
            of 1,000,000 coins.

[Chronicle] The Age of Expansion has ended. A new era begins:
            the Age of Conflict.
```

---

## Config

Default config at `plugins/ServerStoryEngine/config.yml`. Everything is toggleable.

```yaml
# Age thresholds
ages:
  thresholds:
    settlement-population: 5
    expansion-buildings: 50
    conflict-pvp-rate: 10
    prosperity-economy: 1000000

# What to track
tracking:
  pvp-kills: true
  player-deaths: true
  block-placement: true
  dragon-kills: true
  player-joins: true
  economy: true

# Discord
discord:
  enabled: false
  token: ""
  channel-id: ""
  format: embed
```

---

## Build

```bash
git clone https://github.com/Zke-plof/ServerStoryEngine.git
cd ServerStoryEngine
mvn clean package
```

Output: `target/ServerStoryEngine-1.0.0.jar`

---

<div align="center">

[Download latest release](https://github.com/Zke-plof/ServerStoryEngine/releases) · [Report a bug](https://github.com/Zke-plof/ServerStoryEngine/issues)

</div>

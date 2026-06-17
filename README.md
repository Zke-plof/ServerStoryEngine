<div align="center">

![Server Story Engine](.github/assets/banner.svg)

[![Paper](https://img.shields.io/badge/Paper-26.1.2+-black?style=flat-square&labelColor=gold)](https://papermc.io)
[![License](https://img.shields.io/badge/license-MIT-gray?style=flat-square)](LICENSE)
[![Version](https://img.shields.io/github/v/release/Zke-plof/ServerStoryEngine?style=flat-square&labelColor=gray)](https://github.com/Zke-plof/ServerStoryEngine/releases)

---

*Every SMP has stories. This plugin makes sure they're never forgotten.*

</div>

## What it does

Server Story Engine watches your Paper server and turns events into a living chronicle. PvP kills, dragon slayings, wars, economy milestones, builds -- all of it becomes narrative history that players can read, collect, and interact with.

No two servers will ever have the same history.

## Features

- **Chronicle** -- browse server history in chat or a chest GUI
- **Ages** -- server shifts between Settlement, Expansion, Conflict, Prosperity based on activity
- **Books** -- auto-generated written books players can hold and collect
- **Landmarks** -- gold blocks at key locations with right-click lore
- **NPC Memory** -- villagers remember and talk about past events
- **Discord** -- live history feed with colored embeds

## Installation

```
1. Drop ServerStoryEngine.jar into plugins/
2. Restart server
3. Edit plugins/ServerStoryEngine/config.yml (optional)
```

Requires Paper 26.1.2+ and Java 21+.

## Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/chronicle [page]` | `storyengine.chronicle` | Browse history |
| `/chronicle gui` | `storyengine.chronicle` | History browser GUI |
| `/age` | `storyengine.age` | View current age |
| `/book [title]` | `storyengine.book` | Get a history book |
| `/landmark` | `storyengine.landmark` | Find nearby landmarks |
| `/chronicle-admin add <title> <text>` | `storyengine.admin` | Add custom entry |
| `/chronicle-admin delete <id>` | `storyengine.admin` | Remove entry |
| `/chronicle-admin stats` | `storyengine.admin` | History stats |
| `/chronicle-admin age <type>` | `storyengine.admin` | Force age change |
| `/chronicle-admin book <title>` | `storyengine.admin` | Generate book |
| `/chronicle-admin reload` | `storyengine.admin` | Reload config |
| `/discord ...` | `storyengine.admin` | Discord setup |

## Discord

```
/discord token YOUR_BOT_TOKEN
/discord channel YOUR_CHANNEL_ID
/discord enable
```

Bot needs **Message Content Intent** + **Send Messages** + **Embed Links** permissions. Create at [discord.com/developers](https://discord.com/developers/applications).

## Example

```
[Chronicle] The Age of Dragons ended when Valdris struck down the beast
            and claimed victory over the End.

[Chronicle] Blood was spilled when DarkKnight claimed victory over
            ShadowMage in a fierce duel.

[Chronicle] The Crimson War has begun! The Iron Legion and The Shadow
            Clan clash in a devastating conflict.

[Chronicle] A merchant empire rises. PotatoLord amassed a fortune
            of 1,000,000 coins.
```

## Build

```bash
git clone https://github.com/Zke-plof/ServerStoryEngine.git
cd ServerStoryEngine
mvn clean package
```

Output: `target/ServerStoryEngine-1.0.0.jar`

## License

MIT
# Server Story Engine

Your SMP has a story. This plugin writes it down.

Most plugins add commands. This one adds **history**. Every dragon kill, every war, every fortune earned gets turned into lore your players can actually read, collect, and remember.

---

## The problem

> "Remember when that guy stole the dragon egg?"
> "Wait, which guy? When did that happen?"

Minecraft forgets. Your server forgets. Players join and have no idea what happened before them.

## The fix

Server Story Engine watches your server and generates a living chronicle. Events become narrative. History becomes tangible.

```
[Chronicle] The Age of Dragons ended when Valdris struck down the beast
            and claimed victory over the End.

[Chronicle] The Crimson War has begun! The Iron Legion and The Shadow
            Clan clash in a devastating conflict.

[Chronicle] A merchant empire rises. PotatoLord amassed a fortune
            of 1,000,000 coins.
```

No two servers will ever have the same chronicle.

---

## What you get

**Chronicle** -- every event becomes a story. Browse in chat or a chest GUI.

**Ages** -- your server shifts between eras based on what's happening. Low pop = Settlement. Lots of building = Expansion. PvP spiking = Conflict. Economy booming = Prosperity.

**Books** -- auto-generated written books players can hold, trade, and stash in libraries. History you can hold in your hand.

**Landmarks** -- gold blocks drop at dragon kills, battle sites, great builds. Right-click to read what happened there.

**NPC Memory** -- villagers remember. Right-click one and they might tell you about old wars or fallen heroes.

**Discord** -- every event posts to your Discord with colored embeds. Red for kills, gold for money, magenta for dragons.

---

## Install

```
1. drop the jar in plugins/
2. restart the server
3. done
```

Requires **Paper 26.1.2+** and **Java 21+**

---

## Commands

### Players
| Command | Description |
|---------|-------------|
| `/chronicle [page]` | Read server history |
| `/chronicle gui` | Chest GUI browser |
| `/age` | See the current server age |
| `/book [title]` | Get a history book |
| `/landmark` | Find nearby monuments |

### Admins
| Command | Description |
|---------|-------------|
| `/chronicle-admin add <title> <text>` | Write your own entry |
| `/chronicle-admin delete <id>` | Remove an entry |
| `/chronicle-admin stats` | Server history stats |
| `/chronicle-admin age <type>` | Force an age change |
| `/chronicle-admin book <title>` | Create a history book |
| `/chronicle-admin reload` | Reload config |

---

## Discord setup

```bash
/discord token <your-bot-token>
/discord channel <channel-id>
/discord enable
```

Or set it in `config.yml`:

```yaml
discord:
  enabled: true
  token: "your-token-here"
  channel-id: "123456789012345678"
```

**Bot setup:** create at [discord.com/developers](https://discord.com/developers/applications) > enable **Message Content Intent** > invite with **Send Messages** + **Embed Links** > copy channel ID (right-click, Developer Mode on).

---

## What players see

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

Everything in `plugins/ServerStoryEngine/config.yml` is toggleable.

```yaml
ages:
  thresholds:
    settlement-population: 5
    expansion-buildings: 50
    conflict-pvp-rate: 10
    prosperity-economy: 1000000

tracking:
  pvp-kills: true
  player-deaths: true
  block-placement: true
  dragon-kills: true
  player-joins: true

discord:
  enabled: false
  token: ""
  channel-id: ""
```

---

## Build from source

```bash
git clone https://github.com/Zke-plof/ServerStoryEngine.git
cd ServerStoryEngine
mvn clean package
```

Output: `target/ServerStoryEngine-1.0.0.jar`

---

MIT License
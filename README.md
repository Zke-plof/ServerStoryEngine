<div align="center">
![Uploading minecraft_title.png…]()

# Server Story Engine

**Your server's memory. Written in stone.**

A Paper plugin that watches what happens and writes it into history.

</div>

---

> *"That guy stole the dragon egg."*
> *"Our kingdom fought a war for 3 weeks."*
> *"Someone became rich from selling potatoes."*
>
> Minecraft forgets most of it. This plugin doesn't.

---

## What it does

Server Story Engine listens to what players do and turns it into **narrative history**. Not logs. Not timestamps. Actual stories.

```
[Chronicle] The Age of Dragons ended when Zhayke struck down the beast
            and claimed victory over the End.
```

Every server builds its own unique chronicle. Wars, heroes, empires, betrayals -- it all gets written down.

---

## Features

<details>
<summary><b>Chronicle</b> -- the living record</summary>

Every kill, every build, every milestone gets a generated story entry. Browse it in chat or through a chest GUI.

```
/chronicle        -- read the history
/chronicle gui    -- browse it visually
/chronicle 3      -- jump to page 3
```
</details>

<details>
<summary><b>Ages</b> -- your server evolves</summary>

The plugin watches your server and shifts between eras based on what's happening:

| Age | When | What it means |
|-----|------|---------------|
| Settlement | Low population | The land is quiet |
| Expansion | Lots of building | Structures rise everywhere |
| Conflict | PvP spiking | War breaks out |
| Prosperity | Economy booming | Riches everywhere |

Players feel like they're living through history.
</details>

<details>
<summary><b>Books</b> -- collectible server lore</summary>

Auto-generated written books you can hold, trade, and hoard.

```
/book The Fall of New Haven     -- get a book with server history
```

Imagine finding a book that tells the story of a war that happened 3 months ago.
</details>

<details>
<summary><b>Landmarks</b> -- history you can touch</summary>

The plugin drops monuments at key spots -- dragon kill sites, battlefields, great builds. Gold blocks appear at the location.

Walk up, right-click. Read what happened there.

> *Here 24 warriors fought during the Crimson War.*
</details>

<details>
<summary><b>NPC Memory</b> -- villagers that remember</summary>

Right-click a villager and they might tell you about the dragon war, the great battle, or the rise of a merchant empire.

> *"Have you heard? Zhayke defeated the dragon years ago."*
</details>

<details>
<summary><b>Discord</b> -- history goes online</summary>

Every event posts to your Discord channel with rich embeds. Your community sees what happens even when they're offline.

Red for kills. Gold for money. Magenta for dragons. It looks good.
</details>

---

## Setup

**Requirements:** Paper 26.1.2+, Java 21+

```
1. drop the jar in plugins/
2. restart the server
3. done
```

Config lives at `plugins/ServerStoryEngine/config.yml`.

---

## Discord Setup

```bash
/discord token <your-bot-token>
/discord channel <channel-id>
/discord enable
```

Or set it in config:

```yaml
discord:
  enabled: true
  token: "your-token-here"
  channel-id: "123456789012345678"
```

**Full bot setup:**
1. [Create a bot](https://discord.com/developers/applications) and copy the token
2. Enable **Message Content Intent** under Privileged Gateway Intents
3. Invite with `Send Messages` + `Embed Links` permissions
4. Right-click your channel (Developer Mode on) -> Copy Channel ID
5. Paste both into the config or use the commands above

---

## Commands

<details>
<summary>Player commands</summary>

| Command | What it does |
|---------|-------------|
| `/chronicle [page]` | Read server history |
| `/chronicle gui` | Open the history browser |
| `/age` | See the current server age |
| `/book [title]` | Get a history book |
| `/landmark` | Find nearby monuments |
</details>

<details>
<summary>Admin commands</summary>

| Command | What it does |
|---------|-------------|
| `/chronicle-admin add <title> <text>` | Write your own entry |
| `/chronicle-admin delete <id>` | Remove an entry |
| `/chronicle-admin stats` | Server history stats |
| `/chronicle-admin age <type>` | Force an age change |
| `/chronicle-admin book <title>` | Create a book |
| `/chronicle-admin reload` | Reload config |
| `/discord <setup commands>` | Configure Discord |
</details>

---

## Examples

> *The Age of Dragons ended when Zhayke struck down the beast and claimed victory over the End.*

> *Blood was spilled when DarkKnight claimed victory over ShadowMage in a fierce duel.*

> *The Crimson War has begun! The Iron Legion and The Shadow Clan clash in a devastating conflict.*

> *A merchant empire rises. PotatoLord amassed a fortune of 1,000,000 coins.*

> *The Age of Expansion has ended. A new era begins: the Age of Conflict.*

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

**[Download Latest Release](https://github.com/Zke-plof/ServerStoryEngine/releases)**

</div>

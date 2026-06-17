# Server Story Engine

A Paper plugin that watches your server and writes its history.

---

## What it does

Tracks PvP, dragon kills, wars, economy milestones, builds, and turns them into narrative entries. Your server gets a living chronicle, automatic ages, books, landmarks, NPC memory, and Discord integration.

No two servers have the same story.

---

## Install

```
1. drop jar in plugins/
2. restart
3. edit plugins/ServerStoryEngine/config.yml
```

Requires: **Paper 26.1.2+**, **Java 21+**

---

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/chronicle [page]` | storyengine.chronicle | Read history |
| `/chronicle gui` | storyengine.chronicle | Chest GUI browser |
| `/age` | storyengine.age | Current server age |
| `/book [title]` | storyengine.book | Get history book |
| `/landmark` | storyengine.landmark | Find nearby monuments |
| `/chronicle-admin add <title> <text>` | storyengine.admin | Add custom entry |
| `/chronicle-admin stats` | storyengine.admin | History stats |
| `/chronicle-admin age <type>` | storyengine.admin | Force age change |
| `/discord token <token>` | storyengine.admin | Set bot token |
| `/discord channel <id>` | storyengine.admin | Set channel |
| `/discord enable` | storyengine.admin | Enable Discord |

---

## Discord

```bash
/discord token YOUR_BOT_TOKEN
/discord channel YOUR_CHANNEL_ID
/discord enable
```

Or in `config.yml`:

```yaml
discord:
  enabled: true
  token: "your-token"
  channel-id: "123456789012345678"
```

Bot needs: **Message Content Intent** + **Send Messages** + **Embed Links** permissions.

---

## Example output

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

## Ages

The plugin auto-detects your server state:

| Age | Trigger |
|-----|---------|
| Settlement | Low population |
| Expansion | Lots of building |
| Conflict | High PvP |
| Prosperity | Economy booming |

---

## Build

```bash
git clone https://github.com/Zke-plof/ServerStoryEngine.git
cd ServerStoryEngine
mvn clean package
```

Output: `target/ServerStoryEngine-1.0.0.jar`

---

## License

MIT
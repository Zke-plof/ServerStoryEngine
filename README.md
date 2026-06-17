# Server Story Engine

**A living chronicle plugin that turns player actions into server history.**

Server Story Engine watches important events on your Minecraft server and transforms them into a living, breathing history. Every PvP kill, every dragon slaying, every war, every fortune earned -- the plugin remembers it all and writes it into an epic chronicle.

No two servers will ever have the same story.

---

## Features

### Living Chronicle
Every significant event is recorded and turned into narrative text. Players can browse the server's history with `/chronicle` or through a chest GUI.

### Server Ages
The plugin detects the state of your server and transitions between ages:

| Age | Trigger | Description |
|-----|---------|-------------|
| **Age of Settlement** | Low population | The land is quiet, few souls call it home |
| **Age of Expansion** | Many structures built | New structures rise across the horizon |
| **Age of Conflict** | High PvP activity | The clash of steel echoes through the land |
| **Age of Prosperity** | Economy booms | Wealth flows like rivers through the markets |

### Historical Books
Automatically generate written books collecting server history. Players can literally hold the server's story in their hands.

### Landmarks
The plugin places monuments at significant locations -- first dragon kills, battle sites, great constructions. Right-click to read about what happened there.

### NPC Memory
Villagers remember events. Right-click a villager and they might tell you about the dragon war, the great battle, or the rise of a merchant empire.

### Discord Integration
Post every chronicle event to a Discord channel in real-time with rich embeds. Your community stays connected to the server's story even when offline.

---

## Requirements

- **Paper** 26.1.2 or later (also works on Spigot/Purpur)
- **Java** 21 or later
- **Minecraft** 26.1.2+ (Tiny Takeover / Chaos Cubed)

---

## Installation

1. Download `ServerStoryEngine-1.0.0.jar` from [Releases](https://github.com/Zke-plof/ServerStoryEngine/releases)
2. Drop it into your server's `plugins/` folder
3. Restart the server
4. Edit `plugins/ServerStoryEngine/config.yml` to customize settings

---

## Commands

### Player Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/chronicle [page]` | `storyengine.chronicle` | Browse the server chronicle |
| `/chronicle gui` | `storyengine.chronicle` | Open the chronicle GUI |
| `/age` | `storyengine.age` | View the current server age |
| `/book [title]` | `storyengine.book` | Generate a history book |
| `/landmark` | `storyengine.landmark` | Find nearby landmarks |

### Admin Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/chronicle-admin add <title> <narrative>` | `storyengine.admin` | Manually add an event |
| `/chronicle-admin delete <id>` | `storyengine.admin` | Delete an event |
| `/chronicle-admin stats` | `storyengine.admin` | View chronicle statistics |
| `/chronicle-admin age <type>` | `storyengine.admin` | Force an age transition |
| `/chronicle-admin book <title>` | `storyengine.admin` | Create a book and receive it |
| `/chronicle-admin reload` | `storyengine.admin` | Reload configuration |

### Discord Commands

| Command | Description |
|---------|-------------|
| `/discord token <token>` | Set the Discord bot token |
| `/discord channel <id>` | Set the channel for history posts |
| `/discord enable` | Enable Discord integration |
| `/discord disable` | Disable Discord integration |
| `/discord format <embed\|text>` | Set message format |
| `/discord status` | Check bot connection status |
| `/discord test` | Send a test message |

---

## Configuration

```yaml
# Database
database:
  file: chronicle.db

# Discord Integration
discord:
  enabled: false
  token: ""              # Bot token from discord.com/developers/applications
  channel-id: ""          # Channel ID to post history
  post-events: true
  format: embed           # "embed" or "text"
  timestamps: true
  prefix: "[Chronicle]"

# Age System
ages:
  enabled: true
  check-interval: 6000    # Ticks between age checks
  thresholds:
    settlement-population: 5
    expansion-buildings: 50
    conflict-pvp-rate: 10
    prosperity-economy: 1000000

# Event Tracking
tracking:
  pvp-kills: true
  player-deaths: true
  block-placement: true
  dragon-kills: true
  player-joins: true
  economy: true
  advancements: true

# Landmarks
landmarks:
  enabled: true
  max-distance: 10
  material: GOLD_BLOCK
  particles: true

# NPC Memory
npc-memory:
  enabled: true
  max-memory: 50
  mention-cooldown: 600

# Books
books:
  enabled: true
  max-pages: 15
  auto-generate: true

# Admin GUI
gui:
  enabled: true
  items-per-page: 45
```

---

## Discord Bot Setup

1. Go to [Discord Developer Portal](https://discord.com/developers/applications)
2. Click **New Application** -> name it -> **Create**
3. Go to **Bot** tab -> click **Reset Token** -> copy the token
4. Under **Privileged Gateway Intents**, enable:
   - `MESSAGE CONTENT INTENT`
5. Go to **OAuth2** -> **URL Generator**
6. Select scopes: `bot`
7. Select permissions: `Send Messages`, `Embed Links`
8. Copy the generated URL and invite the bot to your server
9. Enable Developer Mode in Discord (Settings -> Advanced -> Developer Mode)
10. Right-click your channel -> **Copy Channel ID**
11. In Minecraft, run:
    ```
    /discord token YOUR_TOKEN_HERE
    /discord channel YOUR_CHANNEL_ID
    /discord enable
    ```
12. Restart the server

---

## Event Examples

### Dragon Kill
> The Age of Dragons ended when Zhayke struck down the beast and claimed victory over the End.

### PvP Kill
> Blood was spilled when DarkKnight claimed victory over ShadowMage in a fierce duel.

### War
> The Crimson War has begun! The Iron Legion and The Shadow Clan clash in a devastating conflict.

### Economy
> A merchant empire rises. PotatoLord amassed a fortune of 1,000,000 coins.

### Age Transition
> The Age of Expansion has ended. A new era begins: the Age of Conflict.

---

## Building from Source

```bash
git clone https://github.com/Zke-plof/ServerStoryEngine.git
cd ServerStoryEngine
mvn clean package
```

The compiled JAR will be in `target/ServerStoryEngine-1.0.0.jar`.

---

## License

MIT License

```
MIT License

Copyright (c) 2026 Server Story Engine

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

# MetaMorph

[![GitHub](https://i.imgur.com/lsyt3GJ.png)](https://github.com/AlbusThePenguin/MetaMorph)
[![Donate](https://i.imgur.com/68XTxXM.png)](https://www.paypal.com/donate/?hosted_button_id=CZ8E9USK64P78)

![MetaMorph Animation](https://i.imgur.com/fypQrp2.gif)

MetaMorph is a versatile Minecraft plugin designed for players and administrators to customize their items with unique models. With seamless integration into Minecraft 1.20+ and Vault economy, players can unlock and apply stunning models to their items directly through an intuitive GUI. Administrators benefit from simple configuration management and powerful commands to control permissions and access. Allow players to select their own models to use! 😊

![Feature Showcase](https://i.imgur.com/H7jMeHG.gif)

---

## Features

- **Customizable Item Models:** Easily set unique Minecraft item models for various items.
- **Preview Models:** Players can preview models using an animated item display feature.
- **Permission-Based Model Access:** Each model can have individual permissions or be accessible to everyone with the "none" setting.
- **Economy Integration:** Models can be purchased using Vault-compatible economy plugins.
- **Simple GUI Interaction:**
  - Players can view owned and unowned models for the material of the item they hold.
  - Purchase and apply models directly through an easy-to-use interface.
- **Administrative Tools:**
  - Reload plugin configurations effortlessly.
  - Grant models to players directly via commands.
- **File-Based Configuration:**
  - Create and edit model configurations in the `/plugins/metamorph/models/` directory.

---

## Requirements

- **Minecraft:** Version 1.20 or higher.
- **Java:** Version 17 or higher.
- **[Vault](https://www.spigotmc.org/resources/vault.34315/):** Must be installed with a compatible economy plugin.
- **[LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/):** Required for permission management.

---

## Commands

### Player Commands

- **`/metamorph`**
  - **Description:** Opens the GUI to view, purchase, and apply models to the item in hand.
  - **Usage:** `/metamorph`

### Administrative Commands

- **`/metamorph give <player> <model>`**
  - **Description:** Grants a specific model to a player.
  - **Usage:** `/metamorph give Steve dragon_model`
- **`/metamorph reload`**
  - **Description:** Reloads the plugin configuration.
  - **Usage:** `/metamorph reload`

---

## Permissions

### General Permissions

- **Model-Specific Permissions:** Configure each model's permission.
  - **Description:** Grants access to a specific model.
  - **Note:** Set permission to `"none"` to allow anyone to access a model.
- **`mm.preview`**: Allows users to preview models with an animation started by middle mouse click on said model in the GUI.

### Administrative Permissions

- **`mm.admin`**
  - **Description:** Grants access to all administrative commands, including model management and configuration reloads.
  - **Default:** Operators only.

---

## Configuration Management

### Adding or Editing Models

1. Navigate to the plugin folder: `/plugins/metamorph/models/`
2. Create a new file or edit an existing one (e.g., `dragon_model.yml`).
3. Define the model's properties and permissions.
4. Reload configurations with `/metamorph reload`.

---

MetaMorph simplifies item customization in Minecraft, making it an essential tool for server administrators and players alike.

If you enjoy this plugin, feel free to consider a [donation](https://www.paypal.com/donate/?hosted_button_id=CZ8E9USK64P78) or a star or two!

![bStats](https://bstats.org/signatures/bukkit/MetaMorph.svg)

For bugs or issues, please use the [discussion](https://github.com/AlbusThePenguin/MetaMorph/discussions) section or PM me.

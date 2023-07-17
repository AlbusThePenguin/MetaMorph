MetaMorph is a plugin that gives all the players an interactive way of setting the item model or skin on any item in the game.
It uses permission(s) to give them the ability to set, remove, and change models on the item in their hand by selecting the model they want in a GUI.
You can easily add the items in-game to add new items into the config, or the old way by manually adding them into the config.
It's highly customizable. You can customize most messages (Supports Bukkit AND Hex Colors), and most aspects of the GUI can be changed like Size, filler item, next and back buttons, and the 'clean' button.

Permissions aren't that challenging, for players you would give them the mm.player for access to commands, and mm.admin for staff commands.

Commands: aliases: [memo, mem]
/metamorph - opens the GUI with all the models listed equal to the item in your hand. (mm.player)
/metamorph add - adds the item to the config to give the players more available models. (mm.admin)
/metamorph remove - removes the item model in your hand from the models.yml only if it exists in the models.yml. (mm.admin)
/metamorph reload <messages | models | config> - gives you access to reload one of them at a time. (mm.admin)
/metamorph clean - remove the model from the item in your hand. (mm.player)

Item Permission(s).
To give the players access to any/or multiple items you give them the permission mm.<item name>.<id> You can also do mm.<item name>.*

This project is available for download and usage at no cost and permissively licensed so it can remain free forever.

https://www.spigotmc.org/resources/metamorph-give-the-players-an-interactive-and-fun-way-of-changing-item-models-in-game.109270/add-version

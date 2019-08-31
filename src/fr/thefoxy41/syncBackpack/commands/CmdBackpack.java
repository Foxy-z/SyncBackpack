package fr.thefoxy41.syncBackpack.commands;

import fr.thefoxy41.syncBackpack.core.Messages;
import fr.thefoxy41.syncBackpack.core.managers.ChestManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBackpack implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is available only for players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("backpack.command")) {
            player.sendMessage(Messages.PREFIX);
            return true;
        }

        if (args.length < 2) {
            sendHelper(player);
            return true;
        }

        String arg1 = args[0];
        String arg2 = args[1];

        if (!player.hasPermission("backpack.command." + arg1.toLowerCase())) {
            player.sendMessage(Messages.CMD_NO_PERMISSION);
            return true;
        }

        if (arg1.equalsIgnoreCase("addchest")) {
            if (ChestManager.isCreatingChest(player.getUniqueId())) {
                player.sendMessage(Messages.ERROR + "Tu es déjà en train d'ajouter un coffre... Si tu souhaites changer le nom, attends quelques secondes !");
                return true;
            }

            if (ChestManager.chestExists(arg2)) {
                player.sendMessage(Messages.ERROR + "Ce coffre existe déjà sur ce serveur. Choisis un autre nom !");
                return true;
            }

            player.sendMessage(Messages.PREFIX + "Clique sur le coffre que tu souhaites transformer en backpack, tu as 15 secondes !");
            ChestManager.setCreatingChest(player, arg2);
        } else if (arg1.equalsIgnoreCase("removechest")) {
            String chestName = ChestManager.getNameExact(arg2);
            if (chestName == null) {
                player.sendMessage(Messages.ERROR + "Il n'y a aucun coffre portant ce nom sur ce serveur...");
                return true;
            }

            player.sendMessage(Messages.PREFIX + "Le coffre §a" + chestName + " §7a été supprimé !");
            ChestManager.removeChest(chestName);
        } else if (arg1.equalsIgnoreCase("chestlist")) {
            // TODO add this command
            player.sendMessage(Messages.PREFIX + "Soon !");
        } else if (arg1.equalsIgnoreCase("info")) {
            // TODO add this command
            player.sendMessage(Messages.PREFIX + "Soon !");
        } else if (arg1.equalsIgnoreCase("clear")) {
            // TODO add this command
            player.sendMessage(Messages.PREFIX + "Soon !");
        } else if (arg1.equalsIgnoreCase("view")) {
            // TODO add this command
            player.sendMessage(Messages.PREFIX + "Soon !");
        } else {
            sendHelper(player);
        }
        return false;
    }

    private void sendHelper(Player player) {
        player.sendMessage(
                Messages.PREFIX + "Mauvais usage, fais" +
                        "\n§7 - §a/backpack addchest <nom>" +
                        "\n§7 - §a/backpack removechest <nom>" +
                        "\n§7 - §a/backpack chestlist <server/all>" +
                        "\n§7 - §a/backpack info <pseudo>" +
                        "\n§7 - §a/backpack clear <pseudo>" +
                        "\n§7 - §a/backpack view <pseudo>"
        );
    }
}

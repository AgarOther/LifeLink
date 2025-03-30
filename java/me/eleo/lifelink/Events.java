package me.eleo.lifelink;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Events implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        // Si on ne reçoit pas de dégâts, on ne fait rien. (réglage du bug #2 dans la vidéo)
        // getFinalDamage est utilisée plutôt que getDamage car elle récupère les dégâts après armure/bouclier/...
        if (e.getFinalDamage() == 0)
            return ;

        // Change la barre de vie liée selon les dégâts reçus
        if (e.getEntityType() == EntityType.PLAYER) {
            // On récupère les dégâts avec la formule: (barre de vie liée - dégâts subis)
            double damage = LifeLink.sharedLife - e.getFinalDamage();
            // Si les dégâts sont inférieurs à 0, on les mets à 0 pour ne pas aller en négatif, et on met à jour la barre de vie liée
            LifeLink.sharedLife = damage > 0 ? damage : 0;
            // On envoie le son de prise de dégâts à chaque joueur
            for (Player p : Bukkit.getOnlinePlayers())
            {
                // Ne pas jouer le son si le joueur actuellement dans la boucle est le joueur ayant déclenché l'événement (pour ne pas qu'il l'entende 2 fois)
                if (e.getEntity() == p)
                    continue ;
                p.playSound(p, Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0F, 0.85F + (float) (Math.random() * 0.3));
            }
        }
    }

    @EventHandler
    public void regenHealth(EntityRegainHealthEvent e) {
        // Lorsqu'un joueur récupère de la vie, la barre de vie liée est mise à jour (avec une protection pour rester <= 20)
        if (e.getEntityType() == EntityType.PLAYER)
            LifeLink.sharedLife = (LifeLink.sharedLife + e.getAmount() > 20.0 ? 20 : LifeLink.sharedLife + e.getAmount());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        // Ne mets pas de message de mort dans le chat (suspense :D)
        e.setDeathMessage("");
    }

}

package fr.lightnew.ameliorama.entities;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WellOfSoul {

    private final UUID id;
    private Location location;
    private int nearBy;
    private int totalInspiredSoul;

    public WellOfSoul(Location location, int nearBy) {
        this.id = UUID.randomUUID();
        this.location = location;
        this.nearBy = nearBy;
        this.totalInspiredSoul = 0;
    }

    public WellOfSoul(Location location) {
        this(location, 5);
    }
}

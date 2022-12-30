package de.neo.signfinder.sign;

import org.bukkit.Location;

import java.util.List;

public class SignInfo {

    private Location location;
    private List<String> lines;

    public SignInfo(Location location, List<String> lines) {
        this.location = location;
        this.lines = lines;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getLines() {
        return lines;
    }
}

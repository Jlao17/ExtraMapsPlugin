package com.extramaps;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
enum DungeonIcon
{

    EAGLES_PEAK_DUNGEON("Eagles' Peak Dungeon", new WorldPoint(2328, 3496, 0));
    private final String tooltip;
    private final WorldPoint location;

    DungeonIcon(String tooltip, WorldPoint location)
    {
        this.tooltip = tooltip;
        this.location = location;
    }
}


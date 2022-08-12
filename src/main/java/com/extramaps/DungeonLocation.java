package com.extramaps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.util.ImageUtil;
import org.apache.commons.lang3.Range;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

@Getter
enum DungeonLocation
{
    APE_ATOLL_DUNGEON(Range.between(2760,2764), Range.between(2701,2705), 0, "/ApeAtollDungeon.jpg"),
    LIGHTHOUSE_BASEMENT(Range.between(2506, 2510), Range.between(3642, 3646), 0, "/LighthouseBasement.jpg"),
    SMOKE_DUNGEON(Range.between(3307, 3311), Range.between(2960, 2964), 0, "/SmokeDungeon.jpg"),
    UNDERGROUND_PASS(Range.between(2432, 2436), Range.between(3313, 3317), 0, "/UndergroundPass.jpg");

    private final Range<Integer> xRange;
    private final Range<Integer> yRange;
    private final int plane;
    private final String imagePath;

    DungeonLocation(Range<Integer> xRange, Range<Integer> yRange, int plane, String imagePath)
    {
        this.xRange = xRange;
        this.yRange = yRange;
        this.plane = plane;
        this.imagePath = imagePath;
    }

    public Range<Integer> getXRange ()
    {
        return this.xRange;
    }

    public Range<Integer> getYRange()
    {
        return this.yRange;
    }

    public int getPlane()
    {
        return this.plane;
    }

    public String getImagePath()
    {
        return this.imagePath;
    }

}

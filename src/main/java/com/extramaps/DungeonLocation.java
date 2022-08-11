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
    SHILO_VILLAGE_GEM_MINE(Range.between(2821,2825), Range.between(2999, 3003), 0, "/test2.png"),
    APE_ATOLL_DUNGEON(Range.between(2760,2764), Range.between(2701,2705), 0, "/ApeAtollDungeon.jpg"),
    LIGHTHOUSE_BASEMENT(Range.between(2507, 2509), Range.between(3643, 3645), 0, "/LighthouseBasement.jpg");

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

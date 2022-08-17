package com.extramaps;

import lombok.Getter;
import org.apache.commons.lang3.Range;


@Getter
enum DungeonLocation
{
    APE_ATOLL_DUNGEON(Range.between(2760,2764), Range.between(2701,2705), 0, "Ape Atoll Dungeon", "/dungeons/ApeAtollDungeon.jpg"),
    LIGHTHOUSE_BASEMENT(Range.between(2506, 2510), Range.between(3642, 3646), 0, "Lighthouse Basement", "/dungeons/LighthouseBasement.jpg"),
    SMOKE_DUNGEON(Range.between(3307, 3311), Range.between(2960, 2964), 0, "Smoke Dungeon" , "/dungeons/SmokeDungeon.jpg"),
    UNDERGROUND_PASS(Range.between(2432, 2436), Range.between(3313, 3317), 0, "Underground Pass" , "/dungeons/UndergroundPass.jpg"),
    JIGGIG_DUNGEON(Range.between(2483, 2487), Range.between(3041, 3045), 0, "Jiggig Dungeon" , "/dungeons/JiggigDungeon.jpg"),
    EAGLES_PEAK_DUNGEON(Range.between(2326, 2330), Range.between(3494, 3498), 0, "Eagles' Peak Dungeon" , "/dungeons/EaglesPeakDungeon.jpg");

    private final Range<Integer> xRange;
    private final Range<Integer> yRange;
    private final int plane;
    private final String dungeonName;
    private final String imagePath;

    DungeonLocation(Range<Integer> xRange, Range<Integer> yRange, int plane, String dungeonName, String imagePath)
    {
        this.xRange = xRange;
        this.yRange = yRange;
        this.plane = plane;
        this.dungeonName = dungeonName;
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

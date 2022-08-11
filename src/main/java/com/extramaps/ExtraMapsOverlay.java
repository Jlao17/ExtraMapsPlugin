package com.extramaps;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ExtraMapsOverlay extends Overlay
{
    private final Client client;
    private final ExtraMapsPlugin plugin;
    private final ExtraMapsConfig config;
    private final int MAP_GROUP_ID = 595;
    private final int MAP_BORDER_CHILD_ID = 5;
    private final int CLOSE_ICON_CHILD_ID = 38;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WorldMapOverlay worldMapOverlay;

    @Inject
    private ExtraMapsOverlay(Client client, ExtraMapsPlugin plugin, ExtraMapsConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setLayer(OverlayLayer.MANUAL);
        setPosition(OverlayPosition.DYNAMIC);
        drawAfterLayer(595, 22);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget widget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        if (widget == null)
        {
            return null;
        }
        renderTest(graphics);
        return null;
    }

    private void renderTest(Graphics2D graphics)
    {
        // The map widget.
        Widget mapWidget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        Point mapPoint = mapWidget.getCanvasLocation();
        int mapX = mapPoint.getX();
        int mapY = mapPoint.getY();
        int mapWidth = mapWidget.getWidth();
        int mapHeight = mapWidget.getHeight();
        // The close icon widget.
        Widget closeWidget = client.getWidget(MAP_GROUP_ID, CLOSE_ICON_CHILD_ID);
        Point closePoint = closeWidget.getCanvasLocation();
        int closeX = closePoint.getX();
        int closeY = closePoint.getY();
        int closeWidth = closeWidget.getWidth();
        int closeHeight = closeWidget.getHeight();

        BufferedImage map = ImageUtil.loadImageResource(ExtraMapsOverlay.class, plugin.getImagePath());
        BufferedImage closeIcon = ImageUtil.loadImageResource(ExtraMapsOverlay.class, "/CloseIcon.png");

        overlayManager.remove(worldMapOverlay);
//        graphics.setColor(Color.BLACK);
//        graphics.fillRect(mapX, mapY, mapWidth, mapHeight);
        graphics.drawImage(map, mapX, mapY, mapWidth, mapHeight, null);
        graphics.drawImage(closeIcon, closeX, closeY, closeWidth, closeHeight, null);

    }
}

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
        renderMap(graphics);
        return null;
    }

    private void renderMap(Graphics2D graphics)
    {
        // The map widget
        Widget mapWidget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        Point mapPoint = mapWidget.getCanvasLocation();
        int mapX = mapPoint.getX();
        int mapY = mapPoint.getY();
        int mapWidth = mapWidget.getWidth();
        int mapHeight = mapWidget.getHeight();
        // The close icon widget
        Widget closeWidget = client.getWidget(MAP_GROUP_ID, CLOSE_ICON_CHILD_ID);
        Point closePoint = closeWidget.getCanvasLocation();
        int closeX = closePoint.getX();
        int closeY = closePoint.getY();
        int closeWidth = closeWidget.getWidth();
        int closeHeight = closeWidget.getHeight();

        BufferedImage map = ImageUtil.loadImageResource(ExtraMapsOverlay.class, plugin.getImagePath());
        BufferedImage closeIcon = ImageUtil.loadImageResource(ExtraMapsOverlay.class, "/CloseIcon.png");
        BufferedImage compassIcon = ImageUtil.loadImageResource(ExtraMapsOverlay.class, "/OSRSCompass.png");

        // Remove worldMapOverlay to prevent WorldMapPlugin icons from drawing over the map
        overlayManager.remove(worldMapOverlay);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(mapX, mapY, mapWidth, mapHeight);
        drawScaledImage(map, mapWidget, graphics);
        graphics.drawImage(closeIcon, closeX, closeY, closeWidth, closeHeight, null);
        graphics.drawImage(compassIcon, closeX - 20, closeY + 30, null);

    }

    public static void drawScaledImage(BufferedImage image, Widget widget, Graphics2D graphics)
    {
        // Get map top left coordinates
        Point widgetPoint = widget.getCanvasLocation();
        int widgetX = widgetPoint.getX();
        int widgetY = widgetPoint.getY();

        int widgetWidth = widget.getWidth();
        int widgetHeight = widget.getHeight();

        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);

        double imgAspect = (double) imgHeight / imgWidth;


        double widgetAspect = (double) widgetHeight / widgetWidth;

        int x1 = 0; // Top left X position
        int y1 = 0; // Top left Y position
        int x2 = 0; // Bottom right X position
        int y2 = 0; // Bottom right Y position

        if (imgWidth < widgetWidth && imgHeight < widgetHeight)
        {
            // The image is smaller than the map
            x1 = (widgetWidth - imgWidth)  / 2;
            y1 = (widgetHeight - imgHeight) / 2;
            x2 = imgWidth + x1;
            y2 = imgHeight + y1;

        }
        else
        {
            if (widgetAspect > imgAspect)
            {
                y1 = widgetHeight;
                // Keep image aspect ratio
                widgetHeight = (int) (widgetWidth * imgAspect);
                y1 = (y1 - widgetHeight) / 2;
            }
            else
            {
                x1 = widgetWidth;
                // Keep image aspect ratio
                widgetWidth = (int) (widgetHeight / imgAspect);
                x1 = (x1 - widgetWidth) / 2;
            }
            x2 = widgetWidth + x1;
            y2 = widgetHeight + y1;
        }

        graphics.drawImage(image, widgetX , widgetY , x2, y2,
                0, 0, imgWidth, imgHeight, null);
    }
}

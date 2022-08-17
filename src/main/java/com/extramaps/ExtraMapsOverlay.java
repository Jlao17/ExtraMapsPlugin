package com.extramaps;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;


@Slf4j
public class ExtraMapsOverlay extends OverlayPanel implements MouseWheelListener, MouseMotionListener, MouseListener
{

    private final Client client;
    private final ExtraMapsPlugin plugin;
    private final ExtraMapsConfig config;
    private final int MAP_GROUP_ID = 595;
    private final int CLOSE_ICON_CHILD_ID = 38;
    private final String TITLE_FONT_COLOR = "#ff981f";
    private double zoomLevel = 1;
    public boolean showOriginal = true;
    public int curX;
    public int curY;
    private int dragX;
    private int dragY;
    private int deltaX;
    private int deltaY;
    private int pressedX;
    private int pressedY;

    private final BufferedImage closeIcon = ImageUtil.loadImageResource(ExtraMapsOverlay.class, "/CloseIcon.png");
    private final BufferedImage compassIcon = ImageUtil.loadImageResource(ExtraMapsOverlay.class, "/OSRSCompass.png");

    @Inject
    private ExtraMapsOverlay(Client client, ExtraMapsPlugin plugin, ExtraMapsConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        client.getCanvas().addMouseWheelListener(this::mouseWheelMoved);
        client.getCanvas().addMouseListener(this);
        client.getCanvas().addMouseMotionListener(this);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGHEST);
        setLayer(OverlayLayer.MANUAL);
        drawAfterInterface(WidgetID.WORLD_MAP_GROUP_ID);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        final Widget mapWidget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        if (mapWidget == null)
        {
            return;
        }

        if (mapWidget.getBounds().contains(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY()))
        {
            if (e.getWheelRotation() == 1)
            {
                zoomLevel = zoomLevel - 0.05;
            } else
            {
                zoomLevel = zoomLevel + 0.05;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        Widget mapWidget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        if (mapWidget == null)
        {
            return;
        }

        if (mapWidget.getBounds().contains(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY()))
        {
            showOriginal = false;
            java.awt.Point p = e.getPoint();

            dragX = p.x;
            dragY = p.y;
            deltaX = dragX - pressedX;
            deltaY = dragY - pressedY;
            pressedX = dragX;
            pressedY = dragY;

            if (curX == 0 && curY == 0)
            {
                curX = mapWidget.getCanvasLocation().getX();
                curY = mapWidget.getCanvasLocation().getY();
            } else
            {
                curX = curX + deltaX;
                curY = curY + deltaY;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Widget mapWidget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        if (mapWidget == null)
        {
            return;
        }

        if (mapWidget.getBounds().contains(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY()))
        {
            java.awt.Point point = e.getPoint();

            pressedX = point.x;
            pressedY = point.y;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }


    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Widget widget = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        if (widget == null)
        {
            return null;
        }

        renderMap(graphics);

        return super.render(graphics);
    }

    /**
     * Draws the map image on the overlay.
     *
     * @param graphics Graphics2D used to draw the items.
     * @return The map image with icons and black background.
     * @author Jlao17
     */
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
        // Title fonts and centering
        Font titleFont = new Font("Arial", Font.BOLD, 30);
        FontMetrics metrics = graphics.getFontMetrics(titleFont);
        int titleX = mapX + (mapWidth - metrics.stringWidth(plugin.getDungeonName())) / 2;
        int titleY = mapY + ((mapHeight - metrics.getHeight()) / 20) + metrics.getAscent();

        final Rectangle worldMapRectangle = mapWidget.getBounds();
        final Shape mapViewArea = getWorldMapClipArea(worldMapRectangle);

        // Set clip area of the map
        graphics.setClip(mapViewArea);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(mapX, mapY, mapWidth, mapHeight);
        drawScaledImage(plugin.getBufferedImage(), mapWidget, graphics);
        graphics.drawImage(closeIcon, closeX, closeY, closeWidth, closeHeight, null);
        graphics.drawImage(compassIcon, closeX - 20, closeY + 30, null);
        graphics.setColor(Color.decode(TITLE_FONT_COLOR));
        // Set the font
        graphics.setFont(titleFont);
        // Draw the String
        graphics.drawString(plugin.getDungeonName(), titleX, titleY);
    }

    /**
     * Draws the map image on the overlay.
     *
     * @param image    The image of the map to be displayed.
     * @param widget   The map widget to be used as reference for the image.
     * @param graphics Graphics2D used to draw the image.
     * @return An image drawn on the overlay.
     * @author Jason Lao
     */
    public void drawScaledImage(BufferedImage image, Widget widget, Graphics2D graphics)
    {
        // Get map top left coordinates
        Point widgetPoint = widget.getCanvasLocation();
        int widgetX = widgetPoint.getX();
        int widgetY = widgetPoint.getY();

        // Get image properties
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);

        // Apply zoom value
        int imageWidth = (int) (imgWidth * zoomLevel);
        int imageHeight = (int) (imgHeight * zoomLevel);

        if (showOriginal)
        {
            graphics.drawImage(image, widgetX, widgetY, imageWidth, imageHeight, null);
        } else
        {
            graphics.drawImage(image, curX, curY, imageWidth, imageHeight, null);
        }
    }

    /**
     * Gets a clip area which excludes the area of widgets which overlay the world map.
     *
     * @param baseRectangle The base area to clip from
     * @return An {@link Area} representing <code>baseRectangle</code>, with the area
     * of visible widgets overlaying the world map clipped from it.
     * @author WorldMapPlugin creators.
     */
    private Shape getWorldMapClipArea(Rectangle baseRectangle)
    {
        final Widget overview = client.getWidget(WidgetInfo.WORLD_MAP_OVERVIEW_MAP);
        final Widget surfaceSelector = client.getWidget(WidgetInfo.WORLD_MAP_SURFACE_SELECTOR);

        Area clipArea = new Area(baseRectangle);
        boolean subtracted = false;

        if (overview != null && !overview.isHidden())
        {
            clipArea.subtract(new Area(overview.getBounds()));
            subtracted = true;
        }

        if (surfaceSelector != null && !surfaceSelector.isHidden())
        {
            clipArea.subtract(new Area(surfaceSelector.getBounds()));
            subtracted = true;
        }

        // The sun g2d implementation is much more efficient at applying clips which are subclasses of rectangle2d,
        // so use that as the clip shape if possible
        return subtracted ? clipArea : baseRectangle;
    }
}

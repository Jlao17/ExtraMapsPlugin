package com.extramaps;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.RenderOverview;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.Arrays;


@Slf4j
@PluginDescriptor(
	name = "Extra Maps"
)
public class ExtraMapsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExtraMapsConfig config;

	@Inject
	private WorldMapPointManager worldMapPointManager;

	@Inject
	private WorldMapOverlay worldMapOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ExtraMapsOverlay extraMapsOverlay;

	private BufferedImage imagePath;

	public boolean overlayDrawn = false;



	@Override
	protected void startUp() throws Exception
	{
		log.info("ExtraMaps started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("ExtraMaps stopped!");
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		final Widget map = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
		if (map != null)
		{

			Point mousePos = client.getMouseCanvasPosition();
			float zoom = client.getRenderOverview().getWorldMapZoom();
			RenderOverview renderOverview = client.getRenderOverview();
			final WorldPoint mapWorldPoint = new WorldPoint(renderOverview.getWorldMapPosition().getX(), renderOverview.getWorldMapPosition().getY(), 0);
			final Point middle = worldMapOverlay.mapWorldPointToGraphicsPoint(mapWorldPoint);
			final int dx = (int) ((mousePos.getX() - middle.getX()) / zoom);
			final int dy = (int) ((-(mousePos.getY() - middle.getY())) / zoom);

			// Iterate over the DungeonLocation enum and set the correct map when a corresponding WorldPoint is clicked.
			Arrays.stream(DungeonLocation.values())
					.forEach(l ->
							{
								if (l.getXRange().contains(mapWorldPoint.dx(dx).dy(dy).getX()) &&
										l.getYRange().contains(mapWorldPoint.dx(dx).dy(dy).getY()) &&
										l.getPlane() == mapWorldPoint.dx(dx).dy(dy).getPlane() &&
										client.getMouseCurrentButton() == 1)
								{
									setBufferedImage(l.getImagePath());
									if (!overlayDrawn)
									{
										overlayManager.remove(worldMapOverlay);
										overlayManager.add(extraMapsOverlay);
										overlayDrawn = true;
									}
								}
							});
		}

		if (map == null)
		{
			overlayManager.add(worldMapOverlay);
			overlayManager.remove(extraMapsOverlay);
			overlayDrawn = false;
		}
	}



	public BufferedImage getBufferedImage()
	{
		return imagePath;
	}

	public void setBufferedImage(String p)
	{
		this.imagePath = ImageUtil.loadImageResource(ExtraMapsOverlay.class, p);
	}

	@Provides
	ExtraMapsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExtraMapsConfig.class);
	}

}

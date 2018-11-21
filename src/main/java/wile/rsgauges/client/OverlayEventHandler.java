/**
 * @file OverlayEventHandler.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Renders switch stati in one line if switches are looked
 * at and reconfigured. Replaces chat based switch status
 * messages to prevent chat spams.
**/
package wile.rsgauges.client;

import wile.rsgauges.ModAuxiliaries;
import wile.rsgauges.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Overlay rendering event handling
 */
@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class OverlayEventHandler extends Gui
{
  private final Minecraft mc;
  private static long deadline_;
  private static String text_;

  public static synchronized String text()
  { return text_; }

  public static synchronized long deadline()
  { return deadline_; }

  public static synchronized void hide()
  { deadline_ = 0; text_ = new String(); }

  public static synchronized void show(TextComponentTranslation s, int displayTimeoutMs)
  { text_ = (s==null)?(""):(s.getFormattedText()); deadline_ = System.currentTimeMillis() + displayTimeoutMs; }

  public static synchronized void show(String s, int displayTimeoutMs)
  { text_ = new String((s==null)?(""):(s)); deadline_ = System.currentTimeMillis() + displayTimeoutMs; }

  public static void register()
  { if(ModAuxiliaries.isClientSide()) MinecraftForge.EVENT_BUS.register(new OverlayEventHandler()); }

  OverlayEventHandler()
  { super(); mc=Minecraft.getMinecraft(); }

  @SubscribeEvent
  public void onRenderGui(RenderGameOverlayEvent.Post event)
  {
    if(event.getType() != RenderGameOverlayEvent.ElementType.CHAT) return;
    if(deadline() < System.currentTimeMillis()) return;
    String txt = text();
    if(txt.isEmpty()) return;
    ScaledResolution scaled = new ScaledResolution(mc);
    final FontRenderer fr = mc.fontRenderer;
    final boolean was_unicode = fr.getUnicodeFlag();
    try {
      final int cx = scaled.getScaledWidth() / 2;
      final int cy = (int)(scaled.getScaledHeight() * ModConfig.z_switch_status_overlay_y);
      final int w = fr.getStringWidth(txt);
      final int h = fr.FONT_HEIGHT;
      drawGradientRect(cx-(w/2)-3, cy-2, cx+(w/2)+2, cy+h+2, 0xaa333333, 0xaa444444);
      drawHorizontalLine(cx-(w/2)-3, cx+(w/2)+2,   cy-2, 0xaa333333);
      drawHorizontalLine(cx-(w/2)-3, cx+(w/2)+2, cy+h+2, 0xaa333333);
      drawVerticalLine  (cx-(w/2)-3,       cy-2, cy+h+2, 0xaa333333);
      drawVerticalLine  (cx+(w/2)+2,       cy-2, cy+h+2, 0xaa333333);
      drawCenteredString(fr, txt, cx , cy+1, 0x00ffaa00);
    } finally {
      fr.setUnicodeFlag(was_unicode);
    }
  }
}

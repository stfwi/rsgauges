/*
 * @file OverlayEventHandler.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Renders switch stati in one line if switches are looked
 * at and reconfigured. Replaces chat based switch status
 * messages to prevent chat spams.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * Overlay rendering event handling
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class OverlayEventHandler extends AbstractGui
{
  private final Minecraft mc;
  private static long deadline_;
  private static String text_;

  public static synchronized String text()
  { return text_; }

  public static synchronized long deadline()
  { return deadline_; }

  public static synchronized void hide()
  { deadline_ = 0; text_ = ""; }

  public static synchronized void show(ITextComponent s, int displayTimeoutMs)
  { text_ = (s==null)?(""):(s.getFormattedText()); deadline_ = System.currentTimeMillis() + displayTimeoutMs; }

  public static synchronized void show(String s, int displayTimeoutMs)
  { text_ = (s == null) ? ("") : (s); deadline_ = System.currentTimeMillis() + displayTimeoutMs; }

  public static void register()
  { if(ModRsGauges.proxy.mc() != null) MinecraftForge.EVENT_BUS.register(new OverlayEventHandler()); }

  OverlayEventHandler()
  { super(); mc = ModRsGauges.proxy.mc(); }

  @SubscribeEvent
  public void onRenderGui(RenderGameOverlayEvent.Post event)
  {
    if(event.getType() != RenderGameOverlayEvent.ElementType.CHAT) return;
    if(deadline() < System.currentTimeMillis()) return;
    String txt = text();
    if(txt.isEmpty()) return;
    final MainWindow win = mc.getMainWindow();
    final FontRenderer fr = mc.fontRenderer;
    final boolean was_unicode = fr.getBidiFlag();
    try {
      final int cx = win.getScaledWidth() / 2;
      final int cy = (int)(win.getScaledHeight() * ModConfig.switch_status_overlay_y);
      final int w = fr.getStringWidth(txt);
      final int h = fr.FONT_HEIGHT;
      fillGradient(cx-(w/2)-3, cy-2, cx+(w/2)+2, cy+h+2, 0xaa333333, 0xaa444444);
      hLine(cx-(w/2)-3, cx+(w/2)+2,   cy-2, 0xaa333333);
      hLine(cx-(w/2)-3, cx+(w/2)+2, cy+h+2, 0xaa333333);
      vLine(cx-(w/2)-3,       cy-2, cy+h+2, 0xaa333333);
      vLine(cx+(w/2)+2,       cy-2, cy+h+2, 0xaa333333);
      drawCenteredString(fr, txt, cx , cy+1, 0x00ffaa00);
    } finally {
      fr.setBidiFlag(was_unicode);
    }
  }

}

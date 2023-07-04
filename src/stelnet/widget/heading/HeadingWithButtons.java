package stelnet.widget.heading;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.*;
import uilib.Button;
import uilib.RenderableComponent;
import uilib.UiConstants;

public abstract class HeadingWithButtons extends RenderableComponent {

    protected void renderQueryHeading(
        final TooltipMakerAPI tooltip,
        final boolean isEnabled,
        final String headingText
    ) {
        renderHeading(tooltip, isEnabled, "", Global.getSector().getPlayerFaction());
        overlapQueryHeading(tooltip, isEnabled, headingText);
    }

    protected UIComponentAPI renderFirstButton(final Button delete, final float width, final TooltipMakerAPI tooltip) {
        delete.render(tooltip);
        final UIComponentAPI component = tooltip.getPrev();
        final PositionAPI componentPosition = component.getPosition();
        componentPosition.setXAlignOffset(width - componentPosition.getWidth() - 5);
        componentPosition.setYAlignOffset(UiConstants.DEFAULT_ROW_HEIGHT);
        return component;
    }

    protected UIComponentAPI renderNextButton(
        final Button button,
        final TooltipMakerAPI tooltip,
        final UIComponentAPI previousComponent
    ) {
        return renderNextButton(button, tooltip, previousComponent, 1);
    }

    protected UIComponentAPI renderNextButton(
        final Button button,
        final TooltipMakerAPI tooltip,
        final UIComponentAPI previousComponent,
        final float padding
    ) {
        button.render(tooltip);
        final UIComponentAPI currentComponent = tooltip.getPrev();
        currentComponent.getPosition().leftOfTop(previousComponent, padding);
        return currentComponent;
    }

    private void renderHeading(
        final TooltipMakerAPI tooltip,
        final boolean isEnabled,
        final String headingText,
        final FactionAPI faction
    ) {
        Color textColor = faction.getBaseUIColor();
        Color backgroundColor = faction.getDarkUIColor();
        if (!isEnabled) {
            textColor = Misc.scaleAlpha(textColor, 0.3f);
            backgroundColor = Misc.scaleAlpha(backgroundColor, 0.2f);
        }
        tooltip.addSectionHeading(headingText, textColor, backgroundColor, Alignment.LMID, 0);
    }

    private void overlapQueryHeading(final TooltipMakerAPI tooltip, final boolean isEnabled, final String heading) {
        tooltip.setParaFontVictor14();
        tooltip.setParaFontColor(Misc.getBrightPlayerColor());
        if (!isEnabled) {
            tooltip.setParaFontColor(Misc.scaleAlpha(Misc.getBrightPlayerColor(), 0.2f));
        }
        tooltip.addPara(heading, 0);
        tooltip.getPrev().getPosition().setYAlignOffset(16);
        tooltip.addSpacer(0);
        tooltip.getPrev().getPosition().setYAlignOffset(-3);
    }
}

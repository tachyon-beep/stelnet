package uilib;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.*;
import lombok.Setter;
import uilib.property.Size;

@Setter
public class ShowCargo extends RenderableShowComponent {

    private final CargoAPI cargo;
    private final String optionalTitle;
    private final String emptyDescription;
    private Color titleColor = Misc.getTextColor();

    public ShowCargo(final CargoAPI cargo, final String emptyDescription, final Size size) {
        this(cargo, null, emptyDescription, size);
    }

    public ShowCargo(final CargoAPI cargo, final String optionalTitle, final String emptyDescription, final Size size) {
        super(cargo.getStacksCopy().size());
        this.cargo = cargo;
        this.optionalTitle = optionalTitle;
        this.emptyDescription = emptyDescription;
        setSize(size);
    }

    @Override
    public void render(final TooltipMakerAPI tooltip) {
        if (optionalTitle != null) {
            final String fullTitle = String.format("%s (%d)", optionalTitle, cargo.getStacksCopy().size());
            addSectionTitle(tooltip, fullTitle, titleColor, getSize().getWidth() - UiConstants.DEFAULT_SPACER);
        }
        if (cargo.isEmpty()) {
            tooltip.addSpacer(UiConstants.DEFAULT_SPACER);
            tooltip.addPara(emptyDescription, 0);
            return;
        }
        tooltip.showCargo(cargo, getMaxElements(), false, 5);
        setOffsetOfLast(tooltip, -6);
    }
}

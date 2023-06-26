package stelnet.board.exploration;

import com.fs.starfarer.api.ui.ButtonAPI.UICheckboxSize;
import stelnet.util.L10n;
import uilib2.UiConstants;
import uilib2.button.Button;
import uilib2.button.CheckboxBasic;
import uilib2.intel.IntelCallbackBuilder;
import uilib2.intel.IntelUiAction;
import uilib2.intel.actions.UpdateIntelList;

public class BankButton extends EnumButton {

    private final IntelUiAction refreshAction;

    public BankButton(
        final ExplorationL10n buttonType,
        final IntelUiAction refreshAction,
        final float width,
        final boolean withShift
    ) {
        super(buttonType, width, withShift);
        this.refreshAction = refreshAction;
    }

    protected Button getButton(final String memoryKeyChecked, final boolean isChecked) {
        return new CheckboxBasic(
            (width - UiConstants.BUTTON_PADDING) / 2,
            UiConstants.BUTTON_HEIGHT,
            L10n.get(buttonType),
            new IntelCallbackBuilder()
                .addConfirmAction(new UpdateMemoryFlag(memoryKeyChecked, !isChecked))
                .addConfirmAction(new ActionFilterIntel())
                .addConfirmAction(new UpdateIntelList())
                .addConfirmAction(refreshAction)
                .build(),
            UICheckboxSize.SMALL,
            0
        );
    }
}

package uilib;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import java.awt.*;
import lombok.Getter;
import lombok.Setter;
import uilib.property.Size;

@Getter
@Setter
public class Button extends RenderableComponent implements ButtonHandler {

    private String title;
    private Color textColor;
    private Color backgroundColor;
    private boolean isEnabled;
    private ButtonHandler handler;
    private CutStyle cutStyle = CutStyle.ALL;
    private int padding = UiConstants.DEFAULT_BUTTON_PADDING;
    private int shortcut = 0;
    private boolean highlight = false;

    public Button(final Size size, final String title, final boolean isEnabled) {
        this(size, title, isEnabled, ColorHelper.basePlayerColor(), ColorHelper.darkPlayerColor());
    }

    public Button(final Size size, final String title, final boolean isEnabled, final Color color) {
        this(size, title, isEnabled, color, color);
        scaleBackground(0.5f);
    }

    public Button(
        Size size,
        final String title,
        final boolean isEnabled,
        final Color textColor,
        final Color backgroundColor
    ) {
        this.title = title;
        this.isEnabled = isEnabled;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        if (size.getWidth() == 0) {
            size = new Size(Global.getSettings().computeStringWidth(title, Fonts.DEFAULT_SMALL) + 20, size.getHeight());
        }
        setSize(size);
        setWithScroller(false);
    }

    @Override
    public boolean hasPrompt() {
        if (handler != null) {
            return handler.hasPrompt();
        }
        return false;
    }

    @Override
    public void onCancel(final IntelUIAPI ui) {
        if (handler != null) {
            handler.onCancel(ui);
        }
    }

    @Override
    public void onConfirm(final IntelUIAPI ui) {
        if (handler != null) {
            handler.onConfirm(ui);
        }
    }

    @Override
    public void onPrompt(final TooltipMakerAPI tooltipMaker) {
        if (handler != null) {
            handler.onPrompt(tooltipMaker);
        }
    }

    @Override
    public void render(final TooltipMakerAPI tooltip) {
        final Size size = getSize();
        final Color foregroundColor = getTextColor();
        final Color backgroundColor = getBackgroundColor();
        final ButtonAPI button = tooltip.addButton(
            tooltip.shortenString(getTitle(), size.getWidth()),
            this,
            foregroundColor,
            backgroundColor,
            Alignment.MID,
            cutStyle,
            size.getWidth() - padding,
            size.getHeight() - padding,
            padding / 2
        );
        button.setEnabled(isEnabled);
        if (shortcut > 0) {
            button.setShortcut(shortcut, false);
        }
        if (highlight) {
            button.highlight();
        }
    }

    public void scaleTextColor(final float scale) {
        textColor = Misc.scaleColor(textColor, scale);
    }

    public void scaleBackground(final float scale) {
        backgroundColor = Misc.scaleColor(backgroundColor, scale);
    }
}

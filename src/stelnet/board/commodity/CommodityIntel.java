package stelnet.board.commodity;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.*;
import java.util.List;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import stelnet.EmptyIntel;
import stelnet.board.IntelBasePlugin;
import stelnet.board.commodity.price.DemandPrice;
import stelnet.board.commodity.price.SupplyPrice;
import stelnet.board.commodity.view.intel.CommodityIntelInfo;
import stelnet.board.commodity.view.intel.CommodityIntelViewFactory;
import stelnet.settings.Modules;
import stelnet.util.L10n;
import stelnet.util.ModConstants;
import stelnet.util.StelnetHelper;
import uilib.Renderable;
import uilib.RenderableIntelInfo;
import uilib.property.Size;

@Getter
@Log4j
public class CommodityIntel extends IntelBasePlugin {

    private final String commodityId;
    private final IntelTracker intelTracker;
    private final MarketAPI market;
    private final float buyPrice;
    private final float sellPrice;
    private final String tag = ModConstants.TAG_COMMODITY;

    public CommodityIntel(final String commodityId, final IntelTracker intelTracker, final MarketAPI market) {
        super(market.getFaction(), market.getPrimaryEntity());
        this.commodityId = commodityId;
        this.intelTracker = intelTracker;
        this.market = market;
        this.buyPrice = getSupplyPrice();
        this.sellPrice = getDemandPrice();
    }

    public Object readResolve() {
        if (commodityId == null) {
            log.warn("Commodity is null, removing intel");
            remove();
            return new EmptyIntel();
        }
        if (!StelnetHelper.hasCommodity(commodityId)) {
            log.warn("Commodity " + commodityId + " no longer exists, removing intel");
            remove();
            return new EmptyIntel();
        }
        return this;
    }

    @Override
    public boolean isEnding() {
        final float supplyPrice = getSupplyPrice();
        final float demandPrice = getDemandPrice();
        final boolean buyChanged = isDifferent(buyPrice, supplyPrice);
        final boolean sellChanged = isDifferent(sellPrice, demandPrice);
        return buyChanged || sellChanged;
    }

    @Override
    public String getIcon() {
        return getCommodity().getIconName();
    }

    @Override
    public boolean isHidden() {
        return Modules.COMMODITIES.isHidden();
    }

    @Override
    public Color getCircleBorderColorOverride() {
        if (isEnding()) {
            return Misc.getGrayColor();
        }
        final String commodityId = getCommodityId();
        final CommodityOnMarketAPI commodityOnMarket = market.getCommodityData(commodityId);
        if (commodityOnMarket.getExcessQuantity() > 0) {
            return Misc.getPositiveHighlightColor();
        }
        if (commodityOnMarket.getDeficitQuantity() > 0) {
            return Misc.getNegativeHighlightColor();
        }
        return Misc.getTextColor();
    }

    public CommoditySpecAPI getCommodity() {
        return Global.getSector().getEconomy().getCommoditySpec(commodityId);
    }

    public void remove() {
        intelTracker.remove(this);
    }

    public float getDemandPrice() {
        return (new DemandPrice(commodityId)).getUnitPrice(market);
    }

    public float getSupplyPrice() {
        return (new SupplyPrice(commodityId)).getUnitPrice(market);
    }

    public String getTitle() {
        return L10n.commodity("INTEL_TITLE", getCommodity().getName(), market.getName());
    }

    public boolean isDifferent(final float oldPrice, final float newPrice) {
        return Math.abs(oldPrice - newPrice) >= 1;
    }

    @Override
    protected List<Renderable> getRenderableList(final Size size) {
        return (new CommodityIntelViewFactory(market, this)).create(size);
    }

    @Override
    protected RenderableIntelInfo getIntelInfo() {
        return new CommodityIntelInfo(this);
    }
}

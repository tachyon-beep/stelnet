package stelnet.board.exploration;

import com.fs.starfarer.api.impl.campaign.intel.AnalyzeEntityMissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.SurveyPlanetMissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.bar.events.historian.BaseHistorianOffer;
import com.fs.starfarer.api.impl.campaign.intel.bases.LuddicPathBaseIntel;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseIntel;
import com.fs.starfarer.api.impl.campaign.intel.misc.BreadcrumbIntel;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import stelnet.filter.Filter;
import stelnet.filter.IntelContainsTitle;
import stelnet.filter.IntelIsClass;
import stelnet.filter.IntelLocationHasMemory;
import stelnet.filter.LogicalAnd;
import stelnet.filter.LogicalNot;
import stelnet.filter.LogicalOr;
import stelnet.settings.CaptainsLogSettings;
import stelnet.util.ModConstants;

public class FilterFactory {

    private final Map<ExplorationL10n, Filter> enumToFilterMap = new LinkedHashMap<>();

    public FilterFactory() {
        final Filter bankFilter = new IntelIsClass(BreadcrumbIntel.class);
        final Filter captainsLogFilter = new IntelLocationHasMemory(ModConstants.CAPTAINS_LOG_INTEL);
        addTypes(bankFilter, captainsLogFilter);
        addBanks(bankFilter);
    }

    public Set<ExplorationL10n> keySet() {
        return enumToFilterMap.keySet();
    }

    public Filter get(final ExplorationL10n key) {
        return enumToFilterMap.get(key);
    }

    private void addTypes(final Filter bankFilter, final Filter captainsLogFilter) {
        final Map<ExplorationL10n, Filter> localMap = new LinkedHashMap<>();
        localMap.put(ExplorationL10n.TYPE_ANALYZE_MISSION, new IntelIsClass(AnalyzeEntityMissionIntel.class));
        localMap.put(ExplorationL10n.TYPE_HISTORIAN_OFFER, new IntelIsClass(BaseHistorianOffer.class));
        localMap.put(ExplorationL10n.TYPE_MEMORY_BANK, bankFilter);
        localMap.put(ExplorationL10n.TYPE_RAIDING_BASE, getRaidingBaseFilter());
        localMap.put(ExplorationL10n.TYPE_SURVEY_MISSION, new IntelIsClass(SurveyPlanetMissionIntel.class));
        if (CaptainsLogSettings.COLONY_STRUCTURES.isEnabled()) {
            localMap.put(ExplorationL10n.TYPE_COLONY_STRUCTURE, getTitleFilter(captainsLogFilter, "Structure"));
        }
        if (CaptainsLogSettings.COMM_RELAYS.isEnabled()) {
            localMap.put(ExplorationL10n.TYPE_COMM_RELAY, getTitleFilter(captainsLogFilter, "Comm Relay"));
        }
        if (CaptainsLogSettings.SALVAGEABLE.isEnabled()) {
            localMap.put(ExplorationL10n.TYPE_SALVAGEABLE, getTitleFilter(captainsLogFilter, "Salvageable"));
        }
        if (CaptainsLogSettings.RUINS.isEnabled()) {
            localMap.put(ExplorationL10n.TYPE_ANY_RUINS, getTitleFilter(captainsLogFilter, "Ruins"));
        }
        final Filter otherFilter = getOtherFilter(localMap);
        enumToFilterMap.put(ExplorationL10n.TYPE_OTHER, otherFilter);
        enumToFilterMap.putAll(localMap);
    }

    private void addBanks(final Filter bankFilter) {
        final Map<ExplorationL10n, Filter> localMap = new LinkedHashMap<>();
        localMap.put(ExplorationL10n.BANK_ANY_CACHE, getTitleFilter(bankFilter, "Cache"));
        localMap.put(ExplorationL10n.BANK_DEBRIS_FIELD, getTitleFilter(bankFilter, "Debris Field"));
        localMap.put(ExplorationL10n.BANK_DERELICT_SHIP, getTitleFilter(bankFilter, "Derelict Ship"));
        localMap.put(ExplorationL10n.BANK_DOMAIN_ERA_ENTITY, getTitleFilter(bankFilter, "Domain-era"));
        localMap.put(ExplorationL10n.BANK_ORBITAL_HABITAT, getTitleFilter(bankFilter, "Orbital Habitat"));
        localMap.put(ExplorationL10n.BANK_RUINS_LOCATION, getTitleFilter(bankFilter, "Ruins Location"));
        localMap.put(ExplorationL10n.BANK_SURVEY_DATA, getTitleFilter(bankFilter, "Survey Data for"));
        final Filter otherFilter = new LogicalAnd(
            Arrays.asList(enumToFilterMap.get(ExplorationL10n.TYPE_MEMORY_BANK), getOtherFilter(localMap)),
            "Other Banks"
        );
        enumToFilterMap.put(ExplorationL10n.BANK_OTHER, otherFilter);
        enumToFilterMap.putAll(localMap);
    }

    private Filter getTitleFilter(final Filter bankFilter, final String title) {
        return new LogicalAnd(
            Arrays.asList(bankFilter, new IntelContainsTitle(title)),
            "Compound Title Filter: " + title
        );
    }

    private Filter getOtherFilter(final Map<ExplorationL10n, Filter> localMap) {
        return new LogicalNot(new LogicalOr(localMap.values(), "Everything Else"));
    }

    private Filter getRaidingBaseFilter() {
        return new LogicalOr(
            Arrays.<Filter>asList(new IntelIsClass(LuddicPathBaseIntel.class), new IntelIsClass(PirateBaseIntel.class)),
            "Raiding Bases"
        );
    }
}

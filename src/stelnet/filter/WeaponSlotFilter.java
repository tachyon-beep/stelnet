package stelnet.filter;

import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import java.util.List;

public abstract class WeaponSlotFilter extends ShipHullFilter {

    @Override
    public boolean accept(final Object object) {
        if (object instanceof WeaponSlotAPI) {
            return acceptWeaponSlot((WeaponSlotAPI) object);
        }
        return super.accept(object);
    }

    @Override
    protected boolean acceptFleetMember(final FleetMemberAPI ship) {
        return acceptShipHull(ship.getHullSpec());
    }

    @Override
    protected boolean acceptShipHull(final ShipHullSpecAPI shipHull) {
        final List<WeaponSlotAPI> weaponSlots = shipHull.getAllWeaponSlotsCopy();
        for (final WeaponSlotAPI weaponSlot : weaponSlots) {
            if (acceptWeaponSlot(weaponSlot)) {
                return true;
            }
        }
        return false;
    }

    protected abstract boolean acceptWeaponSlot(WeaponSlotAPI weaponSlot);
}

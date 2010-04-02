package com.wwflgames.fury.item.effect.damage;

public abstract class Damage {

    public static final Damage MELEE_DAMAGE = new MeleeDamage("Melee");
    public static final Damage CRUSH_DAMAGE = new CrushDamage();
    public static final Damage SLASH_DAMAGE = new SlashDamage();
    public static final Damage STAB_DAMAGE = new StabDamage();
    public static final Damage MAGIC_DAMAGE = new MagicDamage();

    public static final Damage[] ALL_DAMAGE_TYPES = {
            MELEE_DAMAGE,
            CRUSH_DAMAGE,
            SLASH_DAMAGE,
            STAB_DAMAGE,
            MAGIC_DAMAGE
    };

    public static Damage forType(String type) {
        for (Damage damage : ALL_DAMAGE_TYPES) {
            if (type.equalsIgnoreCase(damage.getType())) {
                return damage;
            }
        }
        return null;
    }

    private String type;

    public Damage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

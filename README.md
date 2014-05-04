# Hard Mode Tweaks

Hard Mode Tweaks is a compilation of gameplay modifications to make survival a little more challenging, or things that I find interesting. One big feature included is the ability to change the lenth of the minecraft day. Other features include changing the damage that monster do, changing game rules from the config file and the hability to heal when you eat.

## Download

Download latest version: [Hard Mode Tweaks v1.0b3](http://www.mediafire.com/download/dctpqwbfacz5224/hardmodetweaks-1.0b3.jar)

## Installation

1. Install Forge.
2. Copy the "hardmodetweaks-1.0.b3.jar" file to the "mods" folder.

## Features

### Day Length Setting

Config option in hardmodetweaks.cfg:
```
options {
    D:dayLengthMultiplier=1.0
}
```

This configuration option allows you to change the length of the day. A value
of 2.0 makes the day twice as long, a value of 0.5 makes the day half of the
normal time. Defaults to 1.0, i.e. the same as vanilla, which is 20 minutes
(10 minutes daytime/10 minutes nighttime).

### Eating Regen

Config option in hardmodetweaks.cfg:
```
options {
    B:doEatingRegen=true
    I:foodHealingMinimum=3
    D:foodHealingMultiplier=0.3
}
```

When this option is set to true, you will heal a little bit depending on the food value of what you are eating (see http://minecraft.gamepedia.com/Food#Foods). The formula goes "(foodValue - foodHealingMinimum) * foodHealingMultiplier" rounded up, if this value is greater than zero, it heals you for that much. The lowest forms of food like carrots will not heal you anything at all. This setting is mainly targeted to use when the "naturalRegeneration" rule is turned off.

### Game Rules section

The whole "gamerules" section allows you to change the values of vanilla
gamerules. This gamerules are applied when the server is started up/the save
is loaded, and only if the gamemode is set to hard.

### Mobs section
```
mobs {
        I:creeperExplosionRadius=3
        D:skeletonDamageMultiplier=1.0
        D:spiderDamageMultiplier=1.0
        D:zombieDamageMultiplier=1.0
        D:endermanDamageMultiplier=1.0
        I:ghastExplosionRadius=1
        D:silverfishDamageMultiplier=1.0
}
```

In the "mobs" section you can modify the damage that monsters do.

# Hard Mode Tweaks

Hard Mode Tweaks is a compilation of gameplay modifications to either make
survival a little more challenging or that I find interesting. One of the
biggest features included, is the ability to change the lenth of the
minecraft day.

## Download

Download latest version: [Hard Mode Tweaks v1.0b1](http://www.mediafire.com/download/drwt3bb5krlcuds/hardmodetweaks-1.0b1.jar)

## Installation

1. Install Forge.
2. Copy the "hardmodetweaks-1.0.b1.jar" file to the "mods" folder.

## Features

### Day Lenth Setting

Config option in hardmodetweaks.cfg:
```
options {
    D:dayLenthMultiplier=1.0
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
}
```

When this option is set to true, you will heal a little bit depending on how
much the food you are eating replenishes the hunger bar. The lowest forms
of food like carrots don't heal at all. This setting is mainly targeted to
use when the "naturalRegeneration" rule is turned off.

### Game Rules section

The whole "gamerules" section allows you to change the values of vanilla
gamerules. This gamerules are applied when the server is started up/the save
is loaded, and only if the gamemode is set to hard.

# T.E.A.T.I.M.E.
Tea Time is a Minecraft editor I write back in 2011. It doesn't work any more (thanks to some game-changing updates that I was too tired to change), but I figured I'd put the source code here anyways -- moreso for keeping track of than for anything else. The files contained within reflect a snippet of my past, terrible practices and all. I haven't changed anything aside from adjusting the directory hierarchy a little and removing some old information from the original readme, which is below.

Feel free to do whatever you wish with it.

# Original readme.txt
T.E.A.T.I.M.E.
Current version : 1.0 Beta
Changelog:
    2011.03.31 Initial Release

Created by the Protractor Ninja Bugs? Glitches? Other mysterious
problems? Please, do some research first. But if you can't solve your
problem, contact me and I'll be happy to help.

Tea Time is free and always will be free. Some day, it may even become
open source software. So if you love Tea Time so much that you'd like to
give me a little financial aid,  please let me know! If there's any
demand I'll make it possible for you to donate a few dollars (or less)
to yours truly.

DISCLAIMER: BE CAREFUL. DON'T DO ANYTHING STUPID. I'VE ATTEMPTED TO MAKE
IT IMPOSSIBLE TO DO ANYTHING THAT WOULD SERIOUSLY SCREW UP YOUR SYSTEM,
BUT THERE'S ALWAYS A CHANCE.

Tea Time is a Minecraft inventory and world data editor, written in pure
Java. Its goal is to be easy to use, portable, and cross platform.
Please enjoy.

ITEMS.TXT GUIDE 
The available items list in Tea Time is regulated by a
file in the /lib/ directory, called items.txt. This file has two types
of entries, items and groups. Lines starting with a hash (#) or empty
lines are ignored.

Items must be presented in this syntax:
id(1) name(2) location(3) coordinates(4) special(5)(optional)

1. The id of the item can be any number, but must be numerical.
2. The name of the item can either be an entry in the en_us.lang
    file, or a custom name with the syntax "Name_here". Spaces must
    me replaced with underscores.
3. The location is the name of the file containing the item's icon.
    The two derived from minecraft.jar, terrain.png and items.png,
    will always only return proper icons for those that work with 
    the current minecraft version. Others may be given, but unless
    they have a corresponding image file inside the .jar archive,
    tea time will crash. Special.png is an example. "Null" or any
    location that cannot be found will be replaced with a cross icon.
4. The coordinates are the numerical x,y coordinates of the tile for
    the given item's respective location(3). 0,0 will be the top-left
    tile of an image.
5. Optional arguments can be one of three things.
    a. a + sign followed by a number - this is the maximum possible damage
        of an item. This is not currently used, but it may be later.
    b. an "x" followed by a number - this is the maximum count of the item.
        normally, this number defaults to 64.
    c. just a number - this is the permanent damage value of the item, used
        for items with a specific damage value such as wool, dye, or wood.

* At least 4 arguments must be filled, otherwise Tea Time will crash.

Groups must be presented in this syntax:
~ name(1) iconid(2) id,id,id,id(3)

1. The name of the group is what is displayed on the group's tab in Tea Time.
2. The iconid will determine what item's icon will be displayed on the tab,
    next to the group's name.
3. The list of included ids must be numerical and must be included in the items 
    list. They are separated by one comma. Special notation may be used for 
    items of a permanent damage value; <id>d<number> will add an item with
    the given id and damage value. <id>d<number>-<number> will add all items
    of the given damage range, inclusively. For example, 351d1-15 will add
    every dye item, and 531d1 will only add black dye.

Simple, right?


SHORTCUTS (Inventory Tab)
Dragging from the items list:
    Ctrl + drag to inventory slot : Fill row with maximum count of the item
    Alt + drag to inventory slot  : Fill column with max. count of the item
    Right click & drag            : Fill slot with max. count
    Left click & drag             : Fill slot with one item; increment if dragging to an item of the same ID
From the inventory slots
    Ctrl + drag to Full Button    : Fill all remaining slots with a copy of the item (sans armor)
    Alt + drag to Full Button     : Fill empty row slots with a copy of the item
    Ctrl + drag to Trash Button   : Erase entire row
    Alt + drag to Trash Button    : Erase all items of the same ID (even with differing damage values)
    Right click + drag            : Split item to both slots, as close to half as can be.
    Left click + drag             : Switch items, or if items are the same (either by id or by id and special damage values) combine them
    Ctrl + drag                   : Copy the item to the destination slot
    Alt + drag                    : Replace whatever item is in the destination with the source item

# Original Planning Document
Main.class
    + Starts everything up and going. Logs starting time, initiates the registry, and initiates the Tea Time window.
    
Registry.class
    + Links to other important classes (and static variables) that provide functions which need to be accessible to all teatime instances, e.g.:
    - Build.class (Reg.build)
        + Functions for building certain GUI elements easily
    - Util.class (Reg.util)
        + Utility functions for things like getting the minecraft folder location
    - XML.class (Reg.xml)
        + Accessor for serializing/deserializing stuff via XML.
    
TeaTime.class
    + The main window. Acts as a container for a variety of stuff, such as:
        - The menu bar
            + Load Menu
                - Load any world in MC directory
                - Browse for a world
            + Save Menu
                - Save to loaded world
                - Save to a new world
                - Save INVENTORY ONLY to another world (maybe)
            + Schemes Menu
                - Load Scheme
                - Save Scheme
                - Delete Scheme
            + Tools Menu
                - Show / Hide Advanced editor
                - Open item list manager
            + About Menu
                - Display about dialog
                - Go to protractorninja.net
        - The tab pane, which includes tabs for:
            + Inventory editing
                - Inventory pane
                    + Armor slots
                    + Inventory Slots
                    + Quick Slots
                    + Tool slots (^ is shift, @ is alt)
                        - Fill
                            + Click   - 
                            + ^Click  -
                            + @Click  -
                            + ^@Click -
                        - Trash
                            + Click   - 
                            + ^Click  -
                            + @Click  -
                            + ^@Click -
                        - Repair
                            + Click   - 
                            + ^Click  -
                            + @Click  -
                            + ^@Click -
                - Advanced editor pane
                - Name pane
                - Items tab pane
            + Level data editing
            + Map data editing ( location and scale only; very simple )
            
Item.class
    + Contains item data. Fields for:
        - Name
        - ID
        - Damage
        - Count
        - Editable (Boolean)
        - Icon

# Original Todo document

Working on:
    - Cross platform support (Basically just making the getMCWorldsPath() more extensible) 
     (Mr V must test this)
     
To do:
- More cool keyboard shortcuts!
        - ctrl + drag to trash = kill row
        - alt + drag to trash = kill all of the same ID
        - select an item + mouse scroll + keyboard shortcut = good
        
- Organize code; Commenting /** *content */ helps.
- Make it so that it could use texture packs in certain instances?
- Add editing of en_us.lang names, because they suck 
- Inventory schemes, for favorites.
- Some kind of search dialog.
- Operator hotkeys (F5 for refresh, ctrl-s to save, etc)
- "Do you wish to save changes before you exit?"
    
Completed:
- A bunch of stuff
- Added forced .dat file loading and saving (if the saved file doesn't end with ".dat" it doesn't add it; is also case insensitive)
- Created a tile grabber class
- Created a stopwatch class
- Fixed world loading to include custom save folder names
- Added an error popup that can save a log file of the error for moi
- Added tool button support & fixed tile data import
- Reworked the image loading and items file parsing: starts faster now!
- Combined classes used only by the inventory into various nested classes
- Renamed a bunch of stuff to make it make a little more sense in an organizational way
- Added a bunch of keyboard shortcuts and functions for inventory editing; e.g. rightclick->drag halves, combining, etc.
- Added advanced editor panel
- Added generic item data saving/loading/editing
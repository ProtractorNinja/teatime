T.E.A.T.I.M.E.
Current version : 1.3b
Changelog:
	2011.04.01 Initial Release
	2011.04.03 1.1b Released
		- Added search bar
		- Added time-of-day slider
	2011.04.03 1.2 Released
		- Scrolling with the mouse wheel now can edit count / damage
	2011.04.06 1.3 Released
		- Fixed a bug with item counts 
		- Added button link to latest version
		- Modified data loading
		- Modified "save as" availability
		- Added some schemes backend
	2011.04.11 1.4 Released
		- Added inventory schemes
		- Changed download link to page w/ version history, etc.
	
---- THE PROGRAMMER ---------
Created by the Protractor Ninja
webpage : http://protractorninja.blogspot.com/
email   : protractorninja@gmail.com
Bugs? Glitches? Other mysterious problems?
Please, do some research first. But if you can't
solve your problem, contact me with the address above
and I'll be happy to help.

---- ABOUT TEA TIME ---------
Tea Time is a Minecraft inventory and world data editor, written
in pure Java. Its goal is to be easy to use, portable, and cross
platform.

Tea Time is free and always will be free. Currently, it is 
backed by a creative commons BY-NC-ND license, detailed here:
http://creativecommons.org/licenses/by-nc-nd/3.0/

Basically, this means that you are free to distribute Tea Time
all you like, as long as you...
a. Don't try to sell it
b. Don't edit the code and claim you made it (through decompilation)
c. Include this readme.txt file in the .jar archive, unmodified.

This shouldn't be hard to do - if you like Tea Time, you can just
give the .jar to your friends, problem free. Any questions? Ask me. :)

Eventually, once I'm more or less finished with Tea Time, I'll 
release the source code and apply a different, more appropriate
license. But that's for later.

I hope you enjoy using Tea Time!

---- DISCLAIMER -------------
BE CAREFUL. DON'T DO ANYTHING STUPID. I'VE
ATTEMPTED TO MAKE IT IMPOSSIBLE TO DO ANYTHING THAT WOULD
SERIOUSLY SCREW UP YOUR SYSTEM, BUT THERE'S ALWAYS A CHANCE.

---- CREDITS ----------------
Many thanks to:
Notch, for creating Minecraft,
Malvaviscos, for support, testing, encouragement, and being a great friend,
Sun, for creating Java (But not Oracle. I dislike Oracle.),
Minecraftwiki.net for the NBT IO class and block data reference,
Stackoverflow.com and the Java API for technical support,
Copyboy, for creating INVedit and for his items.txt layout and features,
and Dawidds + Shockah, creators of Loleditor, for feature inspiration.

Interface icons courtesy of:
The Fugue pack - http://p.yusukekamiyamane.com/
The FamFamFam Silk Pack - http://www.famfamfam.com/
The FamFamFam Silk Companion I pack - http://damieng.com/
The Breakfast icon pack - http://www.cutelittlefactory.com/

---- ITEMS.TXT GUIDE --------
The available items list in Tea Time is regulated by a file in the Tea Time.jar/lib/
directory, called items.txt. This file has two types of entries, items
and groups. Lines starting with a hash (#) or empty lines are ignored.

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

---- INVENTORY SHORTCUTS ----
Buttons:
	Full Button                   : Fill all slots with maximum item count
	Repair Button                 : Set all non-special-damage items to 0 damage
	Trash Button                  : Clear all slots
Dragging from the items list:
	Ctrl + drag to inventory slot : Fill column with maximum count of the item
	Alt + drag to inventory slot  : Fill row with max. count of the item
	Right click & drag            : Fill slot with max. count
	Left click & drag             : Fill slot with one item; increment if dragging to an item of the same ID
From the inventory slots
	Ctrl + drag to Full Button    : Fill empty column slots with a copy of the item
	Alt + drag to Full Button     : Fill empty row slots with a copy of the item
	Right click + drag to Full    : Fill all remaining slots with a copy of the item (sans armor)
	Ctrl + drag to Trash Button   : Erase entire column
	Alt + drag to Trash Button    : Erase entire row
	Right click + drag to Trash   : Erase all items of the same ID (even with differing damage values)
	Right click + drag            : Split item to both slots, as close to half as can be.
	Left click + drag             : Switch items, or if items are the same (either by id or by id and special damage values) combine them
	Ctrl + drag                   : Copy the item to the destination slot
	Alt + drag                    : Replace whatever item is in the destination with the source item
	
---- TOOLBAR BUTTONS --------
Open Menu (Folder)
	<World Name> : Loads the world into memory. This should be parsing the list of saves from the MC directory.
	Browse...    : Loads a chosen .dat file into memory. This must be a .dat, in the MC world format.
Save Menu (Floppy Disk)
	Save the World : Writes all current data to the loaded world.
	Save As...     : Writes all data to a new or chosen world.
World Menu (Planet)
	Refresh World : Undoes all changes to the world (as long as it hasn't been saved)
	Clear World   : Releases the current world from memory, clears all fields. Acts as if Tea Time was just booted.
Tools Menu (Wand & Hat)
	Toggle Editor  : Toggles the advanced item information editor on or off.
	Edit item list : This doesn't work, but it will have an icon until I'm sure that it will never work. See the ITEMS.TXT GUIDE.
About Menu ("I" symbol)
	About Tea Time : Brings up an about menu. Woo.
	Visit Protractor Ninja : Takes your web browser on a journey to my homepage. Hooray!
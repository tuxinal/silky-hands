# silky-hands

A minecraft mod that makes a specified player (or players) drop any blocks that they break

keep in mind this is not the same as silk touch, it applies to all blocks (e.g. you can break obsidian with your fist and it will drop) and **your hand has to be empty**

## Usage

You have to add a tag to a player

by default this tag is `silkyhands`

so you would do

```
/tag PlayerNameHere add silkyhands
```

this tag is configurable through the config file (located at `config/silky-hands.json`)

you can also set some blocks for it to ignore

by default beds and crops are ignored, because beds will duplicate and crops will not drop as much as they should
(if you find any other blocks that you think should be ignored by default open an issue!)

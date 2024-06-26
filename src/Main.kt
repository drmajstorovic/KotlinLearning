class Position(var x: Int, var y: Int){
    override fun equals(other: Any?): Boolean {
        if(other is Position)
            if((this.x == other.x) and (this.y == other.y))
                return true
        return false
    }
    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

var fighting = false
fun main() {
    print("Enter your name: ")
    val nameInput = readLine()
    var player = Player(nameInput ?: "", 30, 30, 10, 30, 1, 0, mutableListOf(), Position(0, 0), 'Ø')
    var potion = Potion("Name", "desc", 0, 0, 0)
    var drop: Potion = potion

    var ghoul1 = Monster("Ghoul", 50, 20, 20, drop, 'Ĝ')
    var orc1 = Monster("Orc", 80, 15, 20, drop, 'Ŏ')
    var skeleton1 = Monster("Skeleton", 20, 20, 15, drop, 'Ś')
    var goblin1 = Monster("Goblin", 30, 10, 10, drop, 'ĝ')

    // Overworld map and monster generation
    var worldMap: HashMap<Position, Being> = hashMapOf(
        Position(4, 4) to ghoul1,
        Position(1, 1) to orc1,
        Position(1, 2) to skeleton1,
        Position(1, 3) to goblin1
    )
    worldMap.put(player.position,player);
    // Exploring the map and encountering monsters
    while (!fighting) {
        if (worldMap.containsKey(player.position)  && worldMap.getValue(player.position) != player ) {
            println("You've encountered a monster")
            val monster = worldMap[player.position]!!
            //REFACTOR THIS
            //monster.drop = monster.monsterDrop()
            fighting = true
            combatLoop(player, monster, worldMap)
        }
        drawWorldMap(worldMap)
        player.playerMovement()
    }
}

private fun drawWorldMap(worldMap: HashMap<Position, Being>) {
    for (x in 0..5) {
        for (y in 0..5) {
            var currentPos = Position(x, y)
            if (worldMap.containsKey(currentPos)) {
                var monster = worldMap[currentPos]
                if (monster != null) {
                      print(" ${monster.icon} ")
                    }
                }
            else{
                print(" X ")
            }

        }
        println()
    }
    }


fun combatLoop(player: Player, monster: Being, worldMap: HashMap<Position, Being>) {
    while (player.hp > 0 && monster.hp > 0) {
        println("""${player.name}'s health : ${player.hp}, ${monster.name}'s health: ${monster.hp}""")
        println("Choose your actions: ")
        println("1) Attack")
        println("2) Magic")
        println("3) Wait")
        println("4) Inventory")
        println("5) Use potion")

        val userInput = readLine()?.lowercase()?.trim()
        when (userInput) {
            "attack" -> player.Attack(monster)
            "magic" -> player.MagicAttack(monster)
            "wait" -> println("You reconsider your approach.")
            "inventory" -> player.openInventory()
            "use potion" -> {
                if (player.potionInventory.isEmpty()) {
                    println("You don't have any potions!")
                } else {
                    println("Select a potion to use:")
                    for ((index, potion) in player.potionInventory.withIndex()) {
                        println("${index + 1}) ${potion.name}")
                    }
                    val potionIndex = readLine()?.toIntOrNull()
                    if (potionIndex != null && potionIndex >= 1 && potionIndex <= player.potionInventory.size) {
                        val selectedPotion = player.potionInventory[potionIndex - 1]
                        player.usePotion(selectedPotion)
                    } else {
                        println("Invalid input. Please enter a valid potion number.")
                    }
                }
            }
            else -> println("Invalid input.")
        }
        // Check if combat should end
        if (player.hp <= 0) {
            println("Oh dear, you've died.")
            break
        } else if (monster.hp <= 0) {
            println("You've slain the monster!")
            println("You gain ${monster.xp} XP!")
            player.xp += monster.xp
            //check for level-up
            player.levelUp()
            //REFACTOR THIS
            //player.addItem(monster.drop)
            fighting = false
            worldMap.remove(player.position)
        }
    }
}


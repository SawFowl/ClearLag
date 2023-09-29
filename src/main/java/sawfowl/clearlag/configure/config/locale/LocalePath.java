package sawfowl.clearlag.configure.config.locale;

public class LocalePath {

	private static final Object MESSAGES = "Messages";
	private static final Object COMMANDS = "Commands";

	private static final Object COMMAND_HELP = "Help";
	private static final Object COMMAND_KILL = "Kill";
	private static final Object GARBAGECOLLECTOR = "garbagecollector";
	private static final Object HALT = "halt";

	public static final Object[] PREFIX = {MESSAGES, "Prefix"};
	public static final Object[] REMOVE_ITEMS = {MESSAGES, "RemoveItems"};
	public static final Object[] CHANGE_TICK_SPEED = {MESSAGES, "ChangeTickSpeed"};
	public static final Object[] CHANGE_VIEWING_RADIUS = {MESSAGES, "ChangeViewingRadius"};
	public static final Object[] CHANGE_CLEAR_WARN10S = {MESSAGES, "ClearWarn10s"};
	public static final Object[] CHANGE_CLEAR_WARN30S = {MESSAGES, "ClearWarn30s"};
	public static final Object[] COMMAND_HELP_LIST = {COMMANDS, COMMAND_HELP, "List"};
	public static final Object[] COMMAND_HELP_TITLE = {COMMANDS, COMMAND_HELP, "Title"};
	public static final Object[] COMMAND_KILL_HELP = {COMMANDS, COMMAND_KILL, "Help"};
	public static final Object[] COMMAND_KILL_MONSTERS = {COMMANDS, COMMAND_KILL, "Monsters"};
	public static final Object[] COMMAND_KILL_CREATURE = {COMMANDS, COMMAND_KILL, "Creature"};
	public static final Object[] COMMAND_KILL_AMBIENT = {COMMANDS, COMMAND_KILL, "Ambient"};
	public static final Object[] COMMAND_KILL_WATER_AMBIENT = {COMMANDS, COMMAND_KILL, "WaterAmbient"};
	public static final Object[] COMMAND_KILL_WATER_CREATURE = {COMMANDS, COMMAND_KILL, "WaterCreature"};
	public static final Object[] COMMAND_KILL_MISC = {COMMANDS, COMMAND_KILL, "Ambient"};
	public static final Object[] COMMAND_KILL_ALL = {COMMANDS, COMMAND_KILL, "Ambient"};
	public static final Object[] COMMAND_GARBAGECOLLECTOR = {COMMANDS, GARBAGECOLLECTOR, "Message"};
	public static final Object[] COMMAND_HALT_ENABLE = {COMMANDS, HALT, "Enable"};
	public static final Object[] COMMAND_HALT_DISABLE = {COMMANDS, HALT, "Disable"};

}

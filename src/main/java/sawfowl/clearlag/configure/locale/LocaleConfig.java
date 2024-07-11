package sawfowl.clearlag.configure.locale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.clearlag.configure.locale.config.Commands;
import sawfowl.clearlag.configure.locale.config.Messages;
import sawfowl.localeapi.api.LocaleReference;

@ConfigSerializable
public class LocaleConfig implements LocaleReference {

	@Setting("Messages")
	private Messages messages = new Messages();
	@Setting("Commands")
	private Commands commands = new Commands();
	public LocaleConfig() {}

	public Messages getMessages() {
		return messages;
	}

	public Commands getCommands() {
		return commands;
	}

	public static LocaleConfig createRu() {
		LocaleConfig ru = new LocaleConfig();
		ru.messages = Messages.createRu();
		ru.commands = Commands.createRu();
		return ru;
	}

}

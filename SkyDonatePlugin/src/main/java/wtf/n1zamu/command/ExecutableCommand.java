package wtf.n1zamu.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public abstract class ExecutableCommand {
    private final String name;
    private final int needArgs;
    private final String usage;

    public ExecutableCommand() {
        this.name = this.getClass().getAnnotation(CommandAnnotation.class).getName();
        this.needArgs = this.getClass().getAnnotation(CommandAnnotation.class).getNeedArgs();
        this.usage = this.getClass().getAnnotation(CommandAnnotation.class).getUsage();
    }

    public abstract void execute(CommandSender sender, String[] args);
}


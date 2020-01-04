package greenbot.plugin.model.optional;

import java.util.function.Function;

import greenbot.plugin.model.PluginRequest;
import greenbot.plugin.model.PluginResponse;

public interface Task extends Function<PluginRequest,PluginResponse>{

}

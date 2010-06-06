package org.todomap.spamegg;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class ScriptedSpamFilter implements SpamFilter {

	final Map<String, SpamFilter> backends;
	final String script;
	public ScriptedSpamFilter(Map<String, SpamFilter> backends, String script,
			String scriptEngine) {
		super();
		this.backends = backends;
		this.script = script;
		this.scriptEngine = scriptEngine;
	}

	final String scriptEngine;

	@Override
	public void falseNegative(Content content) throws SpamFilterException {
		for(SpamFilter filter : backends.values()) {
			filter.falseNegative(content);
		}
	}

	@Override
	public void falsePositive(Content content) throws SpamFilterException {
		for(SpamFilter filter : backends.values()) {
			filter.falsePositive(content);
		}
	}

	@Override
	public boolean isSpam(Content content) throws SpamFilterException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName(scriptEngine);
		try {
			return (Boolean) engine.eval(script, getBindings(content));
		} catch (ScriptException e) {
			throw new SpamFilterException(e);
		}
	}

	private Bindings getBindings(Content content) {
		final SimpleBindings bindings = new SimpleBindings();
		for(String key : backends.keySet()) {
			bindings.put(key, backends.get(key));
		}
		bindings.put("content", content);
		return bindings;
	}

}

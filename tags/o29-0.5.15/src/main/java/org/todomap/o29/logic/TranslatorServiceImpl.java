package org.todomap.o29.logic;

import org.todomap.latetrans.Translator;
import org.todomap.o29.beans.Translatable;

public class TranslatorServiceImpl implements TranslatorService {

	public TranslatorServiceImpl(final Translator translator) {
		super();
		this.translator = translator;
	}

	private final Translator translator;

	@Override
	public void updateLanguage(final Translatable translatable) {
		final String text = translatable.getText();
		if (text != null && !text.isEmpty()) {
			translatable.setLanguage(translator.getLanguage(text));
		}
	}

}

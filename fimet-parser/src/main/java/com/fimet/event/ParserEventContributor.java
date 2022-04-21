package com.fimet.event;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.EventManager;
import com.fimet.FimetException;
import com.fimet.parser.IEFieldGroup;
import com.fimet.parser.IEParser;
import com.fimet.parser.IFieldGroup;
import com.fimet.parser.IParser;

@Component
public class ParserEventContributor implements IEventContributor {

	@Autowired private EventManager eventManager;
	
	@Override
	public Object[] getEventTypes() {
		return ParserEvent.values();
	}
	@PostConstruct
	public void init() {
		eventManager.register(this);
	}
	@Override
	public void fireEvent(IEvent event, IEventListener listener) {
		ParserEvent type = (ParserEvent)event.getType();
		switch (type) {
		case FIELDGROUP_INSERTED:
			((IFieldGroupInserted)listener).onFieldGroupInserted((IEFieldGroup)event.getParams()[0]);
			break;
		case FIELDGROUP_UPDATED:
			((IFieldGroupUpdated)listener).onFieldGroupUpdated((IEFieldGroup)event.getParams()[0]);
			break;
		case FIELDGROUP_DELETED:
			((IFieldGroupDeleted)listener).onFieldGroupDeleted((IEFieldGroup)event.getParams()[0]);
			break;
		case FIELDGROUP_LOADED:
			((IFieldGroupLoaded)listener).onFieldGroupLoaded((IFieldGroup)event.getParams()[0]);
			break;
		case FIELDGROUP_REMOVED:
			((IFieldGroupRemoved)listener).onFieldGroupRemoved((IFieldGroup)event.getParams()[0]);
			break;
		case PARSER_INSERTED:
			((IParserInserted)listener).onParserInserted((IEParser)event.getParams()[0]);
			break;
		case PARSER_UPDATED:
			((IParserUpdated)listener).onParserUpdated((IEParser)event.getParams()[0]);
			break;
		case PARSER_DELETED:
			((IParserDeleted)listener).onParserDeleted((IEParser)event.getParams()[0]);
			break;
		case PARSER_LOADED:
			((IParserLoaded)listener).onParserLoaded((IParser)event.getParams()[0]);
			break;
		case PARSER_REMOVED:
			((IParserRemoved)listener).onParserRemoved((IParser)event.getParams()[0]);
			break;
		default:
			throw new FimetException("Invalid Event "+event);
		}
	}

}

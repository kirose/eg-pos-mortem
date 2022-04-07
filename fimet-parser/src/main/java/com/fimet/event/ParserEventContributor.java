package com.fimet.event;

import com.fimet.FimetException;
import com.fimet.parser.IEFieldGroup;
import com.fimet.parser.IEParser;
import com.fimet.parser.IFieldGroup;
import com.fimet.parser.IParser;

public class ParserEventContributor implements IEventContributor {

	@Override
	public Object[] getEventTypes() {
		return ParserEvent.values();
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

package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class EGCamara extends AbstractSimulatorModel {

	public EGCamara () {
		super("EGCamara");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0200".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetEntryMode.getInstance().simulate(msg,"63.EZ.5");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0220".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetEntryMode.getInstance().simulate(msg,"63.EZ.5");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0221".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetEntryMode.getInstance().simulate(msg,"63.EZ.5");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0420".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetEntryMode.getInstance().simulate(msg,"63.EZ.5");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0421".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.10.track2");
			com.fimet.simulator.field.IfHasSetEntryMode.getInstance().simulate(msg,"63.EZ.5");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.11");
			return msg;
		}
		if ("0800".equals(mti)){
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("15","0903");
			msg.setValue("48","0111000122S00484");
			msg.setValue("70","301");
			return msg;
		}
		if ("0800".equals(mti)){
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("15","0903");
			msg.setValue("48","0111000122S00484");
			msg.setValue("70","301");
			return msg;
		}
		return msg;
	}

	/**
	* @param IMessage msg is the incoming message
	**/
	@Override
	public IMessage simulateResponse(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.setValue("39","00");
			return msg;
		}
		if ("0100".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","18","25","43","48","51","60","62","63.Q1","63.Q2","63.B2","63.B3","63.B4","63.C6");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("18","43","48","60","62","63.Q1","63.B2","63.B3","63.B4");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			msg.setValue("44","02 0");
			msg.setValue("63.C4","111111111111");
			return msg;
		}
		if ("0220".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","15","17","18","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0221".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","15","17","18","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0421".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}

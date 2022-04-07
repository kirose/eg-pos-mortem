package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class Visa extends AbstractSimulatorModel {

	public Visa () {
		super("Visa");
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
			return msg;
		}
		if ("0120".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			return msg;
		}
		if ("0400".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
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
			msg.removeAll("14","18","22","35","43","55","59","60","126","38","39","44","104");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			msg.setValue("44","5");
			msg.setValue("62.1","A");
			com.fimet.simulator.field.SetRandom15N.getInstance().simulate(msg,"62.2");
			return msg;
		}
		if ("0120".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0400".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("14","18","22","43","60","59","38");
			msg.setValue("39","00");
			msg.setValue("62.1","A");
			return msg;
		}
		return null;
	}
}

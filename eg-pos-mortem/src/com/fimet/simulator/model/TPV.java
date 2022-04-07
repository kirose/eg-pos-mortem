package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class TPV extends AbstractSimulatorModel {

	public TPV () {
		super("TPV");
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
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.9.track2");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.10");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.QK.1.1");
			com.fimet.simulator.field.IfHasSetAmount.getInstance().simulate(msg,"63.QK.1.8");
			com.fimet.simulator.field.IfHasSetRRN.getInstance().simulate(msg,"63.QK.1.13");
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
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.9.track2");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.10");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.QK.1.1");
			com.fimet.simulator.field.IfHasSetAmount.getInstance().simulate(msg,"63.QK.1.8");
			com.fimet.simulator.field.IfHasSetRRN.getInstance().simulate(msg,"63.QK.1.13");
			return msg;
		}
		if ("0400".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.10");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.QK.1.1");
			com.fimet.simulator.field.IfHasSetAmount.getInstance().simulate(msg,"63.QK.1.8");
			com.fimet.simulator.field.IfHasSetRRN.getInstance().simulate(msg,"63.QK.1.13");
			return msg;
		}
		if ("0800".equals(mti)){
			msg.setValue("3","990000");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("12","094837");
			msg.setValue("13","0903");
			msg.setValue("41","00000001");
			msg.setValue("56.00","VF323892932");
			msg.setValue("56.01","BMRRES27_01");
			msg.setValue("56.02","RE520G18083000");
			msg.setValue("56.15","");
			msg.setValue("56.16","3");
			msg.setValue("56.20","50");
			msg.setValue("37","123465789012");
			msg.setValue("42","254484010122345");
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
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"63.EZ.9.track2");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.EZ.10");
			com.fimet.simulator.field.IfHasSetPanLast4Digits.getInstance().simulate(msg,"63.QK.1.1");
			com.fimet.simulator.field.IfHasSetAmount.getInstance().simulate(msg,"63.QK.1.8");
			com.fimet.simulator.field.IfHasSetRRN.getInstance().simulate(msg,"63.QK.1.13");
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
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			return msg;
		}
		return null;
	}
}
